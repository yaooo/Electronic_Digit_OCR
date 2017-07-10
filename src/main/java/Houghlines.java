import org.bytedeco.javacpp.Pointer;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import org.bytedeco.javacpp.indexer.*;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import structures.Node;

import javax.swing.*;

/**
 * @author Yao Shi
 * Two methods are provided: HoughTransform and HoughLP
 * Currently, I am using HoughLP because it is easier to read, but
 */
public class Houghlines {
    public static CvSeq HoughTransform(Mat src1){
        //CanvasFrame source = new CanvasFrame("Source");
        CanvasFrame hough = new CanvasFrame("Hough");
        //CanvasFrame canny = new CanvasFrame("CANNY FROM HOUGH");

        IplImage src = new IplImage(src1);
        IplImage dst = cvCreateImage(cvGetSize(src), src.depth(), 1);
        IplImage colorDst;
        CvMemStorage storage = cvCreateMemStorage(0);
        CvSeq lines = new CvSeq();

        dst = cvCreateImage(cvGetSize(src), src.depth(), 1);
        colorDst = cvCreateImage(cvGetSize(src), src.depth(), 3);
        //OpenCVFrameConverter.ToIplImage sourceConverter = new OpenCVFrameConverter.ToIplImage();
        OpenCVFrameConverter.ToIplImage houghConverter = new OpenCVFrameConverter.ToIplImage();
        //OpenCVFrameConverter.ToIplImage cannyConverter = new OpenCVFrameConverter.ToIplImage();



        dst = cvCreateImage(cvGetSize(src), src.depth(), 1);
        colorDst = cvCreateImage(cvGetSize(src), src.depth(), 3);

        //Canny detection, store edges in canny
        cvCanny(src, dst, 50, 200, 3);
        cvCvtColor(dst, colorDst, CV_GRAY2BGR);

        //canny.showImage(houghConverter.convert(dst));
        //canny.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Probabilistic Hough transform, store lines in lines
        lines = cvHoughLines2(dst, storage, CV_HOUGH_PROBABILISTIC, 5, CV_PI / 180, 160, 100, 10,0, CV_PI/4);

        int i = 0;
        for (; i < lines.total(); i++){
            Pointer line = cvGetSeqElem(lines,i);
            CvPoint p1 = new CvPoint(line).position(0);
            CvPoint p2 = new CvPoint(line).position(1);
            System.out.println("Line spotted: ");
            System.out.println("\t pt1: " + p1);
            System.out.println("\t pt2: " + p2);

            double slope = (double) ((p2.y()-p1.y())/(p2.x()-p1.x()));
            System.out.print("\t slope:" + slope);
            if(Math.abs(slope) < 1){
                System.out.print("**********\n");
            }else System.out.print("\n");

            cvLine(colorDst,p1,p2,CV_RGB(255,0,0),1,CV_AA,0);
        }

        System.out.println("The number of lines:" + i);

       // source.showImage(sourceConverter.convert(src));
        hough.showImage(houghConverter.convert(colorDst));

       // source.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hough.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return lines;
    }

    public static Node HoughLP(Mat src, String name){
        Mat dst =  new Mat();
        Mat color_dst = new Mat();
        Canny( src, dst, 50,200,3,true);
        cvtColor(dst, color_dst, CV_GRAY2BGR);

        return LineFinder(dst, name);
    }
    private static Node LineFinder(Mat binary, String name){
        Mat lines = new Mat();
        Node head = null;
        Mat rgb = new Mat();
        cvtColor(binary, rgb, CV_GRAY2BGR);

        HoughLinesP(binary, lines, 1, CV_PI/180,80,120,30);

        IntRawIndexer sI = lines.createIndexer();
        for (int y = 0; y < lines.rows(); y++) {

            int x1 = sI.get(y,0);
            int y1 = sI.get(y,1);
            int x2 = sI.get(y,2);
            int y2 = sI.get(y,3);
            Point p1 = new Point(x1,y1);
            Point p2 = new Point(x2,y2);
            line(rgb,p1,p2,new Scalar (0,0,255,0),2,8,0);
     /*       System.out.println("Line spotted: ");
            System.out.println("\t pt1: (" + p1.x()+","+p1.y()+")");
            System.out.println("\t pt2: (" + p2.x()+","+p2.y()+")");*/

            head = structures.Node.add(x1,y1,x2,y2,head);

        }
        imshow(name, rgb);

        return head;
    }



}
