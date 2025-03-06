package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.input.ConnectionDetails;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SnowflakeRepository {
    List<Map<String, String >> findAll() throws SQLException, ClassNotFoundException;

    List<Map<String, String>> findData(ConnectionDetails connectionDetails) throws SQLException, ClassNotFoundException, UnsupportedEncodingException;

}
