package com.aiksanov.pptxProcessorsService.web.controller;

import com.aiksanov.pptxProcessorsService.data.PptConfigurationData;
import com.company.data.ProjectGeneral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ppt")
public class PptGeneratorController {
    private static Logger LOGGER = LoggerFactory.getLogger(PptGeneratorController.class);

    @GetMapping("/hello")
    public String getHello() {
        LOGGER.info("/hello");
        return "Hello!";
    }

    @PostMapping("/test")
    public void getDataForPpt(@RequestBody PptConfigurationData data) {
        LOGGER.info("POST /api/ppt/test");
        System.out.println(data);
    }
}
