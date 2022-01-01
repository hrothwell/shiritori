[![CI Gradle](https://github.com/hrothwell/shiritori/actions/workflows/ci.yml/badge.svg)](https://github.com/hrothwell/shiritori/actions/workflows/ci.yml)
# shiritori
Primarily a personal project and a means to experiment working with web sockets, deploying to AWS, etc. 

# running locally in Eclipse IDE (or any other IDE, steps may differ) - may need to install Apache Tomcat
1. Clone project, open Eclipse 
2. Within Eclipse select File > Import... From here, navigate to the Gradle folder and select "Existing Gradle Project"
3. When asked for project root directory, navigate to your cloned root folder. Select finish and Gradle should handle the rest
4. Open the ShiritoriApplication.java file and run the app. Server should be running at `localhost:8080`

# building and running in Tomcat
1. Clone project as before
2. In terminal/CMD/PowerShell/etc, navigate to root project folder
3. Run `./gradlew build` 
4. a new .war file should be generated in {root}/build/libs, copy this file
5. Navigate to local Tomcat folder, paste .war file in webapps folder
6. Rename war to `ROOT.war` and delete any old "ROOT" references in webapps folder (ROOT.war, ROOT folder, etc)
7. In terminal/CMD/PowerShell/etc, navigate to Tomcat bin folder
8. run `.\catalina.bat start` to start server
9. If start successful, server is now running at `localhost:8080/`
