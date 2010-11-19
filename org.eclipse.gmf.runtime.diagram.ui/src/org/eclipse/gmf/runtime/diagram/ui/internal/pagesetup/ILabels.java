/******************************************************************************
 * Copyright (c) 2002, 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;

/**
 * Internationalized labels displayed Page Setup Dialog.
 * 
 * @author etworkow
 */
public interface ILabels {

	public static String ID_PAGE_SETUP_PREF_PAGE = "id.pageSetupPreferencePage"; //$NON-NLS-1$
	
	///////////////////////
	// Page Setup Dialog //
	///////////////////////
	
	/** Represents a title for Page Setup Dialog. */
	public static String LABEL_TITLE_PAGE_SETUP = DiagramUIMessages.PageSetupDialog_title;
	
	/** Represents a title of Page Setup Tab in Page Setup Dialog. */
	public static String LABEL_TITLE_PAGE_SETUP_TAB_ITEM = DiagramUIMessages.PageSetupDialog_title_tab;

	/////////////////////////
	//PSConfigurationBlock //
	/////////////////////////
	/** Represents a title of unit group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_UNITS = DiagramUIMessages.PageSetupDialog_title_group_unit;
	
	/** Represents a label of inches radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_INCHES = DiagramUIMessages.PageSetupDialog_button_inches;
	
	/** Represents a label of millimetres radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_MILLIMETRES = DiagramUIMessages.PageSetupDialog_button_millimetres;
	
	// Orientation group
	/** Represents a title of orientation group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_ORIENTATION = DiagramUIMessages.PageSetupDialog_title_group_orientation;
	
	/** Represents a label of portrait radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_PORTRAIT = DiagramUIMessages.PageSetupDialog_button_portrait;
	
	/** Represents a label of landscape radio button in Page Setup Dialog. */
	public static String LABEL_BUTTON_LANDSCAPE = DiagramUIMessages.PageSetupDialog_button_landscape;
	
	// Size group
	/** Represents a label of group paper size in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_PAPER_SIZE = DiagramUIMessages.PageSetupDialog_title_group_paperSize;
	
	/** Represents a label of page size combo in Page Setup Dialog. */
	public static String LABEL_PAGE_SIZE = DiagramUIMessages.PageSetupDialog_text_page_size;
	
	/** Represents a label of page width text field in Page Setup Dialog. */
	public static String LABEL_PAGE_WIDTH_INCHES = DiagramUIMessages.PageSetupDialog_text_page_width_inches;
    
    /** Represents a label of page width text field in Page Setup Dialog. */
    public static String LABEL_PAGE_WIDTH_MM = DiagramUIMessages.PageSetupDialog_text_page_width_mm;
	
	/** Represents a label of page height text field in Page Setup Dialog. */
	public static String LABEL_PAGE_HEIGHT_INCHES = DiagramUIMessages.PageSetupDialog_text_page_height_inches;
    
    /** Represents a label of page height text field in Page Setup Dialog. */
    public static String LABEL_PAGE_HEIGHT_MM = DiagramUIMessages.PageSetupDialog_text_page_height_mm;
	
	// Margin group
	/** Represents a label of margin group in Page Setup Dialog. */
	public static String LABEL_TITLE_GROUP_MARGIN= DiagramUIMessages.PageSetupDialog_title_group_margin;
	
	/** Represents a label of top margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_TOP_INCHES = DiagramUIMessages.PageSetupDialog_text_page_margin_top_inches;
    
    /** Represents a label of top margin text field in Page Setup Dialog. */
    public static String LABEL_MARGIN_TOP_MM = DiagramUIMessages.PageSetupDialog_text_page_margin_top_mm;
	
	/** Represents a label of bottom margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_BOTTOM_INCHES = DiagramUIMessages.PageSetupDialog_text_page_margin_bottom_inches;
    
    /** Represents a label of bottom margin text field in Page Setup Dialog. */
    public static String LABEL_MARGIN_BOTTOM_MM = DiagramUIMessages.PageSetupDialog_text_page_margin_bottom_mm;
	
	/** Represents a label of left margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_LEFT_INCHES = DiagramUIMessages.PageSetupDialog_text_page_margin_left_inches;
    
    /** Represents a label of left margin text field in Page Setup Dialog. */
    public static String LABEL_MARGIN_LEFT_MM = DiagramUIMessages.PageSetupDialog_text_page_margin_left_mm;
	
	/** Represents a label of right margin text field in Page Setup Dialog. */
	public static String LABEL_MARGIN_RIGHT_INCHES = DiagramUIMessages.PageSetupDialog_text_page_margin_right_inches;
    
    /** Represents a label of right margin text field in Page Setup Dialog. */
    public static String LABEL_MARGIN_RIGHT_MM = DiagramUIMessages.PageSetupDialog_text_page_margin_right_mm;
	
	/** Represents an inches label appearing after margin text field in Page Setup Dialog. */
	public static String LABEL_INCHES = DiagramUIMessages.PageSetupDialog_label_inches;
	
	/** Represents a millimetres label appearing after margin text field in Page Setup Dialog. */
	public static String LABEL_MILLIMETRES = DiagramUIMessages.PageSetupDialog_label_millimetres;
	
	///////////////////////////////////
	// PSSelectionConfigurationBlock //
	///////////////////////////////////
	/** Represents an inches label appearing after margin text field in Page Setup Dialog. */
	public static String TITLE_PAGE_SETUP_TAB_ITEM = DiagramUIMessages.PageSetupDialog_title_tab;
	
	/** Represents an label for use workspace settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_USE_WORKSPACE_SETTINGS = DiagramUIMessages.PageSetupDialog_button_workspace;
	
	/** Represents a label for configure workspace settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_CONFIGURE_WORKSPACE_SETTINGS = DiagramUIMessages.PageSetupDialog_button_configure;
	
	/** Represents a label for use diagram settings button in Page Setup Dialog. */
	public static String LABEL_BUTTON_USE_DIAGRAM_SETTINGS = DiagramUIMessages.PageSetupDialog_button_diagram;
	
	//////////////////////////////
	// Printing Preference Page //
	//////////////////////////////
	/** Represents a title of Printing Preference Page.*/
	public static String LABEL_PREFERENCE_PAGE_PRINTING = DiagramUIMessages.PageSetupPreferencePage_title;

	/** Represents an error message appearing in Print Preference Page if one of the user input values is invalid. */
	public static String LABEL_PRINT_PREFERENCE_PAGE_ERROR_MSG = DiagramUIMessages.PrintPreferencePage_message_error;

	/** Represents instructional text appearing at the top of Print Preference Page. */
	public static String LABEL_PRINT_PREFERENCE_PAGE_INSTRUCTION = DiagramUIMessages.PrintPreferencePage_instruction;

}
