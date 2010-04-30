/******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * The menu-like pop-up widget that allows the user to select a value for the line styles.
 * 
 * @author Anthony Hunter
 * @since 2.1
 */
public abstract class LineStylesPopup {

	/**
	 * The map of items in the popup. The key is the return value of the popup
	 * and the value in the map is the image.
	 */
	protected Map imageMap = new LinkedHashMap();

	protected Shell shell;

	protected Object selectedItem = null;

	/**
	 * Creates a LineStylesPopup below the specified shell.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 */
	public LineStylesPopup(Shell parent) {
		shell = new Shell(parent, SWT.FLAT);
		shell.setLayout(new GridLayout(1, true));

		initializeImageMap();

		for (Iterator e = imageMap.keySet().iterator(); e.hasNext();) {
			Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
			GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			data.heightHint = 20;
			data.widthHint = 67;
			button.setLayoutData(data);

			final Object item = e.next();
			final Image image = (Image) imageMap.get(item);
			button.setImage(image);
			button.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e1) {
					selectedItem = item;
					shell.dispose();
				}
			});
		}
		// close dialog if user selects outside of the shell
		shell.addListener(SWT.Deactivate, new Listener() {

			public void handleEvent(Event e) {
				shell.setVisible(false);
			}
		});

	}

	/**
	 * Initialize the image map by adding items and images to the imageMap.
	 */
	protected abstract void initializeImageMap();

	/**
	 * Open the popup, waits for an item to be selected and then closes popup.
	 * 
	 * @param location
	 *            the initial location of the popup; the popup will be
	 *            positioned so that it does not run off the screen and the
	 *            largest number of items are visible
	 */
	public void open(Point location) {
		open(location, -1);
		return;
	}
	
	/**
	 * Opens the popup ensuring that it doesn't run of the screen and doesn't
	 * hide the launching button
	 * 
	 * @param location
	 *            the initial location of the popup
	 * @param lowerY
	 *            if the final y is above location.y, then the popup is moved
	 *            above lowerY
	 * @since 1.2
	 */
	public void open(Point location, int lowerY) {

		Point listSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		shell.setBounds(location.x, location.y, listSize.x, listSize.y);
		// Ensure the popup doesn't run off the screen and doesn't hide the
		// launching button.
		Point newLocation = WindowUtil.constrainWindowLocation(shell,
				location, lowerY);
		shell.setLocation(newLocation.x, newLocation.y);
		shell.open();
		shell.setFocus();
		Display display = shell.getDisplay();
		while (!shell.isDisposed() && shell.isVisible()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return;
	}	

	/**
	 * Gets the item the user selected. Could be null as the user may cancel the
	 * gesture.
	 * 
	 * @return the selected item or null.
	 */
	public Object getSelectedItem() {
		return selectedItem;
	}
	
	/**
	 * Gets the image corresponding to the item the user selected.  Could be null.
	 * 
	 * @return Image corresponding to the item the user selected or null.
	 * @since 1.4
	 */
	public Image getSelectedItemImage() {
		if (getSelectedItem() == null) {
			return null;
		} else {
			return (Image) imageMap.get(getSelectedItem());
		}
	}
	
}