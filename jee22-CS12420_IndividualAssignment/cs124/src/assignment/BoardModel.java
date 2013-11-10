package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * <p>
 * The 'Model' side of the Director aspect of the Animation Suite.
 * </p>
 * <p>
 * This class handles all methods that relate to the underlying data used for
 * the board/grid that is displayed with the <code>SuiteCanvas</code> and
 * <code>SuiteSwing</code> classes.
 * </p>
 * <p>
 * It handles what to do with the colors/chars, how it should react to tools
 * being used and also how to create/delete/move through different frames in the
 * animation being created.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.1
 */

public class BoardModel extends Model {

	/**
	 * <p>
	 * The constructor takes parameters for the size of the <code>grid</code>.
	 * The default is 30. Other sizes are determined by imported files and
	 * users.
	 * </p>
	 * 
	 * <p>
	 * Once the rows/columns for this particular project are stored, a new
	 * <code>grid</code> based on it is created, using these dimensions and
	 * passing the RGB code for Black with the character <code>'Z'</code> (Which
	 * refers to 'Blank', 'Clear' or what the user will believe to be 'White').
	 * </p>
	 * 
	 * <p>
	 * If the input is somehow less than 1 or greater than 100, the parameters
	 * will be ignored and set as 30 instead. This scenario should not happen.
	 * </p>
	 * 
	 * @param rows
	 *            the amount of rows the grid should have
	 * @param columns
	 *            the amount of columns the grid should have
	 */
	public BoardModel(final int rows, final int columns) {
		if (rows < 1 || rows > 100 || columns < 1 || columns > 100) {
			projectSize = 30;
		} else {
			projectSize = rows;
		}
		super.newGrid(projectSize, projectSize, new MyColor(MAX_RGB_VALUE,
				MAX_RGB_VALUE, MAX_RGB_VALUE, 'Z'));

	}

	/**
	 * <p>
	 * Sets a <code>char</code> at a requested place in the array
	 * </p>
	 * <p>
	 * <code>setValue()</code> takes the row and the column of the square to be
	 * painted/element to be changed to the given char(the colour) and the tool
	 * that are in use to determine exactly which character should be added and
	 * if more than one element should be updated (and how, if yes).
	 * </p>
	 * <p>
	 * It determines what it should do, based upon the tool used:
	 * </p>
	 * 
	 * <p>
	 * Pencil: Simple 'one click, one square updated'.
	 * </p>
	 * 
	 * <p>
	 * Spill can: Fills an area of the same color clicked on with the current
	 * active color on whichever mouse button was used. Does not work diagonally
	 * (just like other Image Manipulation programs). <br />
	 * How it works: "Using grid co-ordinates/array locations given, get the old
	 * color in a variable/memory. Change the location to the new colour. Then,
	 * check if any of the surrounding squares (above, below, left and right)
	 * were of the same original color we just copied over. If they were, call
	 * on this method again (almost like recursion!) but with the parameters for
	 * the method updated to represent the square that was adjacent to the
	 * original square (the one that we now know needs updating also). Finally,
	 * update the color of this square to be the new color.<br />
	 * This process is then repeated for all other sides of the square that
	 * require it until it can no longer be called/there are no squares to
	 * update.
	 * </p>
	 * 
	 * <p>
	 * Paint Brush: Paints a 'Cross' pattern by painting the square clicked on
	 * and then the four surrounding it. (or in the array, those that are 1
	 * position around it, in both rows and columns.
	 * </p>
	 * 
	 * @param row
	 *            the grid row to be used
	 * @param column
	 *            the grid column to be used
	 * @param color
	 *            the character representing the colour to be set
	 * @param tool
	 *            the integer representing the tool to be used
	 */
	public final void setValue(final int row, final int column,
			final char color, final int tool) {
		// PENCIL.
		if (tool == 1) {
			grid[row][column] = color;
		}
		// SPILL CAN.
		/*
		 * The code is quite cumbersome and I feel could probably be re-factored
		 * somewhat, however, this is the way I chose to implement this method
		 * and functions as best I can see fit for the time being and scope of
		 * this application.
		 */
		else if (tool == 2) {
			if (grid[row][column] != color) { // This if statement stops an
												// Exception caused by
												// the Spill tool if the grid
												// color clicked on is already
												// the same color as that in the
												// tool.
				char oldColor = grid[row][column];
				grid[row][column] = color;
				if (grid[row + 1][column] == oldColor
						&& row + 1 < grid.length - 1) {
					setValue(row + 1, column, color, tool);
					grid[row + 1][column] = color;
				}
				if (grid[row][column + 1] == oldColor
						&& column + 1 < grid.length - 1) {
					setValue(row, column + 1, color, tool);
					grid[row][column + 1] = color;
				}
				if (grid[row - 1][column] == oldColor && row - 1 > 0) {
					setValue(row - 1, column, color, tool);
					grid[row - 1][column] = color;
				}
				if (grid[row][column - 1] == oldColor && column - 1 > 0) {
					setValue(row, column - 1, color, tool);
					grid[row][column - 1] = color;
				}
			}
			// PAINTBRUSH.
		} else if (tool == 3) {
			grid[row][column] = color; // Middle square
			grid[row + 1][column] = color; // Right Square
			grid[row][column + 1] = color; // Bottom Square
			grid[row - 1][column] = color; // Left Square
			grid[row][column - 1] = color; // Top Square
		}

	}

	// ========== FILE MENU METHODS ==========
	/**
	 * <p>
	 * Makes a full new blank project for the user.
	 * </p>
	 * <p>
	 * Clears all Frames held in memory and creates a new <code>grid</code>
	 * based on the <code>newSize</code> using the
	 * <code> super.newGrid(int, int, MyColor)</code> from the super abstract
	 * class <code>Model</code> to create whole new blank project.
	 * </p>
	 * 
	 * @param newSize
	 *            An integer that the new <code>grid</code> should be a size of.
	 *            This will be used for both rows and columns.
	 * @param selectedColor
	 *            whatever colour the user selected to use from the
	 *            <code>NewProjectMenu</code> (or set by the method that called
	 *            to this, usually set to blank/</code>(255,255,255,'Z')</code>.
	 */
	public final void newProject(final int newSize, final MyColor selectedColor) {
		projectFrames.clear();
		super.newGrid(newSize, newSize, selectedColor);
	}

	/**
	 * <p>
	 * Saves the the current project the user is working on to a file on their
	 * system, named by themselves.
	 * </p>
	 * <p>
	 * The given file is saved to another <code>File</code> variable and checked
	 * to see if it ends with <code>.txt</code>. If it doesn't,
	 * <code>.txt</code> is appended to the file name.
	 * </p>
	 * <p>
	 * By creating a new <code>File</code> with the constructor
	 * <code>File(parent File, String)</code> and checking that the user named
	 * file ends with <code>.txt</code>, we can ensure that the file will always
	 * be saved as a <code>.txt</code> file no matter how the user named the
	 * file.
	 * </p>
	 * <p>
	 * Similar to the method <code>'previousFrame()'</code>, whereby if a user
	 * tries to move off the frame it must first be saved or else the date is
	 * lost and replaced with that of the first frame's data, the same must be
	 * done before the project is saved.<br />
	 * This only applies to the final frame however, as any frame in between the
	 * first and final is automatically saved into the <code>ArrayList</code>
	 * upon creation.
	 * </p>
	 * 
	 * @param file
	 *            The file of choice, either already loaded into the application
	 *            or chosen by the user with <code>JFileChooser</code>.
	 * @return savingFile Whatever file is created, it is return up to
	 *         <code>SuiteDirector</code>. This is to update the
	 *         <code>currentFile</code> field. It is passed at the end just in
	 *         case the file name needed to be changed by the saving process.
	 */
	public final File saveFile(final File file) {

		File savingFile = file;
		if (!file.getName().endsWith(".txt")) {
			savingFile = new File(file.getParent(), file.getName() + ".txt");
		}

		if (frameNo == projectFrames.size() - 1) {
			projectFrames.add(frameNo, grid);
			projectFrames.remove(frameNo + 1);
		}

		// Gets the current Grid to save, starting from 0.
		PrintWriter writer;
		char[][] gridToSave = projectFrames.get(0);
		try {
			writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(savingFile)));
			// Prints out the amount of Frames in the Projects and the
			// dimensions of the Frames.
			writer.println(getTotalFrames());
			writer.print(getGridDimensions());
			// Similar method to the Load method. Just Saving out.
			for (int n = 1; n <= getTotalFrames(); n++) {
				for (int i = 1; i < getGridDimensions() + 1; i++) {
					writer.println();
					for (int j = 1; j < getGridDimensions() + 1; j++) {
						writer.print(gridToSave[j][i]);
					}
				}
				// Due to the buffers, there is strange behavior when saving
				// the final frame.
				// This if statement ensures the correct frame is saved.
				if (n < projectFrames.size() - 1) {
					gridToSave = projectFrames.get(n + 1);
				}
			}
			writer.close(); // Close the writer.
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"File not found - Could not save " + file,
					"Error - File Not Found", JOptionPane.ERROR_MESSAGE);
		}
		return savingFile; // Return the file that is saved in the end for use
							// in Director.
	}

	// ========== PROJECT MENU METHODS ==========
	/**
	 * <p>
	 * Creates a new blank frame after the current frame that the user is
	 * working with.
	 * </p>
	 * <p>
	 * The first <code>if</code> part of this method translates to:<br />
	 * "If the frame the user is currently on is neither the first nor the last
	 * frame in their project, then create a new grid of chars object, populate
	 * it (fill the 2D <code>char</code> array with 'Z' for white, update the
	 * active held frame number by one and then add the new frame in between the
	 * adjacent frames."
	 * </p>
	 * 
	 * <p>
	 * This allows a user to create a set of frames for their animation and
	 * then, should they use the next/previous frame buttons to get somewhere
	 * between the first and last frames, they can add in an entirely new 'grid'
	 * frame that is not at the end.
	 * </p>
	 * 
	 * <p>
	 * The second <code>else</code> part of this method is practically the same
	 * as the first, except that it adds the current grid into the
	 * <code>ArrayList</code> <em>before</em> creating a new grid of
	 * <code>char</code>s. This is so that when the user is on the last side of
	 * their animation, the current frame they have been working on will be
	 * added to the end of the <code>ArrayList</code> (set 'manually' by the
	 * <code>frameNo</code> reference telling the application what 'frame' it
	 * should currently be on.
	 * </p>
	 * <p>
	 * It then creates a new grid of <code>char</code>s and populates it,
	 * updating the active display <code>grid</code>, before finally updating
	 * the active frame number to one higher than previously, to indicate to
	 * both the user and the application that a new frame has been added.
	 * </p>
	 */
	public final void newFrame() {

		if ((projectFrames.size() - frameNo) > 1) {
			grid = new char[projectSize + 2][projectSize + 2];
			clear('Z');
			frameNo = frameNo + 1;
			projectFrames.add(frameNo, grid);
		} else {
			projectFrames.add(frameNo, grid);
			grid = new char[projectSize + 2][projectSize + 2];
			clear('Z');
			frameNo = frameNo + 1;
		}
	}

	/**
	 * <p>
	 * Copies the current frame to the next new frame.
	 * </p>
	 * <p>
	 * Much like <code>newFrame()</code> but copies over the previous frame
	 * design by making a new temporary <code>grid</code> to hold the old
	 * <code>grid</code>, creates a new <code>grid</code>, puts the old
	 * <code>grid</code> into this one and then uses that for the new frame
	 * displayed to the user as a new <code>grid</code> to work on
	 * </p>
	 * .
	 */
	public final void copy() {
		char[][] tempGrid = new char[grid.length][grid.length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				tempGrid[i][j] = grid[i][j];
			}
		}

		if ((projectFrames.size() - frameNo) > 1) {
			grid = new char[projectSize + 2][projectSize + 2];
			clear('Z');
			for (int h = 0; h < grid.length; h++) {
				for (int k = 0; k < grid.length; k++) {
					grid[h][k] = tempGrid[h][k];
				}
			}
			frameNo = frameNo + 1;
			projectFrames.add(frameNo, grid);
		} else {
			projectFrames.add(frameNo, grid);
			grid = new char[projectSize + 2][projectSize + 2];
			clear('Z');
			for (int h = 0; h < grid.length; h++) {
				for (int k = 0; k < grid.length; k++) {
					grid[h][k] = tempGrid[h][k];
				}
			}
			frameNo = frameNo + 1;
		}
	}

	/**
	 * <p>
	 * Deletes the current frame and returns to the previous frame.
	 * </p>
	 * <p>
	 * The method gets the previous <code>grid</code> from the
	 * <code>ArrayList</code> and sets it as the active <code>grid</code> for
	 * the user to work on. it then deletes the <code>grid</code> the user was
	 * previous on from the <code>ArrayList</code>.
	 * </p>
	 * <p>
	 * NOTE: The user is not allowed to delete the first frame. Even if there
	 * are frames created after it.
	 * <p>
	 */
	public final void deleteFrame() {
		if (frameNo > 1) {
			grid = projectFrames.get(frameNo - 1);// Get the previous grid and
													// make it the current
													// visible.

			projectFrames.remove(frameNo); // Remove the desired frame from the
											// ArrayList/animation.
			frameNo = frameNo - 1; // Decrement; so now on the 'previous' frame.
		}
	}

	// ========== GETTER & SETTERS ==========
	/**
	 * <p>
	 * Returns the full grid that the parameter asks for.
	 * </p>
	 * 
	 * @param num
	 *            The location in the <code>ArrayList</code> that should be
	 *            accessed to retrieve the requested <code>grid</code>.
	 * @return Returns the grid found at the requested location.
	 */
	public final char[][] getGrid(final int num) {
		return projectFrames.get(num);
	}

}
