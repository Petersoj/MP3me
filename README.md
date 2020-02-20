<p align="center"><img src="https://i.imgur.com/5ZzuMhk.png"></p><h1 align="center">MP3me</h1>

Download Youtube, Soundcloud, and others as a song (add album cover image, artist name, and more). Uses [youtube-dl](https://github.com/ytdl-org/youtube-dl), [SWT](https://www.eclipse.org/swt/), and `jpackage` with [OpenJDK 14](https://jdk.java.net/14/).

## Building
```shell script
git clone <this repository URL>
cd MP3me
./gradlew build
```
To create a packaged image for your host OS in the `build/jpackage` directory:
```shell script
./gradlew jpackage
```
