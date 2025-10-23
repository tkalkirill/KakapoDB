package org.kakapo.db.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import org.jetbrains.annotations.Nullable;

public class KakapoDriver implements Driver {
    private static final String URL_PREFIX = "jdbc:kakapo";

    private static final KakapoDriver INSTANCE = new KakapoDriver();

    private static final Object REGISTER_DRIVER_MUX = new Object();

    /** Guarded by {@link #REGISTER_DRIVER_MUX}. */
    private static boolean registered;

    static {
        registerDriver();
    }

    @Override
    public @Nullable KakapoConnection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }

        return new KakapoConnection();
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (url == null) {
            throw new SQLException("URL is null");
        }

        return url.startsWith(URL_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        throw new SQLException("Not implemented");
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        // Kakapo JDBC driver is not fully JDBC compliant.
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("No logger support");
    }

    /** Returns an instance of the driver and registers it if this has not happened yet. */
    public static KakapoDriver get() {
        registerDriver();

        return INSTANCE;
    }

    private static void registerDriver() {
        synchronized (REGISTER_DRIVER_MUX) {
            if (registered) {
                return;
            }

            try {
                DriverManager.registerDriver(INSTANCE);

                registered = true;
            } catch (SQLException e) {
                throw new RuntimeException("Failed to register Kakapo JDBC driver", e);
            }
        }
    }
}
