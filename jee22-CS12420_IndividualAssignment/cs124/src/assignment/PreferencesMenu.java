package assignment;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * <p>
 * Sets up a <code>JPanel</code> for the 'Preferences' menu called when a users
 * clicks the Preferences <code>MenuItem</code> in <code>SuiteDirector</code>.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PreferencesMenu extends JPanel implements ActionListener {
	private JPanel preferencesPanel;
	private JPanel framePrefPanel;
	private JPanel gridPrefPanel;
	private JPanel copyButtonPanel;
	private JPanel displayGridPanel;
	private JRadioButton copyYes;
	private JRadioButton copyNo;
	private JRadioButton displayYes;
	private JRadioButton displayNo;
	private ButtonGroup copyGroup;
	private ButtonGroup displayGroup;
	private JLabel copyLabel;
	private JLabel gridDisplayLabel;
	private JLabel titleLabel;
	private String copyString = " Copy previous Frame when creating a new Frame? ";
	private String showGridString = " Display Grid Lines on board? ";

	private boolean copyGridChoice = false;
	private boolean gridLinesDisplay = true;

	private static final int BORDER_SIZE = 5;

	/**
	 * <p>
	 * Creates all of the different sections, buttons and containers of the
	 * <code>JPanel</code>, ready to be called later.
	 * </p>
	 * <p>
	 * Creates each necessary component and containers to hold them in, sets
	 * their sizes, particular borders, labls/text, assigns <code>this</code>
	 * class as an <code>ActionListener</code> and adds them to the main
	 * <code>JPanel</code>. This main panel is then added to the class for use
	 * when called.
	 * </p>
	 */
	public PreferencesMenu() {
		preferencesPanel = new JPanel();
		framePrefPanel = new JPanel();
		framePrefPanel.setLayout(new BoxLayout(framePrefPanel,
				BoxLayout.PAGE_AXIS));
		copyButtonPanel = new JPanel(new GridLayout(1, 0));

		titleLabel = new JLabel(" Frames ");
		copyLabel = new JLabel(copyString);

		copyYes = new JRadioButton("Copy previous Frame ");
		copyYes.setActionCommand("copyyes");
		copyYes.setSelected(true);

		copyNo = new JRadioButton("Create new blank Frame ");
		copyNo.setActionCommand("copyno");

		copyYes.addActionListener(this);
		copyNo.addActionListener(this);

		copyGroup = new ButtonGroup();
		copyGroup.add(copyYes);
		copyGroup.add(copyNo);

		copyButtonPanel.add(copyLabel);
		copyButtonPanel.add(copyYes);
		copyButtonPanel.add(copyNo);
		copyButtonPanel.setBorder(BorderFactory.createLineBorder(
				Color.lightGray, BORDER_SIZE));
		copyButtonPanel.setLayout(new BoxLayout(copyButtonPanel,
				BoxLayout.PAGE_AXIS));

		framePrefPanel.add(titleLabel);
		framePrefPanel.add(copyButtonPanel);

		gridPrefPanel = new JPanel();
		gridPrefPanel.setLayout(new BoxLayout(gridPrefPanel,
				BoxLayout.PAGE_AXIS));
		displayGridPanel = new JPanel(new GridLayout(1, 0));

		titleLabel = new JLabel(" Grid ");
		gridDisplayLabel = new JLabel(showGridString);

		displayYes = new JRadioButton("Show Grid Lines ");
		displayYes.setActionCommand("displayyes");
		displayYes.setSelected(true);

		displayNo = new JRadioButton("Do Not Show Grid Lines ");
		displayNo.setActionCommand("displayno");

		displayYes.addActionListener(this);
		displayNo.addActionListener(this);

		displayGroup = new ButtonGroup();
		displayGroup.add(displayYes);
		displayGroup.add(displayNo);

		displayGridPanel.add(Box.createGlue());
		displayGridPanel.add(gridDisplayLabel);
		displayGridPanel.add(displayYes);
		displayGridPanel.add(displayNo);
		displayGridPanel.setBorder(BorderFactory.createLineBorder(
				Color.lightGray, BORDER_SIZE));
		displayGridPanel.setLayout(new BoxLayout(displayGridPanel,
				BoxLayout.PAGE_AXIS));

		gridPrefPanel.add(titleLabel);
		gridPrefPanel.add(displayGridPanel);

		preferencesPanel.setLayout(new BoxLayout(preferencesPanel,
				BoxLayout.PAGE_AXIS));
		preferencesPanel.add(framePrefPanel);
		preferencesPanel.add(gridPrefPanel);

		add(preferencesPanel);
	}

	/**
	 * <p>
	 * Determines what to do when an action is performed on the component.
	 * </p>
	 * <p>
	 * The first half of the method checks to see what the state of the
	 * <code>ButtonGroup</code> for whether a frame should be copied or made
	 * blank when newly created.<br />
	 * The second half is identical except that it determines if the grid lines
	 * should be displayed or not.
	 * </p>
	 * <p>
	 * Depending what is selected depends how a field is set and which
	 * <code>JRadioButton</code> is set to be selected as default.
	 * </p>
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		// Determining if a new frame should be a blank/a copy.
		ButtonModel copySet = copyGroup.getSelection();
		if (copySet.getActionCommand() == "copyyes") {
			copyGridChoice = true;
			copyYes.setSelected(true);
		} else {
			copyGridChoice = false;
			copyNo.setSelected(true);
		}

		// Determining if the Grid Lines should be displayed.
		ButtonModel displayGrid = displayGroup.getSelection();

		if (displayGrid.getActionCommand() == "displayyes") {
			gridLinesDisplay = true;
			displayYes.setSelected(true);
		} else {
			gridLinesDisplay = false;
			displayNo.setSelected(true);
		}

	}

	/**
	 * <p>
	 * Returns whether the grid should be blank/copy.
	 * </p>
	 * 
	 * @return a <code>boolean</code> of if all new frames should be a copy(
	 *         <code>true</code>) or blank when new(<code>false</code>).
	 */
	protected final boolean getFrameCopyOption() {
		return copyGridChoice;
	}

	/**
	 * <p>
	 * Returns whether the grid lines should be displayed.
	 * </p>
	 * 
	 * @return a <code>boolean</code> of if the grid lines should be displayed(
	 *         <code>true</code>) or not(<code>false</code>).
	 */
	protected final boolean getGridOption() {
		return gridLinesDisplay;
	}

}
