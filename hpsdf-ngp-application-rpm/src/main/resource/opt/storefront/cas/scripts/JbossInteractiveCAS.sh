#!/usr/bin/perl -w
print "start configuration cas interactive\n";
print "start generate client key store, please ensure the server.crt file is under the same directory which is get from the cas server side\n";
system("rm -rf /opt/storefront/cas/scripts/server.keystore");
system("keytool -import -alias cascert -keypass changeit -file /opt/storefront/cas/scripts/server.crt -keystore /opt/storefront/cas/scripts/server.keystore -storepass changeit -noprompt");
system("cp /opt/storefront/cas/file/jboss-service.xml /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/META-INF/");
system("cp /opt/storefront/cas/file/context.xml /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/portal-server.war/WEB-INF/");
system("cp /opt/storefront/cas/file/start.sh /opt/storefront/");
system("cp /opt/storefront/cas/file/casclient.jar /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/lib");
system("chown sdpadmin:sdpadmin -R /opt/storefront");
print "finish configuration cas interactive\n";
