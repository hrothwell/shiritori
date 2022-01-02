
cd /home/ec2-user
echo "killing shiritori"
#Force to kill all "shiritori" processes, which should hopefully just be my jar
SHIRITORI_SERVICES=$(ps -ef | grep shiritori | grep -v grep | awk '{print $2}')
if [ $SHIRITORI_SERVICES != "" ]; then
	echo "killing shiritori services already running"
	sudo kill -9 $SHIRITORI_SERVICES
fi

if [ "$?" != "0" ]; then 
	echo "Killing process failed"
	exit 1
fi
#TODO I don't know if this really works?
# delete the app folder if it already exists
echo "removing theratden folder"
sudo rm -rf theratden
if [ "$?" != 0 ]; then
	echo "Removing folder failed"
	exit 1
fi
