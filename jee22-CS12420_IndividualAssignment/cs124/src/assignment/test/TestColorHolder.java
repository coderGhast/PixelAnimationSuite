package assignment.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import assignment.ColorHolder;
import assignment.MyColor;


public class TestColorHolder {
	

	private ColorHolder colors;
	
	@Before
	public void setupColorHolder() {
		colors = new ColorHolder();
	}
	@Test
	public void testGettingColorFromChar() {
		MyColor tempColor = colors.getColor('`');
		assertEquals("No match on colour character.", '`', tempColor.getChar());
		assertEquals("No match on RGB value - Red", 255, tempColor.getRed());
		assertEquals("No match on RGB value - Green", 51, tempColor.getGreen());
		assertEquals("No match on RGB value - Blue", 0, tempColor.getBlue());
	}
	
	@Test
	public void testGettingColorFromPosition() {
		MyColor tempColor = colors.getColorFromPosition(24);
		assertEquals("No match on colour character.", 'r', tempColor.getChar());
		assertEquals("No match on RGB value - Red", 0, tempColor.getRed());
		assertEquals("No match on RGB value - Green", 0, tempColor.getGreen());
		assertEquals("No match on RGB value - Blue", 102, tempColor.getBlue());
	}

}
