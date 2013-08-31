#!/bin/sh
Y=`date +%y`
month=`date +%m`
d=`date +%d`
H=`date +%H`
M=`date +%M`
S=`date +%S`
pids=`ps -ef|grep storefront/jboss|grep -v "grep"|awk '{print $2}'`
user=`whoami`

if [ "$STOREFRONT" != "sdpadmin" ] ; then
        echo "you have not permission to run it."
        exit
fi
if [ "$pids" = "" ] ; then
        echo "$Y-$month-$d $H:$M:$S starting HP Store Front Portal...">>/opt/storefront/operation.log
        echo "Starting HP Store Front Portal..."
        export JBOSS_HOME="/opt/storefront/jboss/"
        export JAVA_OPTS="-Xms1024m -Xmx1024m -XX:PermSize=64m -XX:MaxPermSize=256m"
        nohup /opt/storefront/jboss/bin/run.sh -c all -g storefrontgroup -b 0.0.0.0 > /dev/null &
	echo "please wait at least 3 minutes to execute checkStatus.sh script to check the server detailed status."
else
         echo "HP Store Front Portal has already been started, please execute checkStatus.sh script to check the server detailed status."
fi
