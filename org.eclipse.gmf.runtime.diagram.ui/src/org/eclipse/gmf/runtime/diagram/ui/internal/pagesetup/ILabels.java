/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;

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
	public static String LABEL_TITLE_PAGE_SETUP = PresentationResourceManager.getInstance().getString("PageSetupDialog.title"); //$NON-NLS-1$
	
	/** Represents a title of Page Setup Tab in Page Setup Dialog. */
	public static String LABEL_TITLE_PAGE_SETUP_TAB_ITEM = PresentationResourceManager.getInstance().getString("PageSetupDialog.title.tab"); //$NON-NLS-1$

	/////////////////////////
	//PSConfigurationBlock //
	/////////////////////////
	/** Represents a title of unit group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_UNITS = PresentationResourceManager.getInstance().getString("PageSetupDialog.title.group.unit"); //$NON-NLS-1$
	
	/** Represents a label of inches radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_INCHES = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.inches"); //$NON-NLS-1$
	
	/** Represents a label of millimetres radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_MILLIMETRES = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.millimetres"); //$NON-NLS-1$
	
	// Orientation group
	/** Represents a title of orientation group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_ORIENTATION = PresentationResourceManager.getInstance().getString("PageSetupDialog.title.group.orientation"); //$NON-NLS-1$
	
	/** Represents a label of portrait radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_PORTRAIT = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.portrait"); //$NON-NLS-1$
	
	/** Represents a label of landscape radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_LANDSCAPE = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.landscape"); //$NON-NLS-1$
	
	// Size group
	/** Represents a label of group paper size in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_PAPER_SIZE = PresentationResourceManager.getInstance().getString("PageSetupDialog.title.group.paperSize"); //$NON-NLS-1$
	
	/** Represents a label of page size combo in Page Setup Dialog. */
	public static String LABEL_PAGE_SIZE = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.size"); //$NON-NLS-1$
	
	/** Represents a label of page width text field in Page Setup Dialog. */
	public static String LABEL_PAGE_WIDTH = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.width"); //$NON-NLS-1$
	
	/** Represents a label of page height text field in Page Setup Dialog. */
	public static String LABEL_PAGE_HEIGHT = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.height"); //$NON-NLS-1$
	
	// Margin group
	/** Represents a label of margin group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_MARGIN= PresentationResourceManager.getInstance().getString("PageSetupDialog.title.group.margin"); //$NON-NLS-1$
	
	/** Represents a label of top margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_TOP = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.top"); //$NON-NLS-1$
	
	/** Represents a label of bottom margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_BOTTOM = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.bottom"); //$NON-NLS-1$
	
	/** Represents a label of left margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_LEFT = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.left"); //$NON-NLS-1$
	
	/** Represents a label of right margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_RIGHT = PresentationResourceManager.getInstance().getString("PageSetupDialog.text.page.margin.right"); //$NON-NLS-1$
	
	/** Represents an inches label appearing after margin text field in Page Setup Dialog. */
	public static String LABEL_INCHES = PresentationResourceManager.getInstance().getString("PageSetupDialog.label.inches"); //$NON-NLS-1$
	
	/** Represents a millimetres label appearing after margin text field in Page Setup Dialog. */
	public static String LABEL_MILLIMETRES = PresentationResourceManager.getInstance().getString("PageSetupDialog.label.millimetres"); //$NON-NLS-1$
	
	///////////////////////////////////
	// PSSelectionConfigurationBlock //
	///////////////////////////////////
	/** Represents an inches label appearing after margin text field in Page Setup Dialog. */
	public static String TITLE_PAGE_SETUP_TAB_ITEM = PresentationResourceManager.getInstance().getString("PageSetupDialog.title.tab"); //$NON-NLS-1$
	
	/** Represents an label for use workspace settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_USE_WORKSPACE_SETTINGS = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.workspace"); //$NON-NLS-1$
	
	/** Represents a label for configure workspace settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_CONFIGURE_WORKSPACE_SETTINGS = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.configure"); //$NON-NLS-1$
	
	/** Represents a label for use diagram settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_USE_DIAGRAM_SETTINGS = PresentationResourceManager.getInstance().getString("PageSetupDialog.button.diagram"); //$NON-NLS-1$
	
	//////////////////////////////
	// Printing Preference Page //
	//////////////////////////////
	/** Represents a title of Printing Preference Page.*/
	public static String LABEL_PREFERENCE_PAGE_PRINTING = PresentationResourceManager.getInstance().getString("PageSetupPreferencePage.title"); //$NON-NLS-1$

	/** Represents an error message appearing in Print Preference Page if one of the user input values is invalid. */
	public static String LABEL_PRINT_PREFERENCE_PAGE_ERROR_MSG = PresentationResourceManager.getInstance().getString("PrintPreferencePage.message.error"); //$NON-NLS-1$

}