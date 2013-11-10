// NEEDS - 
// - Write Help Menu

package assignment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * <p>
 * This class handles all operations involving the use of the GUI for the
 * 'Director' part of the Animation Suite.
 * <p>
 * 
 * <p>
 * It builds the required components, places them in the correct areas and
 * containers, assigns any necessary values and names and handles any actions
 * that take place on this tab.<br />
 * This includes: Color selection (including which mouse button), tool
 * selection, options for this tab and naturally, is the class that deals with
 * interacting with the <code>SuiteCanvas</code> class, which controls the
 * majority of the display results for the actual grid and 'animation' area that
 * the user interacts with.
 * </p>
 * 
 * <p>
 * While the original project specification only lays out the idea for a basic
 * 'Director', using each mouse button for a different color and clicking on a
 * square again to clear it, I wanted something that was quite reminiscent of
 * the old simple and basic MS Paint. While it is no way near as advanced as
 * that, I feel satisfied with the results.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.2
 */
@SuppressWarnings("serial")
public class SuiteDirector extends JPanel implements ActionListener {

	// ========== ITEM CREATIONS ==========

	// ---------- Menu Bar and Menus ----------
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private RecentItemsMenu recentItems;
	private JMenu editMenu;
	private JMenu projectMenu;
	private JMenu helpMenu;

	// ---------- Menu Buttons ----------
	// FILE
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenu openRecentMenu;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem exitMenuItem;
	// EDIT
	private JMenuItem clearMenuItem;
	private JMenuItem fillWithCol1;
	private JMenuItem fillWithCol2;
	// PROJECT
	private JMenuItem newFrameMenuItem;
	private JMenuItem deleteFrameMenuItem;
	private JMenuItem nextFrameMenuItem;
	private JMenuItem previousFrameMenuItem;
	private JMenuItem preferencesMenuItem;
	// HELP
	private JMenuItem aboutMenuItem;
	private JMenuItem helpMenuItem;

	// ---------- Sidebar Panels -----------
	private JPanel colorHolderPanel;
	private JPanel sideBar;
	private JPanel toolPanel;
	private JPanel foregroundColor;
	private JPanel foreContainer;
	private JPanel backgroundColor;
	private JPanel backContainer;

	// ---------- Sidebar Buttons -----------
	private ButtonGroup group; // To group all tool Radio buttons together.
	private JRadioButton pencilButton;
	private JRadioButton spillButton;
	private JRadioButton paintbrushButton;

	// ---------- Sidebar Icons ----------
	private ImageIcon pencilIcon;
	private ImageIcon paintbrushIcon;
	private ImageIcon spillIcon;
	private ImageIcon iconSpace;

	// ---------- Sidebar Labels and Displays ----------
	/*
	 * Many labels are there to help the user understand what each section is
	 * and to know what to do when using the application. For example,
	 * 'mouseColorText' simple states 'Colours on mouse buttons', yet by doing
	 * this it can convey to a user that the two colors shown below, coupled
	 * with the text 'Left' and 'Right' imply those are the colors available to
	 * each mouse button, and that each mouse button can have a color assigned
	 * to it depending on the click when choosing colors.
	 */
	private JLabel foregroundText;
	private JLabel backgroundText;
	private JLabel selectToolText;
	private JLabel mouseColorText;
	private JLabel heldToolText;
	private JLabel frameDisplayText;

	// ---------- Color Picker Displays ----------
	private JPanel colorButtonsPanel;
	private JPanel colorBarPanel;
	private JButton colorButton;
	private ArrayList<JButton> arrayOfColors;

	// ---------- Frame Preview Panel ----------
	private PreviewMaker previewMaker;
	private JPanel previewPanel;
	private JPanel framePreviewPanel;
	private JPanel framePreviewButtonHolder;
	private JPanel framePreviewPanelForButtons;
	private JScrollPane framePreviewScroll;
	private JLabel previewImage;
	private JButton previewNextButton;
	private JButton previewPreviousButton;

	// --------- Grid Board ----------
	private SuiteCanvas canvas; // Area the grid is shown in.

	// ---------- Opening Menu Items ---------
	private NewProjectMenu newProjectPanel;
	private PreferencesMenu preferences;

	// ---------- Help & About Items ----------
	private JLabel aboutText;

	// ---------- Additional ----------
	private File currentFile;
	private String selectedSize;
	private MyColor selectedColor;
	private ColorHolder colorList = new ColorHolder();
	private JFileChooser fileChooser;

	private int defaultRows = 30;
	private int defaultColumns = 30;

	private char tempLeft;
	private char tempRight;
	private int tempTool;

	private static final int CANVAS_BORDER_SIZE = 3;
	private static final int HORIZONTAL_STRUT_SIZE = 10;
	private static final int TOTAL_AMOUNT_OF_COLORS = 36;
	private static final int COLOR_BUTTON_DIMENSIONS = 20;
	private static final int COLOR_BAR_PANEL_WIDTH = 100;
	private static final int COLOR_BAR_PANEL_HEIGHT = 180;
	private static final int COLOR_BUTTONS_GRID_VALUE = 6;
	private static final int TOOL_ICON_HEIGHT = 30;
	private static final int SIDEBAR_RIGID_AREA_DIMENSION = 14;
	private static final int TOOLBAR_RIGID_AREA_DIMENSION = 5;
	private static final int HELD_COLOURS_SIZE = 45;
	private static final int PREVIEW_SCROLLBAR_SIZE = 93;
	private static final int ABOUT_TEXT_WIDTH = 300;
	private static final int ABOUT_TEXT_HEIGHT = 100;

	/**
	 * <p>
	 * Invokes a number of methods that setup the components of this application
	 * for use.
	 * </p>
	 * <p>
	 * Sets the layout of the this (Director). Adds components to the correct
	 * places on the application panel. Creates the canvas to draw upon, borders
	 * it and then adds it to the panel.<br />
	 * Sets up the frame panel and adds it. This must be done AFTER the canvas
	 * in order for it to work.
	 * </p>
	 */
	public SuiteDirector() {
		setupMenuBar();
		setupColorBar();
		setupToolIcons();
		setupSidebar();
		setupNewProject();
		setupPreferences();
		setupAbout();

		setLayout(new BorderLayout());

		add(sideBar, BorderLayout.EAST);
		add(menuBar, BorderLayout.NORTH);

		canvas = new SuiteCanvas(defaultRows, defaultColumns);
		canvas.setBorder(BorderFactory.createLineBorder(Color.black,
				CANVAS_BORDER_SIZE));
		add(canvas, BorderLayout.CENTER);

		setupFramePreviewPanel();
		add(previewPanel, BorderLayout.SOUTH);

	}

	// ---------- FILE Menu Bar ----------
	/**
	 * <p>
	 * A method to set up the <code>menuBar</code> that goes across the top of
	 * the <code>SuiteDirector</code> panel.
	 * </p>
	 * <p>
	 * Each new key is named, has a Mnemonic key added to it for easier
	 * traversal around the program without a mouse and is set with an
	 * <code>ActionCommand</code> to be used by the <code>ActionListener</code>.
	 * </p>
	 * <p>
	 * Many keys are also set with <code>Accelerator</code>s (Shortcuts), using
	 * common keys and the normal Shortcut Key for whatever operating system is
	 * in use. The keys are then added to the correct menu.
	 * </p>
	 * <p>
	 * A number of keys are also set for <code>AccessibleContext</code> with
	 * descriptions for those users who require this feature.
	 * </p>
	 */
	private void setupMenuBar() {
		menuBar = new JMenuBar(); // Creates a new JMenuBar.

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		newMenuItem = new JMenuItem("New Project", KeyEvent.VK_N);
		newMenuItem.setActionCommand("new");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		newMenuItem.getAccessibleContext().setAccessibleDescription(
				"Creates a New set of frames");
		fileMenu.add(newMenuItem);

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

		saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
		saveMenuItem.setActionCommand("save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		saveMenuItem.getAccessibleContext().setAccessibleDescription(
				"Saves current frameset");
		fileMenu.add(saveMenuItem);

		saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.setActionCommand("saveas");
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
						| InputEvent.SHIFT_MASK));
		saveAsMenuItem.getAccessibleContext().setAccessibleDescription(
				"Save project as");
		fileMenu.add(saveAsMenuItem);

		fileMenu.addSeparator();

		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setActionCommand("exit");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		exitMenuItem.getAccessibleContext().setAccessibleDescription(
				"Exit Suite");
		fileMenu.add(exitMenuItem);

		menuBar.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_SIZE)); // Creates
																		// a
																		// small
																		// amount
																		// of
		// space between items on
		// the Menu in order to make
		// it more appealing to a
		// users eye.

		// ---------- EDIT Menu Bar ----------
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);

		clearMenuItem = new JMenuItem("Clear Grid", KeyEvent.VK_C);
		clearMenuItem.setActionCommand("clear");
		clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		clearMenuItem.getAccessibleContext().setAccessibleDescription(
				"Clears current grid");
		editMenu.add(clearMenuItem);

		editMenu.addSeparator();

		fillWithCol1 = new JMenuItem("Fill Grid with Left Color");
		fillWithCol1.setActionCommand("fill1");
		fillWithCol1.getAccessibleContext().setAccessibleDescription(
				"Fills the whole grid with Left click colour");
		editMenu.add(fillWithCol1);

		fillWithCol2 = new JMenuItem("Fill Grid with Right Color");
		fillWithCol2.setActionCommand("fill2");
		fillWithCol2.getAccessibleContext().setAccessibleDescription(
				"Fills the whole grid with Right click colour");
		editMenu.add(fillWithCol2);

		menuBar.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_SIZE));

		// ---------- Project Menu Bar ----------
		projectMenu = new JMenu("Project");
		projectMenu.setMnemonic(KeyEvent.VK_P);
		menuBar.add(projectMenu);

		newFrameMenuItem = new JMenuItem("New Frame", KeyEvent.VK_N);
		newFrameMenuItem.setActionCommand("newframe");
		newFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.SHIFT_MASK));
		newFrameMenuItem.getAccessibleContext().setAccessibleDescription(
				"Add a new Frame after the current selected");
		projectMenu.add(newFrameMenuItem);

		deleteFrameMenuItem = new JMenuItem("Delete Frame", KeyEvent.VK_D);
		deleteFrameMenuItem.setActionCommand("deleteframe");
		deleteFrameMenuItem.getAccessibleContext().setAccessibleDescription(
				"Deletes current Frame");
		projectMenu.add(deleteFrameMenuItem);

		projectMenu.addSeparator();

		nextFrameMenuItem = new JMenuItem("Next Frame");
		nextFrameMenuItem.setActionCommand("nextframe");
		nextFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_RIGHT, Toolkit.getDefaultToolkit()
						.getMenuShortcutKeyMask()));
		nextFrameMenuItem.getAccessibleContext().setAccessibleDescription(
				"Change to next saved Frame");
		projectMenu.add(nextFrameMenuItem);

		previousFrameMenuItem = new JMenuItem("Previous Frame");
		previousFrameMenuItem.setActionCommand("previousframe");
		previousFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_LEFT, Toolkit.getDefaultToolkit()
						.getMenuShortcutKeyMask()));
		previousFrameMenuItem.getAccessibleContext().setAccessibleDescription(
				"Change to previous saved Frame");
		projectMenu.add(previousFrameMenuItem);

		projectMenu.addSeparator();

		preferencesMenuItem = new JMenuItem("Preferences");
		preferencesMenuItem.setActionCommand("preferences");
		preferencesMenuItem.getAccessibleContext().setAccessibleDescription(
				"Set project preferences");
		projectMenu.add(preferencesMenuItem);

		menuBar.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_SIZE));

		// ---------- ABOUT Menu Bar ----------
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);

		helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
		helpMenuItem.setActionCommand("help");
		helpMenuItem.getAccessibleContext().setAccessibleDescription(
				"Help with the Suite");
		helpMenu.add(helpMenuItem);

		aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutMenuItem.setActionCommand("about");
		aboutMenuItem.getAccessibleContext().setAccessibleDescription(
				"About Animation Suite");
		helpMenu.add(aboutMenuItem);

		// ---------- Add Action Listeners for the menu buttons ---------
		// This class is it's own ActionListener.
		// FILE
		newMenuItem.addActionListener(this);
		openMenuItem.addActionListener(this);
		saveMenuItem.addActionListener(this);
		saveAsMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		// EDIT
		clearMenuItem.addActionListener(this);
		fillWithCol1.addActionListener(this);
		fillWithCol2.addActionListener(this);
		// PROJECT
		newFrameMenuItem.addActionListener(this);
		deleteFrameMenuItem.addActionListener(this);
		nextFrameMenuItem.addActionListener(this);
		previousFrameMenuItem.addActionListener(this);
		preferencesMenuItem.addActionListener(this);
		// HELP
		helpMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
		// END OF MENU GUI CREATION ===========
	}

	/**
	 * <p>
	 * Sets up the Color Bar that sits at the bottom of the Side bar panel with
	 * 36 different colours made by <code>MyColor</code>.
	 * </p>
	 * <p>
	 * Each <code>MyColor</code> is made as a <code>JButton</code> and stored in
	 * an <code>ArrayList</code>.<br />
	 * A <code>for</code> loop runs for 36 times, creating a new
	 * <code>JButton</code> based on a colour that is matched to a
	 * <code>char</code>.<br />
	 * By using <code>setContentAreaFilled(boolean)</code>, the buttons appear
	 * as basic squares, without a large button graphic on them (in the Windows
	 * look&feel). Setting to Opaque ensures that the button is visible, even
	 * without the button graphic.
	 * </p>
	 * <p>
	 * Each button has the inner class <code>ColorListener</code> added to it as
	 * a <code>MouseListener</code> in order to detect which button is pressed
	 * when clicking on the colours. When the listener is added, it stores an
	 * 'index' of where that colour will be held in the <code>ArrayLisT</code>
	 * holding the <code>JButtons</code>. This is used to find out which button
	 * was clicked upon the mouse press.
	 * </p>
	 * <p>
	 * Lastly, the method sets the layout, adds the color palette
	 * <code>colourButtonsPanel</code>, aligns it correctly, changes the
	 * background colour to make it visible, restricts the size to fit in with
	 * the rest of the Side bar, sets a border and then defines the layout for
	 * the colour palette.
	 * </p>
	 */
	private void setupColorBar() {
		// ========== Colors GUI ==========

		colorBarPanel = new JPanel(); // Main Container for the Color Bar
		colorButtonsPanel = new JPanel(); // Creates an instance of the panel
											// to hold the colors.
		colorBarPanel.add(new JLabel("Color Palette: ")); // A simple label to
															// display above the
															// colors.

		// ---------- Color Buttons -----------

		arrayOfColors = new ArrayList<JButton>();
		char charScroll = 'Z'; // The first character to be assigned to a Color.
								// The same as that in MyColor custom Color
								// class.
		for (int i = 0; i < TOTAL_AMOUNT_OF_COLORS; i++) {
			// 1. Makes a new button.
			colorButton = new JButton();
			// 2. Sets it's color to that of the actual rgb color and alters
			// some attributes for display.
			colorButton.setBackground(colorList.getColor(charScroll));
			colorButton.setPreferredSize(new Dimension(COLOR_BUTTON_DIMENSIONS,
					COLOR_BUTTON_DIMENSIONS));
			colorButton.setContentAreaFilled(false);
			colorButton.setOpaque(true);
			colorButton.setBorder(BorderFactory
					.createLineBorder(Color.LIGHT_GRAY));
			// 3. Increments the charScroll to get the next char.
			charScroll++;
			// 4. Adds a mouse listener to the button, using my own custom
			// MouseListerner, holding an index to know which button is which.
			colorButton.addMouseListener(new ColorListener(i));
			// 5. Adds the color to the ArrayList.
			arrayOfColors.add(colorButton);
			// 6. Adds the button to the Panel
			colorButtonsPanel.add(arrayOfColors.get(i));
			// 7. repeat until all 36 colors are added.
		}
		/*

		 */
		colorBarPanel.setLayout(new FlowLayout());
		colorBarPanel.add(colorButtonsPanel);
		colorBarPanel.setAlignmentX(LEFT_ALIGNMENT);
		colorBarPanel.setBackground(Color.WHITE);
		colorBarPanel.setPreferredSize(new Dimension(COLOR_BAR_PANEL_WIDTH,
				COLOR_BAR_PANEL_HEIGHT));
		colorBarPanel.setBorder(BorderFactory.createLineBorder(
				Color.LIGHT_GRAY, 1));

		colorButtonsPanel.setLayout(new GridLayout(COLOR_BUTTONS_GRID_VALUE,
				COLOR_BUTTONS_GRID_VALUE));

		// END OF COLOR PANEL GUI CREATION ==========
	}

	/**
	 * <p>
	 * Passes the names of the <code>Image</code> files to be used in the
	 * application as <code>ImageIcons</code> as <code>String</code>s to
	 * <code>addToolIcon(String)</code>.
	 * </p>
	 */
	private void setupToolIcons() {
		String pnI = "pencil.png";
		String pbI = "paintbrush.png";
		String spI = "spill.png";

		pencilIcon = addToolIcon(pnI);
		paintbrushIcon = addToolIcon(pbI);
		spillIcon = addToolIcon(spI);
	}

	/**
	 * <p>
	 * Adds an icon to the <code>JRadioButtons</code> for tools.
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
	 * <code>JRadioButtons</code>.
	 * </p>
	 * <p>
	 * The created ImageIcon is returned and assigned to each correct
	 * JRadioButton.
	 * </p>
	 * 
	 * @param filePath
	 *            A <code>String</code> with the name of the file to be used.
	 * @return the created <code>ImageIcon</code> for use with the
	 *         <code>JRadioButton</code>s.
	 */
	private ImageIcon addToolIcon(final String filePath) {
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
	 * Sets up the sidebar to the right hand side of the application. This class
	 * holds the tools, colours on mouse buttons and colour palette.
	 * </p>
	 * <p>
	 * <p>
	 * The tools buttons are <code>JRadioButton</code>s. This ensures that a
	 * user can only select one tool at a time and gives a good visual display
	 * to the user of what it is they have selected (they are also told with a
	 * <code>JLabel</code>and there are little image icons too).
	 * </p>
	 * <p>
	 * When a tool button is created, it is given a display name, i.e. "Pencil"
	 * and has a Mnemonic key set for quick access to it for a user, i.e. If a
	 * user hits the key 'b' on their keyboard, the paintbrush tool will be
	 * selected. The default selected tool is the Pencil.
	 * </p>
	 * <p>
	 * Tool Buttons:<br />
	 * Each radio button is also assigned an <code>ImageIcon</code> based on the
	 * tool, created by myself. The <code>.createGlue()</code> is used to ensure
	 * that the gray background continues across the whole line of the box the
	 * tool selection area is contained in.<br />
	 * A Preferred size must be set for this to work, as without it the height
	 * of the selections is less than that of the <code>ImageIcons</code> and
	 * cuts off part of the image.
	 * </p>
	 * <p>
	 * There is more explanation of what each tool does in the BoardModel class.
	 * </p>
	 * <p>
	 * A display is set to tell the user what frame number of how many frames
	 * they are on. This text is set to blue to make it visible. There are also
	 * bits of text to display to the user what tool they are currently
	 * 'holding'/using.
	 * </p>
	 * <p>
	 * By using <code>RigidAreas</code>, I can control the amount of whitespace
	 * on the Side Bar. Ensuring a good, clean and organized display for the
	 * user.
	 * </p>
	 * <p>
	 * Color Previews:<br />
	 * To let the user know what colors are bound to each mouse click button, I
	 * have used two small square panels to display them with text above to show
	 * which mouse button they are bound to. To further imply that these are the
	 * colors bound to the mouse buttons, I have told them this is the case with
	 * <code>'mouseColorText</code>.
	 * </p>
	 * <p>
	 * In true keeping with common Image Manipulation programs, the two starting
	 * default colors on each click are Black on left, White on right.
	 * </p>
	 * <p>
	 * The colour buttons are bordered for ease of a users viewing. This is
	 * especially helpful when White or Light Gray might be the chosen color, as
	 * they would otherwise blend into the Panel background and not be as
	 * visible.
	 * </p>
	 */
	private void setupSidebar() {
		// =========== Sidebar (Tools and Color Preview) GUI ===========

		// ---------- Panels & Labels -----------
		sideBar = new JPanel(); // The actual Side Bar Panel.
		frameDisplayText = new JLabel(); // Tells the user Frame n of x.
		toolPanel = new JPanel();

		// ----------- Tool Labels ----------
		selectToolText = new JLabel(); // To tell user to select a tool.
		mouseColorText = new JLabel(); // To tell user "Selected mouse colors".
		heldToolText = new JLabel(); // To tell user what tool they have in
										// their mighty hands of power!

		// ---------- Tool Buttons ----------
		pencilButton = new JRadioButton("Pencil", pencilIcon);
		pencilButton.setBackground(Color.LIGHT_GRAY);
		pencilButton.setMnemonic(KeyEvent.VK_P);
		pencilButton.add(Box.createGlue());
		pencilButton.setPreferredSize(new Dimension(0, TOOL_ICON_HEIGHT));
		pencilButton.setSelected(true); // Sets tool as default selected.
		spillButton = new JRadioButton("Spill Can", spillIcon);
		spillButton.setBackground(Color.white);
		spillButton.setMnemonic(KeyEvent.VK_S);
		spillButton.add(Box.createGlue());
		spillButton.setPreferredSize(new Dimension(0, TOOL_ICON_HEIGHT));
		paintbrushButton = new JRadioButton("Paintbrush", paintbrushIcon);
		paintbrushButton.setBackground(Color.white);
		paintbrushButton.setMnemonic(KeyEvent.VK_B);
		paintbrushButton.add(Box.createGlue());
		paintbrushButton.setPreferredSize(new Dimension(0, TOOL_ICON_HEIGHT));

		// ---------- Group Tool Buttons together ----------
		group = new ButtonGroup();
		group.add(pencilButton);
		group.add(spillButton);
		group.add(paintbrushButton);

		// ---------- Add Buttons for Tool Menu ----------
		frameDisplayText.setText(" Frame: 1 of 1");
		frameDisplayText.setForeground(Color.blue);
		selectToolText.setText(" Select A Tool:");
		heldToolText.setText("  Holding a Pencil!");
		sideBar.add(Box.createRigidArea(new Dimension(0,
				SIDEBAR_RIGID_AREA_DIMENSION)));
		sideBar.add(frameDisplayText);
		sideBar.add(Box.createRigidArea(new Dimension(0,
				SIDEBAR_RIGID_AREA_DIMENSION)));
		sideBar.add(heldToolText);
		sideBar.add(Box.createRigidArea(new Dimension(0,
				SIDEBAR_RIGID_AREA_DIMENSION)));

		toolPanel.add(selectToolText);
		toolPanel.add(Box.createRigidArea(new Dimension(0,
				TOOLBAR_RIGID_AREA_DIMENSION)));
		toolPanel.add(Box.createHorizontalGlue());
		toolPanel.add(pencilButton);
		toolPanel.add(spillButton);
		toolPanel.add(paintbrushButton);
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.PAGE_AXIS));
		toolPanel
				.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		toolPanel.setBackground(Color.white);

		sideBar.add(toolPanel);

		sideBar.add(Box.createRigidArea(new Dimension(0,
				SIDEBAR_RIGID_AREA_DIMENSION)));

		// ---------- Color Previews ----------
		mouseColorText.setText(" Colours on Mouse Buttons: ");
		sideBar.add(mouseColorText);

		colorHolderPanel = new JPanel(); // Holds the Left/Right click
		// colors/text.
		foregroundColor = new JPanel(); // Displays color on left-click.
		foregroundText = new JLabel(); // To tell user "Left" for click/color.
		foreContainer = new JPanel();
		backgroundColor = new JPanel(); // Displays color on right-click.
		backgroundText = new JLabel(); // To tell user "Right" for click/color.
		backContainer = new JPanel();

		foreContainer.add(foregroundText);
		foreContainer.add(backgroundText);
		colorHolderPanel.add(foreContainer);

		backContainer.add(foregroundColor);
		backContainer.add(backgroundColor);
		colorHolderPanel.add(backContainer);

		colorHolderPanel.setLayout(new BoxLayout(colorHolderPanel,
				BoxLayout.PAGE_AXIS));

		colorHolderPanel.setAlignmentX(LEFT_ALIGNMENT);

		sideBar.add(colorHolderPanel);
		sideBar.add(Box.createRigidArea(new Dimension(0,
				SIDEBAR_RIGID_AREA_DIMENSION)));
		/*
		 * The ColorBar is added to the SideBar.
		 */
		sideBar.add(colorBarPanel);

		// Left Click preview
		foregroundText.setText(" Left   ");
		foregroundColor.setPreferredSize(new Dimension(HELD_COLOURS_SIZE,
				HELD_COLOURS_SIZE));
		foregroundColor.setBackground(Color.black); // The default color for
													// left click is Black.
		foregroundColor
				.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		// Right Click preview
		backgroundText.setText(" Right");
		backgroundColor.setPreferredSize(new Dimension(HELD_COLOURS_SIZE,
				HELD_COLOURS_SIZE));
		backgroundColor.setBackground(Color.white); // The default color for
													// right click is White.
		backgroundColor
				.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

		// ---------- Add Action Listeners ----------
		pencilButton.addActionListener(this);
		spillButton.addActionListener(this);
		paintbrushButton.addActionListener(this);

		sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));

		// END OF SIDEBAR/TOOLS GUI CREATION ===========

	}

	/**
	 * <p>
	 * Creates a new panel to show small previews of each Frame.
	 * </p>
	 * <p>
	 * The method that creates these is elsewhere, this just creates the
	 * components for the panel they are added to.
	 * </p>
	 * <p>
	 * These mini previews only update once a user has created a new frame or
	 * moved to a different frame. The panel they are on gains a scroll bar
	 * should the amount of them expand outside of the panel bounds.
	 * </p>
	 * <p>
	 * <code>openFramePreview()</code> sets up the first preview for a new
	 * project or all the previews for an opened file.
	 * </p>
	 */
	private void setupFramePreviewPanel() {
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout());
		framePreviewPanel = new JPanel();
		framePreviewPanel.setLayout(new FlowLayout());
		framePreviewButtonHolder = new JPanel();
		framePreviewButtonHolder.setLayout(new FlowLayout());
		framePreviewPanelForButtons = new JPanel();
		framePreviewPanelForButtons.setLayout(new GridLayout(2, 1));

		framePreviewScroll = new JScrollPane(framePreviewPanel);
		framePreviewPanel.setAutoscrolls(true);
		framePreviewScroll.setPreferredSize(new Dimension(0,
				PREVIEW_SCROLLBAR_SIZE));
		framePreviewScroll.setViewportView(framePreviewPanel);

		JLabel previewLabel = new JLabel(" Frames in Animation: "); // Just to
																	// display
																	// what this
																	// section
																	// is for.
		previewNextButton = new JButton("Next");
		previewNextButton.addActionListener(this);
		previewNextButton.setActionCommand("nextframe"); // Shared with
															// <code>Project -
															// Next Frame</code>
															// menu item.
		previewPreviousButton = new JButton("Previous");
		previewPreviousButton.addActionListener(this);
		previewPreviousButton.setActionCommand("previousframe"); // Shared with
																	// <code>Project
																	// -
																	// Previous
																	// Frame</code>
																	// menu
																	// item.
		
		framePreviewButtonHolder.add(previewPreviousButton);
		framePreviewButtonHolder.add(previewNextButton);
		framePreviewPanelForButtons.add(previewLabel);
		framePreviewPanelForButtons.add(framePreviewButtonHolder);
		previewPanel.add(framePreviewScroll, BorderLayout.CENTER);
		previewPanel.add(framePreviewPanelForButtons, BorderLayout.WEST);

		openFramePreview(); // Creates the first preview frame for the first
							// grid. This applies to the very first white grid
							// and also loads in all of the existing grids
							// whenever an existing file is opened.

	}

	/**
	 * <p>
	 * Creates a new instance of the <code>NewProjectMenu</code> panel to be
	 * used later.
	 * </p>
	 */
	private void setupNewProject() {

		newProjectPanel = new NewProjectMenu();
	}

	/**
	 * <p>
	 * Creates a new instance of the <code>PreferencesMenu</code> panel to be
	 * used later.
	 * </p>
	 */
	private void setupPreferences() {
		preferences = new PreferencesMenu();
	}

	/**
	 * <p>
	 * Sets up the <code>JPanel</code> for the About menu.
	 * </p>
	 * <p>
	 * After some online research, I found the best, most common and recommended
	 * way to make a <code>JLabel</code> have wrapped text on a
	 * <code>JPanel</code> was to use HTML tags as you would do in web mark-up.
	 * </p>
	 * <p>
	 * My About section is a very brief description of why this program exists.
	 * </p>
	 */
	private void setupAbout() {
		aboutText = new JLabel(
				"<html><p>This Animation Suite was for a University assignment at Aberystwyth University, "
						+ "module CS124 \"Software Development\" in the first year of an undergraduate course,"
						+ " degree scheme in Computer Science.</p><br /><p>The creator is jee22@aber.ac.uk</p></html>");
		aboutText.setPreferredSize(new Dimension(ABOUT_TEXT_WIDTH,
				ABOUT_TEXT_HEIGHT));
	}

	// ========== ACTIONS AND RESPONSES ==========
	@Override
	public final void actionPerformed(final ActionEvent e) {
		String command = e.getActionCommand();
		// ========== MENU BUTTONS ==========
		// ---------- File Menu ---------
		if (command.equals("new")) {
			newProject();
		}
		if (command.equals("open")) {
			openFile();
		}
		// For the 'Open Recent' menu. This gets whatever file is linked in the
		// Recent Items Menu that was clicked and uses it as a parameter in the
		// openFile method.
		if (command.equals(RecentItemsMenu.RECENT_FILE_COMMAND)) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			File file = (File) menuItem.getClientProperty("filename");
			openFile(file);
		}
		if (command.equals("save")) {
			// "If there is no known file held, use save as, otherwise save on the current known file".
			if (currentFile == null) {
				saveAs();
			} else {
				save(currentFile);
			}
		}
		if (command.equals("saveas")) {
			saveAs();
		}
		if (command.equals("exit")) {
			exit();
		}

		// ---------- Edit Menu ---------
		// The three methods here each use 'clear'. Clear doesn't really clear
		// anything, it just fills the 2D char array with whatever character it
		// is passed.
		if (command.equals("clear")) {
			canvas.clear('Z');
		}
		if (command.equals("fill1")) {
			canvas.clear(canvas.getLeftColor());
		}
		if (command.equals("fill2")) {
			canvas.clear(canvas.getRightColor());
		}

		// --------- Project Menu ---------
		if (command.equals("newframe")) {
			canvas.newFrame();
			updateFrameDisplay();
			updateFramePreview();
		}
		if (command.equals("deleteframe")) {
			canvas.deleteFrame();
			updateFrameDisplay();
			updateFramePreview();
		}
		if (command.equals("nextframe")) {
			canvas.nextFrame();
			updateFrameDisplay();
			updateFramePreview();
		}
		if (command.equals("previousframe")) {
			canvas.previousFrame();
			updateFrameDisplay();
			updateFramePreview();
		}
		if (command.equals("preferences")) {
			openPreferences();
			updateFrameDisplay();
		}
		if (command.equals("about")) {
			openAbout();
		}

		// ---------- Tool Buttons ----------
		/*
		 * Pressing a tool button will both set the text label to tell the user
		 * what tool they are using and sends an integer to the canvas object,
		 * so that it can know which tool the user is holding and what action to
		 * take when the board is clicked using this. A border is also set
		 * around each one to further display the current tool in use.
		 */
		if (e.getSource() == pencilButton) {
			canvas.setToolInUse(1);
			heldToolText.setText("  Holding a Pencil!");
			pencilButton.setBackground(Color.LIGHT_GRAY);
			paintbrushButton.setBackground(Color.WHITE);
			spillButton.setBackground(Color.WHITE);
		}
		if (e.getSource() == spillButton) {
			canvas.setToolInUse(2);
			heldToolText.setText("  Holding a Spill Can!");
			pencilButton.setBackground(Color.WHITE);
			paintbrushButton.setBackground(Color.WHITE);
			spillButton.setBackground(Color.LIGHT_GRAY);
		}
		if (e.getSource() == paintbrushButton) {
			canvas.setToolInUse(3);
			heldToolText.setText("  Holding a Paintbrush!");
			pencilButton.setBackground(Color.WHITE);
			paintbrushButton.setBackground(Color.LIGHT_GRAY);
			spillButton.setBackground(Color.WHITE);
		}

		// END OF ACTIONS AND RESPONSES ==========

	}

	// ========== COMMAND METHODS & STATUS UPDATES ==========
	/**
	 * <p>
	 * Creates a new project
	 * </p>
	 * <p>
	 * When a user clicks to create a New Project from the File Menu, it will
	 * open a <code>JOptionPane</code> to ask the user what size they would like
	 * to create their new project, restricting them to sizes within reason and
	 * that are multiples of 5 (not all multiples available) (5 - 100). This is
	 * to avoid letting the user choose a ridiculous size for the grid and/or
	 * put in invalid numbers right from the get go.
	 * </p>
	 * <p>
	 * This menu will also let the user choose a new background, displayed to
	 * them as colours in a drop down box, using the custom 36 colour palette.
	 * </p>
	 * <p>
	 * Once the user has chosen, a method is called in canvas that will create a
	 * whole new grid for the user, while still remembering their current
	 * left/right button colours and tool in use. It just requires the new size
	 * and background colour of the project to make.
	 * </p>
	 * <p>
	 * After this, the <code>FrameDisplay</code> in the Side Bar and the bottom
	 * Frame Previews are update to reflect the new project.
	 * </p>
	 * <p>
	 * If the users chooses 'Cancel' on making a new Project, nothing happens.
	 * </p>
	 */
	private void newProject() {
		openNewProject();

		if (selectedSize != null) {
			// Converts the Object 'selection' by first casting it to
			// a type of String and then parsing into an integer.
			int newSize = Integer.parseInt((String) selectedSize);

			canvas.newProject(newSize, selectedColor);
		}
		updateFrameDisplay();
		updateFramePreview();
		// Reset the selected back to null. If these are still selected,
		// there is a chance that they will still change the current project
		// into a new one even if the close window button is chosen.
		selectedSize = null;
		selectedColor = null;
	}

	/**
	 * <p>
	 * Opens a file on the users system to edit
	 * </p>
	 * <p>
	 * Uses <code>JFileChooser</code> to allow the user to select any file in
	 * their system.
	 * </p>
	 * <p>
	 * In order to filter out invalid files, I have created a customer
	 * <code>FileFilter</code> that only displays <code>.txt</code> files.
	 * </p>
	 * <p>
	 * Once a users has selected a file, it passes it through onto another
	 * method by the same name, just using the File selected as a parameter.
	 * </p>
	 */
	private void openFile() {
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int chosen = fileChooser.showOpenDialog(this);
		if (chosen == JFileChooser.APPROVE_OPTION) {
			openFile(fileChooser.getSelectedFile());
		}
	}

	/**
	 * <p>
	 * Continues the open file operation, using either the file from
	 * <code>RecentItemsMenu</code> or chosen in the no parameter version of
	 * <code>openFile()</code>.
	 * </p>
	 * <p>
	 * Sets the file passed in the parameters as the current file for use
	 * later/elsewhere.
	 * </p>
	 * <p>
	 * Passes the File onto canvas (which passes it onto board) to load in the
	 * characters that will build the 2D char arrays/Frames.
	 * </p>
	 * <p>
	 * Updates the side bar by getting information from canvas/board once the
	 * File is loaded, updates the Frame Display panel.
	 * </p>
	 * <p>
	 * Uses the same method that is used on the first creation of the Frame
	 * Display panel in order to load in the new opened up file's Frames into
	 * preview boxes.
	 * </p>
	 * 
	 * @param file
	 *            the file to be opened.
	 */
	private void openFile(final File file) {

		setCurrentFile(file);

		canvas.openFile(file);

		getForUpdateSidebar();
		setForUpdateSidebar();
		updateFrameDisplay();
		openFramePreview();
	}

	/**
	 * <p>
	 * By returning the file, the <code>currentFile</code> can either be set to
	 * the file that was originally passed or the new saved file which includes
	 * the correct <code>.txt</code> extension (based on the results from the
	 * <code>JFileChooser</code> and <code>TxtFileFilter</code>.
	 * </p>
	 * 
	 * @param file
	 *            the file to be saved.
	 */
	private void save(final File file) {
		File returnedFile = canvas.saveFile(file);
		setCurrentFile(returnedFile);
	}

	/**
	 * <p>
	 * Using <code>JFileChooser</code> and TxtFileFilter again, as with
	 * openFile.
	 * </p>
	 */
	private void saveAs() {
		//
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int chosen = fileChooser.showSaveDialog(this);
		if (chosen == JFileChooser.APPROVE_OPTION) {
			save(fileChooser.getSelectedFile());
		}
	}

	/**
	 * <p>
	 * Exits the program.
	 * </p>
	 * <p>
	 * Before exiting, confirms the user wants to save before exiting the
	 * project they are working on.
	 * </p>
	 */
	private void exit() {
		int result = JOptionPane.showConfirmDialog(null,
				"Would you like to save before exiting?", "Exit with save",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {

		} else {
			if (result == JOptionPane.YES_OPTION) {
				if (currentFile == null) {
					saveAs();
				} else {
					save(currentFile);
				}
			} else if (result == JOptionPane.NO_OPTION) {

			}
			System.exit(0);
		}
	}

	/**
	 * <p>
	 * Sets the <code>currentFile</code? in use and adds it to the
	 * <code>RecentItemsMenu</code> for use in OpenRecent.
	 * </p>
	 * 
	 * @param file
	 *            the <code>File</code> to be set as the
	 *            <code>currentFile</code>.
	 */
	private void setCurrentFile(final File file) {

		currentFile = file;
		recentItems.addItem(file);
	}

	/**
	 * <p>
	 * Gets updates required for correct display and use of the Side Bar
	 * </p>
	 */
	private void getForUpdateSidebar() {
		tempLeft = canvas.getLeftColor();
		tempRight = canvas.getRightColor();
		tempTool = canvas.getToolInUse();
	}

	/**
	 * <p>
	 * Sets the correct values for the Side Bar.
	 * </p>
	 */
	private void setForUpdateSidebar() {
		canvas.setToolInUse(tempTool);
		canvas.setLeftColor(tempLeft);
		canvas.setRightColor(tempRight);
	}

	/**
	 * <p>
	 * Updates the display of "Frame of Frames"
	 * </p>
	 * <p>
	 * Updates the display in the Side Bar to tell the user what Frame they are
	 * on out of however many Frames there are in the current Project.
	 * </p>
	 */
	private void updateFrameDisplay() {
		frameDisplayText.setText(" Frame: " + canvas.getFrameNo() + " of "
				+ canvas.getTotalFrames());
	}

	/**
	 * <p>
	 * Sets up the preview images for a new project or loaded in file.
	 * </p>
	 * <p>
	 * <code>PreviewMaker</code> is a custom class that creates the small images
	 * for use in the frame previews.
	 * </p>
	 * <p>
	 * This method is for when the application is opened or when a new File is
	 * opened.
	 * </p>
	 * <p>
	 * It removes any pre-existing previews from the panel and updates it (this
	 * stops shadows being left from any previous frames displayed). Next it
	 * goes through every frame in the current project, and for each calls the
	 * method <code>generateImage()</code> in the <code>PreviewMaker</code>
	 * class, passing it the location of the frame in the <code>ArrayList</code>
	 * of <code>char[][]</code>.<br />
	 * This is because <code>PreviewMaker</code> is passed a copy of the current
	 * <code>board</code> when it is first created, and so therefore knows the
	 * most up to date state of the current project.
	 * </p>
	 * <p>
	 * A border is added to makes frames easier to see before they are added to
	 * the panel. The method also finds the current frame the user is looking at
	 * and sets a Black border to it in order to make it clearly visible.
	 * </p>
	 */
	private void openFramePreview() {
		previewMaker = new PreviewMaker(canvas.getBoard());
		framePreviewPanel.removeAll();
		framePreviewPanel.updateUI();
		for (int i = 0; i < canvas.getTotalFrames(); i++) {
			previewImage = previewMaker.generateImage(i);
			previewImage.setBorder(BorderFactory
					.createLineBorder(Color.GRAY, 2));
			framePreviewPanel.add(previewImage);
		}
		JLabel tempPreview = (JLabel) framePreviewPanel.getComponent(canvas
				.getFrameNo() - 1);
		tempPreview.setBorder(BorderFactory.createLineBorder(Color.black, 2));
	}

	/**
	 * <p>
	 * Setups up and updates frame previews of any project after opening.
	 * </p>
	 * <p>
	 * Similar to <code>openFramePreview()</code> but for projects currently in
	 * use.<br />
	 * The difference is the slight way in which it handles searching through
	 * the frames, as the <code>ArrayLists</code> change distinctly (e.g. the
	 * buffers) when a new project is made or a <code>File</code> opened
	 * compared to a project in use.
	 * </p>
	 */
	private void updateFramePreview() {
		previewMaker = new PreviewMaker(canvas.getBoard());
		framePreviewPanel.removeAll();
		framePreviewPanel.updateUI();
		for (int i = 0; i < canvas.getTotalFrames(); i++) {
			previewImage = previewMaker.generateImage(i);
			previewImage.setBorder(BorderFactory
					.createLineBorder(Color.GRAY, 2));
			framePreviewPanel.add(previewImage);
		}
		JLabel tempPreview = (JLabel) framePreviewPanel.getComponent(canvas
				.getFrameNo() - 1);
		tempPreview.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		if (canvas.getTotalFrames() == 0) {
			// The very final frame in the ArrayList is a 'buffer' on the
			// creation of the ArrayList. This needs to be removed on the
			// display of the first frame. It is not an issue for later frames
			// as the buffer has become an actual frame in use.
			framePreviewPanel.remove(canvas.getTotalFrames() - 1);
		}
		if (canvas.getTotalFrames() == canvas.getFrameNo()) {
			// Just as in the canvas class, the final frame is not saved into
			// the application unless the user does something with it, such as
			// flicking back to a previous frame. In order to keep the final
			// frame in the preview panel updated to the correct frame, it was
			// required to force the board class (via canvas) to move to the
			// previous frame and then back again for a 'force save'. Although a
			// little bit strange, this code does exactly what is needed, and so
			// I feel is acceptable to be placed here.
			canvas.previousFrame();
			canvas.nextFrame();
		}

	}

	/**
	 * <p>
	 * Creates a new <code>JOptionPane</code> for use with the
	 * <code>NewProjectMenu</code> object to make a new project.
	 * </p>
	 */
	private void openNewProject() {
		int value = JOptionPane.showConfirmDialog(null, newProjectPanel,
				"Create New Project", JOptionPane.OK_CANCEL_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			selectedSize = newProjectPanel.getSelectedSize();
			selectedColor = newProjectPanel.getSelectedColor();
		}
	}

	/**
	 * <p>
	 * Creates a new <code>JOptionPane</code> for use with the
	 * <code>PreferencesMenu</code> object.
	 * </p>
	 */
	private void openPreferences() {
		int value = JOptionPane.showConfirmDialog(null, preferences,
				"Preferences", JOptionPane.OK_CANCEL_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			// If the user pressed OK on their choies:
			// Determine whether each new Frame should start blank or with the
			// last Frame copied over.
			if (!preferences.getFrameCopyOption()) {
				canvas.setBlankStart(true);
			} else {
				canvas.setBlankStart(false);
			}
			// Determine whether to have Grid Lines on/off.
			if (preferences.getGridOption()) {
				canvas.showGrid(true);
			} else {
				canvas.showGrid(false);
			}
		}
		canvas.updateUI(); // Stops the Grid from leaving shadows of the
							// previous choice.
	}

	/**
	 * <p>
	 * Opens the About panel
	 * </p>
	 */
	private void openAbout() {
		JOptionPane.showMessageDialog(new JFrame(), aboutText, "About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// ========== INNER CLASS ColoListener - Inherits from MouseAdapter.
	/**
	 * <p>
	 * A MouseListener to keep track of which button is clicked for the 36
	 * colour palette.
	 * </p>
	 * 
	 * @author James Euesden - jee22@aber.ac.uk
	 * @version 1.0
	 * 
	 */
	private class ColorListener extends MouseAdapter {
		private final int index; // Used for the class to keep track of which
									// colour is which (determined by the order
									// they were added). When created and used,
									// it is passed the index of the colour
									// clicked by the user.

		public ColorListener(final int index) {
			this.index = index;
		}

		@Override
		public void mouseClicked(final MouseEvent e) {
			// Using the index of the colour, get the colour from the
			// ColorHolder.
			MyColor tempColor = colorList.getColorFromPosition(index);
			// Depending which mouse button was clicked, set the colour
			// accordingly in both the canvas and this class for use and
			// display.
			if (e.getButton() == MouseEvent.BUTTON1) {
				foregroundColor.setBackground(tempColor);
				canvas.setLeftColor(tempColor.getChar());
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				backgroundColor.setBackground(tempColor);
				canvas.setRightColor(tempColor.getChar());
			}

		}
	}

}
