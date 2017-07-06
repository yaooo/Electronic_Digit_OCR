import com.github.jaiimageio.plugins.tiff.TIFFImageWriteParam;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.LoadLibs;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

public class BasicExample {
    public static void main(String[] args) {
        File image = new File("C:\\Users\\Yao\\workspace\\OCR_DIGIT\\src\\test\\java\\eurotext.png");
        ITesseract api = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
        api.setDatapath(tessDataFolder.getParent());
        try {
            String result = api.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    protected boolean saveTiff(String filename, BufferedImage image) {

        File tiffFile = new File(filename);
        ImageOutputStream ios = null;
        ImageWriter writer = null;

        try {

            // find an appropriate writer
            Iterator it = ImageIO.getImageWritersByFormatName("TIF");
            if (it.hasNext()) {
                writer = (ImageWriter)it.next();
            } else {
                return false;
            }

        // setup writer
            ios = ImageIO.createImageOutputStream(tiffFile);
            writer.setOutput(ios);
            TIFFImageWriteParam writeParam = new TIFFImageWriteParam(Locale.ENGLISH);
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        // see writeParam.getCompressionTypes() for available compression type strings
            writeParam.setCompressionType("PackBits");

        // convert to an IIOImage
            IIOImage iioImage = new IIOImage(image, null, null);

        // write it!
            writer.write(null, iioImage, writeParam);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

}