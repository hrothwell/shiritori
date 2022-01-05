# start the server

cd /home/ec2-user/theratden/build/libs

SHIRITORI_SERVICES_BEFORE_START=$(ps -ef | grep shiritori | grep -v grep | awk '{print $2}')
echo "Here are the services RIGHT before the start calls"
echo $SHIRITORI_SERVICES_BEFORE_START
#Try stopping services here instead of clean.sh
echo "starting server"
# run as background and also close things according to AWS docs
sudo java -jar ./shiritori.jar > /dev/null 2> /dev/null < /dev/null & 