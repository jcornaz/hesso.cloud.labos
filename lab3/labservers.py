import yaml
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
    def __init__(self, driver, net, size, image_id, public_ip):
        super(RestClient, self).__init__(driver, net, size, image_id)
        self._public_ip = public_ip

    @property
    def name(self):
        return 'RestClient'

    def run(self, mongo_server):
        self.attach_public_ip(self._public_ip)
        super(RestClient, self).run("python restclient.py " + mongo_server.private_ip)
        self.detach_public_ips()


class RestServer(Server):
    def __init__(self, driver, net, size, image_id, public_ip):
        super(RestServer, self).__init__(driver, net, size, image_id)
        self._public_ip = public_ip

    @property
    def name(self):
        return 'RestServer'

    def run(self, mongo_server):
        self.attach_public_ip(self._public_ip)
        super(RestServer, self).run("python restserver.py " + mongo_server.private_ip)


class Stack(object):
    @staticmethod
    def load(driver, filepath):
        """
        Load a stack from it YAML file description
        :param driver: The libcloud driver to use
        :param filepath: The file path of the YAML file that describe the stack
        :return: The stack instance.
        """

        print('loading config...')
        with open(filepath, 'r') as file:
            config = yaml.safe_load(file)

        net = [net for net in driver.ex_list_networks() if net.id == config['net_id']][0]

        mongo = MongoDB(
            driver,
            net,
            config['mongodb']['size'],
            config['mongodb']['image_id']
        )

        rest_client = RestClient(
            driver,
            net,
            config['rest_client']['size'],
            config['rest_client']['image_id'],
            config['rest_client']['public_ip']
        )

        rest_server = RestServer(
            driver,
            net,
            config['rest_server']['size'],
            config['rest_server']['image_id'],
            config['rest_server']['public_ip']
        )

        return Stack(mongo, rest_client, rest_server)

    def __init__(self, mongo, rest_client, rest_server):
        self._mongo = mongo
        self._rest_client = rest_client
        self._rest_server = rest_server

    def run(self):
        """
        * Instanciate and run the servers
        * Wait until the key 'A' is pressed
        * And then destroy the instances
        """

        print('==== Starting the stack ====')
        with self._mongo as mongo:
            with self._rest_client as rest_client:
                with self._rest_server as rest_server:
                    rest_client.run(mongo)
                    rest_server.run(mongo)

                    print('==== The stack is up and running ====')
                    wait_until_user_abort()
                    print('==== Destroying the stack ====')

        print('==== Stack destroyed ====')
