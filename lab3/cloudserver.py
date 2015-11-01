from libcloud.compute.ssh import ParamikoSSHClient


class Server(object):
    def __init__(self, driver, net, size, imageid):
        self._driver = driver
        self._net = net
        self._sizename = size
        self._imageid = imageid
        self._private_ip = None
        self._public_ips = []

    def __enter__(self):
        """ Create the instance """

        print("gather arguments for " + self.name + "...")
        self._size = [size for size in self._driver.list_sizes() if size.name == self._sizename][0]
        self._image = [image for image in self._driver.list_images() if image.id == self._imageid][0]

        print("instanciating " + self.name + "...")
        self._node = self.node = self._driver.create_node(
            name=self.name,
            size=self._size,
            networks=[self._net],
            image=self._image
        )

        return self

    def __exit__(self, exc_type, exc_value, traceback):
        """ Destroy the instance """
        try:
            print("destroying " + self.name + "...")
        except Exception as e:
            raise e
        finally:
            self._driver.destroy_node(self._node)
            self._node = None

    def attach_public_ip(self, ip):
        if not self._node:
            raise Exception("No instanciated node")

        print("attaching floating ip " + ip + " to " + self.name + "...")

        ips = [floating_ip.ip_address for floating_ip in self._driver.ex_list_floating_ips() if floating_ip.ip_address == ip]

        if not ips:
            raise Exception("the ip " + ip + " is not available")

        self._driver.ex_attach_floating_ip_to_node(self._node, ips[0])
        self._public_ips.append(ips[0])

    def detach_public_ips(self):
        if not self._node:
            raise Exception("No instanciated node")

        for ip in self._public_ips:
            print("detaching floating ip " + ip + " from " + self.name + "...")
            self._driver.ex_detach_floating_ip_from_node(self._node, ip)

        self._public_ips = []

    def run(self, command):
        if not self._node:
            raise Exception("No instanciated node")
        elif not self._public_ips:
            raise Exception("No public ip attached")

        ip = self._driver.wait_until_running([self._node], ssh_interface='public_ips')[0][1][0]
        print("connecting to " + ip)
        ssh = ParamikoSSHClient(ip, 22, 'ubuntu', key_files=['./keypair.pem'])
        if ssh.connect():
            full_command = "nohup " + command + " </dev/null >logfile.log 2>&1 &"
            print("execute \"" + full_command + "\" on " + ip + "...")
            ssh.run(full_command)
            ssh.close()
        else:
            raise Exception("Unable to connect to the remote via ssh")

    @property
    def private_ip(self):
        if not self._node:
            raise Exception("No instanciated node")

        if not self._private_ip:
            print("wait on " + self.name + " to get a private ip ...")
            self._private_ip = self._driver.wait_until_running([self._node], ssh_interface='private_ips')[0][1][0]

        return self._private_ip

    @property
    def name(self):
        return self._imageid
