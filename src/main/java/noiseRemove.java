/**
 * Created by Yao on 6/15/2017.
 */
public class noiseRemove {



    /**
     * This is a helper method to clear noise for binary(black & white) images
     * @param pixels Input matrix for the image
     * @param width Image width
     * @param height Image height
     * @param numOfByte how many consecutive bytes when removing noise
     * @param numOfOperations how many times to run this code
     *                        suggest to set it to 2, but it depends on the image quality
     */
    public static void removeNoise(byte[][] pixels, int width, int height, int numOfByte, int numOfOperations){
        int xMax = pixels.length - numOfByte;
        int yMax = pixels[0].length -numOfByte;
        System.out.println("dimension " + yMax + " " + xMax);
        for(int i = 1; i < xMax; i ++){
            for (int j = 1; j < yMax; j++){
                isConcecutiveBlack(i, j, numOfByte, pixels);
            }
        }

    }

    private static void isConcecutiveBlack(int x, int y, int numOfByte, byte[][]pixels){
        switch (numOfByte) {

            case 1:
                if (pixels[x][y] == (byte) 0) {
                    if (pixels[x + 1][y] == (byte) 1 && pixels[x - 1][y] == (byte) 1) {
                        pixels[x][y] = (byte) 1;
                    }
                }
                if (pixels[x][y] == (byte) 0) {
                    if (pixels[x][y+1] == (byte) 1 && pixels[x][y-1] == (byte) 1) {
                        pixels[x][y] = (byte) 1;
                    }
                }
                break;

            case 3:
                if (pixels[x][y] == (byte) 0 && pixels[x + 1][y] == (byte) 0 && pixels[x + 2][y] == (byte) 0) {
                    if (pixels[x + 3][y] == (byte) 1 && pixels[x - 1][y] == (byte) 1) {
                        pixels[x][y] = (byte) 1;
                        pixels[x + 1][y] = (byte) 1;
                        pixels[x + 3][y] = (byte) 1;
                    }
                }
                break;

            case 2:
                if (pixels[x][y] == (byte) 0 && pixels[x + 1][y] == (byte) 0) {
                    if (pixels[x + 2][y] == (byte) 1 && pixels[x - 1][y] == (byte) 1) {
                        pixels[x][y] = (byte) 1;
                        pixels[x + 1][y] = (byte) 1;
                    }
                }
                break;
        }

    }




}
