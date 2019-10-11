package com.github.rirfanuddin.pdfmanipulation.service;

import org.apache.xmpbox.xml.XmpParsingException;

import java.io.IOException;
import java.util.Map;

/**
 * @author rirfanuddin
 */
public interface ExtractMetadataService {

    Map<String, String> extractMetadata(String filePath) throws IOException, XmpParsingException;

}
