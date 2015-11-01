import sys

from libcloud.compute.types import Provider
from libcloud.compute.providers import get_driver
from labservers import Stack

def create_switch_engine_driver(user, tenant, password):
    print("creating a driver for SwtichEngine...")
    return get_driver(Provider.OPENSTACK)(
        user,
        password,
        ex_tenant_name=tenant,
        ex_force_auth_url="https://keystone.cloud.switch.ch:5000/v2.0/tokens",
        ex_force_auth_version='2.0_password',
        ex_force_service_region="LS"
    )


def create_amazon_driver(user, tenant, password):
    pass

if __name__ == '__main__':

    if len(sys.argv) < 3:
        raise Exception("Not enough arguments")
    elif len(sys.argv) < 4 or sys.argv[3] == 'switch':
        driver = create_switch_engine_driver(sys.argv[1], sys.argv[1], sys.argv[2])
        stack = Stack.load(driver, 'switch-engine-stack.yml')
    elif sys.argv[3] == 'amazon':
        driver = create_amazon_driver(sys.argv[1], sys.argv[2], sys.argv[1])
        stack = Stack.load(driver, 'amazon-stack.yml')
    else:
        raise Exception("Not supported cloud provider : " + sys.argv[3])

    stack.run()
