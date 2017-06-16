
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class testing {

	public static void main(String[] args) throws IOException {
		// input a txt file that contains directory of the images

//		Picture x = new Picture("C:\\Users\\Yao\\workspace\\OCR_DIGIT\\resources\\Photos\\t (1).jpg");
//		int rgb = x.getBufferedImage().getRGB(2, 2);
//
//		int alpha = (rgb >> 24) & 0xFF;
//		int red = (rgb >> 16) & 0xFF;
//		int green = (rgb >> 8) & 0xFF;
//		int blue = (rgb) & 0xFF;
//		System.out.println(red + " " + green + " " + blue);
		// ImageIO.write(x.getBufferedImage(), "JPG", new
		// File("C:\\Users\\Yao\\Desktop\\exp1.jpg"));

		handlePicture("C:\\Users\\Yao\\workspace\\OCR_DIGIT\\resources\\testing\\dir.txt");
		System.out.println("Done.");
	}

	/**
	 * Input a txt file in the image folder. The txt file should contain the
	 * names of all images.
	 */
	public static void handlePicture(String args) throws IOException {
		File file = new File(args);

		String abs = file.getParentFile().getAbsolutePath();

		System.out.println(abs);

		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader(file));

		while ((sCurrentLine = br.readLine()) != null) {

			String path = abs + "\\" + sCurrentLine;

			// System.out.println("path: " + path + " will be modified.");

			Picture x = new Picture(path);

			int indexOfDot = path.indexOf('.');

			String newPath;
			System.out.println(path + " is processing...");

			// Adjust the brightness and change the image to binary
			newPath = path.substring(0, indexOfDot) + "_new(0.0).jpg";
			ImageIO.write(x.filtToBlackWhite(0), "JPG", new File(newPath));
			// this one applies rotation
			double ID = 0;
			extractBytes(newPath,ID);

			newPath = path.substring(0, indexOfDot) + "_new(1.0).jpg";
			ImageIO.write(x.filtToBlackWhite(1.0f), "JPG", new File(newPath));
			ID = 1;
			extractBytes(newPath,ID);

			newPath = path.substring(0, indexOfDot) + "_new(1.5).jpg";
			ImageIO.write(x.filtToBlackWhite(1.5f), "JPG", new File(newPath));
			ID = 1.5;
			extractBytes(newPath,ID);

			newPath = path.substring(0, indexOfDot) + "_new(2.0).jpg";
			ImageIO.write(x.filtToBlackWhite(2.0f), "JPG", new File(newPath));
			ID = 2;
			extractBytes(newPath,ID);
/*
			newPath = path.substring(0, indexOfDot) + "_new(2.5).jpg";
			ImageIO.write(x.filtToBlackWhite(2.5f), "JPG", new File(newPath));
			ID = 2.5;
			extractBytes(newPath,ID);

			newPath = path.substring(0, indexOfDot) + "_new(3.0).jpg";
			ImageIO.write(x.filtToBlackWhite(3.0f), "JPG", new File(newPath));
			ID = 3;
			extractBytes(newPath,ID);

			newPath = path.substring(0, indexOfDot) + "_new(3.5).jpg";
			ImageIO.write(x.filtToBlackWhite(3.5f), "JPG", new File(newPath));
			ID = 3.5;
			extractBytes(newPath,ID);*/
		}
		br.close();

	}

/*	public static void getMiddleColumnsByte(String ImageName)throws IOException{
		 File imgPath = new File(ImageName);
		 BufferedImage bufferedImage = ImageIO.read(imgPath);
		 WritableRaster raster = bufferedImage.getRaster();
		 DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		 FileUtils.
				 writeByteArrayToFile(new File("C:\\Users\\Yao\\Desktop\\exp_data"), data.getData());
		 System.out.println("Image dimension: width:" + bufferedImage.getWidth() + ",height:" + bufferedImage.getHeight());
		 //return (data.getData());
	}*/


	/**
	 * Extract the byte array of the image
	 * Output only 0s and 1s because the image we are working with should already be type_binary(black & white)
	 */
	public static void extractBytes(String ImageName, double ID) throws IOException {

		BufferedImage image = ImageIO.read(new File("C:\\Users\\Yao\\Desktop\\exp.jpg"));

		int height = image.getHeight();
		int width = image.getWidth();

		byte[][] pixels = new byte[width][];

		for (int x = 0; x < width; x++) {
			pixels[x] = new byte[height];

			for (int y = 0; y < image.getHeight(); y++) {
				pixels[x][y] = (byte) (image.getRGB(x, y) == 0xFFFFFFFF ? 1 : 0);
			}
			// stem.out.println(x);

			FileOutputStream fos = new FileOutputStream("C:\\Users\\Yao\\Desktop\\exp_data_original"+ID+".bin", true);
			fos.write(pixels[x]);
			fos.close();
		}

		clearNoise(width,height,pixels);

		for (int i = 0; i < width; i++){

			FileUtils.writeByteArrayToFile(new
			File("C:\\Users\\Yao\\Desktop\\exp_data_edited"+ID+".bin"), pixels[i], true);
		}

	}

	public static void clearNoise(int width, int height, byte[][] pixels){
		noiseRemove.removeNoise(pixels,width,height,3,2);
		noiseRemove.removeNoise(pixels,width,height,2,2);
		noiseRemove.removeNoise(pixels,width,height,1,2);
		noiseRemove.removeNoise(pixels,width,height,3,1);
		noiseRemove.removeNoise(pixels,width,height,2,1);
		noiseRemove.removeNoise(pixels,width,height,1,1);
	}



}
