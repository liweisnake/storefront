#!/bin/sh


usage()
{
        echo "usage:`basename $0` [options]"
        echo -e "\t-storefront Configuration storefront portal page."
        echo -e "\t-oam Configuration oam portal page"
}


Y=`date +%y`
month=`date +%m`
d=`date +%d`
H=`date +%H`
M=`date +%M`
S=`date +%S`

OPT=$1

if [ $# -ge 1 ] ;
then
    case $OPT in
    -storefront)
        rm -rf /opt/storefront/deploy/jboss-portal.sar/portal-server.war/WEB-INF/storefront-object.xml
        ln /opt/storefront/jboss/server/all/customize/storefront-object.xml /opt/storefront/deploy/hpsdf-ngp-portal.war/WEB-INF
        cp default-object_storefront.xml /opt/storefront/deploy/jboss-portal.sar/conf/data/default-object.xml
        echo "finish the storefront portal configuration, please restart the system"
        ;;
     -oam)
        rm -rf /opt/storefront/deploy/jboss-portal.sar/portal-server.war/WEB-INF/oam-object.xml
        ln /opt/storefront/jboss/server/all/customize/oam-object.xml /opt/storefront/deploy/hpsdf-ngp-portal.war/WEB-INF
        cp default-object_oam.xml /opt/storefront/deploy/jboss-portal.sar/conf/data/default-object.xml
        echo "finish the oam portal configuration, please restart the system"
        ;;
     -help)
        usage
       ;;
   esac
else
   echo "you should type -help for help."

fi
