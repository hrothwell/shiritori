# start the server
echo "starting server"
cd /home/ec2-user/theratden/build/libs
#show the contents for debugging
ls
# run as background and also close things according to AWS docs
sudo java -jar ./shiritori.jar > /dev/null 2> /dev/null < /dev/null & 