#!/bin/sh

set -x
set -e

IP=`ip addr show dev eth0 | grep "\<inet\>" | awk '{print $2}' | sed -e 's,/.*$,,'`
SERVERIP=`echo $IP | sed -E 's,[^.]+$,1,'`
VLAN=`echo $IP | awk -F . '{print $3}'`
SVR_BASEPATH="/home/gpw2018/nfs"

mount -t nfs -o rw,nolock,nfsvers=3 ${SERVERIP}:${SVR_BASEPATH}/data_prd_${VLAN} /data
