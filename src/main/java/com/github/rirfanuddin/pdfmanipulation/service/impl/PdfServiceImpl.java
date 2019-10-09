package com.github.rirfanuddin.pdfmanipulation.service.impl;

import com.github.rirfanuddin.pdfmanipulation.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author rirfanuddin
 */
@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public void createPdfFromImage(String input, String photo, String output) throws IOException {
        try (PDDocument doc = PDDocument.load(new File(input)))
        {
            //we will add the image to the first page.
            PDPage page = doc.getPage(0);

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage,
            // call LosslessFactory.createFromImage() instead
            PDImageXObject pdImage = PDImageXObject.createFromFile(photo, doc);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true))
            {
                // contentStream.drawImage(ximage, 20, 20 );
                // better method inspired by http://stackoverflow.com/a/22318681/535646
                // reduce this value if the image is too large
                float scale = 1.0f;
                float widthNewImage = doc.getPage(0).getMediaBox().getWidth();
                float heightNewImage = doc.getPage(0).getMediaBox().getHeight();
                if(pdImage.getWidth() > widthNewImage) {
                    widthNewImage = pdImage.getWidth() - (Math.abs(widthNewImage - pdImage.getWidth()));
                    heightNewImage = pdImage.getHeight() - (Math.abs(heightNewImage - pdImage.getHeight()));
                }
                contentStream.drawImage(pdImage, 20, 20, widthNewImage * scale, heightNewImage * scale);
            }
            doc.save(output);
        }
    }
}
