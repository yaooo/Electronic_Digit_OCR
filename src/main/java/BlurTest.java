import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.indexer.IntRawIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;

import static org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * @author Yao Shi
 */

public class BlurTest {
    final private static double delta = 10; // Have the limit as 10

    /**
     * @param path The image path
     * @return true for blurry, false for normal
     */
    public static boolean ifBlurry(String path){

        Mat mat = imread(path);
        Mat outGray =  new Mat();
        Mat gray = new Mat();
        int Iij;
        double Iave = 0, Idelta = 0;

        cvtColor(mat,gray,CV_RGB2GRAY);

        //Apply Laplace
        Laplacian(gray,outGray,gray.depth());

        UByteRawIndexer sI = outGray.createIndexer();

        int height = outGray.rows();
        int width = outGray.cols();


         for(int i = 0; i < height; i++){
             for(int j = 0; j < width; j++){
                    Iij = sI.get(i*width+j);
                    Idelta += (Iij - Iave) * (Iij - Iave);
             }

         }
        Idelta = Idelta/(width*height);

        //For testing
        imshow(path,mat);
        moveWindow(path,0, 0);

        if(Idelta < delta) {
            System.out.println("Square difference:" + Idelta );
            System.out.println("Old_OCR.Picture :" + path + " is blurry.");
        }
        /*waitKey(0);
        destroyWindow(path);*/
        return (Idelta < delta);
    }
}
