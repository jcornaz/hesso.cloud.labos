import yaml
from labservers import MongoDB, RestServer, RestClient


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

        nets = [net for net in driver.ex_list_networks() if net.id == config['net_id']]

        if not nets:
            print("Unavailable network : " + config['net_id'] + "\navailable networks :\n" +
                  '\n'.join([net.id for net in driver.ex_list_networks()]))
        else:
            net = nets[0]

        mongo = MongoDB(
            driver,
            net,
            config['mongodb']['size'],
            config['mongodb']['image_id'],
        )

        rest_client = RestClient(
            driver,
            net,
            config['rest_client']['size'],
            config['rest_client']['image_id'],
            config['key_pair'],
            config['rest_client']['public_ip']
        )

        rest_server = RestServer(
            driver,
            net,
            config['rest_server']['size'],
            config['rest_server']['image_id'],
            config['key_pair'],
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
