package net.jacobpeterson.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class BlurredImageBackground extends Canvas implements PaintListener {

    private MainShell mainShell;
    private Image image;
    public int blurValue = 1;

    /**
     * Instantiates a new Blurred image background.
     *
     * @param mainShell the main shell
     */
    public BlurredImageBackground(MainShell mainShell) {
        super(mainShell.getShell(), SWT.TRANSPARENT | SWT.NO_REDRAW_RESIZE);

        this.mainShell = mainShell;
        this.addPaintListener(this);
    }

    @Override
    public void paintControl(PaintEvent paintEvent) {
        if (image == null || image.isDisposed()) {
            return;
        }

        GC gc = paintEvent.gc;
        Rectangle bounds = this.getBounds();
    }

    /**
     * Draw blurred image.
     *
     * @param gc the gc
     */
    private void drawBlurredImage(GC gc) {
        Rectangle shellBounds = mainShell.getShell().getBounds();
        Rectangle imageBounds = image.getBounds();

        int shellAspectRatio = shellBounds.width / shellBounds.height;
        int imageAspectRatio = imageBounds.width / imageBounds.height;
        if (imageAspectRatio > shellAspectRatio) {
            // Cut off sides of image
        } else if (imageAspectRatio < shellAspectRatio) {
            // Cut off tops of image
        } else { // The aspect ratios are equal

        }
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
