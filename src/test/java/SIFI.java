import org.bytedeco.javacpp.opencv_core.DMatchVector;
import org.bytedeco.javacpp.opencv_core.KeyPoint;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_xfeatures2d.*;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;


public class SIFI{


    public static void main(String[] args) {
        Mat img=imread("C:\\Users\\Yao\\workspace\\OCR_DIGIT\\src\\test\\java\\OMRON (2).PNG");
        Mat img2=imread("C:\\Users\\Yao\\workspace\\OCR_DIGIT\\src\\test\\java\\rightbp.jpg");

//		@SuppressWarnings("resource")
        SIFT sift=SIFT.create(0, 3, 0.03, 10, 1.6);
        KeyPointVector kp1 = new KeyPointVector();
        sift.detect(img, kp1);
        Mat outimg1 = new Mat();

        opencv_features2d.drawKeypoints(img, kp1, outimg1,new Scalar(255, 255, 255, 0), DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
        imshow("image1 key points", outimg1);

        for(long i=0;i<kp1.size();i++){
            KeyPoint kp=kp1.get(i);
            if(i==0)
                System.out.println(kp.angle()+"\t"+kp.class_id()+"\t"+kp.octave()+"\t"+kp.pt().position()+"\t"+kp.response());
        }
        Mat outimg2=new Mat();
        KeyPointVector kp2 = new KeyPointVector();
        opencv_features2d.drawKeypoints(img2, kp2, outimg2);
        imshow("image2 key points",outimg2);

        SIFT extractor=SIFT.create(0, 3, 0.03, 10, 1.6);
        Mat des1=new Mat();Mat des2=new Mat();
        extractor.compute(img, kp1, des1);
        extractor.compute(img2, kp2, des2);
//        imshow("desc", des1);
        DMatchVector matches=new DMatchVector();
        @SuppressWarnings("resource")
        BFMatcher matcher=new BFMatcher();
        Mat img_match=new Mat();
        matcher.match(des1, des2, matches);
        opencv_features2d.drawMatches(img, kp1, img2, kp2, matches, img_match);
        imshow("matches", img_match);
        waitKey();

    }

}