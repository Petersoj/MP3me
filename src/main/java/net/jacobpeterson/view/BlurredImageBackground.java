package net.jacobpeterson.view;

import net.jacobpeterson.util.ImageUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class BlurredImageBackground extends Canvas implements PaintListener {

    private Image blurredImage;

    /**
     * Instantiates a new Blurred image background.
     *
     * @param mainShell the main shell
     */
    public BlurredImageBackground(MainShell mainShell) {
        super(mainShell.getShell(), SWT.TRANSPARENT);

        this.setBackground(mainShell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        this.addPaintListener(this);
    }

    @Override
    public void paintControl(PaintEvent paintEvent) {
        if (blurredImage == null || blurredImage.isDisposed()) {
            return;
        }

        GC gc = paintEvent.gc;
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.ON);

        Rectangle bounds = this.getBounds();
        Rectangle imageBounds = blurredImage.getBounds();
        double thisAspectRatio = (double) bounds.width / bounds.height;
        double imageAspectRatio = (double) imageBounds.width / imageBounds.height;
        int imageHeight = bounds.height;
        int imageWidth = bounds.width;
        if (imageAspectRatio < thisAspectRatio) { // Cut off tops of image
            imageHeight = (int) (bounds.width / imageAspectRatio);
        } else { // Cut off sides of image
            imageWidth = (int) (bounds.height * imageAspectRatio);
        }
        int imageX = (bounds.width / 2) - (imageWidth / 2);
        int imageY = (bounds.height / 2) - (imageHeight / 2);

        gc.drawImage(blurredImage, 0, 0, imageBounds.width, imageBounds.height,
                imageX, imageY, imageWidth, imageHeight);
    }

    /**
     * Gets blurred image.
     *
     * @return the blurred image
     */
    public Image getBlurredImage() {
        return blurredImage;
    }

    /**
     * Sets image. This method will block until the underlying image is blurred on the current thread. Use {@link
     * Composite#redraw()} to force the blurred image to update on the UI thread.
     *
     * @param image      the image
     * @param blurRadius the blur radius. If this is null, then no blur is applied to the passed in image.
     */
    public void setBlurredImage(Image image, Integer blurRadius) {
        if (blurRadius != null) {
            this.blurredImage = new Image(image.getDevice(), ImageUtil.boxBlur(image.getImageData(), blurRadius));
        } else {
            this.blurredImage = image;
        }
    }
}
