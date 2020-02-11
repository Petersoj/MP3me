package net.jacobpeterson;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MP3me {

    public static void main(String[] args) throws Exception {
        System.out.println(new Gson().toJson(new JsonPrimitive(123)));

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setBounds(0, 0, 1920, 1080);
        shell.setLayout(new FillLayout());
        shell.layout(); // Layout everything
        shell.open(); // Shows the Window

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
