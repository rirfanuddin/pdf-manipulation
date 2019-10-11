package com.github.rirfanuddin.pdfmanipulation.service.impl;

import com.github.rirfanuddin.pdfmanipulation.service.ExtractMetadataService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

/**
 * @author rirfanuddin
 */
@Service
public class ExtractMetadataServiceImpl implements ExtractMetadataService {

    @Override
    public Map<String, String> extractMetadata(String filePath) throws IOException, XmpParsingException{
        Map<String, String> result = new HashMap<>();

        try (PDDocument document = PDDocument.load(new File(filePath))) {

            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDMetadata meta = catalog.getMetadata();

            if (meta != null) {
                DomXmpParser xmpParser = new DomXmpParser();
                try {
                    XMPMetadata metadata = xmpParser.parse(meta.createInputStream());

                    DublinCoreSchema dc = metadata.getDublinCoreSchema();
                    if (dc != null) {
                        result.put("title", dc.getTitle());
                        result.put("description", dc.getDescription());
                        result.put("creators", listToString(dc.getCreators()));

                        result.put("dates", listCalendar(dc.getDates()));
                        result.put("subjects", listToString(dc.getSubjects()));
                    }

                    AdobePDFSchema pdf = metadata.getAdobePDFSchema();

                    if (pdf != null) {
                        result.put("keywords", pdf.getKeywords());
                        result.put("pdfVersion", pdf.getPDFVersion());
                        result.put("pdfProducer", pdf.getProducer());
                    }

                    XMPBasicSchema basic = metadata.getXMPBasicSchema();
                    if (basic != null) {
                        result.put("createdDate", listCalendar(Arrays.asList(basic.getCreateDate())));
                        result.put("modifyDate", listCalendar(Arrays.asList(basic.getModifyDate())));
                        result.put("creatorTool", basic.getCreatorTool());
                    }
                }
                catch (XmpParsingException e) {
                    System.err.println("An error ouccred when parsing the meta data: " + e.getMessage());
                }
            }
            else {
                // The pdf doesn't contain any metadata, try to use the
                // document information instead
                PDDocumentInformation information = document.getDocumentInformation();
                if (information != null) {
                    result.put("title", information.getTitle());
                    result.put("subject", information.getSubject());
                    result.put("author", information.getAuthor());
                    result.put("creator", information.getAuthor());
                    result.put("produces", information.getProducer());
                }
            }
        }

        return result;
    }

    private static String listToString(List<String> list) {
        String result = "";
        if(list.size()>0) {
            for(int i=0; i<list.size(); i++) {
                result += list.get(i);
                if(i<list.size()-1) {
                    result+=", ";
                }
            }
        }
        return result;
    }

    private static String listCalendar(List<Calendar> list) {
        String result = "";
        if (list == null) return result;
        for (Calendar calendar : list) {
            result += format(calendar);
            result += "-";
        }
        return result;
    }

    private static String format(Object o) {
        if (o instanceof Calendar) {
            Calendar cal = (Calendar) o;
            return DateFormat.getDateInstance().format(cal.getTime());
        }
        else {
            return o.toString();
        }
    }

}
