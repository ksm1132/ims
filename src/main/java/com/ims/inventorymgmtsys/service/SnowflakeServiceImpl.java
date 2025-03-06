package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.controller.SessionController;
import com.ims.inventorymgmtsys.input.ConnectionDetails;
import com.opencsv.CSVWriter;
import com.ims.inventorymgmtsys.repository.SnowflakeRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class SnowflakeServiceImpl implements SnowflakeService {

    private final SnowflakeRepository snowflakeRepository;
    public final SessionController sessionController;

    public SnowflakeServiceImpl(SnowflakeRepository snowflakeRepository, SessionController sessionController) {
        this.snowflakeRepository = snowflakeRepository;
        this.sessionController = sessionController;
    }

    @Override
    public List<Map<String, String>> getSnowflakeTestData() throws SQLException, ClassNotFoundException {
        return snowflakeRepository.findAll();
    }

    @Override
    public List<Map<String, String>> fetchData(ConnectionDetails connectionDetails) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        ConnectionDetails sessionConnectionDetails = sessionController.getConnectionDetails();
        return snowflakeRepository.findData(sessionConnectionDetails);
    }

    @Override
    public ByteArrayInputStream exportDataToCsv(ConnectionDetails connectionDetails) throws Exception {
        List<Map<String, String>> data = fetchData(connectionDetails);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {
            if (data != null && !data.isEmpty()) {
                String[] header = data.get(0).keySet().toArray(new String[0]);
                writer.writeNext(header);

                for (Map<String, String > row : data) {
                    String[] values = row.values().toArray(new String[0]);
                    writer.writeNext(values);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to export data to CSV", e);
        }
        return new ByteArrayInputStream(out.toByteArray());

    }

    @Override
    public void setConnectionDetails(ConnectionDetails connectionDetails) {
        sessionController.setConnectionDetails(connectionDetails);
    }

    @Override
    public ConnectionDetails getConnectionDetails(){
        return sessionController.getConnectionDetails();
    }

}
