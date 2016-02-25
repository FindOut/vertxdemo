package app;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLConnection;

public class DemoDatabaseServiceImpl implements DemoDatabaseService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private AsyncSQLClient client;

    public DemoDatabaseServiceImpl(Vertx vertx) {
        final String user = System.getenv("MYSQL_USER");
        final String password = System.getenv("MYSQL_PASSWORD");
        final String database = System.getenv("MYSQL_DATABASE");
        log.info(System.getenv());
        final String db_port = System.getenv("DB_PORT");
        if (db_port == null) {
            throw new RuntimeException("DB_PORT was null. This indicates the environment properties created by Docker is missing.");
        }
        final String db_port_num = db_port.split(":")[2];
        final String tcpAddr = db_port.split(":")[1].replaceAll("//", "");

        log.info("Trying MYSQL_USER {0} and MYSQL_PASSWORD {1} for MYSQL_DATABASE {2}, DB_PORT_{3}_TCP_ADDR {4}",
                user, password, database, db_port_num, tcpAddr);

        JsonObject mySQLClientConfig = new JsonObject()
                .put("host", tcpAddr)
                .put("username", user)
                .put("password", password)
                .put("database", database
                );
        client = MySQLClient.createShared(vertx, mySQLClientConfig, "MySQLPool1");
    }

    @Override
    public void save(String collection, JsonObject document, Handler<AsyncResult<Void>> resultHandler) {
        System.out.println("Service got: " + document.encodePrettily());

        client.getConnection(conn -> {
            if (conn.failed()) {
                log.error(conn.cause().getMessage());
                return;
            }

            SQLConnection connection = conn.result();

            // Got a connection
            connection.execute("insert into demodata values (NOW(), '"+document.encodePrettily()+"')", insert -> {
                if (insert.failed()) {
                    log.error("SELECT failed due to {0}", insert.cause());
                    resultHandler.handle(Future.failedFuture(insert.cause()));
                }
                else {
                    resultHandler.handle(Future.succeededFuture());
                }

                connection.close(done -> {
                    if (done.failed()) {
                        throw new RuntimeException(done.cause());
                    }
                });
            });

        });
    }

    @Override
    public void close() {
        client.close();
    }
}
