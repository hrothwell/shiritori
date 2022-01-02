# runs the gradle build command
echo "starting gradleBuild.sh"
cd /home/ec2-user/theratden
pwd
sudo ./gradlew --stop
#Test should not be needed, tests are ran during github actions's build step 
# Clean means it will delete the current build folder? 
sudo ./gradlew clean -x test build
if [ "$?" != "0" ]; then 
	echo "error building jar"
	exit 1
fi 