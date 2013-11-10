package assignment;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * <p>
 * <code>Tabs</code> is the overall application Frame, that holds the
 * <code>JPanels</code> for the Director and Display in tabs.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
class Tabs extends JFrame {
	// ========== Create required components ==========
	private JTabbedPane tabbedPane;
	private JPanel builderPanel;
	private JPanel playerPanel;
	private static final int APPLICATION_WIDTH = 675;
	private static final int APPLICATION_HEIGHT = 695;

	// ========== CONSTRUCTOR ==========
	/**
	 * <p>
	 * Constructs a new instance of panels to become tabs and then places
	 * <code>SuiteDirector</code> and <code>SuitePlayer</code> on them.
	 * </p>
	 */
	public Tabs() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To 'x' close the
														// program.
		setTitle("Animation Suite"); // Sets the text for the title bar.
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT); // Sets the size for the
														// program to open at.
		setResizable(false);
		setBackground(Color.gray); // Sets the background color.

		JPanel tabsPanel = new JPanel(); // Creates a new Panel to hold the
											// tabbed pane (that will
											// itself hold the tabs).
		tabsPanel.setLayout(new BorderLayout()); // Define how the Panel should
													// be laid out.
		add(tabsPanel); // Adds the tabs panel to this class, which inherits
						// JFrame.

		// Create the content to go onto the tabs via methods which
		// make new instances of their relevant Objects.
		createBuilder(); // Calls the method to make the Director.
		createPlayer(); // Calls the method to make the Display.

		// Create a tabbed pane
		tabbedPane = new JTabbedPane(); // Creates the Pane to hold the tabs
										// themselves.
		tabbedPane.addTab("Director", builderPanel); // Adds tab for 'Director'
														// and 'Display' to the
														// Pane
		tabbedPane.addTab("Player", playerPanel); // and names them for viewing
													// as text on the tab.
		tabsPanel.add(tabbedPane, BorderLayout.CENTER); // Adds the Pane holding
														// the tabs to the
														// overall Panel
														// made earlier.
		setVisible(true); // So we can see everything!
	}

	/**
	 * <p>
	 * Creates an new instance of the Director.
	 * </p>
	 */
	private void createBuilder() {
		builderPanel = new SuiteDirector();
	}

	/**
	 * <p>
	 * Creates a new instance of the Display.
	 * </p>
	 */
	private void createPlayer() {
		playerPanel = new SuitePlayer();
	}

}