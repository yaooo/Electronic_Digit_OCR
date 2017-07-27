package LabelImage;

import java.io.*;

/**
 * The typical image files we get are jpg files
 * This class is used to check if the file is too small or it is not correctly coded in JPG
 * @author Yao Shi
 */
public class CorruptionTest {
    public static void main(String[] args) throws IOException{

        String p = "C:\\Users\\Yao\\Desktop\\sample\\tmp.txt";
        readFileNameFromTxt(p);
    }


    /**
     * Test whether the file is correctly coded in JPG format or whether it is too small
     * @param path the image path
     */
    public static boolean jpegEnded(String path) throws IOException {

        File img = new File(path);
        // this path does not give us a file or the file is smaller than 20kB
        if(!img.isFile() || img.length()/1000 < 20){
            System.out.println("Not a file or too small.");
            return false;
        }

        try (RandomAccessFile fh = new RandomAccessFile(path, "r")) {
            fh.seek(fh.length()- 2);
            byte[] eoi = new byte[2];
            fh.readFully(eoi);
            return eoi[0] == -1 && eoi[1] == -39; //EE D9
        }
    }

    private static void readFileNameFromTxt(String txtFile) throws IOException {
        File file = new File(txtFile);
        String abs = file.getParentFile().getAbsolutePath();
        String sCurrentLine;
        BufferedReader br = new BufferedReader(new FileReader(file));

        int c = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            String path = abs + "\\" + sCurrentLine;
            if(!jpegEnded(path))
                c ++;

        }
        br.close();
        System.out.println(c);
    }


}
