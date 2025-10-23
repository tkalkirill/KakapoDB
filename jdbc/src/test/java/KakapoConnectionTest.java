import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import org.kakapo.db.internal.test.common.BaseKakapoDbAbstractTest;
import org.kakapo.db.jdbc.KakapoConnection;
import org.kakapo.db.jdbc.KakapoStatement;

/** For {@link KakapoConnection} testing. */
public class KakapoConnectionTest extends BaseKakapoDbAbstractTest {
    @Test
    void createStatement() throws Exception {
        try (
                KakapoConnection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            assertInstanceOf(KakapoStatement.class, statement);
        }
    }

    private static KakapoConnection getConnection() throws Exception {
        return (KakapoConnection) DriverManager.getConnection("jdbc:kakapo");
    }
}
