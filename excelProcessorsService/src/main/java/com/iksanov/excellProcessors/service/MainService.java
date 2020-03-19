package com.iksanov.excellProcessors.service;

import com.iksanov.excellProcessors.utils.Utils;
import data.CostDTO;
import data.RisksDTO;
import exceptions.NoSheetFoundException;
import exceptions.WrongBDValueException;
import exceptions.WrongFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.CostExtractor;
import service.RisksExtractor;

import java.io.IOException;

@Service
public class MainService {
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
}
