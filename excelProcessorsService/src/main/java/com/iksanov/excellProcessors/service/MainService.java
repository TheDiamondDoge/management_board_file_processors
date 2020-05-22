package com.iksanov.excellProcessors.service;

import com.iksanov.excellProcessors.utils.Utils;
import data.CostDTO;
import data.PlainXlsxDataDTO;
import data.Risk;
import data.RisksDTO;
import exceptions.NoSheetFoundException;
import exceptions.WrongBDValueException;
import exceptions.WrongFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.CostExtractor;
import service.PlainXlsxCreator;
import service.RisksExtractor;
import service.RisksFileGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MainService {
    @Value("${tmp.file.storage}")
    private String filepathDirectory;
    private Utils utils;

    @Autowired
    public MainService(Utils utils) {
        this.utils = utils;
    }

    public RisksDTO processRisksFile(MultipartFile file) throws IOException, WrongFileFormatException, NoSheetFoundException {
        String path = utils.saveFile(file, "risks_");
        RisksExtractor risksExtractor = new RisksExtractor(path);
        return risksExtractor.extract();
    }

    public CostDTO processCostFile(MultipartFile file, String bd) throws IOException, WrongBDValueException, NoSheetFoundException {
        String path = utils.saveFile(file, "cost_");
        CostExtractor costExtractor = new CostExtractor(path, bd);
        return costExtractor.extract();
    }

    public ByteArrayResource getRiskFile(List<Risk> risks, String projectName) throws IOException, NoSheetFoundException {
        RisksFileGenerator generator = new RisksFileGenerator(filepathDirectory);
        String filepath = generator.generateXlsxFile(risks, projectName);
        return new ByteArrayResource(Files.readAllBytes(Paths.get(filepath)));
    }

    public ByteArrayResource getPlainXlsxFile(PlainXlsxDataDTO data) throws IOException {
        PlainXlsxCreator creator = new PlainXlsxCreator(filepathDirectory);
        String filepath = creator.createXlsxFromHeadersAndData(data.getHeader(), data.getData());
        return new ByteArrayResource(Files.readAllBytes(Paths.get(filepath)));
    }
}
