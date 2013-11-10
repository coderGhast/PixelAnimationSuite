package assignment;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

// Note:
// This class was written by Neil Taylor and provided while on my course module, CS124.
// Full credit of this class belongs to himself. 
// Implementation of this class elsewhere in the Application is of my own work.

/**
 * <p>
 * Represents a menu that holds a list of recently used items. This class
 * manages a JMenu, which is added to the application menu outside of this
 * class. It is intended that this class is the only one that manages the
 * content of the menu.
 * </p>
 * 
 * <p>
 * The starting state for the menu is that there is a menu item that indicates
 * there are no recent items. There is a separator and then a menu item that can
 * be used to clear the items and return it to this default state.
 * </p>
 * 
 * <p>
 * As new items are added, using <code>addItem(File)</code>, a check is made to
 * determine if the item is already displayed. If it is already displayed, it is
 * removed before the item is added again to the top of the menu (at position
 * 0).
 * </p>
 * 
 * <p>
 * This class is used to respond to the menu item to clear the list. It will
 * reset the menu by calling <code>resetMenu</code>.
 * </p>
 * 
 * <p>
 * Selection of the menu items that represent previous files is handled
 * differently. When such a menu item is created (using the local utility method
 * <code>createMenuItem</code>), the <code>action listener</code> is set to be
 * the one that is passed in as a parameter to the constructor. If you look at
 * the line in ApplicationFrame which creates the RecentItemsMenu, you will see
 * that the ApplicationFrame object is set as the action listener.
 * </p>
 * 
 * <pre>
 * recentItems = new RecentItemsMenu(recentItemMenu, this);
 * </pre>
 * 
 * <p>
 * Therefore, the ApplicationFrame class will respond to the selection of the
 * previous files. Look for the following test in the ApplicationFrame method
 * <code>actionPerformed(ActionEvent)</code> to see where the selection is
 * handled.
 * </p>
 * 
 * <pre>
 * else if(command.equals(RecentItemsMenu.RECENT_FILE_COMMAND)) { 
 * 		
 *    // an item on the recent items menu has been selected. 
 *    JMenuItem menuItem = (JMenuItem)event.getSource(); 
 *    File file = (File)menuItem.getClientProperty("filename");
 *    openFile(file); 
 * }
 * </pre>
 * 
 * @author Neil Taylor (nst@aber.ac.uk)
 * @version 1.0
 */
public class RecentItemsMenu implements ActionListener {

	/** Command name used for all menu options that represent a recent file. */
	public static final String RECENT_FILE_COMMAND = "recentFileSelected";

	/** The menu that holds the list of recent items */
	private JMenu menu;

	/** Used to attach to all sub-items */
	private ActionListener actionListener;

	/**
	 * The menu item that is used to indicate that there aren't any recent
	 * entries.
	 */
	private JMenuItem noRecentEntries = new JMenuItem("No Recent Entries");

	/** The menu item that is used to reset (clear) the list of recent entries. */
	private JMenuItem clearMenu = new JMenuItem("Clear List");

	public RecentItemsMenu(final JMenu menu, final ActionListener listener) {
		this.menu = menu;
		this.actionListener = listener;
		resetMenu();
	}

	/**
	 * Resets the menu to hold the 'no recent entries' option, a separator and a
	 * 'clear menu' option.
	 */
	protected final void resetMenu() {
		// ensure that there are no other items
		menu.removeAll();
		menu.add(noRecentEntries);
		menu.addSeparator();
		menu.add(clearMenu);
		clearMenu.addActionListener(this);
	}

	public final void addItem(final File file) {

		if (menu.getItem(0) == noRecentEntries) {
			menu.remove(0);
		} else {
			for (Component menuItem : menu.getMenuComponents()) {

				if (menuItem instanceof JMenuItem) {
					JMenuItem item = (JMenuItem) menuItem;
					if (item.getText().equals(file.getAbsolutePath())) {
						menu.remove(menuItem);
					}
				}
			}
		}

		// add the menu item to the top of the list,
		// representing the most recent item.
		menu.add(createMenuItem(file), 0);

	}

	/**
	 * 
	 * @param file
	 *            The file to be associated with this menu item.
	 * 
	 * @return The menu item with the associated data.
	 */
	protected final JMenuItem createMenuItem(final File file) {

		JMenuItem item = new JMenuItem(file.getAbsolutePath());
		item.addActionListener(actionListener);
		item.setActionCommand(RECENT_FILE_COMMAND);
		item.putClientProperty("filename", file);

		return item;
	}

	/**
	 * 
	 * @param e
	 *            The event.
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		resetMenu();
	}
}
