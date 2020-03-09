package net.jacobpeterson.util;

import org.eclipse.swt.graphics.Region;

public class GraphicsUtil {

    /**
     * Gets a region that delineates a rounded rectangle.
     *
     * @param x the initial X corner (of the original rectangle)
     * @param y the initial Y corner (of the original rectangle)
     * @param W the max width of the rectangle
     * @param H the max height of the rectangle
     * @param r the radius of the rounding circles
     *
     * @return the following region:
     * <pre>
     *       P0 (x,y)
     *       . ___ _ _ _ _ _ _ _ _ _ _ _ ___
     *        /   \                     /   \    A
     *       |  ·  |                   |  ·  |   :
     *        \___/                     \___/    :
     *       |   <->                         |   :
     *            r                              :
     *       |                               |   :
     *                                           :
     *       |                               |   : H
     *                                           :
     *       |                               |   :
     *                                           :
     *       |                               |   :
     *                                           :
     *       | ___                       ___ |   :
     *        /   \                     /   \    :
     *       |  ·  |                   |  ·  |   :
     *        \___/ _ _ _ _ _ _ _ _ _ _ \___/    v
     *
     *       <------------------------------->
     *                       W
     * </pre>
     *
     * @see <a href="https://stackoverflow.com/a/45330521/4352701">https://stackoverflow.com/a/45330521/4352701</a>
     */
    public static Region getRoundRectangleRegion(int x, int y, int W, int H, int r) {
        Region region = new Region();
        int d = (2 * r); // diameter

        region.add(getCircleCoordinates(r, (x + r), (y + r)));
        region.add(getCircleCoordinates(r, (x + W - r), (y + r)));
        region.add(getCircleCoordinates(r, (x + W - r), (y + H - r)));
        region.add(getCircleCoordinates(r, (x + r), (y + H - r)));

        region.add((x + r), y, (W - d), H);
        region.add(x, (y + r), W, (H - d));

        return region;
    }

    /**
     * Defines the coordinates of a circle.
     *
     * @param r       radius
     * @param offsetX x offset of the centre
     * @param offsetY y offset of the centre
     *
     * @return the set of coordinates that approximates the circle.
     *
     * @see
     * <a href="http://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet134.java">SWT
     * Snippet</a>
     */
    public static int[] getCircleCoordinates(int r, int offsetX, int offsetY) {
        int[] polygon = new int[8 * r + 4];
        // x^2 + y^2 = r^2
        for (int i = 0; i < 2 * r + 1; i++) {
            int x = i - r;
            int y = (int) Math.sqrt(r * r - x * x);
            polygon[2 * i] = offsetX + x;
            polygon[2 * i + 1] = offsetY + y;
            polygon[8 * r - 2 * i - 2] = offsetX + x;
            polygon[8 * r - 2 * i - 1] = offsetY - y;
        }
        return polygon;
    }

}
