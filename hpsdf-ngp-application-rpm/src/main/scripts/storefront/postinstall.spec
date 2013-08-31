echo ++++++++ Begin Post Installation ++++++++
source /root/storefrontInstallation.sh
cp /opt/storefront/tmp/storefront /etc/init.d/storefront
chown root:root /etc/init.d/storefront
chmod 755 /etc/init.d/storefront
sed -i "s/sdpadmin/$user/g" /etc/init.d/storefront
rm -fr /opt/OC/storefront/tmp
ln -sf /etc/init.d/storefront /etc/rc.d/rc3.d/S20storefront
ln -sf /etc/init.d/storefront /etc/rc.d/rc4.d/S20storefront
ln -sf /etc/init.d/storefront /etc/rc.d/rc5.d/S20storefront
ln -sf /etc/init.d/storefront /etc/rc.d/rc6.d/S20storefront
mkdir /opt/storefront/jboss/server/all/log
mkdir /opt/storefront/assetcatalog
mkdir /opt/storefront/jboss/server/all/deploy-hasingleton
mkdir /opt/storefront/jboss/server/storeclient/deploy-hasingleton
ln -sf /opt/storefront/jboss/server/all/log /opt/storefront/log
ln -sf /opt/storefront/jboss/server/all/customize /opt/storefront/customize
ln -sf /opt/storefront/jboss/server/all/deploy /opt/storefront/deploy
ln -sf /opt/storefront/theme/storefront /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/portal-cms.sar/portal/cms/conf/default-content/default/storefront
ln -sf /opt/storefront/assetcatalog /opt/storefront/jboss/server/all/deploy/jboss-web.deployer/ROOT.war/assetcatalog
dos2unix /opt/storefront/*.sh
dos2unix /opt/storefront/cas/file/*.sh
dos2unix /opt/storefront/cas/scripts/*.sh
dos2unix /opt/storefront/portal/*.sh
chmod 744 /opt/storefront/*.sh
chown $user:$user -R /opt/storefront
rm -fr /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/portal-identity.sar/lib/jcaptcha.jar
rm -fr /opt/storefront/jboss/server/all/deploy/jboss-portal.sar/lib/commons-io.jar
rm -fr /opt/storefront/jboss/server/all/deploy/portal-hsqldb-ds.xml
rm -fr /opt/storefront/jboss/server/storeclient/deploy/portal-hsqldb-ds.xml
echo export STOREFRONT=sdpadmin >> /home/$user/.bash_profile
rm -rf /root/storefrontInstallation.sh
echo ++++++++ Installation Successfully++++++++