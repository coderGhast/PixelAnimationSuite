package assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * <p>
 * An abstract class for implementation by <code>BoardModel</code> and
 * <code>ViewModel</code> to inherit from.
 * </p>
 * <p>
 * This Abstract Model class has both <code>ViewModel</code> and
 * <code>BoardModel</code> inherit from it. By doing this, it saves a lot of
 * time with repeat coding as both of the classed use very similar methods in
 * the way they work.
 * </p>
 * <p>
 * As an Abstract class, both classes can use the same methods and fields as
 * this class that they similarly share, while also having their own methods.
 * </p>
 * <p>
 * I did not require any abstract methods as all the methods were either
 * identical or completely different to one another.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public abstract class Model {
	protected char[][] grid;
	protected ArrayList<char[][]> projectFrames = new ArrayList<char[][]>();
	protected int frameNo = 0; // Updated often and used to tell the application
								// and user
								// what the current 'active frame' on display
								// is.
	protected int projectSize; // Holds the value given in the constructor for
								// later use.
	protected static final int MAX_RGB_VALUE = 255;

	/**
	 * <p>
	 * First constructor is blank for <code>ViewModel</code>.
	 * </p>
	 */
	public Model() {
	}

	/**
	 * <p>
	 * Constructor creates a new <code>grid</code> using
	 * <code>newGrid(int, int, MyColor, char)</code>.
	 * </p>
	 * <p>
	 * The constructor takes parameters for the size of the grid. The default is
	 * 30. Other sizes are determined by imported files and users.
	 * </p>
	 * 
	 * <p>
	 * Once the rows/columns for this particular project are stored, a new
	 * <code>grid</code> based on it is created, using these dimensions and
	 * passing the RGB code for black with the character 'Z' (Which refers to
	 * 'Blank', 'Clear' or what the user will believe to be 'White').
	 * </p>
	 * 
	 * <p>
	 * If the input is somehow less than 1 or greater than 100, the parameters
	 * will be ignored and set as 30 instead. This scenario should not happen.
	 * </p>
	 * 
	 * @param rows
	 *            Amount of rows of project.
	 * @param columns
	 *            Amount of columns of project.
	 */
	public Model(final int rows, final int columns) {
		if (rows < 1 || rows > 100 || columns < 1 || columns > 100) {
			projectSize = 30;
			projectSize = 30;
		} else {
			projectSize = rows;
			projectSize = columns;
		}

		newGrid(projectSize, projectSize, new MyColor(MAX_RGB_VALUE,
				MAX_RGB_VALUE, MAX_RGB_VALUE, 'Z'));

	}

	/**
	 * <p>
	 * Creates a new <code>grid</code> based on the current project size (or new
	 * size) and with whatever colour requested by the call.
	 * </p>
	 * <p>
	 * When creating a new <code>grid</code>, we add two extra elements to the
	 * desired size of the array. This is to make later calculations easier by
	 * adding 'buffer' zones around the edges of our <code>grid</code>.<br />
	 * These buffer zones are to stop <code>NullPointerException</code>s by
	 * searches.
	 * </p>
	 * <p>
	 * After creating a new 2D <code>char</code> array and setting it as the
	 * active <code>grid</code> the method calls <code>populate(MyColor)</code>
	 * to fill it with the requested colour, then adds it into the
	 * <code>ArrayList</code> that holds all of the current project's frames.
	 * </p>
	 * <p>
	 * If the amount of frames stored is currently less than two, another grid
	 * (a copy of the first) is added to the <code>ArrayList</code> in order to
	 * give the application a 'buffer'. This is required in order for the
	 * application to not commit JRE suicide if a user tries to remove a frame
	 * later.
	 * </p>
	 * 
	 * @param rows
	 *            Amount of rows for the new <code>grid</code>.
	 * @param columns
	 *            Amount of columns for the new <code>grid</code>.
	 * @param passedColor
	 *            The colour to be used to fill the new <code>grid</code> with.
	 */
	public final void newGrid(final int rows, final int columns,
			final MyColor passedColor) {
		projectSize = rows;

		grid = new char[rows + 2][columns + 2];

		populate(passedColor);
		projectFrames.add(grid); // Adds a blank grid into position '0' of the
									// ArrayList.
		if (projectFrames.size() < 2) {
			projectFrames.add(grid); // Adding two gives the ArrayList 'buffers'
										// either side of
										// it's boundaries. The .remove() method
										// will not work
										// without this. This only needs to be
										// done on the creation
										// of the first Frame in a project (new
										// or opened) however.
		}
		frameNo = 1; // The next frame to be put into the Array list will be
						// the 'First', so update currentFrame to '1'.
	}

	/**
	 * <p>
	 * Passes the requested colour onto <code>clear(char)</code> in
	 * <code>char</code> form.
	 * </p>
	 * 
	 * @param color
	 *            A <code>MyColor</code> to get the <code>char</code> from in
	 *            order to correctly fill the grid with the requested colour.
	 */
	public final void populate(final MyColor color) {
		clear(color.getChar());
	}

	/**
	 * <p>
	 * Doesn't necessarily 'clear' but fills the <code>grid</code> with a single
	 * particular <code>MyColor char</code>.
	 * </p>
	 * <p>
	 * Fills every space in the <code>grid</code>/every element in the array
	 * with the passed character. This works for Clear, Fill Grid with
	 * Left/Right color and creating new <code>grid</code>s.
	 * </p>
	 * <p>
	 * If the character passed is somehow out of the bounds of what my custom 36
	 * character set uses, the method will instead use 'Z' for blank/white.
	 * </p>
	 * 
	 * @param c
	 *            <code>MyColor</code> character representation to fill all
	 *            elements of the <code>grid</code> array.
	 */
	public final void clear(final char c) {
		char chara = c;
		if (c < 'Z' || c > '}') {
			chara = 'Z';
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = chara;
			}
		}
	}

	/**
	 * <p>
	 * Returns the character at the position in the <code>grid</code> requested.
	 * </p>
	 * 
	 * @param row
	 *            Location in rows.
	 * @param column
	 *            Location in columns.
	 * @return The character at the requested location.
	 */
	public final char getColor(final int row, final int column) {
		return grid[row][column];
	}

	/**
	 * <p>
	 * Opens a <code>File</code> based on one chosen by
	 * <code>JFileChooser</code> in <code>SuiteDirector/SuitePlayer</code>.
	 * </p>
	 * <p>
	 * The method reads the file and takes the first two integers (on separate
	 * lines) as the amount of frames (<code>grid</code>s) in the project and
	 * the amount of squares per row/column.
	 * </p>
	 * <p>
	 * After clearing out any currently opened project from the
	 * <code>ArrayList</code> the method runs through a number of
	 * <code>for</code> loops which translate to the amount of frames in the
	 * project, get the lines of rows, one by one, and add them correctly to the
	 * array.
	 * </p>
	 * <p>
	 * The current active <code>grid</code> is set to the first in the sequence
	 * of the opened project.
	 * </p>
	 * 
	 * @param file
	 *            A <code>File</code> the user selected from
	 *            <code>JFileChooser</code>.
	 */
	public final void openFile(final File file) {

		Scanner inFile;
		try {
			inFile = new Scanner(new InputStreamReader(
					new FileInputStream(file)));

			int fileFrames = inFile.nextInt();
			int fileDimensions = inFile.nextInt();
			projectFrames.clear();

			// "For the amount of frames in this project, create a new grid to
			// fit the correct project dimensions and fill with
			// Black(White)/'Z'. With this grid, for every
			// row, get the next line from the file.
			// For each column, get the character at that column space and put
			// it into the grid (in order column > row)."
			for (int n = 0; n < fileFrames; n++) {
				newGrid(fileDimensions, fileDimensions, new MyColor(
						MAX_RGB_VALUE, MAX_RGB_VALUE, MAX_RGB_VALUE, 'Z'));
				for (int i = 1; i < grid.length - 1; i++) {
					String line = inFile.next(); // Read in the next row/line
													// (e.g. 20 squares
													// horizontally).
					while (line.length() != fileDimensions) {
						line = line.concat("Z"); // Very basic error checking.
													// If the file loaded does
													// not contain the correct
													// amount of characters, or
													// is missing a character or
													// two, then extra
													// characters will be added
													// to fix this.
					}
					for (int j = 1; j < grid.length - 1; j++) {
						grid[j][i] = line.charAt(j - 1); // Must input 'j'
															// (column first)
															// before 'i'
															// (rows), otherwise
															// the image is
															// rotated 90
															// degrees!
					}
				}
			}
			inFile.close(); // Close the file.
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"File could not be found.", "Error - File Not Found",
					JOptionPane.ERROR_MESSAGE);
		} catch (InputMismatchException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Please open the correct file type.",
					"Error - Wrong File Type", JOptionPane.ERROR_MESSAGE);
		}
		grid = projectFrames.get(1);
	}

	/**
	 * <p>
	 * Jumps to the next frame in the sequence (supposing there are more after
	 * the current).
	 * </p>
	 * <p>
	 * <code>nextFrame()</code> checks to see if the next frame is earlier in
	 * the animation than the very last frame. If it is, then that is set as the
	 * new active <code>grid</code>. The <code>frameNo</code> field is then
	 * updated, in order to reflect this change to the rest of the application
	 * the user.
	 * </p>
	 */
	public final void nextFrame() {
		if (frameNo < projectFrames.size() - 1) {
			grid = projectFrames.get(frameNo + 1);
			frameNo = frameNo + 1;
		}
	}

	/**
	 * <p>
	 * Changes the active display to the <code>grid</code> previous to the
	 * current active (if there is one).
	 * </p>
	 * <p>
	 * The final part of this method is the same as the <code>nextFrame()</code>
	 * method above, except with '-' rather than '+'.
	 * </p>
	 * <p>
	 * The difference lies in that it requires an if statement to start with in
	 * order to correctly function within the application.
	 * </p>
	 * <p>
	 * The algorithm for the extra code runs simply: "Before changing to the
	 * previous frame, check if the active frame is the last frame in the
	 * sequence. If yes then add this frame to the ArrayList/animation at the
	 * frame number it should be registered to, then remove the frame above this
	 * frame (to stop frame duplication in the sequence)."
	 * </p>
	 * <p>
	 * The reason for this is similar to the way that the
	 * <code>newFrame()</code> (in <code>SuiteDirector</code> works, whereby
	 * when a frame is added by <code>newFrame()</code>, it is not currently
	 * saved in the <code>ArrayList</code> until another new frame is added
	 * ahead of it. Therefore, if the last frame were not added to
	 * <code>ArrayList</code> before the previous <code>grid</code> was copied
	 * over to the active frame display, it would be lost/the display would be
	 * replaced with another frame (usually the first frame in the animation).<br />
	 * This <code>if</code> statement ensures that the final frame in the
	 * sequence is not lost.
	 * </p>
	 * <p>
	 * This extra bit of code is not required for <code>nextFrame()</code> as it
	 * will not let you skip past the last frame and all previous frames have
	 * already been added into the <code>ArrayList</code>.
	 * </p>
	 */
	public final void previousFrame() {
		if (frameNo == projectFrames.size() - 1) {
			projectFrames.add(frameNo, grid);
			projectFrames.remove(frameNo + 1);
		}
		if (frameNo > 1) {
			grid = projectFrames.get(frameNo - 1);
			frameNo = frameNo - 1;
		}
	}

	// ========== GETTER & SETTERS ==========
	/**
	 * <p>
	 * Returns the <code>frameNo</code>, referring to what place in the
	 * animation sequence the current <code>grid</code> is.
	 * </p>
	 * 
	 * @return The current active <code>grid</code> position in terms of the
	 *         animation order sequence.
	 */
	public final int getFrameNo() {
		return frameNo;
	}

	/**
	 * <p>
	 * Returns the total amount of frames held within
	 * <code>projectFrames ArrayList</code>, minus 1 to not include the buffer.
	 * </p>
	 * 
	 * @return The total amount of frames in the animation. (Minus one for the
	 *         'buffer').
	 */
	public final int getTotalFrames() {
		return (projectFrames.size() - 1);
	}

	/**
	 * <p>
	 * Returns the amount of characters along a single side of a
	 * <code>grid</code>.
	 * </p>
	 * 
	 * @return The amount of rows (and/or columns) across this project, e.g.
	 *         '20'.
	 */
	public final int getGridDimensions() {
		return projectSize;
	}

}
