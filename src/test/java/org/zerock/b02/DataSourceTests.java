package org.zerock.b02;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Slf4j
public class DataSourceTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void conncetionTest() throws SQLException {

        @Cleanup
        Connection con = dataSource.getConnection();

        log.info("Connection con ...................... {} ", con);

        Assertions.assertNotNull(con);
    }
}
