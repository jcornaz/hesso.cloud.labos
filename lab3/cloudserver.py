class Server(object):
    def __init__(self, driver, net, size, imageid):
        self._driver = driver
        self._net = net
        self._sizename = size
        self._imageid = imageid
        self._private_ip = None

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

    @property
    def private_ip(self):

        if not self._private_ip:
            print("wait on " + self.name + " to get a private ip ...")
            self._private_ip = self._driver.wait_until_running([self._node], ssh_interface='private_ips')[0][1][0]
            print("done. (ip = " + self._private_ip + ")")

        return self._private_ip

    @property
    def name(self):
        return self._imageid
