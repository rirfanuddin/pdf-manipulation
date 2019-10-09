package com.github.rirfanuddin.pdfmanipulation.controller;

import com.github.rirfanuddin.pdfmanipulation.dto.AddPhotoToPdfDto;
import com.github.rirfanuddin.pdfmanipulation.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author rirfanuddin
 */
@RestController
@RequestMapping(value = "pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("addImage")
    public void addImageToPdf(@RequestBody AddPhotoToPdfDto dto) throws IOException {
        pdfService.createPdfFromImage(dto.getInput(), dto.getPhoto(), dto.getOutput());
    }

}
