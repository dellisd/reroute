config.resolve = {
    ...config.resolve,
    alias: {
        "sql.js": "@jlongster/sql.js"
    },
    fallback: {
        fs: false,
        path: false,
        crypto: false,
    }
};
