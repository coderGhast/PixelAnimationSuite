package assignment;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * <p>
 * Creates small 'preview' images of each frame in the animation
 * </p>
 * <p>
 * To create the small frame previews used in the <code>SuiteDirector</code>
 * this class needs to get the colours on each frame (stored as RGB and
 * characters) and put them into an <code>Image</code> form for display.<br />
 * It does this using <code>BufferedImage, WritableRaster</code> and
 * <code>Graphics2D</code>.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.1
 */
public class PreviewMaker {
	private int width;
	private int height;
	private BoardModel board;
	private ColorHolder colors;
	private ArrayList<Integer> colorSquares;
	private int[] squareHolder;
	private static final int NEW_IMAGE_SIZE = 60;

	/**
	 * <p>
	 * The constructor has a <code>BoardModel</code> passed as a parameter with
	 * which to get the correct sizings of the <code>grid</code>.
	 * </p>
	 * <p>
	 * A <code>ColourHolder</code> is also stored here so this class can search
	 * for the colours that will be the same as those in
	 * <code>SuiteDirector</code> and correspond to the same RBG/character
	 * values.
	 * </p>
	 * <p>
	 * The <code>board</code> is stored and <code>grid</code> size is held in
	 * two fields for height and width (rows and columns).
	 * </p>
	 * 
	 * @param passedBoard
	 *            a copy of the current state of the <code>board</code> in order
	 *            to get all of the <code>grid</code>s and make preview images
	 *            of them.
	 */
	public PreviewMaker(final BoardModel passedBoard) {
		colors = new ColorHolder();

		board = passedBoard;
		width = board.getGridDimensions();
		height = board.getGridDimensions();
	}

	/**
	 * <p>
	 * Creates the <code>JLabel</code> and <code>ImageIcon</code> that will be
	 * put added to the frame preview panel in <code>SuiteDirector</code>.
	 * </p>
	 * <p>
	 * <code>SuiteDirector</code> calls this method with the parameter for which
	 * frame number to create a preview of. <code>colorSquares</code> holds all
	 * of the RBG values of each colour from the <code>grid</code>.
	 * </p>
	 * <p>
	 * The method goes through all of the colours held in the <code>grid</code>
	 * array by searching using the <code>char</code> representation. The
	 * <code>MyColor</code> found is stored and it's RGB values are extracted
	 * and added to the <code>colorSquares</code> ArrayList.
	 * </p>
	 * 
	 * @param num
	 *            An integer to represent which <code>grid</code> should be made
	 *            into a preview.
	 * @return returns a <code>JLabel</code> containing the preview
	 *         <code>ImageIcon</code>.
	 */
	protected final JLabel generateImage(final int num) {

		colorSquares = new ArrayList<Integer>();
		// Get a copy of the current grid (which is one higher than the one
		// requested due to the buffer).
		char[][] grid = board.getGrid(num + 1);
		for (int i = 1; i <= height; i++) {
			for (int j = 1; j <= width; j++) {
				// Assign the RGB colours and add them.
				MyColor colorish = colors.getColor(grid[j][i]);

				colorSquares.add(colorish.getRed());
				colorSquares.add(colorish.getGreen());
				colorSquares.add(colorish.getBlue());

			}
		}
		// create a new array to hold each 'square' (each block of colour),
		// based on the size of the full ArrayList.
		squareHolder = new int[colorSquares.size()];

		// Move all elements from the ArrayList into the array.
		for (int i = 0; i < colorSquares.size(); i++) {
			squareHolder[i] = colorSquares.get(i);
		}
		// Call the method to create an ImageLabel, based on the array, and
		// return it.
		return createImageLabel(squareHolder);
	}

	/**
	 * <p>
	 * Creates a <code>BufferedImage</code>, which is passed to another method
	 * for resizing, and returns the <code>JLabel</code> to
	 * <code>generateImage()</code>.
	 * </p>
	 * <p>
	 * The method creates a new BufferedImage, based on the size of the
	 * <code>grid</code>, with RGB colour type. From this, a
	 * <code>WritableRaster</code> is created/extracted and it's data is set
	 * with the image width/height and using the RGB values saved into the array
	 * <code>squareHolder</code>.
	 * </p>
	 * <p>
	 * This new <code>Image</code> is sent to be resized in
	 * <code>resizeImage</code> with new sizes and the result is returned as a
	 * <code>JLabel</code>.
	 * </p>
	 * 
	 * @param passedSquareHolder
	 *            An array of all of the RGB values of each colour in the
	 *            <code>grid</code> in left-right order of display</code>.
	 * @return a <code>JLabel</code> holding the created and resized image.
	 */
	protected final JLabel createImageLabel(final int[] passedSquareHolder) {

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// Create a new Raster and set the RGB pixel values to those contained
		// within the array of integers representing the RGB (squareHolder).
		WritableRaster raster = image.getRaster();
		raster.setPixels(0, 0, width, height, passedSquareHolder);
		// Create a new JLabel, with a new ImageIcon on it, based on a resize of
		// the image just created with BufferedImage/WritableRaster.
		JLabel label = new JLabel(new ImageIcon(resizeImage(image,
				NEW_IMAGE_SIZE, NEW_IMAGE_SIZE)));
		// Return the label with ImageIcon.
		return label;
	}

	/**
	 * <p>
	 * Takes an <code>Image</code>, sent as an <code>ImageIcon</code>, and
	 * resizes it.
	 * </p>
	 * <p>
	 * Starts by taking the image and new dimensions for it and create a
	 * <code>BufferedImage</code> based on these new dimensions. Graphics are
	 * extracted from the <code>BufferedImage</code> using the in-built method,
	 * and from this we create a new <code>Graphics2D</code> of the image.<br />
	 * We draw this image, resizing it with the new dimensions and then
	 * <code>dispose()</code> of the <code>Graphics2D</code> and return the new
	 * <code>BufferedImage</code> to be set on the <code>JLabel</code>.
	 * </p>
	 * 
	 * @param image
	 *            The image to be resized.
	 * @param newWidth
	 *            the new width of the image.
	 * @param newHeight
	 *            the new height of the image.
	 * @return the image at it's new size.
	 */
	protected final BufferedImage resizeImage(final Image image,
			final int newWidth, final int newHeight) {
		/*
		 * Originally I attempted to work out the arithmetic to calculate how to
		 * resize the image manually. I found this to be rather challenging
		 * however and decided it would be easier to employ Graphics2D, which
		 * through research on the Internet, seems to be the most efficient way
		 * of doing this.
		 */

		final BufferedImage bufferedImage = new BufferedImage(newWidth,
				newHeight, BufferedImage.TYPE_INT_RGB);
		// Using Graphics2D, create and draw a new version of the image at the
		// new size.
		final Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
		graphics2D.dispose(); // Finish with graphics2D.

		return bufferedImage; // Return the image to the JLabel.
	}

}