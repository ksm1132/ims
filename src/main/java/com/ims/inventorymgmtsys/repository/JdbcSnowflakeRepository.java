package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.input.ConnectionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

@Repository
public class JdbcSnowflakeRepository implements SnowflakeRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcSnowflakeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static final String url = System.getenv("SNOW_URL");

    @Override
    public List<Map<String, String >> findAll() throws SQLException, ClassNotFoundException {
        List<Map<String, String >> data = new ArrayList<>();
        Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.SUPPLIER")) {

            var metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i=1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = rs.getString(i);
                    row.put(columnName, columnValue);
                }
                data.add(row);
            }
        }
        return data;
    }

    @Override
    public List<Map<String, String>> findData(ConnectionDetails connectionDetails) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        List<Map<String, String>> data = new ArrayList<>();
        Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
        String encodedUsername = URLEncoder.encode(connectionDetails.getUsername(), StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(connectionDetails.getPassword(), StandardCharsets.UTF_8);
        String db = connectionDetails.getDbname();
        String schema = connectionDetails.getSchemaname();
        String table = connectionDetails.getTablename();
        String baseUrl = connectionDetails.getUrl();
        String parameters = "user=" + encodedUsername + "&password=" + encodedPassword + "&JDBC_QUERY_RESULT_FORMAT=JSON";
        String fullUrl;

        if (baseUrl.contains("?")) {
            fullUrl = baseUrl + "&" + parameters;
        } else {
            fullUrl = baseUrl + "?" + parameters;
        }

        try (Connection conn = DriverManager.getConnection(fullUrl);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " +  db + "." + schema + "." + table)) {

            var metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = rs.getString(i);
                    row.put(columnName, columnValue);
                }
                data.add(row);
            }
        }
        return data;
    }
}
