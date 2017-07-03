import org.bytedeco.javacpp.opencv_core;
import structures.Location;
import structures.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * @author Yao Shi
 */
public class BlurTesting {

    public static void main(String [] args) throws IOException {
        /*String Path = "C:\\Users\\Yao\\Desktop\\testing\\t (21).jpg";
        imgProc(Path);*/
        String txtPath = "C:\\Users\\Yao\\Desktop\\testing\\dir.txt";
        readFileNameFromTxt(txtPath);
    }

    private static void readFileNameFromTxt(String txtFile) throws IOException {
        File file = new File(txtFile);
        String abs = file.getParentFile().getAbsolutePath();
        String sCurrentLine;
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((sCurrentLine = br.readLine()) != null) {
            String path = abs + "\\" + sCurrentLine;
            //System.out.println(path + " is processing...");
            imgProc(path);
        }
        br.close();
    }

    private static void imgProc(String Path){
        BlurTest.readImage(Path);

    }

}
