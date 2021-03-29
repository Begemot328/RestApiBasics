import com.epam.esm.persistence.pool.DbParameter;
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
