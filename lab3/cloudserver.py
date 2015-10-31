class Server(object):
    def __init__(self, driver, netid, size, imageid):
        self._driver = driver
        self._netid = netid
        self._sizename = size
        self._imageid = imageid

    def __enter__(self):
        """ Create the instance """
        self._net = [net for net in self._driver.ex_list_networks() if net.id == self._netid][0]
        self._size = [size for size in self._driver.list_sizes() if size.name == self._sizename][0]
        self._image = [image for image in self._driver.list_images() if image.id == self._imageid][0]
        print("instanciating " + self.name)
        self._node = self.node = self._driver.create_node(
            name=self.name,
            size=self._size,
            networks=[self._net],
            image=self._image
        )
        print(self.name + " instanciated")

    def __exit__(self, exc_type, exc_value, traceback):
        """ Destroy the instance """
        try:
            print("destoying " + self.name)
        except Exception as e:
            raise e
        finally:
            self._driver.destroy_node(self._node)

    @property
    def private_ip(self):
        self._driver.wait_until_running([self._node])
        return self._node.private_ips[0]

    @property
    def name(self):
        return self._imageid
