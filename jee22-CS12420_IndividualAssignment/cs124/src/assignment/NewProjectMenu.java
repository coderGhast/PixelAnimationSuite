package assignment;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>
 * Sets up a <code>JPanel</code> to be called by the 'New Project'
 * <code>MenuItem</code> in <code>SuiteDirector</code>.
 * </p>
 * 
 * <p>
 * It contains two combo boxes. One for 'Size' and one for 'Colour'.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NewProjectMenu extends JPanel {
	private JPanel newProjectPanel;
	private JComboBox<Object> colors;
	private JComboBox<Object> sizing;
	private String selectedSize;
	private MyColor selectedColor;
	private int defaultSizeSelection = 5; // Not actually 'size 5'. This refers
											// to the item at element 5 in the
											// array of the JComboBox.
	private int defaultColorSelection = 0;
	private ColorHolder colorList = new ColorHolder();
	private Object[] selectionValues = { "5", "10", "15", "20", "25", "30",
			"35", "40", "50", "100" };
	private static final int GRID_LAYOUT_DIMENSION = 7;

	/**
	 * <p>
	 * Sets up all of the attributes and components of the menu, ready to be
	 * called.
	 * </p>
	 * <p>
	 * The constructor makes a new <code>JPanel</code> to hold all of the
	 * components and sets it's Layout. Using <code>JLabel</code>s, the user can
	 * be informed of what each option does.
	 * </p>
	 * <p>
	 * The options for user choices are held in <code>JComboBox</code>s. The
	 * selections presented to the user for their choice of starting background
	 * colour are displayed as the actual colour.<br />
	 * This works by using my <code>CustomComboRenderer</code>.
	 * </p>
	 * <p>
	 * The panel(<code>newProjectPanel</code> is added to this class in order
	 * for it to be used as a <code>JPanel</code> in <code>JOptionPane</code>.
	 * </p>
	 */
	NewProjectMenu() {
		newProjectPanel = new JPanel();
		newProjectPanel
				.setLayout(new GridLayout(2, 2, 2, GRID_LAYOUT_DIMENSION));

		newProjectPanel.add(new JLabel("Select New Grid Size: "));
		sizing = new JComboBox<Object>(selectionValues);
		sizing.setSelectedIndex(defaultSizeSelection); // Sets default choice to
														// 30 or users last
														// choice
		newProjectPanel.add(sizing);

		newProjectPanel.add(new JLabel("Select New Background Color: "));
		colors = new JComboBox<Object>(colorList.getAll());
		colors.setSelectedIndex(defaultColorSelection);
		colors.setRenderer(new CustomComboRenderer()); // Uses a custom
														// ComboRenderer to
														// render the colours
														// used in the Colour
														// drop down box.

		newProjectPanel.add(colors);

		add(newProjectPanel);
	}

	protected final String getSelectedSize() {
		// Returns the user selected Size for SuiteDirector.
		selectedSize = (String) sizing.getSelectedItem();
		defaultSizeSelection = sizing.getSelectedIndex();
		return selectedSize;
	}

	protected final MyColor getSelectedColor() {
		// Returns the user selected Colour for SuiteDirector.
		selectedColor = (MyColor) colors.getSelectedItem();
		defaultColorSelection = colors.getSelectedIndex();
		return selectedColor;
	}

}
