package com.aiksanov.pptxProcessorsService.web.controller;

import com.aiksanov.pptxProcessorsService.data.PptConfigurationData;
import com.aiksanov.pptxProcessorsService.service.PptCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/ppt")
public class PptGeneratorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PptGeneratorController.class);
    private final PptCreatorService service;

    @Autowired
    public PptGeneratorController(PptCreatorService service) {
        this.service = service;
    }

    @PostMapping("/multipage/custom")
    public ByteArrayResource getMultipageCustomPpt(@RequestBody PptConfigurationData data) throws IOException {
        LOGGER.info("POST /api/ppt/multipage/custom");
        return this.service.createMultipageCustomizablePpt(data);
    }

    @PostMapping("/multipage/indicators")
    public ByteArrayResource getMultipageIndicators(@RequestBody PptConfigurationData data) throws IOException {
        LOGGER.info("POST /api/ppt/multipage/indicators");
        return this.service.createMultipageIndicatorsPpt(data);
    }

    @PostMapping("/multipage/review")
    public ByteArrayResource getMultipageReview(@RequestBody PptConfigurationData data) throws IOException {
        LOGGER.info("POST /api/ppt/multipage/review");
        return this.service.createReviewPpt(data);
    }
}
