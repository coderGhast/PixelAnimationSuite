package assignment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * </p>The main class to start the application for Animation Suite.</p>
 * <p>
 * This application aims to provide a user with an Animation Suite, offering
 * both a 'Director' where they can build a number of frames and images to be
 * animated and a 'Display' where they may play their animations, loading them
 * from previously saved files, based on <code>.txt</code> files.
 * </p>
 * <p>
 * NOTE: For ease in coding/to avoid confusion, I will be using the American
 * spelling of the word 'Color' as well as 'Colour' in the majority of my
 * comments and code itself. No offence to the English language meant!
 * </p>
 * <p>
 * Launches the application by creating a new instance of the object
 * <code>Tabs</code>.
 * </p>
 * <p>
 * <code>Tabs</code> is an <code>Object</code> that is a number of 'tabbed'
 * frames in a single frame window. I felt this would be a good way to keep both
 * the Director and the Display together for ease of the user. Having a frame
 * for each of the windows seemed a little cumbersome and like it would take up
 * too much space on a user's screen.
 * </p>
 * <p>
 * Having it as it's own <code>Object</code> means it can contain both Display
 * and Director, and also any additional windows should the Animation Suite
 * require extra in the future (doubtful, but useful functionality all the
 * same).
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class SuiteDriver {
	@SuppressWarnings("unused")
	private static Tabs tabs;

	/**
	 * <p>
	 * Starts the application.
	 * </p>
	 * <p>
	 * Sets the Look and Feel to that of whatever Operating System the user is
	 * on
	 * </p>
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Class not found!",
					"Error: Look and Feel - Class Not Found",
					JOptionPane.ERROR_MESSAGE);
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Not able to Instantiate Look and Feel!",
					"Error: Look and Feel - Unable to Instantiate",
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Illegal Access Exception!",
					"Error: Look and Feel - Illegal Access Exception",
					JOptionPane.ERROR_MESSAGE);
		} catch (UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Look and Feel "
					+ UIManager.getCrossPlatformLookAndFeelClassName()
					+ " not supported!",
					"Error: Look and Feel - Unsupported Look and Feel",
					JOptionPane.ERROR_MESSAGE);
		}
		makeApp();
	}

	/**
	 * <p>
	 * Creates the application with invokeLater().
	 * </p>
	 */
	public static void makeApp() {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					build();
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Exception encountered on attempt to build application",
					"Error - Not able to build application",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * <p>
	 * Makes a new instance of <code>Tabs</code> that opens the Animation Suite
	 * window.
	 * </p>
	 */
	public static void build() {
		tabs = new Tabs();
	}

}
