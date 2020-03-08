package net.jacobpeterson.view.songlist;

import net.jacobpeterson.view.MainShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;

public class SongListBackground extends Canvas {

    public SongListBackground(MainShell mainShell) {
        super(mainShell.getShell(), SWT.TRANSPARENT);
    }
}
