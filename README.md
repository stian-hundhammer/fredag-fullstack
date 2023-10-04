# fredag-fullstack
Kind of web site, full stack Kotlin

My first take on this, so this project is very much based on the following:

# Full Stack JVM & JS App Hands-On Lab

This repository is the code corresponding to the hands-on lab [Building a Full Stack Web App with Kotlin Multiplatform](https://play.kotlinlang.org/hands-on/Full%20Stack%20Web%20App%20with%20Kotlin%20Multiplatform/).

**The master branch is to be used as a template. If you would like to see the completed project, check out the [final](https://github.com/kotlin-hands-on/jvm-js-fullstack/tree/final) branch.**

# YouTube reference
https://www.youtube.com/watch?v=HEH57g-UP4Q

# Deployment

## Environment variables
`
FREDAG_LEGACY_DATA_LOCATION = "/path/to/directory/with/json-file.txt"
FREDAG_PICT = "/path/do/directory/with/pictures"
FREDAG_MUSIC = "/path/to/directory/with/music"
PORT = web server port. Defaults to 8080
`

## Build for deployment
`./gradlew clean build installDist`

## start server

### with script
Make sure you set environment variables before calling `./bin/fredag`

### with Gradle
`./gradlew run`

