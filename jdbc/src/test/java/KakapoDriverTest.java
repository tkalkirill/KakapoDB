import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.kakapo.db.jdbc.KakapoConnection;
import org.kakapo.db.jdbc.KakapoDriver;

/** For {@link KakapoDriver} testing. */
public class KakapoDriverTest {
    @Test
    void driverAvailableThroughServiceLoader() {
        try (Stream<Provider<Driver>> stream = ServiceLoader.load(Driver.class).stream()) {
            assertTrue(stream.map(Provider::type).anyMatch(KakapoDriver.class::equals));
        }
    }

    @Test
    void acceptsNullUrl() {
        assertThrows(SQLException.class, () -> KakapoDriver.get().acceptsURL(null));
    }

    @Test
    void acceptsInvalidUrl() throws Exception {
        assertFalse(KakapoDriver.get().acceptsURL("invalid:url"));
    }

    @Test
    void acceptsValidUrl() throws Exception {
        assertTrue(KakapoDriver.get().acceptsURL("jdbc:kakapo"));
    }

    @Test
    void jdbcCompliant() {
        assertFalse(KakapoDriver.get().jdbcCompliant());
    }

    @Test
    void connectWithInvalidUrl() throws Exception {
        assertNull(KakapoDriver.get().connect("jdbc:invalid:url", new Properties()));
    }

    @Test
    void connectWithValidUrl() throws Exception {
        try (KakapoConnection connection = KakapoDriver.get().connect("jdbc:kakapo", new Properties())) {
            assertNotNull(connection);
        }
    }

    @Test
    void getConnection() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:kakapo")) {
            assertInstanceOf(KakapoConnection.class, connection);
        }
    }
}
