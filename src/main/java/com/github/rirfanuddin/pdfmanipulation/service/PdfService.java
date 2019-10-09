package com.github.rirfanuddin.pdfmanipulation.service;

import java.io.IOException;

/**
 * @author rirfanuddin
 */
public interface PdfService {

    void createPdfFromImage(String input, String photo, String output) throws IOException;

}
