from cloudserver import Server

NET_ID = '25620c4e-2c3b-4403-9f7d-dd1f0068f40c'
_NET = None


def get_net(driver):
    global _NET

    if not _NET:
        print("get the network instance...")
        _NET = [net for net in driver.ex_list_networks() if net.id == NET_ID][0]

    return _NET


class MongoDB(Server):
    def __init__(self, driver):
        super(MongoDB, self).__init__(driver, get_net(driver), 'c1.small', 'eca08949-4d4d-41ad-afeb-9e9cbeaf70fa')

    @property
    def name(self):
        return 'MongoDB'


class RestClient(Server):
    def __init__(self, driver):
        super(RestClient, self).__init__(driver, get_net(driver), 'c1.micro', '11d41ef3-6ebc-4457-800f-bf1fd2ce91bf')

    @property
    def name(self):
        return 'RestClient'

    def run(self, mongo_server):
        self.attach_public_ip('86.119.29.165')
        super(RestClient, self).run("python restclient.py " + mongo_server.private_ip)
        self.detach_public_ips()


class RestServer(Server):
    def __init__(self, driver):
        super(RestServer, self).__init__(driver, get_net(driver), 'c1.micro', 'bafba318-249d-428b-8683-933fbfae62a0')

    @property
    def name(self):
        return 'RestServer'

    def run(self, mongo_server):
        self.attach_public_ip('86.119.29.193')
        super(RestServer, self).run("python restserver.py " + mongo_server.private_ip)
