# runs the gradle build command
echo "starting gradleBuild.sh"
cd /home/ec2-user/theratden
pwd
sudo ./gradlew --stop
sudo ./gradlew build