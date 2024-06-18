package f1b4.webide_server;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class DataSourceTest{
    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException {
        Assertions.assertThat(dataSource.getConnection()).isNotNull();
    }
}
