import org.bytedeco.javacpp.indexer.IntRawIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import structures.Line;
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
    public static void main(String [] args) throws IOException{
        //String txtPath = "C:\\Users\\Yao\\Desktop\\capture\\dir.txt";
        String txtPath = "C:\\Users\\Yao\\Desktop\\cropped\\dir.txt";
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
            imgProcessing(path);

        }
        br.close();
    }

    public static void imgProcessing(String Path){
        //region Image processing
        Mat original = imread(Path);
        imshow("original",original);

        //RGB to Gray
        Mat gray = new Mat();
        cvtColor(original,gray,CV_RGB2GRAY);

        //Gaussian Blur
        Mat blurred = new Mat();
        Size KernelSize = new Size(5,5);
        blur(gray,blurred,KernelSize);

        //Binary image
        Mat BW = IdentifyBinaryThreshold(blurred);//Adjust the threshold when change the image to binary
        //imshow("Binary image",BW);

        //threshold(blurred, dst, 100, 255, THRESH_OTSU);
        Mat otsu =  new Mat();
/*        int bestThresh = otsu(blurred);
        threshold(blurred,otsu,bestThresh,255,CV_THRESH_BINARY);
        imshow("OTSU Binary", otsu);*/

        threshold(blurred, otsu, 0, 255,  CV_THRESH_OTSU);
        //imshow("OTSU",otsu);


        //Morphological closing
        Mat element5 = getStructuringElement(MORPH_ELLIPSE, new Size(5,5));
        Mat closed = new Mat();

        //Mat horizontal = new Mat(1,20,CV_8U);
        //Mat vertical  = new Mat(20,1,CV_8U);

        morphologyEx(BW, closed, MORPH_CLOSE,element5);
        //morphologyEx(closed, closed, MORPH_CLOSE, vertical);

        morphologyEx(otsu, otsu, MORPH_CLOSE, element5);
        //morphologyEx(otsu, otsu, MORPH_CLOSE, vertical);

        //imshow("Closed",closed);

        Mat open = new Mat();
        morphologyEx(closed, open, MORPH_OPEN, element5);
        morphologyEx(otsu, otsu, MORPH_OPEN, element5);
        imshow("open1", otsu);
        imshow("Open",open);
        //endregion

        // Hough Transformation: store the points and slope in a form of a linked list
        Node list = Houghlines.HoughLP(open, "HL  Open");
        Node list1 = Houghlines.HoughLP(otsu, "HL  OTSU");

        int num_list = Node.numberOfNode(list);
        int num_list1 = Node.numberOfNode(list1);

        Node finalList = CombineTwoList(list,list1);

        System.out.println(Node.traverse(finalList));
        System.out.println("---------------------\n\n");

        drawLinesOnImage(original, finalList);
        //Group the nodes based on their slopes and print them out
        Node[] arr = Location.printGrouping(finalList);


        //Line.combineLine(arr);
        //For testing
        moveWindow("Lines using both methods",0, 0);
        moveWindow("HL  Open",480, 0);
        moveWindow("HL  OTSU",960, 0);
        //moveWindow("New Method for Hough",1450, 20);
        waitKey(0);

    }

    private static void drawLinesOnImage(Mat img, Node list){
        Mat src = new Mat(img);
        Node temp = list;
        while(temp != null){
            int x1 = temp.getX1();
            int y1 = temp.getY1();
            int x2 = temp.getX2();
            int y2 = temp.getY2();
            Point p1 = new Point(x1,y1);
            Point p2 = new Point(x2,y2);
            line(src,p1,p2,new Scalar (0,0,255,0),2,8,0);
            temp = temp.next;
        }
        imshow("Lines using both methods", src);

    }

    /**
     * Adjust the threshold when change the image to a binary image, it is set to 50% black for now
     * @param blurred The input image
     * @return Output image with at least 50% black
     */
    private static Mat IdentifyBinaryThreshold(Mat blurred){
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

        while(ratio > 0.7){
            BW.zero();
            threshold(blurred,BW,thresh,255,0);
            ratio = 1 - (double)countNonZero(BW)/(double)BW.rows()/(double)BW.cols();
            thresh -= 5;
        }

        return BW;
    }

    // set the area range for the image, and compare the ratio of the screen
    private static boolean verifySizes(RotatedRect mr) {
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

    private static int otsu(Mat image) {

        int width = image.cols();
        int height = image.rows();
        int pixelCount[] = new int[256];
        float pixelPro[] = new float[256];
        int i, j, pixelSum = width * height, threshold = 0;

        UByteRawIndexer sI = image.createIndexer();

        // Initialize
        for(i = 0; i < 256; i++){
            pixelCount[i] = 0;
            pixelPro[i] = 0;
        }

        // Find the number of pixels of different colors(0-255, since it is gray scaled)
        for(i = 0; i < height; i++) {
            for(j = 0;j <width;j++) {
                int temp = sI.get(i*width+j);
                pixelCount[temp]++;
            }
        }

        // Have a histogram for it
        for(i = 0; i < 256; i++) {
            pixelPro[i] = (float)(pixelCount[i]) / (float)(pixelSum);
        }

        //ostu
        float w0, w1, u0tmp, u1tmp, u0, u1, u,deltaTmp, deltaMax = 0;
        for(i = 0; i < 256; i++)
        {
            w0 = w1 = u0tmp = u1tmp = u0 = u1 = u = deltaTmp = 0;

            for(j = 0; j < 256; j++)
            {
                if(j <= i) {
                    w0 += pixelPro[j];
                    u0tmp += j * pixelPro[j];
                }
                else
                {
                    w1 += pixelPro[j];
                    u1tmp += j * pixelPro[j];
                }
            }

            u0 = u0tmp / w0;
            u1 = u1tmp / w1;
            u = u0tmp + u1tmp;
            deltaTmp = w0 * (u0 - u)*(u0 - u) + w1 * (u1 - u)*(u1 - u);
            if(deltaTmp > deltaMax)
            {
                deltaMax = deltaTmp;
                threshold = i;
            }
        }

        return threshold;
    }

    private static Node CombineTwoList(Node list, Node list1){
        int num_list = Node.numberOfNode(list);
        int num_list1 = Node.numberOfNode(list1);

        if(num_list==0) return list1;
        if(num_list1 == 0) return list;

        boolean ifCombine = false;
        if(num_list <= 5 && num_list1 <= 5){
            ifCombine = true;
        }

        if(!ifCombine){
            return (num_list > num_list1)? list:list1;
        }else{
            while(list != null){
                int x1 = list.getX1();
                int y1 = list.getY1();
                int x2 = list.getX2();
                int y2 = list.getY2();

                list1 = Node.add(x1,y1,x2,y2,list1);
                list = list.next;

            }
            return list1;
        }

    }
}