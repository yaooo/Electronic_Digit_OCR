import org.bytedeco.javacpp.indexer.IntRawIndexer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import structures.Location;
import structures.Node;

import javax.imageio.ImageIO;
import javax.swing.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;
import java.io.*;


/**
 * @author Yao Shi
 */
public class LineDetection {

/*    private static final int[] WHITE = {255, 255, 255};
    private static final int[] BLACK = {0, 0, 0};*/

    public static void main(String [] args) throws IOException{
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
            System.out.println(path + " is processing...");
            imgProc(path);
        }
        br.close();
    }

    private static void imgProc(String Path){
        // Picture p = new Picture(Path);
        Mat original = imread(Path);
        imshow("original",original);

        //RGB to Gray
        Mat gray = new Mat();
        cvtColor(original,gray,6);
        //imshow("gray",gray);

        //Gaussian Blur
        Mat blurred = new Mat();
        Size KernelSize = new Size(3,3);
        blur(gray,blurred,KernelSize);
        //imshow("Blurred", blurred);

        //TODO: canny edge detector, this part is not used anywhere in this program so far
        Mat contours = new Mat();
        Canny(blurred, contours, 20, 40, 3, true);
        //imshow("canny",contours);

        //Binary image
        Mat BW = Binary(blurred);//change the threshold to make sure the black is at lest 50% of the image
        imshow("Binary image",BW);

        //Morphological closing
        Mat element5 = getStructuringElement(MORPH_ELLIPSE, new Size(15,15));
        Mat closed = new Mat();
        morphologyEx(BW, closed, MORPH_CLOSE, element5);
        //imshow("Closed",closed);

        Mat open = new Mat();
        morphologyEx(closed, open, MORPH_OPEN, element5);
        imshow("Open",open);

        // Hough Transformation
        // store the points and slope in a form of a linked list
        Node list_data_slope = Houghlines.HoughLP(open);

        //Node list_data_slope1 = Houghlines.HoughLP(BW);

        Node.traverse(list_data_slope);

        //Group the nodes based on their slopes and print them out
        Node[] arr = Location.printGrouping(list_data_slope);

        moveWindow("original",20, 20);
        moveWindow("Binary image",500, 20);
        moveWindow("Open",980, 20);
        moveWindow("New Method for Hough",1450, 20);




        waitKey(0);

    }

    public static Mat Binary(Mat blurred){
        boolean flag =  false;
        Mat BW = new Mat();
        int thresh = 100;
        double ratio = 0;

        while(ratio < 0.5){
            BW.zero();
            threshold(blurred,BW,thresh,255,0);
            ratio = 1 - (double)countNonZero(BW)/(double)BW.rows()/(double)BW.cols();
            thresh += 5;
        }
        return BW;
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

    static void display(String caption,Mat image) {
        // Create image window named "My Image".
        final CanvasFrame canvas = new CanvasFrame(caption, 1.0);

        // Request closing of the application when the image window is closed.
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Convert from OpenCV Mat to Java Buffered image for display
        final OpenCVFrameConverter converter = new OpenCVFrameConverter.ToMat();
        // Show image on window.
        canvas.showImage(converter.convert(image));
    }

}