package net.jacobpeterson.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

public class SongListComposite extends ScrolledComposite {

    public SongListComposite(MainShell mainShell) {
        super(mainShell.getShell(), SWT.NONE);
    }
}
