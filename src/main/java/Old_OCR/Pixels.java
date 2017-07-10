package Old_OCR;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * @author Yao Shi
 */


/**
 * TODO: This class is basically useless once importing openCV
 */

public class Pixels {
    /**
     * Getting an array that contains height(default: 640) X 20 pixels.
     * In each pixel, there are three color elements: from index 0 - 2: red, green, blue
     * @param ImageName The path of the image
     * @param startFrom_x The x coordinate that indicates where to start collecting pixels
     */
    private static int[][] getMiddlePixels(String ImageName, int startFrom_x) throws IOException {
        BufferedImage img = ImageIO.read(new File(ImageName));
        int[] rgb;
        int[][] pixelData = new int[img.getHeight() * 20][3];

        for (int i = startFrom_x; i < startFrom_x+20; i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                rgb = getPixelData(img, i, j);

            }
        }


        return pixelData;
    }

    private static int[] getPixelData(BufferedImage img, int x, int y) {
        int argb = img.getRGB(x, y);

        int rgb[] = new int[] {
                (argb >> 16) & 0xff, //red
                (argb >>  8) & 0xff, //green
                (argb      ) & 0xff  //blue
        };

        System.out.println("rgb: " + rgb[0] + " " + rgb[1] + " " + rgb[2]);
        return rgb;
    }


    /**
     * If two pixels' colors are close(within the rage)
     * @param range max difference in color accepted, it cannot be negative
     * @param fp first pixel
     * @param sp second pixel
     */
    private static boolean ifColorsAreClose(int[] fp, int[] sp, int range){
        return (Math.abs(fp[0]-sp[0]) <= range && Math.abs(fp[1]-sp[1]) <= range && Math.abs(fp[2]-sp[2]) <= range);
    }

    private static boolean ifInRange(int[] number, int upperBound_R, int lowerBound_R, int upperBound_G, int lowerBound_G, int upperBound_B, int lowerBound_B){

         return (number[0] > lowerBound_R && number[0] < upperBound_R)&&(number[1] > lowerBound_R && number[1] < upperBound_R)&&(number[2] > lowerBound_R && number[2] < upperBound_R);
    }

    /**
     * @param height The height of the image
     */

    private static int detectTheEdge(int height, int[][] pixelData){
        int y = -1;


        for(int i = 0; i < pixelData.length - height; i++){
            int y_cord = i%height;

            if(y_cord < height*0.7){
                if(ifInRange(pixelData[i],125,80,125,70,120,60)){

                }
            }else {
                i = (i/height + 1)*height; // skip the remaining pixels in one column
            }

        }
        return y;
    }

}

