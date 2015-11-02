import sys
import getpass

from libcloud.compute.types import Provider
from libcloud.compute.providers import get_driver
from labstack import Stack

SUPPORTED_PROVIDER_MESSAGE = "the supported providers are 'switch' and 'amazon'"


def get_credentials(userprompt='username', passprompt='password'):
    user = input(userprompt + " [%s]: " % getpass.getuser())

    if not user:
        user = getpass.getuser()

    password = getpass.getpass(passprompt + ": ")

    return user, password


def create_switch_engine_driver():
    print("creating a driver for SwtichEngine ...")
    user, password = get_credentials()
    return get_driver(Provider.OPENSTACK)(
        user,
        password,
        ex_tenant_name=user,
        ex_force_auth_url="https://keystone.cloud.switch.ch:5000/v2.0/tokens",
        ex_force_auth_version='2.0_password',
        ex_force_service_region="LS"
    )


def create_amazon_driver():
    print("creating a driver for amazon ...")
    accessid, accesskey = get_credentials('access id', 'access key')
    return get_driver(Provider.EC2_EU)(accessid, accesskey)


if __name__ == '__main__':

    if len(sys.argv) < 1:
        print("please specify a cloud provider")
        print(SUPPORTED_PROVIDER_MESSAGE)
    else:
        if sys.argv[1] == 'switch':
            driver = create_switch_engine_driver()
            stack = Stack.load(driver, 'switch-engine-stack.yml')
        elif sys.argv[1] == 'amazon':
            driver = create_amazon_driver()
            stack = Stack.load(driver, 'amazon-stack.yml')
        else:
            raise Exception("Not supported cloud provider : " + sys.argv[3] + " (" + SUPPORTED_PROVIDER_MESSAGE + ")")

        if stack:
            stack.run()
