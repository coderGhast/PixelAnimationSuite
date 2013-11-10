package assignment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/**
 * <p>
 * Custom Renderer for <code>JComboBox</code> in <code>NewProjectMenu</code>.
 * </p>
 * <p>
 * This class is the color renderer for the <code>JComboBox</code> that allows
 * the use of just plain colours as options, as opposed to text, in the
 * <code>JComboBox</code> in <code>NewProjectMenu</code> .
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CustomComboRenderer extends JPanel implements
		ListCellRenderer<Object> {

	private Color mainColor = Color.white;
	private static final int BORDER_SIZE_SIDES = 10;
	private static final int BORDER_SIZE_OTHER = 2;

	/**
	 * <p>
	 * Makes a call to the super class constructor and then sets a border for
	 * itself.
	 * </p>
	 */
	public CustomComboRenderer() {
		super();
		setBorder(new CompoundBorder(new MatteBorder(BORDER_SIZE_OTHER,
				BORDER_SIZE_SIDES, BORDER_SIZE_OTHER, BORDER_SIZE_SIDES,
				Color.white), new LineBorder(Color.black)));
	}

	/**
	 * <p>
	 * Gets the List Cell Renderer for <code>JComboBox</code>.
	 * </p>
	 * <p>
	 * Upon being passed the parameters required to make this class function,
	 * the method checks that the options are of <code>MyColor</code>. If they
	 * are, it sets it and returns this class as a Component to render.
	 * </p>
	 * 
	 * @param list
	 *            A list of the Objects passed, in this case
	 *            <code>MyColor</code>. Required to extend ListCellRenderer.
	 * @param color
	 *            The <code>MyColor</code> passed to this class to render.
	 * @param row
	 *            The row of the option.
	 * @param sel
	 *            If selected
	 * @param hasFocus
	 *            If focused
	 * @return This full class as a component to render the colours in
	 *         <code>JComboBox</code>.
	 */
	public final Component getListCellRendererComponent(final JList<?> list,
			final Object color, final int row, final boolean sel,
			final boolean hasFocus) {
		if (color instanceof MyColor) {
			mainColor = (MyColor) color;
		}
		return this;
	}

	/**
	 * <p>
	 * Paints the cells as the colour they should be, defined by their RBG
	 * value.
	 * </p>
	 * 
	 * @param g
	 *            graphics required for paint, as used by the super class.
	 */
	public final void paint(final Graphics g) {
		setBackground(mainColor);
		super.paint(g);
	}
}