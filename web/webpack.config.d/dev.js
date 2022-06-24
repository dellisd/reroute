config.devServer = {
    ...config.devServer,
    proxy: {
        "/reroute": {
            target: "http://localhost:8080/",
            pathRewrite: {"^/reroute": ""}
        },
        "/api": {
            target: "http://localhost:8888/",
            pathRewrite: {"^/api": ""}
        }
    },
    open: false
}
