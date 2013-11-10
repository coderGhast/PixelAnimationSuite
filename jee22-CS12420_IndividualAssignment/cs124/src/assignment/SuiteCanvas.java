package assignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.JPanel;

/**
 * <p>
 * The Swing for the Director side of the application, handling how the user
 * draws on the <code>grid</code> in <code>BoardModel</code>.
 * </p>
 * <p>
 * This class handles much of the Swing with the application. It is the class
 * that allows the user to Draw upon the 'canvas' and is the class 'in the
 * middle' between the <code>SuiteDirector</code> and the
 * <code>BoardModel</code>.<br />
 * This means that it passes parameters between the two and reacts to how the
 * user clicks buttons and panels and also how to handle <code>MouseEvent</code>
 * s on the <code>canvas</code> that the user is allowed to draw on.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.1
 */
@SuppressWarnings("serial")
public class SuiteCanvas extends JPanel implements MouseListener,
		MouseMotionListener {
	private BoardModel board;
	private int frameNo = 1;
	private int totalFrames = 1;
	private char currentLeftColor = '}';
	private char currentRightColor = 'Z';
	private int toolInUse = 1;
	private int rows = 30; // Default is 30, although these may be changed
	private int columns = 30; // by user choice in the constructor when using
								// 'New Project'.
	private int currentButton = 1; // For MouseMotionListener to know which
									// button is pressed.
	private static final double CANVAS_SIZE = 500.0; // Physical screen space.
	private boolean blankStart = false; // For use with Preferences.
	private boolean displayGridLines = true; // For use with Preferences.
	private ColorHolder colorPalette = new ColorHolder();
	private static final int ERASER_NUMBER = 3;

	/**
	 * <p>
	 * Creates a new <code>canvas</code> for a user to draw upon</code>
	 * </p>
	 * <p>
	 * The constructor takes the requested width/height of the current project
	 * (as <code>rows</code> and <code>columns</code>), sets the background of
	 * the full <code>canvas</code> to white and creates a new
	 * <code>BoardModel</code> to match the height/width.<br />
	 * <code>MouseMotionListener</code> and <code>MouseListener</code> are added
	 * for the 'drawing', due to the many colours and left/right click
	 * functionality.
	 * </p>
	 * 
	 * @param rows
	 *            the amount of rows in the project.
	 * @param columns
	 *            the amount of columns in the project.
	 */
	public SuiteCanvas(final int rows, final int columns) {
		this.rows = rows;
		this.columns = columns;
		setBackground(Color.white);
		board = new BoardModel(rows, columns); // Create ROWS x COLUMNS board
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}

	/**
	 * <p>
	 * Paints the <code>Component</code> (the <code>canvas</code> itself)
	 * </p>
	 * <p>
	 * <code>drawGrid()</code> literally draws a grid on the canvas, based on
	 * the characters in the <code>BoardModel</code> array and using
	 * <code>fillRect/drawRect</code>.
	 * </p>
	 */
	@Override
	public final void paintComponent(final Graphics g) {
		super.paintComponent(g);
		drawgrid(g);
	}

	/**
	 * <p>
	 * For each block space, the method gets the <code>char</code> from each
	 * element in the <code>board</code>s current active <code>grid</code> and
	 * uses it to determine how to paint each block.
	 * </p>
	 * <p>
	 * To determine the size of the squares to be painted and try and fit them
	 * within the application bounds, the method rounds the sum of the full
	 * <code>canvas</code> size divided by the rows (width dimension).
	 * </p>
	 * <p>
	 * There is an <code>if</code> statement that changes <code>gaps</code> to
	 * either the same side as the squares or makes it one less than the square,
	 * in order to leave some space around the blocks for the grid lines to be
	 * seen.
	 * </p>
	 * <p>
	 * The method searches through each element of the grid array and checks
	 * what character is there. Depending which character is, it either fills it
	 * with white (no grid lines), paints a light gray rectangle border (grid
	 * lines) or fills it with the colour that the character represents in
	 * <code>ColorHolder</code>'s <code>MyColor</code>.
	 * </p>
	 * <p>
	 * 
	 * @param g
	 *            the graphics from paintComponent super method.
	 */
	private void drawgrid(final Graphics g) {
		int whiteSpace = 1; // Space around the grid.

		double square = CANVAS_SIZE / rows;
		int squareSize = (int) Math.round(square);
		// Gaps literally refers to the gaps between squares. These help the
		// user see where each square boundary is but can be turned off.
		int gaps = squareSize - 1;
		if (!displayGridLines) {
			gaps = squareSize; // To Turn the grid lines off by removing gaps.
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (board.getColor(i + 1, j + 1) == 'Z') {
					if (displayGridLines) {
						g.setColor(Color.LIGHT_GRAY); // As what we consider
														// 'White'
														// is
														// actually a 'Light
														// gray'
														// rectangle in order to
														// show the grid lines.
						g.draw3DRect(whiteSpace + i * squareSize, whiteSpace
								+ j * squareSize, gaps, gaps, true);
					} else {
						// If the users wishes to not have Grid lines, the
						// 'blank' squares are filled with white rather than
						// drawing a hollow black rectangle.
						g.setColor(Color.white);
						g.fillRect(whiteSpace + i * squareSize, whiteSpace + j
								* squareSize, gaps, gaps);
					}
				} else {
					g.setColor(colorPalette.getColor(board.getColor(i + 1,
							j + 1)));
					g.fill3DRect(whiteSpace + i * squareSize, whiteSpace + j
							* squareSize, gaps, gaps, true);
				}
			}
		}
	}

	/**
	 * <p>
	 * Calls to set the <code>char</code> into the correct element in the
	 * <code>board.grid</code> array, based on the position of the users mouse
	 * over the corresponding blocks on the array, the colour and the tool.
	 * </p>
	 * <p>
	 * After setting up some preliminary parameters, a check is made to ensure
	 * the painting is within the boundaries of the array.<br />
	 * The next step is calling to the <code>board</code> with parameters for
	 * the location in the array, what colour is to be used (in
	 * <code>char</code> form, based on if the <code>MouseEvent</code> was
	 * <code>BUTTON1</code> - Left Click, or <code>BUTTON3</code> - Right Click)
	 * and the tool used (as an <code>int</code>).<br />
	 * If any mouse button except left/right are used, the paint action acts
	 * like an Eraser, painting 'white', the size of the paint brush tool.
	 * </p>
	 * 
	 * @param e
	 *            The <code>MouseEvent</code> of the click.
	 */
	private void painting(final MouseEvent e) {
		// This method could probably be improved as there are some notable
		// breaks in the lines of colour if a user tries to drag the mouse
		// across the canvas too quickly.
		int whiteSpace = 1;
		double square = CANVAS_SIZE / rows;
		int squareSize = (int) Math.round(square);
		int x = e.getX();
		int y = e.getY();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				// Checks the user is within the correct boundaries of painting.
				if (x > whiteSpace + i * squareSize
						&& x < whiteSpace + i * squareSize + squareSize
						&& y > whiteSpace + j * squareSize
						&& y < whiteSpace + j * squareSize + squareSize) {

					if (currentButton == MouseEvent.BUTTON1) {
						board.setValue(i + 1, j + 1, currentLeftColor,
								toolInUse);
					} else if (currentButton == MouseEvent.BUTTON3) {
						board.setValue(i + 1, j + 1, currentRightColor,
								toolInUse);
					} else {
						// If any button but Left or Right click are used, the
						// mouse will act like an 'Eraser' tool, using the
						// paintbrush tool and clearing colours off the grid.
						board.setValue(i + 1, j + 1, 'Z', ERASER_NUMBER);
					}

				}
			}
		}
		repaint();
	}

	/**
	 * <p>
	 * Creates a full new blank project. Middle step between Director and Model.
	 * </p>
	 * <p>
	 * Makes a call to <code>board.newProject(int, MyColor)</code> to create
	 * remove the old project and create a new one.<br />
	 * A call to update the <code>rows</code> and <code>columns</code> is done
	 * using <code>board.getGridDimensions()</code> and then attributes are
	 * updated to reflect this new project with <code>updateValues()</code> and
	 * <code>repaint()</code> for the application's display to the user.
	 * </p>
	 * 
	 * @param newSize
	 *            The requested size of the new project. Default or set by user.
	 * @param selectedColor
	 *            The requested new background colour of the project. Default or
	 *            set by user.
	 */
	protected final void newProject(final int newSize,
			final MyColor selectedColor) {
		board.newProject(newSize, selectedColor);
		rows = board.getGridDimensions();
		columns = board.getGridDimensions();
		updateValues();
		repaint(); // Calls to repaint to reflect the new changes.
	}

	/**
	 * <p>
	 * Passes on the call to open a new <code>File</code> and updates values to
	 * reflect this.
	 * </p>
	 * <p>
	 * Sends the <code>File</code> as a parameter to the <code>board</code>,
	 * then sets the correct dimensions in this class based on the loaded in
	 * project.<br />
	 * Updates the values (to update the amount of Frames/current Frame) on
	 * display.<br />
	 * Finished by calling <code>repaint()</code> to update the display.
	 * </p>
	 * 
	 * @param file
	 *            The chosen <code>File</code> from <code>JFileChooser</code>
	 */
	protected final void openFile(final File file) {
		board.openFile(file);
		rows = board.getGridDimensions();
		columns = board.getGridDimensions();
		updateValues();
		repaint();
	}

	/**
	 * <p>
	 * Saves the current project to <code>File</code>
	 * </p>
	 * <p>
	 * Passes the chosen <code>File</code> to the <code>board</code> and saves
	 * the returned <code>File</code> to a variable in order to be passed back
	 * up to <code>SuiteDirector</code>.<br />
	 * This is in case the file name was changed in <code>board</code> based on
	 * the name chosen by the user in <code>JFileChooser</code>.
	 * </p>
	 * 
	 * @param file
	 *            the <code>File</code> chosen by the user to save out to.
	 * @return the same <code>File</code> the user chose to save out to,
	 *         potentially updated with <code>.txt</code> on the end, depending
	 *         how they named the saved file.
	 */
	protected final File saveFile(final File file) {
		File savingFile = board.saveFile(file);
		return savingFile;
	}

	/**
	 * <p>
	 * Passes the call to <code>clear(char)</code> from
	 * <code>SuiteDirector</code> to <code>board</code> and repaints to reflect
	 * changes.
	 * </p>
	 * 
	 * @param c
	 *            The character to fill the active <code>grid</code> in
	 *            <code>board</code> with.
	 */
	protected final void clear(final char c) {
		board.clear(c);
		repaint();
	}

	/**
	 * <p>
	 * Passes the call to create a new frame.
	 * </p>
	 * <p>
	 * Determines in this class whether to get a new frame that copies the old
	 * <code>grid</code> or a blank <code>grid</code> based on the Preferences
	 * menu choices.
	 * </p>
	 */
	protected final void newFrame() {
		if (!blankStart) {
			board.copy();
		} else if (blankStart) {
			board.newFrame();
		}
		updateValues();
		repaint();

	}

	/**
	 * <p>
	 * Passes on the call to delete a frame onwards to <code>board</code> and
	 * updates the values/display.
	 * </p>
	 */
	protected final void deleteFrame() {
		board.deleteFrame();
		updateValues();
		repaint();
	}

	/**
	 * <p>
	 * Passes on the call to jump to the next frame to <code>board</code> and
	 * updates the values/display.
	 * </p>
	 */
	protected final void nextFrame() {
		board.nextFrame();
		updateValues();
		repaint();
	}

	/**
	 * <p>
	 * Passes on the call to jump to the previous frame to <code>board</code>
	 * and updates the values/display.
	 * </p>
	 */
	protected final void previousFrame() {
		board.previousFrame();
		updateValues();
		repaint();
	}

	// ========== GETTERS & SETTERS ==========
	/**
	 * <p>
	 * Returns the full <code>BoardModel</code>.
	 * </p>
	 * 
	 * @return the currently held <code>BoardModel</code>.
	 */
	public final BoardModel getBoard() {
		return board;
	}

	// ---------- Dimensions ----------
	/**
	 * <p>
	 * Returns the amount of <code>rows</code>.
	 * </p>
	 * 
	 * @return the amount of rows.
	 */
	public final int getRows() {
		return rows;
	}

	/**
	 * <p>
	 * Returns the amount of <code>columns</code>.
	 * </p>
	 * 
	 * @return the amount of columns.
	 */
	public final int getColumns() {
		return columns;
	}

	// ---------- Frame Numbers ----------
	/**
	 * <p>
	 * Returns the current active frame number in the sequence of the animation.
	 * </p>
	 * 
	 * @return the current active frame number.
	 */
	protected final int getFrameNo() {
		return frameNo;
	}

	/**
	 * <p>
	 * Returns the total amount of frames in the current project.
	 * </p>
	 * 
	 * @return the total amount of frames.
	 */
	protected final int getTotalFrames() {
		return totalFrames;
	}

	// ---------- Tool in use ----------
	/**
	 * <p>
	 * Sets the current tool in use by the user once they click on a tool button
	 * in <code>SuiteDirector</code>.
	 * </p>
	 * 
	 * @param toolInUse
	 *            an <code>int</code> representing the tool the user is holding.
	 */
	public final void setToolInUse(final int toolInUse) {
		this.toolInUse = toolInUse;
	}

	/**
	 * <p>
	 * Returns the <code>int</code> representing the tool in use.
	 * </p>
	 * 
	 * @return the <code>int</code> representing the tool in use.
	 */
	public final int getToolInUse() {
		return toolInUse;
	}

	// ---------- Mouse Button Colors ----------
	/**
	 * <p>
	 * Returns the colour 'bound' to the left mouse button.
	 * </p>
	 * 
	 * @return the <code>char</code> representing the colour on the left mouse
	 *         button.
	 */
	public final char getLeftColor() {
		return currentLeftColor;
	}

	/**
	 * <p>
	 * Sets the colour to be 'bound' to the left mouse button, based on a
	 * <code>char</code> parameter representing it.
	 * </p>
	 * 
	 * @param c
	 *            <code>char</code> representing the colour to be held for the
	 *            left mouse button.
	 */
	public final void setLeftColor(final char c) {
		currentLeftColor = c;
	}

	/**
	 * <p>
	 * Returns the colour 'bound' to the right mouse button.
	 * </p>
	 * 
	 * @return the <code>char</code> representing the colour on the right mouse
	 *         button.
	 */
	public final char getRightColor() {
		return currentRightColor;
	}

	/**
	 * <p>
	 * Sets the colour to be 'bound' to the right mouse button, based on a
	 * <code>char</code> parameter representing it.
	 * </p>
	 * 
	 * @param c
	 *            <code>char</code> representing the colour to be held for the
	 *            right mouse button.
	 */
	public final void setRightColor(final char c) {
		currentRightColor = c;
	}

	// ---------- Preferences ----------
	/**
	 * <p>
	 * Sets the <code>boolean</code> to determine if a new <code>grid</code>
	 * should be blank or a copy of the previous <code>grid</code>.
	 * </p>
	 * 
	 * @param b
	 *            boolean to represent if a new <code>grid</code> should be
	 *            copied (<code>false</code>) or blank (<code>true</code>).
	 */
	protected final void setBlankStart(final boolean b) {
		blankStart = b;
	}

	protected final void showGrid(final boolean displayGridLines) {
		this.displayGridLines = displayGridLines;
	}

	protected final void updateValues() {
		// This method is used for updating the variables that
		// are used to tell the user what frame number they are
		// on out of however many frames they have so far in
		// their animation.
		frameNo = board.getFrameNo();
		totalFrames = board.getTotalFrames();
	}

	// ---------- Mouse Controls ----------
	/**
	 * <p>
	 * Records which mouse button was used on a click.
	 * </p>
	 */
	@Override
	public final void mouseClicked(final MouseEvent e) {
		// When the mouse is clicked, get which button was used.
		currentButton = e.getButton();
		// Paint it, with the attributes of the mouseEvent.
		painting(e);
	}

	/**
	 * <p>
	 * Calls to the <code>painting(MouseEvent)</code> method on every square the
	 * mouse is dragged over.
	 * </p>
	 */
	@Override
	public final void mouseDragged(final MouseEvent e) {
		// Paint as the mouse is dragged across the canvas.
		painting(e);
	}

	/**
	 * <p>
	 * No function
	 * </p>
	 */
	@Override
	public void mouseMoved(final MouseEvent e) {
	}

	/**
	 * <p>
	 * No function
	 * </p>
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	/**
	 * <p>
	 * Gets the mouse button used when the button is pressed. This is required
	 * for <code>mouseDragged</code> to work.
	 * </p>
	 */
	@Override
	public final void mousePressed(final MouseEvent e) {
		// For MouseDragged to work, we must know which mouse button was clicked
		// when the mouse was pressed.
		currentButton = e.getButton();
	}

	/**
	 * <p>
	 * No function
	 * </p>
	 */
	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	/**
	 * <p>
	 * No function
	 * </p>
	 */
	@Override
	public void mouseExited(final MouseEvent e) {
	}

}
