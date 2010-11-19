/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs;

import java.util.ArrayList;

/**
 * All controls that appear in Page Setup Dialog.
 * 
 * @author etworkow
 */
public class PageSetupControlType {

	private String fName = null;
	
	/** Represents 'Use workspace settings' radio button. */
	public static PageSetupControlType BUTTON_USE_WORKSPACE_SETTINGS = new PageSetupControlType("Use workspace settings"); //$NON-NLS-1$
	
	/** Represents 'Use configure settings' push button. */
	public static PageSetupControlType BUTTON_CONFIGURE_WORKSPACE_SETTINGS = new PageSetupControlType("Configure workspace settings"); //$NON-NLS-1$
	
	/** Represents 'Use diagram settings' radio button. */
	public static PageSetupControlType BUTTON_USE_DIAGRAM_SETTINGS = new PageSetupControlType("Use diagram settings"); //$NON-NLS-1$
	
	/** Represents 'Use inches' radio button. */
	public static PageSetupControlType BUTTON_USE_INCHES = new PageSetupControlType("Inches"); //$NON-NLS-1$
	
	/** Represents 'Use millimetres' radio button. */
	public static PageSetupControlType BUTTON_USE_MILLIM = new PageSetupControlType("Millim"); //$NON-NLS-1$
	
	/** Represents 'Use portrait' radio button. */
	public static PageSetupControlType BUTTON_USE_PORTRAIT = new PageSetupControlType("Portrait"); //$NON-NLS-1$
	
	/** Represents 'Use landscape' radio button. */
	public static PageSetupControlType BUTTON_USE_LANDSCAPE = new PageSetupControlType("Landscape"); //$NON-NLS-1$
	
	/** Represents combo listing supported page sizes. */
	public static PageSetupControlType COMBO_PAGE_SIZE = new PageSetupControlType("Size"); //$NON-NLS-1$
	
	/** Represents 'Page width' text field. */
	public static PageSetupControlType TEXT_PAGE_WIDTH = new PageSetupControlType("Width"); //$NON-NLS-1$
	
	/** Represents 'Page height' text field. */
	public static PageSetupControlType TEXT_PAGE_HEIGHT = new PageSetupControlType("Height"); //$NON-NLS-1$
	
	/** Represents top margin text field. */
	public static PageSetupControlType TEXT_MARGIN_TOP = new PageSetupControlType("Top margin"); //$NON-NLS-1$
	
	/** Represents bottom margin text field. */
	public static PageSetupControlType TEXT_MARGIN_BOTTOM = new PageSetupControlType("Bottom margin"); //$NON-NLS-1$
	
	/** Represents left margin text field. */
	public static PageSetupControlType TEXT_MARGIN_LEFT = new PageSetupControlType("Left margin"); //$NON-NLS-1$
	
	/** Represents right margin text field. */
	public static PageSetupControlType TEXT_MARGIN_RIGHT = new PageSetupControlType("Right margin"); //$NON-NLS-1$

	/** Represents unit label next to page width text field. */
	public static PageSetupControlType LABEL_UNIT_PAGE_WIDTH = new PageSetupControlType("Page width label"); //$NON-NLS-1$
	
	/** Represents unit label next to page height text field. */
	public static PageSetupControlType LABEL_UNIT_PAGE_HEIGHT = new PageSetupControlType("Page height label"); //$NON-NLS-1$
	
	/** Represents unit label next to top margin text field. */
	public static PageSetupControlType LABEL_UNIT_MARGIN_TOP = new PageSetupControlType("Top margin label"); //$NON-NLS-1$
	
	/** Represents unit label next to bottom margin text field. */
	public static PageSetupControlType LABEL_UNIT_MARGIN_BOTTOM = new PageSetupControlType("Bottom margin label"); //$NON-NLS-1$
	
	/** Represents unit label next to left margin text field. */
	public static PageSetupControlType LABEL_UNIT_MARGIN_LEFT = new PageSetupControlType("Left margin label"); //$NON-NLS-1$
	
	/** Represents unit label next to right margin text field. */
	public static PageSetupControlType LABEL_UNIT_MARGIN_RIGHT = new PageSetupControlType("Right margin label"); //$NON-NLS-1$
	
	/** Represents configuration block allowing the user to configure page settings. */
	public static PageSetupControlType CONFIG_BLOCK_PRINT = new PageSetupControlType("Config block"); //$NON-NLS-1$
	
	/** Represents configuration block allowing the user to toggle between diagram and workspace settings. */
	public static PageSetupControlType CONFIG_BLOCK_SELECTION = new PageSetupControlType("Config block selection"); //$NON-NLS-1$
	
	private PageSetupControlType(String name) {
		fName = name;
	}
	
	/**
	 * Returns name of the control.
	 * 
	 * @return String control name
	 */
	public String getName() {
		return fName;
	}
	
	/**
	 * Return a list of text fields that are used to obtain user input.
	 * 
	 * @return ArrayList List of Text widgets that appear in Page Setup Dialog.
	 */
	public static ArrayList getTextFieldTypes() {
		ArrayList list = new ArrayList();
		list.add(TEXT_MARGIN_TOP);
		list.add(TEXT_MARGIN_BOTTOM);
		list.add(TEXT_MARGIN_LEFT);
		list.add(TEXT_MARGIN_RIGHT);
		list.add(TEXT_PAGE_WIDTH);
		list.add(TEXT_PAGE_HEIGHT);
		return list;
	}
}
