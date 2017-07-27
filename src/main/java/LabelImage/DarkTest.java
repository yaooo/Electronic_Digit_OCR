package LabelImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

import java.awt.*;

/**
 * @author Yao Shi
 */
public class DarkTest {

    public static void main(String [] args) {
        long startTime = System.currentTimeMillis();

        String path = "C:\\Users\\Yao\\Desktop\\Photos\\t(25).jpg";
        System.out.println(isBrightColor(path));

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }


    /**
     * @return true for bright; false for dark
     */
    public static boolean isBrightColor(String path) {
        Mat image = imread(path);
        IplImage img = new IplImage(image);

        int count = 0;
        int allPixelsCount = img.width()*img.height();
        int darkPixelCount = 0;

        for (int x = 0; x < img.width(); x++) {
            for (int y = 0; y < img.height(); y++) {
                CvScalar s = cvGet2D(img, y, x);
                double blue = s.val(0);
                double green = s.val(1);
                double red = s.val(2);

                int brightness = (int) Math.sqrt(red*red*.241 + green*green*.691 + blue*blue*.068);

                if(brightness < 10)
                    count++;
            }
        }
        return (darkPixelCount < allPixelsCount * 0.25);
    }
}