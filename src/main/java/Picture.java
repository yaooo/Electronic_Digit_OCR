/**
 * @author Yao Shi
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Picture {

	private String filename;
	private BufferedImage bufferedImage;

	/**
	 * Set up the Constructor for Picture
	 */
	public Picture(String fileName) {
		this.filename = fileName;
		this.bufferedImage = rotateCw(load(filename));
	}

	private BufferedImage load(String fileName) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
		}
		return img;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public String getPath(){return filename;}

	public int getHeight() {
		return bufferedImage.getHeight();
	}

	public int getWidth() {
		return bufferedImage.getWidth();
	}

	public boolean write(String fileName) throws IOException {
		String extension = "png";
		File file = new File(fileName);

		if (!file.getParentFile().canWrite()) {
			System.out.println(fileName + " cannot be written in this directory");
			return false;
		}

		ImageIO.write(bufferedImage, extension, file);
		return true;
	}

	/**
	 * Duplicate the image and return a copy of it
	 */
	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Create a copy of the original image. Adjust the brightness with the input
	 * brightenFactor. Return the new adjusted image.
	 */
	private BufferedImage brighten(float brightenFactor) {
		BufferedImage image = deepCopy(this.bufferedImage);
		RescaleOp op = new RescaleOp(brightenFactor, 0, null);
		image = op.filter(image, image);
		return image;

	}

	/**
	 * Change the image to the gray scale.
	 */
	public BufferedImage toGray(BufferedImage input) {
		BufferedImage image = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = image.createGraphics();
		g2d.drawImage(input, 0, 0, null);
		return image;
	}

	/**
	 * Change the brightness. Apply the gray filter. Apply black&white filter to
	 * clear the noise image.
	 */
	public BufferedImage filtToBlackWhite(float brightenFactor) {

		BufferedImage input;
		if (brightenFactor != 0) {
			input = brighten(brightenFactor);
			input = toGray(input);
		} else {
			input = deepCopy(this.bufferedImage);
		}

		BufferedImage im = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = im.createGraphics();
		g2d.drawImage(input, 0, 0, null);
		return im;// rotateCw(im);
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
