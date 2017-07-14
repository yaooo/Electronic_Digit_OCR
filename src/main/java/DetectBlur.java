import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Yao Shi
 */
public class DetectBlur {

    public static void main(String [] args) throws IOException {
            String txtPath = "C:\\Users\\Yao\\Desktop\\sample\\tmp.txt";
            readFileNameFromTxt(txtPath);
    }

    private static void readFileNameFromTxt(String txtFile) throws IOException {
        File file = new File(txtFile);
        String abs = file.getParentFile().getAbsolutePath();
        String sCurrentLine;
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((sCurrentLine = br.readLine()) != null) {
            String path = abs + "\\" + sCurrentLine;
            imgProc(path);
        }
        br.close();
    }

    private static void imgProc(String Path){
        boolean temp = true;
        if(BlurTest.ifBlurry(Path)){
            if(!(new File(Path)).delete())
                System.out.println("Deletion fails.");
        }

    }


    }

