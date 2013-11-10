package assignment.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import assignment.BoardModel;
import assignment.MyColor;

public class TestBoardModel {

	private BoardModel board;
	private Random randomizer = new Random();
	private File saveFile;
	private File loadFile;
	private static final int MAX_RGB_VALUE = 255;
	private static final String FILE_PATH_LOAD = "loadtest.txt";
	private static final String FILE_PATH_SAVE = "savetest.txt";

	@Before
	public void setupBoardTesting() {
		board = new BoardModel(50, 50);
	}

	@Test
	public void testCreateBoardModelWithParameters() {
		BoardModel board = new BoardModel(50, 50);
		assertEquals("Board dimensions incorrect", 50,
				board.getGridDimensions());
	}

	@Test
	public void testCreateBoardModelWithIllegalHighParameters() {
		BoardModel board = new BoardModel(150, 150);
		assertEquals("Board dimensions too high", 30, board.getGridDimensions());
	}

	@Test
	public void testCreateBoardModelWithIllegalLowParameters() {
		BoardModel board = new BoardModel(-1, -1);
		assertEquals("Board dimensions too low", 30, board.getGridDimensions());
	}

	@Test
	public void testNewGridIsFilledCorrectly() {
		char characterAtElement = 'Z';
		assertEquals("Character is not equal to expected", characterAtElement,
				board.getColor(5, 5));
	}

	@Test
	public void testNewGridWithChosenColor() {
		board.newGrid(50, 50, new MyColor(MAX_RGB_VALUE, 51, 0, '_'));
		assertEquals("Characters do not match on new colour", '_',
				board.getColor(5, 5));
	}

	@Test
	public void testPopulateFillColour() {
		board.populate(new MyColor(MAX_RGB_VALUE, 102, 0, '`'));
		assertEquals("Characters do not match after populate", '`',
				board.getColor(15, 15));
	}

	@Test
	public void testClearFillColour() {
		board.clear('d');
		assertEquals("Characters do not match after clear", 'd',
				board.getColor(15, 15));
	}

	@Test
	public void testClearFillColourBelowBounds() {
		board.clear('Y');
		assertEquals("Characters do not match after clear", 'Z',
				board.getColor(15, 15));
	}

	@Test
	public void testClearFillColourAboveBounds() {
		board.clear('~');
		assertEquals("Characters do not match after clear", 'Z',
				board.getColor(15, 15));
	}

	@Test
	public void testSettingAnIndividualElementColorWithPencil() {
		board.setValue(20, 20, 't', 1);
		assertEquals("Characters do not match on specific colour setting", 't',
				board.getColor(20, 20));
	}

	@Test
	public void testSettingAnIndividualElementColorWithPaintbrush() {
		board.setValue(20, 20, 'm', 2);
		assertEquals("Characters do not match - Brush center", 'm',
				board.getColor(20, 20));
		assertEquals("Characters do not match - Brush bottom", 'm',
				board.getColor(21, 20));
		assertEquals("Characters do not match - Brush right", 'm',
				board.getColor(20, 21));
		assertEquals("Characters do not match - Brush top", 'm',
				board.getColor(19, 20));
		assertEquals("Characters do not match - Brush left", 'm',
				board.getColor(20, 19));
	}

	@Test
	public void testSettingAnIndividualElementColorWithSpill() {
		BoardModel board = new BoardModel(50, 50);
		int loc1X = randomizer.nextInt(50) + 1;
		int loc1Y = randomizer.nextInt(50) + 1;
		int loc2X = randomizer.nextInt(50) + 1;
		int loc2Y = randomizer.nextInt(50) + 1;
		int loc3X = randomizer.nextInt(50) + 1;
		int loc3Y = randomizer.nextInt(50) + 1;
		board.setValue(22, 22, 'l', 2);
		assertEquals("Characters do not match after Spill", 'l',
				board.getColor(loc1X, loc1Y));
		assertEquals("Characters do not match after Spill", 'l',
				board.getColor(loc2X, loc2Y));
		assertEquals("Characters do not match after Spill", 'l',
				board.getColor(loc3X, loc3Y));
	}

	@Test
	public void testNewProject() {
		board.newProject(15, new MyColor(MAX_RGB_VALUE, MAX_RGB_VALUE,
				MAX_RGB_VALUE, 'Z'));
		assertEquals("New Project size does not match", 15,
				board.getGridDimensions());
		assertEquals("New Project colours do not match", 'Z',
				board.getColor(5, 5));
		assertEquals(
				"Project Frames still holds more than the single new Frame", 1,
				board.getTotalFrames());
	}

	@Before
	public void setupFileForLoadTesting() {
		URL url = getClass().getResource(FILE_PATH_LOAD);
		loadFile = new File(url.getPath());
	}

	@Test
	public void testOpeningAFile() {
		board.openFile(loadFile);
		char[][] testGridArray = new char[board.getGridDimensions() + 2][board
				.getGridDimensions() + 2];
		for (int i = 1; i <= board.getGridDimensions(); i++) {
			for (int j = 1; j <= board.getGridDimensions(); j++) {
				testGridArray[j][i] = board.getColor(j, i);
			}
		}
		for (int i = 1; i <= board.getGridDimensions(); i++) {
			for (int j = 1; j <= board.getGridDimensions(); j++) {
				assertEquals("Grids do match up - Loading unsuccessful",
						testGridArray[j][i], board.getColor(j, i));
			}
		}
		assertEquals("Frame amount is not that expected from load", 14,
				board.getTotalFrames());
	}

	@Before
	public void setupFileForSaveTesting() {
		URL url = getClass().getResource(FILE_PATH_SAVE);
		saveFile = new File(url.getPath());
	}

	@Test
	public void testSavingToFile() {
		BoardModel board = new BoardModel(10, 10);
		board.clear('r');
		board.setValue(2, 2, 'f', 1);
		board.saveFile(saveFile);

		board.openFile(saveFile);
		char[][] testGridArray = new char[board.getGridDimensions() + 2][board
				.getGridDimensions() + 2];
		for (int i = 1; i <= board.getGridDimensions(); i++) {
			for (int j = 1; j <= board.getGridDimensions(); j++) {
				testGridArray[j][i] = board.getColor(j, i);
			}
		}
		for (int i = 1; i <= board.getGridDimensions(); i++) {
			for (int j = 1; j <= board.getGridDimensions(); j++) {
				assertEquals("Grids do match up - Loading unsuccessful",
						testGridArray[j][i], board.getColor(j, i));
			}
		}
		assertEquals("Frame amount is not that expected from load", 1,
				board.getTotalFrames());
	}

	@Test
	public void testNewFrame() {
		board.newFrame();
		char[][] testGrid = board.getGrid(2);
		assertEquals("Not as many Frames as expected", 2,
				board.getTotalFrames());
		assertEquals("New Frame is not as expected", 'Z', testGrid[4][4]);
	}

	@Test
	public void testCopyForNewFrame() {
		board.clear('h');
		board.copy();
		char[][] testGrid = board.getGrid(2);
		assertEquals("Not as many Frames as expected", 2,
				board.getTotalFrames());
		assertEquals("New Frame is not as expected", 'h', testGrid[4][4]);
	}

	@Test
	public void testDeleteFrame() {
		assertEquals("Did not start with correct Frames", 1,
				board.getTotalFrames());
		board.newFrame();
		board.deleteFrame();
		assertEquals("Did not correctly delete Frame", 1,
				board.getTotalFrames());
	}

	@Test
	public void testNextAndPreviousFrame() {
		board.newFrame();
		board.newFrame();
		board.newFrame();
		assertEquals("Board did not start with correct amount of Frames", 4,
				board.getTotalFrames());
		board.previousFrame();
		board.previousFrame();
		assertEquals("Board did not change Frames correctly - Previous", 2,
				board.getFrameNo());
		board.nextFrame();
		assertEquals("Board did not change Frames correctly - Next", 3,
				board.getFrameNo());
	}
}
