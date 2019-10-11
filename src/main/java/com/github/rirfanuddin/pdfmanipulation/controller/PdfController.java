package com.github.rirfanuddin.pdfmanipulation.controller;

import com.github.rirfanuddin.pdfmanipulation.dto.AddPhotoToPdfDto;
import com.github.rirfanuddin.pdfmanipulation.dto.ExtractMetadataDto;
import com.github.rirfanuddin.pdfmanipulation.service.AddPhotoToPdfService;
import com.github.rirfanuddin.pdfmanipulation.service.ExtractMetadataService;
import org.apache.xmpbox.xml.XmpParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author rirfanuddin
 */
@RestController
@RequestMapping(value = "pdf")
public class PdfController {

    @Autowired
    private AddPhotoToPdfService addPhotoToPdfService;

    @Autowired
    private ExtractMetadataService extractMetadataService;

    @PostMapping("addImage")
    public void addImageToPdf(@RequestBody AddPhotoToPdfDto dto) throws IOException {
        addPhotoToPdfService.createPdfFromImage(dto.getInput(), dto.getPhoto(), dto.getOutput());
    }

    @PostMapping("extractMetadata")
    public Map<String, String> extractMetadata(@RequestBody ExtractMetadataDto dto) throws IOException, XmpParsingException {
        return extractMetadataService.extractMetadata(dto.getFilePath());
    }

}
