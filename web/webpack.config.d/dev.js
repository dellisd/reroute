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
        },
        "/api/realtime": {
            target: "ws://localhost:8888/realtime/",
            pathRewrite: { '^/api/realtime': '' },
            ws: true,
        }
    },
    open: false
}
