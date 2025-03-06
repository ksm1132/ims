package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.entity.SystemInfo;
import com.ims.inventorymgmtsys.service.SystemInfoService;
import net.snowflake.client.jdbc.internal.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/system")
public class SystemInfoController {
    private final SystemInfoService systemInfoService;

    public SystemInfoController(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @GetMapping("systeminfo")
    public String getSystemInfoData(Model model) {
        model.addAttribute("systemInfoData", systemInfoService.getSystemInfoDataAsJson());
        return "admin/systemInfo";
    }

}
