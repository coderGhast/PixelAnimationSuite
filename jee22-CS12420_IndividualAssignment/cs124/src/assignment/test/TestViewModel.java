package assignment.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import assignment.MyColor;
import assignment.ViewModel;

public class TestViewModel {

	private ViewModel view;
	private File loadFile;
	private static final int MAX_RGB_VALUE = 255;
	private static final String FILE_PATH_LOAD = "loadtest.txt";

	@Before
	public void setUpView() {
		view = new ViewModel();
		URL url = getClass().getResource(FILE_PATH_LOAD);
		loadFile = new File(url.getPath());
		view.openFile(loadFile);
	}

	@Test
	public void testPopulateFillColour() {
		view.populate(new MyColor(MAX_RGB_VALUE, 102, 0, '`'));
		assertEquals("Characters do not match after populate", '`',
				view.getColor(15, 15));
	}

	@Test
	public void testOpenFile() {
		char[][] testGridArray = new char[view.getGridDimensions() + 2][view
				.getGridDimensions() + 2];
		for (int i = 1; i <= view.getGridDimensions(); i++) {
			for (int j = 1; j <= view.getGridDimensions(); j++) {
				testGridArray[j][i] = view.getColor(j, i);
			}
		}
		for (int i = 1; i <= view.getGridDimensions(); i++) {
			for (int j = 1; j <= view.getGridDimensions(); j++) {
				assertEquals("Grids do match up - Loading unsuccessful",
						testGridArray[j][i], view.getColor(j, i));
			}
		}
		assertEquals("Frame amount is not that expected from load", 14,
				view.getTotalFrames());
	}

	@Test
	public void testBackwardStep() {
		view.forwardStep();
		view.forwardStep();
		view.previousFrame();
		assertEquals("Backwards step failed", 2, view.getFrameNo());
	}

	@Test
	public void testForwardStep() {
		view.forwardStep();
		view.forwardStep();
		view.forwardStep();
		assertEquals("Forward step failed", 4, view.getFrameNo());
	}

	@Test
	public void testNewGrid() {
		view.newGrid(50, 50, new MyColor(255,255,255, '}'));
		assertEquals("Size of new grid does match up!", 50,
				view.getGridDimensions());
		assertEquals("Colors of new grid not set correct", '}',
				view.getColor(5, 10));
	}

	@Test
	public void testReturnToFirst() {
		view.forwardStep();
		view.forwardStep();
		view.forwardStep();
		view.forwardStep();
		view.returnToFirst();
	}

	@Test
	public void testSwitchToLast() {
		view.switchToLast();
		assertEquals("Not switched to last frame correctly",
				view.getTotalFrames(), view.getFrameNo());
	}

	@Test
	public void testGetFrameNo() {
		view.forwardStep();
		assertEquals("Did not get correct Frame number", 2, view.getFrameNo());
	}

	@Test
	public void testGetTotalFrames() {
		assertEquals(
				"Did not get the expected amount of total frames as known in the loadtest.txt",
				14, view.getTotalFrames());
	}

	@Test
	public void testGetGridDimensions() {
		assertEquals(
				"Did not get the expected grid dimensions from the loadtest.txt",
				30, view.getGridDimensions());
	}

}
