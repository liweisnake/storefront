#!/bin/sh
export JBOSS_HOME=/opt/storefront/jboss
pids=`ps -ef|grep storefront/jboss|grep -v "grep"|awk '{print $2}'`
if [ "$pids" = "" ] ; then
                 echo "HP Store Front Portal Server is not started"
else

         result=`/opt/storefront/jboss/bin/twiddle.sh -q --user=admin --password=admin --server=jnp://localhost:1299 get jboss.management.local:J2EEServer=Local,j2eeType=J2EEApplication,name=hpsdf-ngp.ear  objectName`
         if [ "$result" = "objectName=jboss.management.local:J2EEServer=Local,j2eeType=J2EEApplication,name=hpsdf-ngp.ear" ] ; then
                echo "HP Store Front Portal Server is running"
         else
                echo "HP Store Front Portal Server has already been started, but encountered some problems"
         fi
fi


