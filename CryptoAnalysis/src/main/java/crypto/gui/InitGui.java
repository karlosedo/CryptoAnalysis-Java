package crypto.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class InitGui {

		public static void main(String[] args) {
			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setText("Crypto");
			shell.setLayout(new GridLayout(1,false));
			PrincipalComposite principal = new PrincipalComposite(shell, 0);
			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if(!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		}
}
