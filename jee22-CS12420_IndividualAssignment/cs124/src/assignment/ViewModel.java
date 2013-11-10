package assignment;

/**
 * <p>
 * The Model for the Player side of the Animation Suite
 * </p>
 * <p>
 * This class is similar to <code>BoardModel</code>, except it is used for
 * displaying the grid as an animation, rather than as something to be drawn
 * upon.
 * </p>
 * <p>
 * This class inherits from Model and so most of it's methods are contained
 * within there.
 * </p>
 * 
 * @author James Euesden
 * @version 1.1
 */
public class ViewModel extends Model {
	/**
	 * <p>
	 * Empty constructor
	 * </p>
	 */
	public ViewModel() {
	}

	/**
	 * <p>
	 * Changes the active display frame to one created earlier but ahead of the
	 * current active in the <code>ArrayList</code>/Animation Order.
	 * </p>
	 * <p>
	 * If the current <code>grid</code> is not the last in the sequence, move
	 * forward one <code>grid</code>
	 * </p>
	 */
	public final void forwardStep() {
		if (frameNo < projectFrames.size() - 1) {
			grid = projectFrames.get(frameNo + 1);
			frameNo = frameNo + 1;
		}
	}

	/**
	 * <p>
	 * Returns the current frame to the first in the sequence.
	 * </p>
	 */
	public final void returnToFirst() {
		grid = projectFrames.get(1);
		frameNo = 1;
	}

	/**
	 * <p>
	 * Jumps to the very last frame (for back step past the first frame).
	 * </p>
	 */
	public final void switchToLast() {
		int lastFrame = projectFrames.size() - 1;
		grid = projectFrames.get(lastFrame);
		frameNo = projectFrames.size() - 1;
	}

}
