/**
 * @author Yao Shi
 */

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

		handlePicture("C:\\Users\\Yao\\Desktop\\testing\\dir.txt");
		System.out.println("Done.");
	}

	/**
	 * Input a txt file in the image folder. The txt file should contain the
	 * names of all images.
	 */
	private static void handlePicture(String args) throws IOException {
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
			for (double i = 1; i <= 3.5; i += 0.5) {

				newPath = path.substring(0, indexOfDot) + "_new(" + i + ").jpg";
				ImageIO.write(x.filtToBlackWhite((float) i), "JPG", new File(newPath));
				double ID = i;
				extractBytes(newPath, ID);
			}
		}
		br.close();

	}

	/**
	 * Extract the byte array of the image
	 * Output only 0s and 1s because the image we are working with should already be type_binary(black & white)
	 */
	private static void extractBytes(String ImagePath, double ID) throws IOException {

		BufferedImage image = ImageIO.read(new File(ImagePath));
		int height = image.getHeight();
		int width = image.getWidth();

		String dataPath_original = ImagePath.substring(0, ImagePath.lastIndexOf('.')) + "_data_original.bin";
		String dataPath_edited = ImagePath.substring(0, ImagePath.lastIndexOf('.')) + "_data_edited.bin";

		File f = new File(dataPath_original);
		if (f.exists() && !f.isDirectory()) {
			f.delete();
		}

		byte[][] pixels = new byte[width][];

		for (int x = 0; x < width; x++) {
			pixels[x] = new byte[height];
			for (int y = 0; y < image.getHeight(); y++) {
				pixels[x][y] = (byte) (image.getRGB(x, y) == 0xFFFFFFFF ? 1 : 0); // The output is either 1 or 0
			}
			FileOutputStream fos = new FileOutputStream(dataPath_original, true);
			fos.write(pixels[x]);
			fos.close();
		}

		clearNoise(width, height, pixels);

		for (int i = 0; i < width; i++) {
			FileUtils.writeByteArrayToFile(new File(dataPath_edited), pixels[i], true);
		}

	}

	private static void clearNoise(int width, int height, byte[][] pixels) {
		noiseRemove.removeNoise(pixels, width, height, 3, 2);
		noiseRemove.removeNoise(pixels, width, height, 2, 2);
		noiseRemove.removeNoise(pixels, width, height, 1, 2);
		noiseRemove.removeNoise(pixels, width, height, 3, 1);
		noiseRemove.removeNoise(pixels, width, height, 2, 1);
		noiseRemove.removeNoise(pixels, width, height, 1, 1);
	}



}