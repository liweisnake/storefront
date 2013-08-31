#!/usr/bin/perl -w
use Sys::Hostname;
$host=hostname;
print "start install cas\n";	
system("rm -rf /opt/storefront/cas/scripts/server.keystore");
system("rm -rf /opt/storefront/cas/scripts/server.crt");
$command="keytool -genkey -alias caskey -keypass changeit -keyalg RSA -keystore /opt/storefront/cas/scripts/server.keystore -storepass changeit -dname CN=".$host.",OU=cmet,O=hp,L=sh,ST=sh,C=cn";
system($command);
print "start generate certification\n";
system("keytool -export -alias caskey -keypass changeit -file /opt/storefront/cas/scripts/server.crt -keystore /opt/storefront/cas/scripts/server.keystore -storepass changeit");
system("cp /opt/storefront/cas/file/server.xml /opt/storefront/jboss/server/all/deploy/jboss-web.deployer/");
system("cp /opt/storefront/cas/file/jboss-service.xml /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/META-INF/");
system("cp -R /opt/storefront/cas/file/cas.war /opt/storefront/jboss/server/all/deploy");
system("cp /opt/storefront/cas/scripts/server.keystore  /opt/storefront/jboss/server/all/conf");
system("cp /opt/storefront/cas/scripts/server.crt  /opt/storefront/jboss/server/all/conf");
system("chown sdpadmin:sdpadmin -R /opt/storefront");
print "finish install cas \n";

