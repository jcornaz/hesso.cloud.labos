import os

from cloudserver import Server


def wait_until_user_abort():
    print("Press 'A' to abort > ")
    pressed_key = os.read(0, 1)
    while pressed_key != 'A':
        print("Press 'A' to abort > ")
        pressed_key = os.read(0, 1)


class MongoDB(Server):
    def __init__(self, driver, net, size, image_id):
        super(MongoDB, self).__init__(driver, net, size, image_id)

    @property
    def name(self):
        return 'MongoDB'


class RestClient(Server):
    def __init__(self, driver, net, size, image_id, key_pair, public_ip):
        super(RestClient, self).__init__(driver, net, size, image_id)
        self._key_pair = key_pair
        self._public_ip = public_ip

    @property
    def name(self):
        return 'RestClient'

    def run(self, mongo_server):
        self.attach_public_ip(self._public_ip)
        super(RestClient, self).run('ubuntu', self._key_pair, "python restclient.py " + mongo_server.private_ip)
        self.detach_public_ips()


class RestServer(Server):
    def __init__(self, driver, net, size, image_id, key_pair, public_ip):
        super(RestServer, self).__init__(driver, net, size, image_id)
        self._key_pair = key_pair
        self._public_ip = public_ip

    @property
    def name(self):
        return 'RestServer'

    def run(self, mongo_server):
        self.attach_public_ip(self._public_ip)
        super(RestServer, self).run('ubuntu', self._key_pair, "python restserver.py " + mongo_server.private_ip)
