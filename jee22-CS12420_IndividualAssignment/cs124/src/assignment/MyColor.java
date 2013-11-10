package assignment;

import java.awt.Color;

/**
 * <p>
 * This class is simply a custom <code>Color</code> class that takes a character
 * in it's constructor for use with the text based 2D <code>char</code> Array.
 * </p>
 * 
 * <p>
 * An extra method to get the character of a colour, as set by
 * <code>MyColor</code> has been added.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MyColor extends Color {
	private char character;

	/**
	 * <p>
	 * Constructs the <code>Color</code> based on parameters.
	 * </p>
	 * 
	 * @param r
	 *            red value
	 * @param g
	 *            green value
	 * @param b
	 *            blue value
	 * @param c
	 *            character representation
	 */
	public MyColor(final int r, final int g, final int b, final char c) {
		super(r, g, b);
		character = c;
	}

	/**
	 * <p>
	 * Returns the character representation of the colour
	 * </p>
	 * 
	 * @return character connected to the colour.
	 */
	public final char getChar() {
		return character;
	}

}
