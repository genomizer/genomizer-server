#!/bin/bash

iptables -P INPUT ACCEPT

iptables -F

# TODO check if 7000 is really needed, (Ludde is contact person)
iptables -N INPUT_ports
iptables -A INPUT_ports -p tcp -d 22 -j ACCEPT
iptables -A INPUT_ports -p tcp -d 80 -j ACCEPT
iptables -A INPUT_ports -p tcp -d 443 -j ACCEPT

iptables -N INPUT_dev
iptables -A INPUT_dev -s 130.239.39.0/24 -j INPUT_ports
iptables -A INPUT_dev -s 130.239.40.0/24 -j INPUT_ports
iptables -A INPUT_dev -s 130.239.41.0/24 -j INPUT_ports

iptables -N INPUT_users
iptables -A INPUT_users -s 130.239.192.0/24 -j INPUT_ports

# TODO iptables -A INPUT smth smth "ctstate RELATED, ACCEPTED
# TODO REJECT     all  --  anywhere             anywhere             reject-with icmp-host-prohibited
iptables -A INPUT -s 127.0.0.0\8 -j ACCEPT
iptables -A INPUT -j INPUT_dev
iptables -A INPUT -j INPUT_users


# TODO iptables -P INPUT DROP

# TODO persist


