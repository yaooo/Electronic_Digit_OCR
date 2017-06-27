import org.bytedeco.javacpp.Pointer;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

/**
 * @author Yao Shi
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
        lines = cvHoughLines2(dst, storage, CV_HOUGH_PROBABILISTIC, 5, CV_PI / 180, 160, 40, 10,0, CV_PI/360);

        for (int i = 0; i < lines.total(); i++){
            Pointer line = cvGetSeqElem(lines,i);
            CvPoint p1 = new CvPoint(line).position(0);
            CvPoint p2 = new CvPoint(line).position(1);
            System.out.println("Line spotted: ");
            System.out.println("\t pt1: " + p1);
            System.out.println("\t pt2: " + p2);

            cvLine(colorDst,p1,p2,CV_RGB(255,0,0),1,CV_AA,0);
        }
       // source.showImage(sourceConverter.convert(src));
        hough.showImage(houghConverter.convert(colorDst));

       // source.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hough.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return lines;
    }

}
