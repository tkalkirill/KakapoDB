import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import org.kakapo.db.internal.test.common.BaseKakapoDbAbstractTest;
import org.kakapo.db.internal.test.common.TestConsumerX;
import org.kakapo.db.jdbc.KakapoConnection;
import org.kakapo.db.jdbc.KakapoResultSet;
import org.kakapo.db.jdbc.KakapoStatement;

/** For {@link KakapoStatement} testing. */
public class KakapoStatementTest extends BaseKakapoDbAbstractTest {
    @Test
    void createStatement() throws Exception {
        try (
                KakapoConnection connection = (KakapoConnection) DriverManager.getConnection("jdbc:kakapo");
                Statement statement = connection.createStatement()
        ) {
            assertInstanceOf(KakapoStatement.class, statement);
        }
    }

    @Test
    void getConnection() throws Exception {
        executeWithStatement(statement -> assertInstanceOf(KakapoConnection.class, statement.getConnection()));
    }

    @Test
    void executeQuery() throws Exception {
        executeQuery("SELECT 1", resultSet -> assertInstanceOf(KakapoResultSet.class, resultSet));
    }

    @Test
    void executeUpdate() throws Exception {
        assertEquals(0, executeUpdate("UPDATE table SET column = value"));
    }

    @Test
    void execute() throws Exception {
        assertFalse(execute("SELECT 1"));
    }

    private static boolean execute(String sql) throws Exception {
        boolean[] result = new boolean[1];

        executeWithStatement(statement -> result[0] = statement.execute(sql));

        return result[0];
    }

    private static int executeUpdate(String sql) throws Exception {
        int[] updateCount = new int[1];

        executeWithStatement(statement -> updateCount[0] = statement.executeUpdate(sql));

        return updateCount[0];
    }

    private static void executeQuery(String sql, TestConsumerX<KakapoResultSet> consumerX) throws Exception {
        try (
                KakapoConnection connection = (KakapoConnection) DriverManager.getConnection("jdbc:kakapo");
                KakapoStatement statement = connection.createStatement();
                KakapoResultSet resultSet = statement.executeQuery(sql)
        ) {
            consumerX.accept(resultSet);
        }
    }

    private static void executeWithStatement(TestConsumerX<KakapoStatement> consumerX) throws Exception {
        try (
                KakapoConnection connection = (KakapoConnection) DriverManager.getConnection("jdbc:kakapo");
                KakapoStatement statement = connection.createStatement()
        ) {
            consumerX.accept(statement);
        }
    }
}
