import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.cvDestroyAllWindows;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author Yao Shi
 */
public class LineDetection {

    private static final int[] WHITE = {255, 255, 255};
    private static final int[] BLACK = {0, 0, 0};

    public static void main(String [] args) throws IOException{
        String Path = "C:\\Users\\Yao\\Desktop\\testing\\rightbp.jpg";
        String tempPath = Path.substring(0,Path.lastIndexOf('.'))+"_temp.jpg"; //Decide if deleting this file later


        Picture p = new Picture(Path);
        Mat original = imread(Path);
        imshow("original",original);

        //RGB to Gray
        Mat gray = new Mat();
        cvtColor(original,gray,6);
        imshow("gray",gray);

        //Gaussian Blur
        Mat blurred = GaussianBlur(gray);
        imshow("Blurred", blurred);

        //canny edge detect
        Mat contours = new Mat();
        Canny(blurred, contours, 20, 40, 3,true); //TODO: adjust the threshold for the upper and lower bounds
        imshow("canny",contours);

        // TO Binary image
        Mat BW = new Mat();
        threshold(blurred,BW,120,255,0);
        imshow("Binary image",BW);
        Mat element5 = getStructuringElement(MORPH_ELLIPSE, new Size(15,15));

        //Morphological closing
        Mat closed = new Mat();
        morphologyEx(BW, closed, MORPH_CLOSE, element5);
        imshow("Closed",closed);


        waitKey(0);

    }


    private static Mat GaussianBlur(Mat src){
        Mat dest = new Mat();
        Size KernelSize = new Size(3,3);
        blur(src,dest,KernelSize);
        return dest;
    }

    private static void displayImage(BufferedImage img2) {
        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
        ImageIcon icon=new ImageIcon(img2);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } // Unused, keep it here just in case

    private static BufferedImage CannyEdgeDetect(Mat mat, String path){
        BufferedImage frame = matToBufferedImage(mat, path);
        System.out.println("dimension" + frame.getWidth() +" "+frame.getHeight());

        //create the detector
        CannyEdgeDetector detector = new CannyEdgeDetector();

        //adjust its parameters as desired
        detector.setLowThreshold(1f);
        detector.setHighThreshold(3f);

        //apply it to an image
        detector.setSourceImage(frame);
        detector.process();
        return detector.getEdgesImage();
    } // Use JavaCV default instead, DO NOT USE THIS


    //TODO: Replace this method for a better one later
    private static BufferedImage matToBufferedImage(Mat matBGR, String path) {
        File temp = new File(path);
        if(temp.exists()){
            if(!temp.delete()) System.out.println("matToBufferedImage deletion fails.");
        }
        imwrite(path,matBGR);
        BufferedImage buff = new Picture(path).getBufferedImage();
        if(!new File(path).delete()) System.out.println("matToBufferedImage deletion fails.");
        return  buff;
    }

}





