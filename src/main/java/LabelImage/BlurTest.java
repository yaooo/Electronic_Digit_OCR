package LabelImage;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * @author Yao Shi
 */

public class BlurTest {

    public static void main(String [] args) throws IOException {
        String txtPath = "C:\\Users\\Yao\\Desktop\\sample\\tmp.txt";
        readFileNameFromTxt(txtPath);
    }

    private static void readFileNameFromTxt(String txtFile) throws IOException {
        File file = new File(txtFile);
        String abs = file.getParentFile().getAbsolutePath();
        String sCurrentLine;
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((sCurrentLine = br.readLine()) != null) {
            String path = abs + "\\" + sCurrentLine;
            imgProc(path);
        }
        br.close();
    }

    private static void imgProc(String Path){
        boolean temp = true;
        if(BlurTest.ifBlurry(Path)){
            if(!(new File(Path)).delete())
                System.out.println("Deletion fails.");
        }

    }




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
