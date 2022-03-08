config.devServer = {
    ...config.devServer,
    proxy: {
        "/reroute": {
            target: "http://localhost:8080/",
            pathRewrite: {"^/reroute": ""}
        }
    },
    open: false
}
