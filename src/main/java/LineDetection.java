import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author Yao Shi
 */
public class LineDetection {

/*    private static final int[] WHITE = {255, 255, 255};
    private static final int[] BLACK = {0, 0, 0};*/

    public static void main(String [] args) throws IOException{
        String Path = "C:\\Users\\Yao\\Desktop\\testing\\rightbp.jpg";
       // String tempPath = Path.substring(0,Path.lastIndexOf('.'))+"_temp.jpg"; //Decide if deleting this file later


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
        Canny(blurred, contours, 20, 40, 3, true);
        imshow("canny",contours);

        //Binary image
        //TODO change the thresh to make sure the black is at lest 50% of the image
        Mat BW = new Mat();
        threshold(blurred,BW,120,255,0);
        imshow("Binary image",BW);
        Mat element5 = getStructuringElement(MORPH_ELLIPSE, new Size(15,15));

        //TODO: try OPEN then CLose
      /*  Mat open = new Mat();
        morphologyEx(BW, open, MORPH_OPEN, element5);
        imshow("Open",open);*/


        //Morphological closing
        Mat closed = new Mat();
        morphologyEx(BW, closed, MORPH_CLOSE, element5);
        imshow("Closed",closed);


        Houghlines.HoughTransform(closed);


        waitKey(0);

        //helpFindContours(closed);

    }


    private static Mat GaussianBlur(Mat src){
        Mat dest = new Mat();
        Size KernelSize = new Size(3,3);
        blur(src,dest,KernelSize);
        return dest;
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

    private static void helpFindContours(Mat input){ //TODO: DEBUG
        IplImage srcImage = new IplImage(input);

        IplImage resultImage = cvCloneImage(srcImage);

        MatVector contours = new MatVector();
        findContours(input,contours,CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE);

        Mat draw = new Mat();
        draw.zero();
        for (int i = 0; i < contours.size(); i++){
            Scalar color = new Scalar(255,255,255,0);

            drawContours(draw, contours, i, color);

        }

        namedWindow( "Contours", CV_WINDOW_AUTOSIZE );
        imshow( "Contours", draw);

        waitKey(0);
        //TODO: Add finding contours and return a rotated Rect obj

    }


    // set the area range for the image, and compare the ratio of the screen
    private static boolean verifySizes(RotatedRect mr)
    {
        float error = 0.3f;


        //size of the bigger screen: 5.2 x 4.7 mm, thus, m_aspect = 1.10638298
        float aspect = 1.10638298f;


        //Set a min and max area.
        int max= Math.round(480*480/aspect); // maximum area
        int min= Math.round(max/4);


        //Get only rectangles that match to a respect ratio.
        float rmin= aspect-aspect*error;
        float rmax= aspect+aspect*error;


        int area = Math.round(mr.size().area());

        float r = mr.size().width() / mr.size().height();
        if(r < 1)
        {
            r= mr.size().height() / mr.size().width();
        }

        return (!( area < min || area > max ) || ( r < rmin || r > rmax ));

    }

}





