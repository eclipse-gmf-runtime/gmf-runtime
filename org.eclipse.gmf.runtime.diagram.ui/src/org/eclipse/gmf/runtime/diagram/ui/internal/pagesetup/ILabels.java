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

package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;

/**
 * Internationalized labels displayed Page Setup Dialog.
 * 
 * @author etworkow
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public interface ILabels {

	public static String ID_PAGE_SETUP_PREF_PAGE = "id.pageSetupPreferencePage"; //$NON-NLS-1$
	
	///////////////////////
	// Page Setup Dialog //
	///////////////////////
	
	/** Represents a title for Page Setup Dialog. */
	public static String LABEL_TITLE_PAGE_SETUP = DiagramResourceManager.getInstance().getString("PageSetupDialog.title"); //$NON-NLS-1$
	
	/** Represents a title of Page Setup Tab in Page Setup Dialog. */
	public static String LABEL_TITLE_PAGE_SETUP_TAB_ITEM = DiagramResourceManager.getInstance().getString("PageSetupDialog.title.tab"); //$NON-NLS-1$

	/////////////////////////
	//PSConfigurationBlock //
	/////////////////////////
	/** Represents a title of unit group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_UNITS = DiagramResourceManager.getInstance().getString("PageSetupDialog.title.group.unit"); //$NON-NLS-1$
	
	/** Represents a label of inches radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_INCHES = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.inches"); //$NON-NLS-1$
	
	/** Represents a label of millimetres radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_MILLIMETRES = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.millimetres"); //$NON-NLS-1$
	
	// Orientation group
	/** Represents a title of orientation group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_ORIENTATION = DiagramResourceManager.getInstance().getString("PageSetupDialog.title.group.orientation"); //$NON-NLS-1$
	
	/** Represents a label of portrait radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_PORTRAIT = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.portrait"); //$NON-NLS-1$
	
	/** Represents a label of landscape radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_LANDSCAPE = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.landscape"); //$NON-NLS-1$
	
	// Size group
	/** Represents a label of group paper size in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_PAPER_SIZE = DiagramResourceManager.getInstance().getString("PageSetupDialog.title.group.paperSize"); //$NON-NLS-1$
	
	/** Represents a label of page size combo in Page Setup Dialog. */
	public static String LABEL_PAGE_SIZE = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.size"); //$NON-NLS-1$
	
	/** Represents a label of page width text field in Page Setup Dialog. */
	public static String LABEL_PAGE_WIDTH = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.width"); //$NON-NLS-1$
	
	/** Represents a label of page height text field in Page Setup Dialog. */
	public static String LABEL_PAGE_HEIGHT = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.height"); //$NON-NLS-1$
	
	// Margin group
	/** Represents a label of margin group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_MARGIN= DiagramResourceManager.getInstance().getString("PageSetupDialog.title.group.margin"); //$NON-NLS-1$
	
	/** Represents a label of top margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_TOP = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.top"); //$NON-NLS-1$
	
	/** Represents a label of bottom margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_BOTTOM = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.bottom"); //$NON-NLS-1$
	
	/** Represents a label of left margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_LEFT = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.left"); //$NON-NLS-1$
	
	/** Represents a label of right margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_RIGHT = DiagramResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.right"); //$NON-NLS-1$
	
	/** Represents an inches label appearing after margin text field in Page Setup Dialog. */
	public static String LABEL_INCHES = DiagramResourceManager.getInstance().getString("PageSetupDialog.label.inches"); //$NON-NLS-1$
	
	/** Represents a millimetres label appearing after margin text field in Page Setup Dialog. */
	public static String LABEL_MILLIMETRES = DiagramResourceManager.getInstance().getString("PageSetupDialog.label.millimetres"); //$NON-NLS-1$
	
	///////////////////////////////////
	// PSSelectionConfigurationBlock //
	///////////////////////////////////
	/** Represents an inches label appearing after margin text field in Page Setup Dialog. */
	public static String TITLE_PAGE_SETUP_TAB_ITEM = DiagramResourceManager.getInstance().getString("PageSetupDialog.title.tab"); //$NON-NLS-1$
	
	/** Represents an label for use workspace settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_USE_WORKSPACE_SETTINGS = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.workspace"); //$NON-NLS-1$
	
	/** Represents a label for configure workspace settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_CONFIGURE_WORKSPACE_SETTINGS = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.configure"); //$NON-NLS-1$
	
	/** Represents a label for use diagram settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_USE_DIAGRAM_SETTINGS = DiagramResourceManager.getInstance().getString("PageSetupDialog.button.diagram"); //$NON-NLS-1$
	
	//////////////////////////////
	// Printing Preference Page //
	//////////////////////////////
	/** Represents a title of Printing Preference Page.*/
	public static String LABEL_PREFERENCE_PAGE_PRINTING = DiagramResourceManager.getInstance().getString("PageSetupPreferencePage.title"); //$NON-NLS-1$

	/** Represents an error message appearing in Print Preference Page if one of the user input values is invalid. */
	public static String LABEL_PRINT_PREFERENCE_PAGE_ERROR_MSG = DiagramResourceManager.getInstance().getString("PrintPreferencePage.message.error"); //$NON-NLS-1$

	/** Represents instructional text appearing at the top of Print Preference Page. */
	public static String LABEL_PRINT_PREFERENCE_PAGE_INSTRUCTION = DiagramResourceManager.getInstance().getString("PrintPreferencePage.instruction"); //$NON-NLS-1$

}