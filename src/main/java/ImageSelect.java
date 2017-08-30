import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ImageSelect {
    public static void main(String[] args) throws IOException {
        String txtPath = "C:\\Users\\Yao\\Documents\\Emory\\test1\\dir.txt";
        readFileNameFromTxt(txtPath);
    }

    private static void readFileNameFromTxt(String txtFile) throws IOException {
        File file = new File(txtFile);
        String abs = file.getParentFile().getAbsolutePath();
        String sCurrentLine;
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((sCurrentLine = br.readLine()) != null) {
            String path = abs + "\\" + sCurrentLine;

            String path1 = CreateTempFile(path);
            File tempFile = new File(path1);

            System.out.println(path + " is processing...");
            selectImage(path);
        }
        br.close();
    }

    public static void selectImage(String path){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane(path));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }


    public static class TestPane extends JPanel {

        private BufferedImage img;
        private JLabel label;

        private JPanel fields;
        private JTextField red;
        private JTextField green;
        private JTextField blue;
        int x1=0,y1=0,width=0,height=0;

        public TestPane(String path) {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            label = new JLabel();
            try {
                img = rotateCw(ImageIO.read(new File(path)));
                label.setIcon(new ImageIcon(img));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            add(label, gbc);

            fields = new JPanel();
            fields.setBorder(new EmptyBorder(5, 5, 5, 5));
            red = new JTextField(3);
            green = new JTextField(3);
            blue = new JTextField(3);
            fields.add(red);
            fields.add(green);
            fields.add(blue);
            add(fields, gbc);


            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    x1 = e.getX();
                    y1 = e.getY();
                    System.out.println("Start: " + x1 + "       "+y1);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    int xf = e.getX();
                    int yf = e.getY();

                    width = Math.abs(x1-xf);
                    height = Math.abs(y1-yf);
                    y1 = Math.min(yf,y1);
                    x1 = Math.min(xf,x1);
                    System.out.println("xy: " + x1 +"   "+y1);

                    System.out.println("W+H: " + width +"   "+height);
                    Mat uncropped = imread(path);
                    Rect roi = new Rect(x1,y1,width,height);

                    Mat cropped = new Mat(uncropped, roi);
                    imwrite(CreateTempFile(path),cropped);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);
                    int xf = e.getX();
                    int yf = e.getY();
                    width = Math.abs(x1-xf);
                    height = Math.abs(y1-yf);
                }
            });


            label.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int packedInt = img.getRGB(e.getX(), e.getY());
                    Color color = new Color(packedInt, true);
                    fields.setBackground(color);
                    red.setText(Integer.toString(color.getRed()));
                    green.setText(Integer.toString(color.getGreen()));
                    blue.setText(Integer.toString(color.getBlue()));
                }
            });

        }

    }


    private static String CreateTempFile(String path){
        int index = path.lastIndexOf('.');

        if(index == -1)
            return path+"_temp";
        else{
            return path.substring(0,index)+"_temp"+path.substring(index);
        }
    }
    /**
     * Rotate clockwise by 90 degrees
     */
    private static BufferedImage rotateCw(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, img.getType());

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                newImage.setRGB(height - 1 - j, i, img.getRGB(i, j));

        return newImage;
    }
}