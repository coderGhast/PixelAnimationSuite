package assignment;

import java.util.ArrayList;

/**
 * <p>
 * Generates and holds an <code>ArrayList</code> of <code>MyColor</code>.
 * </p>
 * <p>
 * This class is like a Colour Palette, that creates and holds my 36 custom
 * colours. One of it's main purposes is to get all of those colours into an
 * array.
 * </p>
 * <p>
 * The process to create the colours is through a series of <code>for</code>
 * loops that run for 5 turns each, changing one, two or all three of the Red,
 * Green, Blue values of a <color>MyColor</color> to represent some form of
 * gradient and variety of colour.
 * </p>
 * <p>
 * This array is returned in one of the methods for use in a
 * <code>JComboBox</code>.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */

public class ColorHolder {

	private ArrayList<MyColor> colorList;
	private MyColor color;
	private Object[] colorCombo;
	private int r = 0;
	private int g = 0;
	private int b = 0;
	private char c = 'Z';
	private static final int MAX_RGB_VALUE = 255;
	private static final int RGB_NUMBER_FOR_ALTERING = 51;
	private static final int COLORS_PER_FOR_LOOP = 5;

	/**
	 * <p>
	 * The Constructor is where all of the colors are created.
	 * </p>
	 * <p>
	 * The first <code>MyColor</code> to be created and added to the
	 * <code>ArrayList colorList</code> is what the application will consider
	 * 'White', but in truth is black (255, 255, 255).<br />
	 * This is due to how the grid is drawn in <code>SuiteCanvas</code>.
	 * </p>
	 * <p>
	 * Between each set of 5 colours, <code>resetColors()</code> is called which
	 * returns the RGB values to 0. Sometime the individual colour values are
	 * manually set after a reset to generate colours of a specific hue and/or
	 * gradient.
	 * </p>
	 * <p>
	 * Once all colours have been added into <code>colorList</code>, the
	 * <code>ArrayList</code> is converted into an array and stored in
	 * <code>colorCombo</code> for use in the <code>JComboBox</code> as part of
	 * <code>NewProjectMenu</code> and the displayed colours of the side bar in
	 * <code>SuiteDirector</code>.
	 * </p>
	 */
	public ColorHolder() {
		colorList = new ArrayList<MyColor>();

		color = new MyColor(MAX_RGB_VALUE, MAX_RGB_VALUE, MAX_RGB_VALUE, c);

		colorList.add(color); // Initial Color - White (In truth, Black, but it
								// is presented as White).
		resetColors();
		// Red to Dark Red
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			r = r + RGB_NUMBER_FOR_ALTERING;
			c++;
			color = new MyColor(r, g, b, c);
			colorList.add(color);
		}
		resetColors();
		// Red to Orange to Yellow
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			r = MAX_RGB_VALUE;
			g = g + RGB_NUMBER_FOR_ALTERING;
			c++;
			color = new MyColor(r, g, b, c);
			colorList.add(color);
		}
		resetColors();
		// Green to Dark Green
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			g = g + RGB_NUMBER_FOR_ALTERING;
			c++;
			color = new MyColor(r, g, b, c);
			colorList.add(color);
		}
		resetColors();
		// Light Blue/Green-Blue to Blue
		g = MAX_RGB_VALUE;
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			g = g - RGB_NUMBER_FOR_ALTERING;
			b = MAX_RGB_VALUE;
			c++;
			color = new MyColor(r, g, b, c);
			colorList.add(color);
		}
		resetColors();
		// Blue to Dark Blue
		b = MAX_RGB_VALUE;
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			c++;
			color = new MyColor(r, g, b, c);
			b = b - RGB_NUMBER_FOR_ALTERING;
			colorList.add(color);
		}
		resetColors();
		// Dark Purple to Purple/Pink
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			r = r + RGB_NUMBER_FOR_ALTERING;
			b = b + RGB_NUMBER_FOR_ALTERING;
			c++;
			color = new MyColor(r, g, b, c);
			colorList.add(color);
		}
		g = MAX_RGB_VALUE;
		// Light Gray to Black
		for (int amount = 1; amount <= COLORS_PER_FOR_LOOP; amount++) {
			r = r - RGB_NUMBER_FOR_ALTERING;
			g = g - RGB_NUMBER_FOR_ALTERING;
			b = b - RGB_NUMBER_FOR_ALTERING;
			c++;
			color = new MyColor(r, g, b, c);
			colorList.add(color);
		}

		// Copy the list over to an Array of Object for use
		// in ComboBox in SuiteDirector.
		colorCombo = colorList.toArray();
	}

	/**
	 * <p>
	 * Returns a <code>MyColor</code> found by searching through the full 36
	 * colours and matching a <code>char</code>.
	 * </p>
	 * <p>
	 * The method looks through the full list of <code>MyColor</code>s in the
	 * <code>colorList</code>. Every time it gets a colour, it stores it in a
	 * variable, which is then used to call <code>getChar()</code> in order to
	 * compare to the <code>char</code> parameter.<br />
	 * If there is a match, the <code>location</code> for the
	 * <code>MyColor</code> in the <code>ArrayList</code> is stored to be used
	 * for returning it.
	 * </p>
	 * 
	 * @param passedChar
	 *            The character representing a <code>MyColor</code> elsewhere in
	 *            the program.
	 * @return The <code>MyColor</code> that matches the <code>char</code>
	 *         parameter.
	 */
	public final MyColor getColor(final char passedChar) {
		// Searches for a Colour in the list based upon it's Character.
		int location = 0;
		for (int i = 0; i < colorList.size(); i++) {
			MyColor tempCol = colorList.get(i);
			if (passedChar == tempCol.getChar()) {
				location = i;
			}
		}
		return colorList.get(location);
	}

	/**
	 * <p>
	 * Returns the <code>MyColor</code> requested by using an <code>int</code>
	 * to <code>get()</code> it directly from the location in the
	 * <code>colorList</code>.
	 * </p>
	 * 
	 * @param i
	 *            the location to look for a <code>MyColor</code> in the
	 *            <code>colorList</code>.
	 * @return the <code>MyColor</code> in the location at <code>i</code>.
	 */
	public final MyColor getColorFromPosition(final int i) {
		// Searches for a Colour based on it's position in the ArrayList.
		return colorList.get(i);
	}

	/**
	 * <p>
	 * Returns the array of <code>MyColor</code> on request.
	 * </p>
	 * <p>
	 * This is used for the <code>JComboBox</code> and for displaying colours in
	 * the side bar.
	 * </p>
	 * 
	 * @return the full list of <code>MyColor</code> in a <code>char</code>
	 *         array.
	 */
	protected final Object[] getAll() {
		// Returns all Colours for the JComboBox.
		return colorCombo;
	}

	/**
	 * <p>
	 * Resets all RGB colour values to 0.
	 * </p>
	 */
	private void resetColors() {
		// Resets the Red, Green, Blue values to 0;
		r = 0;
		g = 0;
		b = 0;
	}
}
