package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.input.ConnectionDetails;
import com.ims.inventorymgmtsys.service.SnowflakeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/external")
public class ExternalCollaboController {
    private static final Logger logger = LogManager.getLogger(ExternalCollaboController.class);
    private final SnowflakeService snowflakeService;

    public ExternalCollaboController(SnowflakeService snowflakeService) {
        this.snowflakeService = snowflakeService;
    }

    @GetMapping("/snowflaketest")
    public String getSnowflakeTestData(Model model) throws SQLException, ClassNotFoundException {
        try {
            model.addAttribute("snowData", snowflakeService.getSnowflakeTestData());
            logger.info("Snowflake test data retrieved successfully.");
            return "admin/testSnowflake";
        } catch (Exception e) {
            logger.error("Error fetching Snowflake test data", e);
            model.addAttribute("error", "データの取得に失敗しました" + e.getMessage());
            return "admin/error";
        }
    }

    @GetMapping("/snowflakeinput")
    public String snowflakeInput(){
        return "admin/SnowflakeInput";
    }


    @PostMapping("/snowflake/connect")
    public String connectToSnowflake(@RequestParam String url, @RequestParam String username, @RequestParam String password, @RequestParam String dbname, @RequestParam String schemaname, @RequestParam String tablename ,Model model) throws UnsupportedEncodingException {
        ConnectionDetails connectionDetails = new ConnectionDetails(url, username, password, dbname, schemaname, tablename);
        snowflakeService.setConnectionDetails(connectionDetails);
        try {
            List<Map<String, String>> snowData = snowflakeService.fetchData(connectionDetails);
            if(snowData != null && !snowData.isEmpty()){
                model.addAttribute("snowData", snowData);
                model.addAttribute("message", "データベースの接続に成功しました。");
            } else {
                model.addAttribute("error", "データが存在しないか、指定に間違いがあります。");
            }
        } catch (SQLException | ClassNotFoundException e) {
            model.addAttribute("error", "データベース接続エラー: " + e.getMessage());
        }
        return "admin/getSnowflake";
    }

    @GetMapping("/exportSnowflakeData")
    public ResponseEntity<InputStreamResource> exportSnowflakeData() throws Exception {
        ConnectionDetails connectionDetails = snowflakeService.getConnectionDetails();

        if (connectionDetails == null) {
            throw new IllegalArgumentException("Connection details not set in session");
        }

        ByteArrayInputStream byteArrayInputStream = snowflakeService.exportDataToCsv(connectionDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=snowflake_data.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
