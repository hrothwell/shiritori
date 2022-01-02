# runs the gradle build command
echo "starting gradleBuild.sh"
cd /home/ec2-user/theratden
pwd
sudo ./gradlew --stop
#Test should not be needed, tests are ran during github actions's build step 
sudo ./gradlew -x test build