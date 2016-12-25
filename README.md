#RPI-Campus-Paths

Are you a new student at RPI, someone who often gets lost, or a returning student who is trying to locate a building you had heard of but never visited? If so, this application is for you! RPI-Campus-Paths finds the shortest walk-able path between any two points at RPI. This application gives the user's instructions on how to get from point A to point B using known landmarks, a map, and even using the cardinal directions.

##Disclaimer

This application was made with a finite set of data points in the year 2015. It uses RPI's campus map of 2015 for it's information.

##Requirements

This application was written in Java 8, and requires a compiler which supports it

##Installation Instructions:

First, cd into the directory you would like to install this application in

Then git clone this repository and cd into it
```bash
git clone https://github.com/zwimer/RPI-Campus-Paths
cd RPI-Campus-Paths
```

Compile the program as follows
```bash
mkdir build && javac GUI/RPICampusPathsMain.java -d build/ 
```

Finally relocate the 'MapInfo' directory
```bash
mv MapInfo/ build/MapInfo/
```

##Usage
This application takes no arguments. Run it as follows
```bash
cd build
java GUI/RPICampusPathsMain
```
