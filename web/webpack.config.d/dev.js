config.devServer = {
    ...config.devServer,
    proxy: [
      {
        context: ["/reroute"],
        target: "http://localhost:8080",
        pathRewrite: {"^/reroute": ""}
      },
      {
        context: ["/api"],
        target: "http://localhost:8888",
        pathRewrite: {"^/api": ""}
      },
    ],
    open: false
}
