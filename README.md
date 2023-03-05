# Reroute

## Run the Web App

Create a `local.properties` file with the following:
```properties
mapbox.key=<your mapbox access token here>
```

You can get an access token from [Mapbox](https://mapbox.com). 

To build and serve the app, run:

```shell
./gradlew :web:jsBrowserRun --continuous
```

Open the app in your browser at [`http://localhost:8080/reroute/`](http://localhost:8080/reroute/).

## Run the Server

Download a GTFS dataset and save it in `server/`.
Preprocess the GTFS data by running this command:

```shell
./gradlew :server:run --arg "preprocess gtfs.zip"
```

This will generate two files: `data/gtfs.db` and `data/gtfs.json`, which the server will use to serve responses to the web app.

With those two files generated, you can now start the server by running:

```shell
./gradlew :server:run --arg "server"
```
