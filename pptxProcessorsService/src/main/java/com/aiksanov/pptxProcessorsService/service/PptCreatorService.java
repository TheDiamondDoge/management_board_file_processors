package com.aiksanov.pptxProcessorsService.service;

import com.company.PptCreatorFacade;
import com.company.data.Options;
import org.springframework.stereotype.Service;

@Service
public class PptCreatorService {
    public void createMultipageCustomizablePpt() {
        PptCreatorFacade creator = new PptCreatorFacade();
        Options options = new Options();
    }
}
