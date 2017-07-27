package LabelImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

/**
 * @author Yao Shi
 */
public class DarkTest {

    public static void main(String [] args) {

        String path = "C:\\Users\\Yao\\Desktop\\sample\\a (191).jpg";
        System.out.println(isBrightColor(path));
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

                int brightness = (int) Math.sqrt(red*red*.299 + green*green*.587 + blue*blue*.114);

                if(brightness < 40) // simplify the histogram for pixels less than 40 in the range of 0-255
                    count++;
            }
        }
        return (count < allPixelsCount * 0.5); // change 0.5 to other factor if need
    }
}