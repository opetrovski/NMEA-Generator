# NMEA-Generator

This project is based on the vessel simulator from Tony Mattheys (https://sourceforge.net/projects/vesselsimulator/).

- The NMEA Generator is simulating a GPS and a compass. 
- It sends NMEA sentences to any application that connects via TCP/IP.
- It was created to learn how OpenCPN works (http://opencpn.org).


# How to build and start
- Install JDK 1.8, GIT, Maven
- Run "git clone https://github.com/opetrovski/NMEA-Generator.git"
- open a shell, change to projet folder and run "mvn clean compile assembly:single"
- change to target folder and run "java -jar nmea-generator-1.0-SNAPSHOT-jar-with-dependencies"

# Requirements
Adding a virtual AIS receiver would be fun for testing OpenCPN AIS features.
