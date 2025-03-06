package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.input.ConnectionDetails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SnowflakeService {

    List<Map<String, String>> getSnowflakeTestData() throws SQLException, ClassNotFoundException;
    List<Map<String, String>> fetchData(ConnectionDetails connectionDetails) throws SQLException, ClassNotFoundException, UnsupportedEncodingException;
    ByteArrayInputStream exportDataToCsv(ConnectionDetails connectionDetails) throws Exception;
    void setConnectionDetails(ConnectionDetails connectionDetails);
    ConnectionDetails getConnectionDetails();

}

