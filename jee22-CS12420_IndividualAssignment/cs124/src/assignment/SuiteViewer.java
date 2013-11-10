package assignment;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import javax.swing.JPanel;

/**
 * <p>
 * This class is the Player version of the Director's 'SuiteCanvas'. It is the
 * space that the Animation is displayed.
 * </p>
 * <p>
 * Most methods are identical to those in SuiteCanvas and so will not be as
 * heavily commented.
 * </p>
 * 
 * @author James Euesden - jee22
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SuiteViewer extends JPanel implements Runnable {
	private ViewModel board;
	private int frameNo = 1;
	private int rows;
	private int columns;
	private boolean repeat;
	private static final double CANVAS_SIZE = 500.0;
	private int frameDelay = 400;

	private ColorHolder colorPalette = new ColorHolder();

	/**
	 * <p>
	 * Creates a new <code>ViewModel</code> to access the necessary data.
	 * </p>
	 */
	SuiteViewer() {
		board = new ViewModel();
		setBackground(Color.white);
	}

	@Override
	public final void paintComponent(final Graphics g) {
		super.paintComponent(g);
		drawgrid(g);
	}

	/**
	 * <p>
	 * Draws the contents of the array to the screen as a visible image as part
	 * of an animation, using <code>char</code> to get a <code>MyColor</code> to
	 * paint.
	 * </p>
	 * 
	 * @param g
	 *            the <code>Graphics</code> from super.
	 */
	private void drawgrid(final Graphics g) {
		int whiteSpace = 1;
		double square = CANVAS_SIZE / rows;
		int squareSize = (int) Math.round(square);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (board.getColor(i + 1, j + 1) == 'Z') {
					g.setColor(Color.white);
					g.draw3DRect(whiteSpace + i * squareSize, whiteSpace + j
							* squareSize, squareSize, squareSize, true);
				} else {
					g.setColor(colorPalette.getColor(board.getColor(i + 1,
							j + 1)));
					g.fill3DRect(whiteSpace + i * squareSize, whiteSpace + j
							* squareSize, squareSize, squareSize, true);
				}

			}
		}
	}

	/**
	 * <p>
	 * Passes the call to open a file on the user's system for play back.
	 * </p>
	 * <p>
	 * After calling to <code>board</code>, related values are updated and this
	 * panel is repainted.
	 * </p>
	 * 
	 * @param file
	 *            the <code>File</code> to be opened.
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
	 * Calls to move back a frame, either in the <code>ArrayList</code> or moves
	 * to the very last frame.
	 * </p>
	 * <p>
	 * Jumps to the previous frames and updates all necessary values. If the
	 * frame is the very first frame of the Animation, it will skip to the very
	 * last frame.
	 * </p>
	 */
	protected final void backwardStep() {
		if (frameNo == 1) {
			board.switchToLast();
			updateValues();
			repaint();
		} else {
			board.previousFrame();
			updateValues();
			repaint();
		}
	}

	/**
	 * <p>
	 * Repeat stays true and continues the animation looping, at the speed set
	 * by the speed slider in <code>SuitePlayer</code>.
	 * </p>
	 * <p>
	 * To play the animation, the method continuously calls to the
	 * <code>forwardStep()</code> method, then sleeps.
	 * </p>
	 * <p>
	 * When the animation reaches an end, it returns the animation to the start
	 * to loop play back.
	 * </p>
	 */
	@Override
	public final void run() {
		// The 'play' method.
		int i = frameNo;
		repeat = true;
		try {
			while (repeat) {
				forwardStep();
				Thread.sleep(frameDelay);
				repaint();

				if (i == board.getTotalFrames()) {
					i = 0;
					board.returnToFirst();
					updateValues();
					repaint();
				}
				i++;
			}
		} catch (InterruptedException e) {
			// An interrupted Exception is thrown here if the Play button is
			// pressed repeatedly without first stopping the animation.
			// This is due to the thread being interrupted during the 'sleep'
			// Since the thread is correctly stopped and I am aware of this
			// issue, I have not printed it out to the command line or
			// further handled the Exception.
		}
	}

	/**
	 * <p>
	 * puts the thread on 'pause', allowing it to be continued at any point
	 * still by changing the <code>boolean</code> that keeps the
	 * <code>Thread</code> running.
	 * </p>
	 */
	protected final void pause() {
		repeat = false;
		repaint();
	}

	/**
	 * <p>
	 * Jumps forward to the next frame. If the current frame is the last, it
	 * will return to the first frame.
	 * </p>
	 */
	protected final void forwardStep() {
		//
		if (frameNo == board.getTotalFrames()) {
			board.returnToFirst();
			updateValues();
			repaint();
		} else {
			board.forwardStep();
			updateValues();
			repaint();
		}
	}

	/**
	 * <p>
	 * Pauses the thread by discontinuing it's loop and returns the frame to the
	 * first.
	 * </p>
	 * <p>
	 * Changes the <code>boolean</code> that keeps the <code>Thread</code> to
	 * play the animation running, returns the <code>board</code> to the first
	 * frame and updates any necessary values.
	 * </p>
	 */
	protected final void stop() {
		repeat = false;
		board.returnToFirst();
		updateValues();
		repaint();
	}

	/**
	 * <p>
	 * Updates the current active frame number that the user can see.
	 * </p>
	 */
	protected final void updateValues() {
		frameNo = board.getFrameNo();
	}

	/**
	 * <p>
	 * Allows SuitePlayer to set the frames per second with the
	 * <code>JSlider</code>.
	 * </p>
	 * 
	 * @param frameDelay
	 *            the amount that is used to determine the delay between frames.
	 */
	protected final void setDelay(final int frameDelay) {
		//
		this.frameDelay = frameDelay;
	}

}
