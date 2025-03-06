package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.entity.Auditlog;
import com.ims.inventorymgmtsys.input.ConnectionDetails;
import com.ims.inventorymgmtsys.service.AuditlogService;
import net.snowflake.client.jdbc.internal.apache.http.entity.InputStreamEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/audit")
public class AuditlogController {
    private final AuditlogService auditlogService;

    public AuditlogController(AuditlogService auditlogService) {
        this.auditlogService = auditlogService;
    }

    @GetMapping("auditlog")
    public String getAuditlog(Model model) {
        model.addAttribute("auditlogjson", auditlogService.findAllAsJson());
        return "admin/auditlog";
    }

        @GetMapping("/download")
        public ResponseEntity<InputStreamResource> downloadAuditlog() {
        List<Auditlog> auditlogList = auditlogService.findAll();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("ID, Username, EventType, Details, CreatedAt");

        for (Auditlog log : auditlogList) {
            writer.printf("%d,%s,%s,%s,%s%n", log.getId(), log.getUsername(), log.getEventType(), log.getDetails(), log.getCreatedAt());
        }
        writer.flush();
        writer.close();

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=auditlog.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new InputStreamResource(resource));
    }



}
