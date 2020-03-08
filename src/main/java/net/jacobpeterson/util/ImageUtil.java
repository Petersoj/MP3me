package net.jacobpeterson.util;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ImageUtil {

    /**
     * Creates a gaussian kernel.
     *
     * @param size   the size
     * @param weight the weight
     *
     * @return the gaussian kernel
     */
    private static double[][] createGaussianKernel(int size, double weight) {
        if (size % 2 == 0) {
            throw new IllegalArgumentException("Size must be odd!");
        }

        double[][] kernel = new double[size][size];
        double kernelSum = 0;
        int kernelHalfWidth = (size - 1) / 2;
        double gaussianConstant = 1D / (2 * Math.PI * weight * weight);

        for (int kernelY = -kernelHalfWidth; kernelY <= kernelHalfWidth; kernelY++) {
            for (int kernelX = -kernelHalfWidth; kernelX <= kernelHalfWidth; kernelX++) {
                double distance = ((kernelY * kernelY) + (kernelX * kernelX)) / (2 * weight * weight);
                double gaussian = gaussianConstant * Math.exp(-distance);
                kernel[kernelY + kernelHalfWidth][kernelX + kernelHalfWidth] = gaussian;
                kernelSum += gaussian;
            }
        }

        for (int kernelY = 0; kernelY < size; kernelY++) {
            for (int kernelX = 0; kernelX < size; kernelX++) {
                kernel[kernelY][kernelX] = kernel[kernelY][kernelX] / kernelSum;
            }
        }

        return kernel;
    }

    /**
     * Blur an image using the gaussian method. Note that this method is not optimized at all and is super slow.
     *
     * @param imageData the image data
     * @param size      the size
     * @param weight    the weight
     *
     * @return the image data
     *
     * @see <a href="https://epochabuse.com/gaussian-blur/">Adapted from
     * https://epochabuse.com/gaussian-blur/</a>
     */
    public static ImageData gaussianBlur(ImageData imageData, int size, double weight) {
        // Apply blur from gaussian kernel (whose values must be between 0 and 1)

        double[][] kernel = createGaussianKernel(size, weight);
        int kernelHalfWidth = (size - 1) / 2;

        ImageData finalImageData = (ImageData) imageData.clone();
        Arrays.fill(finalImageData.data, (byte) 0); // Clear final image data from cloned data
        int channels = finalImageData.depth / 8; // 8 bits / 1 byte per channel

        for (int finalImageY = 0; finalImageY < imageData.height; finalImageY++) {
            for (int finalImageX = 0; finalImageX < imageData.width; finalImageX++) {

                for (int kernelY = -kernelHalfWidth; kernelY <= kernelHalfWidth; kernelY++) {
                    for (int kernelX = -kernelHalfWidth; kernelX <= kernelHalfWidth; kernelX++) {
                        int absoluteKernelY = finalImageY + kernelY;
                        int absoluteKernelX = finalImageX + kernelX;

                        if (absoluteKernelX < 0) {
                            absoluteKernelX = 0;
                        } else if (absoluteKernelX >= imageData.width) {
                            absoluteKernelX = imageData.width - 1;
                        }
                        if (absoluteKernelY < 0) {
                            absoluteKernelY = 0;
                        } else if (absoluteKernelY >= imageData.height) {
                            absoluteKernelY = imageData.height - 1;
                        }

                        // Loop through channels of pixel at absolute kernel coordinate
                        int absoluteKernelChannelStartIndex = (absoluteKernelY * imageData.width + absoluteKernelX)
                                                              * channels;
                        for (int channel = 0; channel < channels; channel++) {
                            int gaussianPixelChannel = (int) (Byte.toUnsignedInt(
                                    imageData.data[absoluteKernelChannelStartIndex + channel]) *
                                                              kernel[kernelY + kernelHalfWidth]
                                                                      [kernelX + kernelHalfWidth]);
                            int finalImageDataChannelIndex = (finalImageY * finalImageData.width + finalImageX)
                                                             * channels + channel;
                            int finalImageDataChannelInt = (Byte.toUnsignedInt(
                                    finalImageData.data[finalImageDataChannelIndex]) + gaussianPixelChannel);

                            if (finalImageDataChannelInt > 255) {
                                finalImageDataChannelInt = 255;
                            } else if (finalImageDataChannelInt < 0) {
                                finalImageDataChannelInt = 0;
                            }

                            finalImageData.data[finalImageDataChannelIndex] = (byte) finalImageDataChannelInt;
                        }
                    }
                }
            }
        }

        return finalImageData;
    }

    /**
     * Blurs an image using the box blur method. Not the best looking blur, but it's fast.
     *
     * @param originalImageData The ImageData to be average blurred. Transparency information will be ignored.
     * @param radius            the number of radius pixels to consider when blurring image.
     *
     * @return A blurred copy of the image data, or null if an error occured.
     *
     * @see
     * <a href="https://www.eclipse.org/articles/article.php?file=Article-SimpleImageEffectsForSWT/index.html">https://www.eclipse.org/articles/article.php?file=Article-SimpleImageEffectsForSWT/index.html</a>
     */
    public static ImageData boxBlur(ImageData originalImageData, int radius) {
        /*
         * This method will vertically blur all the pixels in a row at once.
         * This blurring is performed incrementally to each row.
         *
         * In order to vertically blur any given pixel, maximally (radius * 2 + 1)
         * pixels must be examined. Since each of these pixels exists in the same column,
         * they span across a series of consecutive rows. These rows are horizontally
         * blurred before being cached and used as input for the vertical blur.
         * Blurring a pixel horizontally and then vertically is equivalent to blurring
         * the pixel with both its horizontal and vertical neighbours at once.
         *
         * Pixels are blurred under the notion of a 'summing scope'. A certain scope
         * of pixels in a column are summed then averaged to determine a target pixel's
         * resulting RGB value. When the next lower target pixel is being calculated,
         * the topmost pixel is removed from the summing scope (by subtracting its RGB) and
         * a new pixel is added to the bottom of the scope (by adding its RGB).
         * In this sense, the summing scope is moving downward.
         */
        if (radius < 1) {
            return originalImageData;
        }
        // prepare new image data with 24-bit direct palette to hold blurred copy of image
        ImageData newImageData = new ImageData(originalImageData.width, originalImageData.height, 24,
                                               new PaletteData(0xFF, 0xFF00, 0xFF0000));
        if (radius >= newImageData.height || radius >= newImageData.width) {
            radius = Math.min(newImageData.height, newImageData.width) - 1;
        }
        // initialize cache
        ArrayList<RGB[]> rowCache = new ArrayList<>();
        int cacheSize = Math.min(radius * 2 + 1, newImageData.height); // number of rows of imageData we cache
        int cacheStartIndex = 0; // which row of imageData the cache begins with
        for (int row = 0; row < cacheSize; row++) {
            // row data is horizontally blurred before caching
            rowCache.add(rowCache.size(), blurRow(originalImageData, row, radius));
        }
        // sum red, green, and blue values separately for averaging
        RGB[] rowRGBSums = new RGB[newImageData.width];
        int[] rowRGBAverages = new int[newImageData.width];
        int topSumBoundary = 0; // current top row of summed values scope
        int targetRow = 0; // row with RGB averages to be determined
        int bottomSumBoundary = 0; // current bottom row of summed values scope
        int numRows = 0; // number of rows included in current summing scope
        for (int i = 0; i < newImageData.width; i++) {
            rowRGBSums[i] = new RGB(0, 0, 0);
        }
        while (targetRow < newImageData.height) {
            if (bottomSumBoundary < newImageData.height) {
                do {
                    // sum pixel RGB values for each column in our radius scope
                    for (int col = 0; col < newImageData.width; col++) {
                        rowRGBSums[col].red +=
                                rowCache.get(bottomSumBoundary - cacheStartIndex)[col].red;
                        rowRGBSums[col].green +=
                                rowCache.get(bottomSumBoundary - cacheStartIndex)[col].green;
                        rowRGBSums[col].blue +=
                                rowCache.get(bottomSumBoundary - cacheStartIndex)[col].blue;
                    }
                    numRows++;
                    bottomSumBoundary++; // move bottom scope boundary lower
                    if (bottomSumBoundary < newImageData.height &&
                        (bottomSumBoundary - cacheStartIndex) > (radius * 2)) {
                        // grow cache
                        rowCache.add(rowCache.size(), blurRow(originalImageData, bottomSumBoundary, radius));
                    }
                } while (bottomSumBoundary <= radius); // to initialize rowRGBSums at start
            }
            if ((targetRow - topSumBoundary) > (radius)) {
                // subtract values of top row from sums as scope of summed values moves down
                for (int col = 0; col < newImageData.width; col++) {
                    rowRGBSums[col].red -=
                            rowCache.get(topSumBoundary - cacheStartIndex)[col].red;
                    rowRGBSums[col].green -=
                            rowCache.get(topSumBoundary - cacheStartIndex)[col].green;
                    rowRGBSums[col].blue -=
                            rowCache.get(topSumBoundary - cacheStartIndex)[col].blue;
                }
                numRows--;
                topSumBoundary++; // move top scope boundary lower
                rowCache.remove(0); // remove top row which is out of summing scope
                cacheStartIndex++;
            }
            // calculate each column's RGB-averaged pixel
            for (int col = 0; col < newImageData.width; col++) {
                rowRGBAverages[col] = newImageData.palette.getPixel(new RGB(rowRGBSums[col].red / numRows,
                                                                            rowRGBSums[col].green / numRows,
                                                                            rowRGBSums[col].blue / numRows));
            }
            // replace original pixels
            newImageData.setPixels(0, targetRow, newImageData.width, rowRGBAverages, 0);
            targetRow++;
        }
        return newImageData;
    }

    /**
     * Average blurs a given row of image data. Returns the blurred row as a matrix of separated RGB values.
     *
     * @param originalImageData the original image data
     * @param row               the row
     * @param radius            the radius
     *
     * @return the rgb[]
     *
     * @see
     * <a href="https://www.eclipse.org/articles/article.php?file=Article-SimpleImageEffectsForSWT/index.html">https://www.eclipse.org/articles/article.php?file=Article-SimpleImageEffectsForSWT/index.html</a>
     */
    private static RGB[] blurRow(ImageData originalImageData, int row, int radius) {
        RGB[] rowRGBAverages = new RGB[originalImageData.width]; // resulting rgb averages
        int[] lineData = new int[originalImageData.width];
        originalImageData.getPixels(0, row, originalImageData.width, lineData, 0);
        int r = 0, g = 0, b = 0; // sum red, green, and blue values separately for averaging
        int leftSumBoundary = 0; // beginning index of summed values scope
        int targetColumn = 0; // column of RGB average to be determined
        int rightSumBoundary = 0; // ending index of summed values scope
        int numCols = 0; // number of columns included in current summing scope
        RGB rgb;
        while (targetColumn < lineData.length) {
            if (rightSumBoundary < lineData.length) {
                // sum RGB values for each pixel in our radius scope
                do {
                    rgb = originalImageData.palette.getRGB(lineData[rightSumBoundary]);
                    r += rgb.red;
                    g += rgb.green;
                    b += rgb.blue;
                    numCols++;
                    rightSumBoundary++;
                } while (rightSumBoundary <= radius); // to initialize summing scope at start
            }
            // subtract sum of left pixel as summing scope moves right
            if ((targetColumn - leftSumBoundary) > (radius)) {
                rgb = originalImageData.palette.getRGB(lineData[leftSumBoundary]);
                r -= rgb.red;
                g -= rgb.green;
                b -= rgb.blue;
                numCols--;
                leftSumBoundary++;
            }
            // calculate RGB averages
            rowRGBAverages[targetColumn] = new RGB(r / numCols, g / numCols, b / numCols);
            targetColumn++;
        }
        return rowRGBAverages;
    }

    /**
     * Blur an image using several box blurs. This method is really fast and yields results similar to that of a direct
     * gaussian blur.
     *
     * @param imageData the image data
     * @param size      the size
     *
     * @return the image data
     *
     * @see
     * <a href="http://blog.ivank.net/fastest-gaussian-blur.html">http://blog.ivank.net/fastest-gaussian-blur.html</a>
     */
    public static ImageData oneDimensionalBlur(ImageData imageData, int size) {
        ImageData finalImageData = (ImageData) imageData.clone();

        throw new UnsupportedOperationException("Not implemented!");
    }

    /**
     * Determines the box sizes needed to run a box blur 'n' times to achieve a Gaussian blur equivalent of some radius
     * (no idea how the math works).
     *
     * @param radius   the radius ('sigma')
     * @param boxCount the box count ('n')
     *
     * @return the box dimensions
     *
     * @see
     * <a href="http://blog.ivank.net/fastest-gaussian-blur.html">http://blog.ivank.net/fastest-gaussian-blur.html</a>
     */
    private static int[] getGaussianBoxes(int radius, int boxCount) {
        int wl = (int) Math.sqrt((double) (12 * radius * radius / boxCount) + 1);  // Ideal averaging filter width
        if (wl % 2 == 0) {
            wl--;
        }
        int wu = wl + 2;

        int mIdeal = (12 * radius * radius - boxCount * wl * wl - 4 * boxCount * wl - 3 * boxCount) / (-4 * wl - 4);
        int m = Math.round(mIdeal);

        var sizes = new int[boxCount];
        for (var i = 0; i < boxCount; i++) {
            sizes[i] = i < m ? wl : wu;
        }

        return sizes;
    }

    /**
     * Gets dominant colors of an image. Note that this ignores alpha channels.
     *
     * @param imageData the image data
     * @param amount    the amount of dominant colors
     *
     * @return the dominant colors with key being the color and the value being the number of times it showed up
     */
    public static HashMap<RGB, Integer> getDominantColors(ImageData imageData, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be great than 1!");
        }

        HashMap<RGB, Integer> rgbCounts = new HashMap<>();

        int[] pixels = new int[imageData.width * imageData.height];
        imageData.getPixels(0, 0, pixels.length, pixels, 0);

        for (int y = 0; y < imageData.height; y++) {
            for (int x = 0; x < imageData.width; x++) {
                RGB currentRGB = imageData.palette.getRGB(pixels[y * imageData.width + x]);

                if (rgbCounts.containsKey(currentRGB)) {
                    rgbCounts.replace(currentRGB, rgbCounts.get(currentRGB) + 1);
                } else {
                    rgbCounts.put(currentRGB, 1);
                }
            }
        }

        rgbCounts = MapUtil.sortByValue(rgbCounts, MapUtil.SortOrder.DESCENDING, null, Integer::compareTo);

        if (rgbCounts.size() <= amount) { // Don't do any dominance grouping
            return rgbCounts;
        }

        throw new UnsupportedOperationException("Not implemented yet!");

        /*
        Attempt using weighted range:
        // Get the weighted range
        int weightedGray1 = 127;
        int weightedGray2 = 0;
        for (RGB rgb : rgbCounts.keySet()) {
            Integer rgbCount = rgbCounts.get(rgb);
            double weight = (double) rgbCount / pixels.length;
            int weightedGray = (int) (grayscale(rgb).red * weight);
            if (weightedGray > weightedGray1) {
                weightedGray1 += weightedGray;
            } else if (weightedGray < weightedGray2) {
                weightedGray2 -= weightedGray;
            } else { // weightedGray is in between the weightedGray1 and the weightedGray2
                double midpoint = weightedGray1 - weightedGray2;
                if (weightedGray > midpoint) {
                    weightedGray1 -= weightedGray;
                } else if (weightedGray < midpoint) {
                    weightedGray2 += weightedGray;
                } // If weightedGray is equal to midpoint don't do anything
            }
        }
        int weightedRange = Math.abs(weightedGray2 - weightedGray1);
        System.out.println(weightedGray1);
        System.out.println(weightedGray2);
        System.out.println(weightedRange);
         */

        /*
        Attempt using some other method:
        LinkedHashMap<RGB, Integer> dominantColors = new LinkedHashMap<>(amount);

        // Get the weighted range
        int weightedRange = 0;
        int previousWeightedGray = -1;
        for (RGB rgb : rgbCounts.keySet()) {
            Integer rgbCount = rgbCounts.get(rgb);
            double weight = (double) rgbCount / pixels.length;
            int weightedGray = (int) (grayscale(rgb).red * weight);

            if (previousWeightedGray == -1) {
                previousWeightedGray = weightedGray;
            } else {
                double distance = Math.abs(weightedGray - previousWeightedGray);
                weightedRange += distance * weight;
            }
        }

        System.out.println(weightedRange);

        // Larger the amount then the smaller the grouping distance.
        double groupingDistance = (double) weightedRange / amount;

        for (RGB rgb : rgbCounts.keySet()) {
            Integer rgbCount = rgbCounts.get(rgb);
            double weight = (double) rgbCount / pixels.length;
            // Less amount desired means small distances between high weight colors will be grouped and large
            // distances between high weight colors will not be grouped
            // Whether or not to include another color in the grouping distance is a function of that color's weight,
            // the amount desired, and the current color's weight
        }

        return dominantColors;
        */
        // Grouping distance should be a function of 'amount', the distance, and the weight, so as to group colors when
        // they're close enough, but not group them if their weights are significant enough depending on the total
        // 'amount' desired.
    }

    /**
     * Gets the average color of an image.
     *
     * @param imageData the image data
     *
     * @return the average color
     */
    public static RGB getAverageColor(ImageData imageData) {
        int[] pixels = new int[imageData.width * imageData.height];
        imageData.getPixels(0, 0, pixels.length, pixels, 0);
        int redSum = 0, greenSum = 0, blueSum = 0;

        for (int index = 0; index < pixels.length; index++) {
            RGB indexRGB = imageData.palette.getRGB(pixels[index]);
            redSum += indexRGB.red;
            greenSum += indexRGB.green;
            blueSum += indexRGB.blue;
        }

        return new RGB(redSum / pixels.length, greenSum / pixels.length, blueSum / pixels.length);
    }

    /**
     * Compute the distance between two RGBAs (the distance in 4D space where the RGBA values of the colors are the w,
     * x, y, z coordinates).
     *
     * @param first  the first
     * @param second the second
     *
     * @return the double
     */
    public static double colorDistance(RGBA first, RGBA second) {
        return Math.sqrt(Math.pow(second.rgb.red - first.rgb.red, 2) +
                         Math.pow(second.rgb.green - first.rgb.green, 2) +
                         Math.pow(second.rgb.blue - first.rgb.blue, 2) +
                         Math.pow(second.alpha - first.alpha, 2));
    }

    /**
     * Compute the distance between two RGBs (the distance in 3D space where the RGB values of the colors are the x, y,
     * and z coordinates).
     *
     * @param first  the first
     * @param second the second
     *
     * @return the double
     */
    public static double colorDistance(RGB first, RGB second) {
        return Math.sqrt(Math.pow(second.red - first.red, 2) +
                         Math.pow(second.green - first.green, 2) +
                         Math.pow(second.blue - first.blue, 2));
    }

    /**
     * Average two RGBAs (including alpha channel).
     *
     * @param first  the first
     * @param second the second
     *
     * @return the color
     */
    public static RGBA averageColor(RGBA first, RGBA second) {
        return new RGBA((first.rgb.red + second.rgb.red) / 2,
                        (first.rgb.green + second.rgb.green) / 2,
                        (first.rgb.blue + second.rgb.blue) / 2,
                        (first.alpha + second.alpha) / 2);
    }

    /**
     * Average two RGBs.
     *
     * @param first  the first
     * @param second the second
     *
     * @return the rgb
     */
    public static RGB averageColor(RGB first, RGB second) {
        return new RGB((first.red + second.red) / 2,
                       (first.green + second.green) / 2,
                       (first.blue + second.blue) / 2);
    }

    /**
     * Determines if a colors RGB values are within a certain tolerance of each other.
     *
     * @param color     the color
     * @param tolerance the tolerance (RGB channel difference)
     *
     * @return the boolean
     */
    public static boolean isGrayColor(RGB color, int tolerance) {
        return Math.abs(color.red - color.green) <= tolerance &&
               Math.abs(color.red - color.blue) <= tolerance &&
               Math.abs(color.green - color.blue) <= tolerance;
    }

    /**
     * Grayscales a color.
     *
     * @param color the color
     *
     * @return the grayscaled color RGB
     */
    public static RGB grayscale(RGB color) {
        int average = (color.red + color.green + color.blue) / 3;
        return new RGB(average, average, average);
    }

    /**
     * Clamps the average of the given color to white if it is greater or equal to the threshold and to black if less
     * than the threshold. Same for the alpha channel.
     *
     * @param color          the color
     * @param colorThreshold the threshold (usually 127)
     * @param alphaThreshold the alpha threshold (-1 to disable)
     *
     * @return the clamped RGBA
     */
    public static RGBA clamp(RGBA color, int colorThreshold, int alphaThreshold) {
        RGB clampedRGB = clamp(color.rgb, colorThreshold);
        RGBA clampedRGBA = new RGBA(clampedRGB.red, clampedRGB.green, clampedRGB.blue, 0);

        if (alphaThreshold > 0) {
            clampedRGBA.alpha = color.alpha >= alphaThreshold ? 0b1111_1111 : 0b0000_0000;
        }

        return clampedRGBA;
    }

    /**
     * Clamps the average of the given color to white if it is greater or equal to the threshold and to black if less
     * than the threshold.
     *
     * @param color     the color
     * @param threshold the threshold (usually 127)
     *
     * @return the clamped RGB
     */
    public static RGB clamp(RGB color, int threshold) {
        int clamped = grayscale(color).red >= threshold ? 0b1111_1111 : 0b0000_0000;
        return new RGB(clamped, clamped, clamped);
    }
}
