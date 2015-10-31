#!/bin/bash

# author: Jonathan Cornaz
# date: 11.10.2015
# description: Start all the lab environment

# Load openstack environment variables
source ./openstack-rc-file.sh

# Load nova helpers
source ./nova-helper.sh

keypair='keypair.pem'
sshuser='ubuntu'
sshargs="-q -i $keypair -oStrictHostKeyChecking=no"
publicip=86.119.29.193

function waiton-ssh () {
  if [ $# -eq 0 ]; then
    echo 'The ip address must be supplied as argument'
  else
    until ssh $sshargs $sshuser@$1 exit; do
      sleep 1s
    done
  fi
}

function start-server () {
  if [ $# -ne 3 ]; then
    echo "You must give 3 arguments : server name, floating ip, command"
  else
    waiton $1
    nova add-floating-ip $1 $2
    waiton-ssh $2
    ssh $sshargs $sshuser@$2 "nohup $3 </dev/null >logfile.log 2>&1 &"
  fi
}

echo "Instantiations ..."
common_args="--nic net-id=25620c4e-2c3b-4403-9f7d-dd1f0068f40c"
nova boot --flavor=c1.small --boot-volume=6e897373-2ee2-4cd6-9c21-9421cf886437 --security-groups=default,mongodb $common_args MongoDB > /dev/null
nova boot --flavor=c1.micro --boot-volume=5d567d95-7506-4a38-89ab-12958b3ca3bd --security-groups=default $common_args RESTClient > /dev/null
nova boot --flavor=c1.micro --boot-volume=bccb9518-17d3-4545-a768-040a3ef9fd08 --security-groups=default,restserver $common_args RESTServer > /dev/null

# boot virtual machines
echo "Wait for MongoDB ip ..."
waiton MongoDB
mongodbip=$(instance-ip MongoDB)
echo "MongoDB ip is : $mongodbip"

echo "Start REST Client ..."
start-server RESTClient $publicip "python restclient.py $mongodbip"
nova remove-floating-ip RESTClient $publicip

echo "Start REST Server ..."
start-server RESTServer $publicip "python restserver.py $mongodbip"

echo "Services available at :"
for piid in pi1 pi2 pi3; do
  echo "http://$publicip:18000/getLastSensorsValues/$piid"
done

input=""
until [ "$input" == "A" ]; do
  read -p "Press 'A' (Abort) to stop instances : " -n1 -s input
  echo ""
done

echo "Deleting instances ... "
nova delete RESTServer RESTClient MongoDB

echo "Instances deleted"
