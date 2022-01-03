# start the server
echo "starting server"
cd /home/ec2-user/theratden/build/libs
#show the contents for debugging
ls
#Try stopping services here instead of clean.sh
SHIRITORI_SERVICES=$(ps -ef | grep shiritori | grep -v grep | awk '{print $2}')
if [ $SHIRITORI_SERVICES != "" ]; then
	echo "killing shiritori services already running"
	sudo kill -9 $SHIRITORI_SERVICES
fi
# run as background and also close things according to AWS docs
sudo java -jar ./shiritori.jar > /dev/null 2> /dev/null < /dev/null & 