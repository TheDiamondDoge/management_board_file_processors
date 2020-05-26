package com.iksanov.excellProcessors.controller;

import com.iksanov.excellProcessors.service.MainService;
import data.*;
import exceptions.NoSheetFoundException;
import exceptions.WrongBDValueException;
import exceptions.WrongFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.RisksFileGenerator;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/processors")
public class MainController {
    public static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping("/risks")
    public RisksDTO extractRisksFromFile(MultipartFile file) throws WrongFileFormatException, IOException, NoSheetFoundException {
        LOGGER.info("POST /processors/risks Filename: {}", file.getOriginalFilename());
        return this.mainService.processRisksFile(file);
    }

    @PostMapping("/risksFile/{projectName}")
    public ByteArrayResource getRisksFile(@RequestBody List<Risk> risks, @PathVariable String projectName) throws IOException, NoSheetFoundException {
        LOGGER.info("GET /processors/risksFile/{}", projectName);
        return this.mainService.getRiskFile(risks, projectName);
    }

    @PostMapping("/cost/{bd}")
    public CostDTO extractCostFromFile(MultipartFile file, @PathVariable String bd) throws NoSheetFoundException, IOException, WrongBDValueException {
        LOGGER.info("POST /processors/cost/{} Filename: {}", bd, file.getOriginalFilename());
        return this.mainService.processCostFile(file, bd);
    }

    @PostMapping("/plainXlsx")
    public ByteArrayResource getPlainXlsxFile(@RequestBody PlainXlsxDataDTO data) throws IOException {
        LOGGER.info("POST /processors/plainXlsx");
        return this.mainService.getPlainXlsxFile(data);
    }

    @PostMapping("/contribProjects")
    public ByteArrayResource getContribProjectsFile(@RequestBody ContribProjectsDataDTO data) throws IOException {
        LOGGER.info("POST /processors/contribProjects");
        return this.mainService.getContribProjects(data);
    }
}
