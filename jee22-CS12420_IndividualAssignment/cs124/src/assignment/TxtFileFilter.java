package assignment;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * <p>
 * <code>TxtFileFilter</code> is a custom <code>FileFilter</code> class to only
 * show a user <code>.txt</code> files when they open <code>JFileChooser</code>
 * to save or load a file on their system.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class TxtFileFilter extends FileFilter {

	/**
	 * <p>
	 * <code>return true</code> is checking each path and file, and if they end
	 * with <code>.txt</code> or are a Directory file, they will be displayed.
	 * </p>
	 */
	@Override
	public final boolean accept(final File path) {
		if (path.isDirectory()) {
			return true;
		}
		String name = path.getName().toLowerCase();
		if (name.endsWith("txt")) {
			return true;
		}
		return false;
	}

	@Override
	public final String getDescription() {
		return null;
	}
}
