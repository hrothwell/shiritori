# delete the app folder if it already exists
echo "removing theratden folder"
cd /home/ec2-user
#TODO I don't know if this really works?
sudo rm -rf theratden
#Force to kill all "shiritori" processes, which should hopefully just be my jar
sudo kill -9 (ps -ef | grep shiritori | grep -v grep | awk '{print $2}')