import static org.bytedeco.javacpp.helper.opencv_core.RGB;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.indexer.IntIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
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
        String tempPath = "C:\\Users\\Yao\\Desktop\\testing\\temp.jpg";
        Picture p = new Picture(Path);
        Mat original = imread(Path);
        imshow("original",original);

        Mat blurred = GauissianBlur(p);
        imshow("Blurred", blurred);


        /*Mat contours = new Mat();
        int threshold1 = 125;
        int threshold2 = 350;
        int apertureSize = 3;
        Canny(blurred, contours, threshold1, threshold2, apertureSize,true);
        imshow("canny",contours);*/


        BufferedImage edge = CannyEdgeDetect(blurred,tempPath);
        displayImage(edge);


    }


    private static Mat GauissianBlur(Picture p) throws IOException{
        BufferedImage gray = p.toGray(p.getBufferedImage());
        String path = p.getPath();
        String newPath = path.substring(0,path.lastIndexOf('.'))+"_Gray_temp.jpg";

        File temp = new File(newPath);
        if(temp.exists()){
            if(!temp.delete()) System.out.println("Deletion fails.");
        }

        ImageIO.write(gray,"JPG", temp);
        Mat dest = new Mat();
        Size KernelSize = new Size(3,3);
        Mat src = imread(newPath);
        blur(src,dest,KernelSize);
        return dest;
    }

    private static void imshow(String txt, Mat img) {
        CanvasFrame canvasFrame = new CanvasFrame(txt);
        canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvasFrame.setCanvasSize(img.cols(), img.rows());
        canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
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

    }

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
    }

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





