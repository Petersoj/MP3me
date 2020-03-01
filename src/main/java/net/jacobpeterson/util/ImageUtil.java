package net.jacobpeterson.util;

import org.eclipse.swt.graphics.ImageData;

import java.util.Arrays;

public class ImageUtil {

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
        // Create gaussian kernel

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

        // Apply blur from gaussian kernel (whose values must be between 0 and 1)

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

        return finalImageData;
    }

    /**
     * Determines the approximate box sizes needed to run a box blur 'n' times to achieve a Gaussian blur equivalent of
     * some radius (no idea how the math works).
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

    private static void boxBlurHorizontal(byte[] imageData, byte[] outputData, int width, int height, int radius) {
        // Radius range on either side of a pixel + the pixel itself
        double accumlatorAverager = (double) 1 / (radius + radius + 1);
        for (int i = 0; i < height; i++) {
            int ti = i * width; // Will traverse the width of the image for each loop of the parent "for loop"
            int li = ti; // Trailing pixel index
            int ri = ti + radius; // Pixel index of the furthest reach of the radius

            int firstValue = imageData[ti]; // first pixel value of the row
            int lastValue = imageData[ti + width - 1]; // last pixel value in the row
            int val = (radius + 1) * firstValue; // create a "value accumulator" - we will be calculating the average of pixels surrounding each one - is faster to add newest value, remove oldest, and then average. This initial value is for pixels outside image bounds

            //for length of radius, accumulate the total value of all pixels from current pixel index and record it into the target channel first pixel
            for (int j = 0; j < radius; j++) {
                val += imageData[ti + j];
            }

            // for the next $boxRadius pixels in the row, record pixel value of average of all pixels within the radius and save average into target channel
            for (int j = 0; j <= radius; j++) {
                val += imageData[ri++] - firstValue;
                outputData[ti++] = (byte) Math.round(val * accumlatorAverager);
            }

            // now that we've completely removed the overflow pixels from the value accumulator, continue on, adding new values, removing old ones, and averaging the acculated value
            for (int j = radius + 1; j < width - radius; j++) {
                val += imageData[ri++] - imageData[li++];
                outputData[ti++] = (byte) Math.round(val * accumlatorAverager);
            }

            // finish off the row of pixels, duplicating the edge pixel instead of going out of image bounds
            for (int j = width - radius; j < width; j++) {
                val += lastValue - imageData[li++];
                outputData[ti++] = (byte) Math.round(val * accumlatorAverager);
            }
        }
    }
}
