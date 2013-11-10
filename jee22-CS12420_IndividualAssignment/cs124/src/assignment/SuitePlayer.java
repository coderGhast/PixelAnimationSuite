package assignment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * The 'Display' or 'Player' side of the Animation Suite controller.
 * </p>
 * <p>
 * This class is similar to <code>SuiteDirector</code>, but controls the
 * Animation Player side.
 * </p>
 * <p>
 * It allows the user to load in and watch a previously saved animation. They
 * have the capabilities to Play, Pause, Skip a frame forward/backwards, Stop
 * and increase the frame speed.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.1
 */
@SuppressWarnings("serial")
public class SuitePlayer extends JPanel implements ActionListener,
		ChangeListener {

	// ========== ITEM CREATIONS ==========

	// ---------- Button bar ----------
	// ========== ITEM CREATIONS ==========

	// ---------- Menu Bar and Menus ----------
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private RecentItemsMenu recentItems;

	// ---------- Menu Buttons ----------
	// FILE
	private JMenuItem openMenuItem;
	private JMenu openRecentMenu;
	private JMenuItem exitMenuItem;

	// ---------- JSlider for Speed ----------
	private JPanel speedPanel;
	private JLabel sliderLabel;
	private JSlider speedSlider;

	// ---------- Animation Panel -----------
	private JPanel animationPanel;
	private JButton backwardStepButton;
	private JButton playButton;
	private JButton pauseButton;
	private JButton forwardStepButton;
	private JButton stopButton; // Goes back to Frame #1.

	// ---------- Animation Icons ----------
	private ImageIcon playIcon;
	private ImageIcon pauseIcon;
	private ImageIcon stopIcon;
	private ImageIcon foreIcon;
	private ImageIcon backIcon;

	// --------- Animation Board ----------
	private SuiteViewer viewer; // Area the grid is shown in.
	private Thread runAnimation;

	// --------- Additional ----------
	private boolean fileOpen;
	private boolean threadRunning;
	private static final int MIN_SPEED = 1;
	private static final int MAX_SPEED = 8;
	private static final int INIT_SPEED = 1;
	private ImageIcon iconSpace;
	private static final int STRUT_SIZE = 10;
	private static final int BORDER_SIZE = 3;
	private static final int SPEED_SLIDER_BORDER = 10;
	private static final int BASE_FRAME_SPEED = 400;

	// =========== CONSTRUCTOR ==========

	/**
	 * <p>
	 * Much like <code>SuiteDirector</code>, the constructor calls to a number
	 * of methods to setup it's Components and sets up it's own layout and
	 * attributes.
	 * </p>
	 */
	public SuitePlayer() {
		menuBar = new JMenuBar(); // Creates a new JMenuBar.
		setupFileMenu();
		menuBar.add(Box.createHorizontalStrut(STRUT_SIZE)); // These spread out
															// the Menu buttons.
		setupSpeedPanel();
		setupImageIcons();
		setupAnimationPanel();
		addActionListeners();

		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(speedPanel, BorderLayout.EAST);
		add(animationPanel, BorderLayout.SOUTH);

		viewer = new SuiteViewer();
		viewer.setBorder(BorderFactory.createLineBorder(Color.black,
				BORDER_SIZE));
		add(viewer, BorderLayout.CENTER);

	}

	/**
	 * <p>
	 * Sets up the File menu, just as in <code>SuiteDirector</code>.
	 */
	private void setupFileMenu() {
		// =========== Menu Bar GUI ===========

		// ---------- FILE Menu Bar ----------
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
		openMenuItem.setActionCommand("open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openMenuItem.getAccessibleContext().setAccessibleDescription(
				"Opens a previous made file");
		fileMenu.add(openMenuItem);

		openRecentMenu = new JMenu("Open Recent");
		recentItems = new RecentItemsMenu(openRecentMenu, this);
		openRecentMenu.getAccessibleContext().setAccessibleDescription(
				"Opens a recent file - Sub Menu");
		fileMenu.add(openRecentMenu);

		fileMenu.addSeparator(); // Adds a horizontal line. Logically breaks up
									// the menu to be more appealing to a users
									// eye.

		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setActionCommand("exit");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		exitMenuItem.getAccessibleContext().setAccessibleDescription(
				"Exit Suite");
		fileMenu.add(exitMenuItem);
	}

	/**
	 * <p>
	 * Creates a speed slider to control the frames per second during animation
	 * play back.
	 * </p>
	 * <p>
	 * The speed panel contains a <code>JSlider</code> Speed Slider (1x - 8x
	 * speed) and a Label to tell the user what it is.
	 * </p>
	 */
	private void setupSpeedPanel() {
		speedPanel = new JPanel();
		speedPanel.setLayout(new BorderLayout());

		sliderLabel = new JLabel("  Frame Speed  ");
		speedPanel.add(sliderLabel, BorderLayout.NORTH);

		speedSlider = new JSlider(JSlider.VERTICAL, MIN_SPEED, MAX_SPEED,
				INIT_SPEED);
		speedPanel.add(speedSlider);

		speedSlider.setMajorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setBorder(BorderFactory.createEmptyBorder(0, 0,
				SPEED_SLIDER_BORDER, 0));
	}

	/**
	 * <p>
	 * Sets up <code>ImageIcons</code> just as in <code>SuiteDirector</code>
	 * </p>
	 */
	private void setupImageIcons() {
		String bkI = "backstep.png";
		String plI = "playbutton.png";
		String puI = "pause.png";
		String frI = "forestep.png";
		String stI = "stop.png";

		backIcon = addButtonIcon(bkI);
		playIcon = addButtonIcon(plI);
		pauseIcon = addButtonIcon(puI);
		foreIcon = addButtonIcon(frI);
		stopIcon = addButtonIcon(stI);
	}

	/**
	 * <p>
	 * Adds an icon to the <code>JButtons</code> for animation controls.
	 * </p>
	 * <p>
	 * Creates a <code>ClassLoader</code> based on the current running Thread,
	 * looks through the resources and finds the named file in the Resource
	 * hierarchy. In this case they are held in the 'Images' Source folder.
	 * </p>
	 * <p>
	 * After getting the stream of where the image is, it is read in and stored
	 * as an <code>Image</code>. This <code>Image</code> is then converted into
	 * an <code>ImageIcon</code> for use as an <code>Icon</code> for the
	 * <code>JButtons</code>.
	 * </p>
	 * <p>
	 * The created ImageIcon is returned and assigned to the correct JButton.
	 * </p>
	 * 
	 * @param filePath
	 *            A <code>String</code> with the name of the file to be used.
	 * @return the created <code>ImageIcon</code> for use with the
	 *         <code>JButton</code>s.
	 */
	private ImageIcon addButtonIcon(final String filePath) {
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(filePath);
			Image image = ImageIO.read(input);
			iconSpace = new ImageIcon(image);
			return iconSpace;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Unable to open Image file " + filePath + ".",
					"Error - Unable to load image", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Unable to open Image file " + filePath + ".",
					"Error - Unable to load image", JOptionPane.ERROR_MESSAGE);
		}
		return iconSpace;
	}

	/**
	 * <p>
	 * Sets up the Panel and the Buttons for controlling the play of the
	 * Animations.
	 * </p>
	 */
	private void setupAnimationPanel() {
		animationPanel = new JPanel();

		backwardStepButton = new JButton("Back");
		backwardStepButton.setActionCommand("backward");
		animationPanel.add(backwardStepButton);
		backwardStepButton.setIcon(backIcon);

		playButton = new JButton("Play");
		playButton.setActionCommand("play");
		animationPanel.add(playButton);
		playButton.setIcon(playIcon);

		pauseButton = new JButton("Pause");
		pauseButton.setActionCommand("pause");
		animationPanel.add(pauseButton);
		pauseButton.setIcon(pauseIcon);

		forwardStepButton = new JButton("Forward");
		forwardStepButton.setActionCommand("forward");
		animationPanel.add(forwardStepButton);
		forwardStepButton.setIcon(foreIcon);

		stopButton = new JButton("Stop");
		stopButton.setActionCommand("stop");
		animationPanel.add(stopButton);
		stopButton.setIcon(stopIcon);

		animationPanel.setLayout(new GridLayout());
	}

	/**
	 * <p>
	 * This class is it's own ActionListener and all buttons are connected to it
	 * here.
	 * </p>
	 */
	private void addActionListeners() {
		// FILE
		openMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		// SPEED PANEL
		speedSlider.addChangeListener(this);
		// ANIMATION PANEL
		backwardStepButton.addActionListener(this);
		playButton.addActionListener(this);
		pauseButton.addActionListener(this);
		forwardStepButton.addActionListener(this);
		stopButton.addActionListener(this);
	}

	@Override
	public final void actionPerformed(final ActionEvent e) {
		String command = e.getActionCommand();
		// ========== MENU BUTTONS ==========
		// ---------- File Menu ---------

		if (command.equals("open")) {
			openFile();
		}
		if (command.equals(RecentItemsMenu.RECENT_FILE_COMMAND)) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			File file = (File) menuItem.getClientProperty("filename");
			openFile(file);
		}
		if (command.equals("exit")) {
			exit();
		}
		if (fileOpen) {
			if (command.equals("backward")) {
				viewer.backwardStep();
			}
			if (command.equals("play")) {
				// If the user hits play while it is running, interrupt it and
				// then play.
				if (threadRunning) {
					runAnimation.interrupt();
				}
				threadRunning = true;
				runAnimation = new Thread(viewer);
				runAnimation.start();
			}
			if (command.equals("pause")) {
				viewer.pause();
			}
			if (command.equals("forward")) {
				viewer.forwardStep();
			}
			if (command.equals("stop")) {
				viewer.stop();
			}
		}

	}

	/**
	 * <p>
	 * Uses <code>JFileChooser</code> to open a <code>File</code> for play back.
	 * </p>
	 */
	protected final void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int chosen = fileChooser.showOpenDialog(this);
		if (chosen == JFileChooser.APPROVE_OPTION) {
			openFile(fileChooser.getSelectedFile());
		}
		// When a file is chosen, whether from JFileChooser or the Recent list,
		// it is passed to this second version of 'openFile()' which takes the
		// file as a parameter to be passed down to the BoardModel class.
	}

	/**
	 * <p>
	 * Continuation of opening a <code>File</code>. Either from
	 * <code>JFileChooser</code> or <code>RecentItemsMenu</code>.
	 * </p>
	 * 
	 * @param file
	 *            the file to be opened.
	 */
	protected final void openFile(final File file) {
		if (fileOpen) {
			viewer.stop();
		}
		fileOpen = true;
		setCurrentFile(file);
		viewer.openFile(file);
	}

	/**
	 * <p>
	 * Exits the application
	 * </p>
	 */
	protected final void exit() {
		System.exit(0);
	}

	/**
	 * <p>
	 * Sets the <code>currentFile
	 * </p>
	 * to the <code>File</code> in the parameter.</p>
	 * 
	 * @param file
	 *            file to be the new <code>currentFile</code>.
	 */
	protected final void setCurrentFile(final File file) {
		recentItems.addItem(file);
	}

	@Override
	public final void stateChanged(final ChangeEvent e) {
		// For the Speed Slider, this ChangerListener controls the values and
		// alters the delay of the frame play back (the fps).
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			int frameSpeed = (int) source.getValue();
			if (frameSpeed == 0) {
				if (fileOpen) {
					viewer.stop();
				}
			} else {
				int frameDelay = BASE_FRAME_SPEED / frameSpeed;
				viewer.setDelay(frameDelay);
			}

		}

	}

}
