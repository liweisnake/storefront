echo ++++++++ Begin Previous Installation ++++++++

exec 6<&0 0</dev/tty

echo "Please entry the username who is owner of the storefront portal, the password will same with username";
while read inputline
do
user="$inputline"
echo "you entry" $user
if [ "$user" = "" ] ;
then
	echo "you must entry the username"
else
	# Create user group according user entry
	usersdpadmin=`cat /etc/passwd | grep $user`
	if [ -z $usersdpadmin ]; then
        groupadd $user; 
		useradd -g $user -d /home/$user -l $user;
		echo $user|passwd --stdin $user;
        echo "create user $user"
	fi
	usersdpadmin=`cat /etc/passwd | grep $user`
	if [ -z $usersdpadmin ]; then
        useradd -g $user -d /home/$user -l $user;
		echo $user|passwd --stdin $user;
        echo "create user $user"
	fi
	break
fi
done

exec 0<&6 6<&- 
echo user=$user >> /root/storefrontInstallation.sh

