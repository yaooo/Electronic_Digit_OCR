/**
 * Created by Yao on 6/15/2017.
 */
public class noiseRemove {

    /**
     * This is a helper method to clear noise for binary(black & white) images
     * @param pixels Input matrix for the image
     * @param numOfByte how many consecutive bytes when removing noise
     * @param numOfOperations how many times to run this code
     *                        suggest to set it to 2, but it depends on the image quality
     */
    public static void removeNoise(byte[][] pixels, int numOfByte, int numOfOperations){
        int xMax = pixels.length - numOfByte;
        int yMax = pixels[0].length -numOfByte;
        for(int i = 1; i < xMax; i ++){
            for (int j = 1; j < yMax; j++){
                isConsecutiveBlack(i, j, numOfByte, pixels);
            }
        }

    }


    /**
     * @param pixels The input matrix of the binary data
     * @param x x coordinate
     * @param y y coordinate
     * @param numOfByte The number of byte we consider
     *                  Example: (w = "white", b = "black")
     *                  if numOfByte = 1; when we see "w b w", we change "b" to "w"
     *                  if numOfByte = 2; when we see "w b b w", we change "b b" to "w w", etc.
     */
    private static void isConsecutiveBlack(int x, int y, int numOfByte, byte[][]pixels){
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

    /**
     * When a white block is in between two black block, change it to black
     * @param pixels Input matrix
     */
    public static void addBlack(byte[][] pixels){
        for(int i = 1; i < pixels.length - 1; i ++){
            for (int j = 1; j < pixels[0].length - 1; j++){
               if(pixels[i][j] == (byte)1 && pixels[i][j+1] == (byte)0 && pixels[i][j-1] == (byte)0){
                   pixels[i][j] = (byte)0;
               }
            }
        }

    }


}
