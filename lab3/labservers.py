from cloudserver import Server

NET_ID = '25620c4e-2c3b-4403-9f7d-dd1f0068f40c'


class MongoDB(Server):
    def __init__(self, driver):
        super(MongoDB, self).__init__(driver, NET_ID, 'c1.small', 'eca08949-4d4d-41ad-afeb-9e9cbeaf70fa')

    @property
    def name(self):
        return 'MongoDB'


class RestClient(Server):
    def __init__(self, driver):
        super(RestClient, self).__init__(driver, NET_ID, 'c1.micro', '11d41ef3-6ebc-4457-800f-bf1fd2ce91bf')

    @property
    def name(self):
        return 'RestClient'

    def run(self, ip):
        print("run with rest client mongo ip " + ip)


class RestServer(Server):
    def __init__(self, driver):
        super(RestServer, self).__init__(driver, NET_ID, 'c1.micro', 'bafba318-249d-428b-8683-933fbfae62a0')

    @property
    def name(self):
        return 'RestServer'

    def run(self, ip):
        print("run with reset server mongo ip " + ip)
