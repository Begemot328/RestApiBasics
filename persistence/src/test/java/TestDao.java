import com.epam.esm.persistence.pool.DbParameter;
import com.epam.esm.persistence.resource.DbResourceManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;
import java.util.Scanner;


public class TestDao {
    private static final String TEST_DATABASE = "SQL/test_db.sql";
    private static final String BACKUP_DATABASE = "SQL/db.sql";
    private JdbcTemplate template;

    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                DbResourceManager.getInstance().getValue(DbParameter.DB_DRIVER));
        dataSource.setUrl(
                DbResourceManager.getInstance().getValue(DbParameter.DB_URL));
        dataSource.setUsername(
                DbResourceManager.getInstance().getValue(DbParameter.DB_USER));
        dataSource.setPassword(
                DbResourceManager.getInstance().getValue(DbParameter.DB_PASSWORD));
        Properties properties = new Properties();
        properties.setProperty("serverTimezone",
                DbResourceManager.getInstance().getValue(DbParameter.DB_TIMEZONE));
        properties.setProperty("useUnicode",
                DbResourceManager.getInstance().getValue(DbParameter.DB_USE_UNICODE));
        dataSource.setConnectionProperties(properties);
        template = new JdbcTemplate(dataSource);
    }


    private void setDatabase(String database) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(new File(database)));
        scanner.useDelimiter(";");
        while (scanner.hasNext()) {
            template.update(scanner.next().concat(";"));
        }
    }

    protected JdbcTemplate getJdbcTemplate() {
        return template;
    }

    @BeforeAll
    public void setTestDatabase() throws FileNotFoundException {
        setDatabase(TEST_DATABASE);
    }

    @AfterAll
    public void setBackupDatabase() throws FileNotFoundException {
        setDatabase(BACKUP_DATABASE);
    }
}
