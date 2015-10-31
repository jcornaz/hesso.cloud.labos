#!/bin/bash

# author: Jonathan Cornaz
# date: 11.10.2015
# description: Function to help on using the NOVA API

# Function to return the ip of a running instance
function instance-ip () {
  if [ $# -eq 0 ]; then
    echo 'The server name must be supplied as argument'
  else
    nova list | grep -w $1 | cut -d= -f2 | cut -d ' ' -f1
  fi
}

# Function to check if an instance is active
function is-instance-up () {
  if [ $# -eq 0 ]; then
    echo 'The server name must be supplied as argument'
  else
    status=$(nova list | grep -w MongoDB | cut -d '|' -f4 | tr -d ' ')
    if [ $status == "ACTIVE" ]; then
      return 0
    else
      return 1
    fi
  fi
}

# Function that block the execution until an instance become active
function waiton () {
  if [ $# -eq 0 ]; then
    echo 'The server name must be supplied as argument'
  else
    until is-instance-up $1; do
      sleep 1s
    done
  fi
}
