# start the server
echo "starting server"
cd /home/ec2-user/theratden/build/libs
#show the contents for debugging
ls
# run as background with the "&" to prevent timeout?
sudo java -jar ./shiritori.jar & 