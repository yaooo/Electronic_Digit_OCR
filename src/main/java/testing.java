/**
 * @author Yao Shi
 */

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class testing {

	public static void main(String[] args) throws IOException {

		// input a txt file that contains directory of the image
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

			//gray scale
			BufferedImage gray = x.toGray(x.getBufferedImage());
			//ImageIO.write(gray,"JPG",new File("C:\\Users\\Yao\\Desktop\\img\\Gray.jpg"));



			// Canny Edge filter
			newPath = path.substring(0, indexOfDot) + "_Edge.jpg";
			BufferedImage edge = CannyEdgeDetect(gray);

			ImageIO.write(edge,"JPG",new File(newPath));
			extractBytes(newPath);



			// Adjust the brightness and change the image to binary
/*			for (double i = 1; i <= 4; i += 1) {
				newPath = path.substring(0, indexOfDot) + "_new(" + i + ").jpg";
				ImageIO.write(x.filtToBlackWhite((float) i), "JPG", new File(newPath));
				extractBytes(newPath);
			}*/
		}
		br.close();

	}

	/**
	 * Extract the byte array of the image
	 * Output only 0s and 1s because the image we are working with should already be type_binary(black & white)
	 */
	private static void extractBytes(String ImagePath) throws IOException {

		BufferedImage image = ImageIO.read(new File(ImagePath));
		int height = image.getHeight();
		int width = image.getWidth();

		String dataPath_original = ImagePath.substring(0, ImagePath.lastIndexOf('.')) + "_data_original.bin";
		//String dataPath_edited = ImagePath.substring(0, ImagePath.lastIndexOf('.')) + "_data_edited.bin";

		File data = new File(dataPath_original);
		if(data.exists()){
			if(!data.delete())
				System.out.println("Deletion fails.");
		}

		byte[][] pixels = new byte[width][];
		FileOutputStream fos = new FileOutputStream(dataPath_original, true);

		for (int x = 0; x < width; x++) {
			pixels[x] = new byte[height];
			for (int y = 0; y < image.getHeight(); y++) {
				int color = image.getRGB(x,y);
				pixels[x][y] = (byte) (((color>>16 & 0xFF)> 0xBF && (color>>8 & 0xFF)> 0xBF && (color & 0xFF)> 0xBF)
						? 1 : 0); // The output is either 1 or 0
			}
			fos.write(pixels[x]);
		}
		fos.close();


/*		clearNoise(pixels);

		for (int i = 0; i < width; i++) {
			FileUtils.writeByteArrayToFile(new File(dataPath_edited), pixels[i], true);
		}*/

	}

	/**
	 * Use for clearing the noise edge
	 * Remove single or multiple small black dots from the array of data
	 * Add the single black dot to the white spot where its upper and lower pixels are black
	 */
	private static void clearNoise( byte[][] pixels) {
		noiseRemove.removeNoise(pixels, 3, 2);
		noiseRemove.removeNoise(pixels, 2, 2);
		noiseRemove.removeNoise(pixels, 1, 2);
		noiseRemove.removeNoise(pixels, 3, 1);
		noiseRemove.removeNoise(pixels, 2, 1);
		noiseRemove.removeNoise(pixels, 1, 1);
		noiseRemove.addBlack(pixels);
	}

	/**
	 * The default type of the image is jpg
	 * @param directory The directory where the image will be stored
	 * @param bufferedImage The output image
	 */
	public static void outputImg(String directory, BufferedImage bufferedImage) throws IOException{
		File outputfile = new File(directory);
		ImageIO.write(bufferedImage, "jpg", outputfile);
	}

	public static BufferedImage CannyEdgeDetect(BufferedImage frame){

		//create the detector
		CannyEdgeDetector detector = new CannyEdgeDetector();

		//adjust its parameters as desired
		detector.setLowThreshold(1f);
		detector.setHighThreshold(3f);

		//apply it to an image
		detector.setSourceImage(frame);
		detector.process();
		return detector.getEdgesImage();
	}
}