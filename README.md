# Reroute

## Setup and Run

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
