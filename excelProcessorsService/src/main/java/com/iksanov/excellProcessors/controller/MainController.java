package com.iksanov.excellProcessors.controller;

import com.iksanov.excellProcessors.service.MainService;
import data.RisksDTO;
import exceptions.NoSheetFoundException;
import exceptions.WrongFileFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.RisksExtractor;

import java.io.IOException;

@RestController
@RequestMapping("/processors")
public class MainController {
    public static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/test")
    public RisksDTO test() throws WrongFileFormat, IOException, NoSheetFoundException {
        RisksExtractor risksExtractor = new RisksExtractor("D:\\git\\management_board_file_processors\\excellProcessors\\risksFileProcessor\\src\\main\\resources\\risks.xlsx");
        return risksExtractor.extract();
    }

    @PostMapping("/risks")
    public RisksDTO extractRisksFromFile(MultipartFile file) throws WrongFileFormat, IOException, NoSheetFoundException {
        LOGGER.info("POST /processors/risks Filename: {}", file.getOriginalFilename());
        return this.mainService.processRisksFile(file);
    }
}
