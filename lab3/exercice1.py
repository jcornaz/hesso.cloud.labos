import sys

from libcloud.compute.types import Provider
from libcloud.compute.providers import get_driver


class Server:
    def __init__(self, driver):
        self._driver = driver
        self.node = None
        self._net = [net for net in driver.ex_list_networks() if net.id == '25620c4e-2c3b-4403-9f7d-dd1f0068f40c'][0]
        self._size = [size for size in driver.list_sizes() if size.name == 'c1.micro'][0]

    def instantiate(self):
        image = [image for image in self._driver.list_images() if image.id == self.image_id][0]

        self.node = self._driver.create_node(name=self.name, size=self._size, networks=[self._net], image=image)
        return self.node.private_ip

    @property
    def name(self):
        raise Exception()

    @property
    def image_id(self):
        pass


class MongoDB(Server):

    @property
    def name(self):
        return 'MongoDB'

    @property
    def image_id(self):
        return 'eca08949-4d4d-41ad-afeb-9e9cbeaf70fa'


class RestClient(Server):
    @property
    def name(self):
        return 'RestClient'

    @property
    def image_id(self):
        return '11d41ef3-6ebc-4457-800f-bf1fd2ce91bf'

    def run(self, ip):
        pass


class RestServer(Server):
    @property
    def name(self):
        return 'RestServer'

    @property
    def image_id(self):
        return 'bafba318-249d-428b-8683-933fbfae62a0'

    def run(self, ip):
        pass


if __name__ == '__main__':

    if len(sys.argv) < 3:
        raise Exception("Not enough arguments")
    elif len(sys.argv) < 4 or sys.argv[3] == 'switch':
            provider = get_driver(Provider.OPENSTACK)
    else:
        raise Exception("Not supported cloud provider : " + sys.argv[3])

    driver = provider(
        sys.argv[1],
        sys.argv[2],
        ex_tenant_name=sys.argv[1],
        ex_force_auth_url="https://keystone.cloud.switch.ch:5000/v2.0/tokens",
        ex_force_auth_version='2.0_password',
        ex_force_service_region="LS"
    )

    mongodb_ip = MongoDB(driver).instantiate()
    client = RestClient(driver)
    server = RestServer(driver)

    client.instantiate()
    client.run(mongodb_ip)

    server.instantiate()
    server.run(mongodb_ip)