
cd /home/ec2-user
echo "killing shiritori"
#Force to kill all "shiritori" processes, which should hopefully just be my jar
sudo kill -9 $(ps -ef | grep shiritori | grep -v grep | awk '{print $2}')
if [ "$?" != "0" ]; then 
	echo "Killing process failed"
	exit 1
#TODO I don't know if this really works?
# delete the app folder if it already exists
echo "removing theratden folder"
sudo rm -rf theratden
if [ "$?" != 0 ]; then
	echo "Removing folder failed"
	exit 1
