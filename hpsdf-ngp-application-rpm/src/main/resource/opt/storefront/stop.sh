#!/bin/sh

user=`whoami`

if [ "$STOREFRONT" != "sdpadmin" ] ; then
        echo "you have not permission to run it."
        exit	
fi

usage()
{	
	echo -e "\t Shut down HP Store Front Portal immediately."
}

Y=`date +%y`
month=`date +%m`
d=`date +%d`
H=`date +%H`
M=`date +%M`
S=`date +%S`
pids=`ps -ef|grep storefront/jboss|grep -v "grep"|awk '{print $2}'`

if [ "$pids" = "" ] ; then
	echo "The HP Store Front Portal is not started."
else
	echo "Shutdown HP Store Front Portal immediately."
        for pid in $pids
        do
        	echo "kill pid $pid now"
        	kill -9 $pid
        	echo "$Y-$month-$d $H:$M:$S kill pid $pid now">>/opt/storefront/operation.log
        done
fi
