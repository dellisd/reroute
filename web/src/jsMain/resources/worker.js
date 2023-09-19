// This is dumb, but required by indexeddb-backend in production builds
global.process = {
    env: {
        NODE_ENV: "production"
    }
};

import initSqlJs from "@jlongster/sql.js";
import {SQLiteFS} from "absurd-sql";
import IndexedDBBackend from "absurd-sql/dist/indexeddb-backend";

let db = null;

async function run() {
    let SQL = await initSqlJs({locateFile: file => file});
    let sqlFS = new SQLiteFS(SQL.FS, new IndexedDBBackend());
    SQL.register_for_idb(sqlFS);

    SQL.FS.mkdir('/sql');
    SQL.FS.mount(sqlFS, {}, '/sql');

    const path = '/sql/reroute.db';
    if (typeof SharedArrayBuffer === 'undefined') {
        let stream = SQL.FS.open(path, 'a+');
        await stream.node.contents.readIfFallback();
        SQL.FS.close(stream);
    }

    db = new SQL.Database(path, {filename: true});
    // You might want to try `PRAGMA page_size=8192;` too!
    db.exec(`PRAGMA journal_mode=MEMORY;`);

    // Your code
}

function onModuleReady() {
    const data = this["data"];
    switch (data && data["action"]) {
        case "exec":
            if (!data["sql"]) {
                throw new Error("exec: Missing query string");
            }

            return postMessage({
                id: data.id,
                results: db.exec(data.sql, data.params)[0] ?? {values: []}
            });
        case "begin_transaction":
            return postMessage({
                id: data.id,
                results: db.exec("BEGIN TRANSACTION;")
            })
        case "end_transaction":
            return postMessage({
                id: data.id,
                results: db.exec("END TRANSACTION;")
            })
        case "rollback_transaction":
            return postMessage({
                id: data.id,
                results: db.exec("ROLLBACK TRANSACTION;")
            })
        case "load_data":
            fetch("/api/data/")
            .then(response => response.json())
            .then(content => { console.dir(content); return content })
            .then(({stops, routes, routeVariants, routesAtStops}) => {
                stops.forEach(({id, code, name, position, parent}) => {
                    db.exec("INSERT INTO Stop VALUES (?, ?, ?, ?, ?, ?)", [id, code, name, position[1], position[0], parent || null]);
                });
                routes.forEach(({gtfsId, identifier, destinations}) => {
                    db.exec("INSERT INTO Route VALUES (?, ?, ?)", [gtfsId, identifier, JSON.stringify(destinations)]);
                });
                routeVariants.forEach(({id, gtfsId, directionId, headsign, weight, shape}) => {
                  db.exec("INSERT INTO RouteVariant VALUES (?, ?, ?, ?, ?, ?)", [id, gtfsId, directionId, headsign, weight, JSON.stringify(shape)])
                });

                routesAtStops.forEach(({stopId, routeVariantId, index}) => {
                  db.exec("INSERT INTO RouteVariantAtStop VALUES (?, ?, ?)", [stopId, routeVariantId, index]);
                });

                self.postMessage(({
                    id: data.id
                }));
            });
            break;
        default:
            throw new Error("Invalid action : " + (data && data["action"]));
    }
}

function onError(err) {
    return postMessage({
        id: this.data.id,
        error: err
    });
}

if (typeof importScripts === "function") {
    db = null;
    const sqlModuleReady = run()
    self.onmessage = (event) => {
        return sqlModuleReady
            .then(onModuleReady.bind(event))
            .catch(onError.bind(event));
    }
}
