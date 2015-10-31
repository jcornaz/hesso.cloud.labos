import sys

from libcloud.compute.types import Provider
from libcloud.compute.providers import get_driver
from labservers import MongoDB, RestClient, RestServer

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

    with MongoDB(driver) as mongo:
        with RestClient(driver) as rest_client:
            with RestServer(driver) as rest_server:
                rest_client.run(mongo.private_ip)
                rest_server.run(mongo.private_ip)
