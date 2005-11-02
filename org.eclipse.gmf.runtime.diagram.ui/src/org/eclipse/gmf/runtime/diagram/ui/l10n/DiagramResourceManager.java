/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.l10n;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.AbstractResourceManager;

/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 *
 * @author cmahoney
 *
 */
public class DiagramResourceManager extends AbstractResourceManager {

	
	/**
	 * Enabled subdirectory off of root icon directory
	 */
	private static final String ENABLED_PREFIX = "elcl16/"; //$NON-NLS-1$

	/**
	 * Disabled subdirectory off of root icon directory
	 */
	private static final String DISABLED_PREFIX = "dlcl16/"; //$NON-NLS-1$	

	/**
	 * the image for handle collapse
	 */
	public static final String IMAGE_HANDLE_COLLAPSE = "expanded.gif"; //$NON-NLS-1$
	/**
	 * the image for handle expand
	 */
	public static final String IMAGE_HANDLE_EXPAND = "collapsed.gif"; //$NON-NLS-1$
	
	/**
	 * the image for handle incoming wet
	 */
	public static final String IMAGE_HANDLE_INCOMING_WEST = "handle_incoming_west.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing west
	 */
	public static final String IMAGE_HANDLE_OUTGOING_WEST = "handle_outgoing_west.gif"; //$NON-NLS-1$
	/**
	 * the image for handle incoming east
	 */	
	public static final String IMAGE_HANDLE_INCOMING_EAST = "handle_incoming_east.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing east
	 */	
	public static final String IMAGE_HANDLE_OUTGOING_EAST = "handle_outgoing_east.gif"; //$NON-NLS-1$
	/**
	 * the image for handle incoming south
	 */	
	public static final String IMAGE_HANDLE_INCOMING_SOUTH = "handle_incoming_south.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing south
	 */	
	public static final String IMAGE_HANDLE_OUTGOING_SOUTH = "handle_outgoing_south.gif"; //$NON-NLS-1$
	/**
	 * the image for handle incoming north
	 */	
	public static final String IMAGE_HANDLE_INCOMING_NORTH = "handle_incoming_north.gif"; //$NON-NLS-1$
	/**
	 * the image for handle outgoing north
	 */	
	public static final String IMAGE_HANDLE_OUTGOING_NORTH = "handle_outgoing_north.gif"; //$NON-NLS-1$
	
	/**
	 * the image for note
	 */	
	public static final String DESC_NOTE = "note.gif";//$NON-NLS-1$
	/**
	 * the image for large note
	 */	
	public static final String DESC_NOTE_LARGE = "note_24x24.gif";//$NON-NLS-1$
	/**
	 * the image for text
	 */	
	public static final String DESC_TEXT = "text.gif";//$NON-NLS-1$
	/**
	 * the image for large text
	 */	
	public static final String DESC_TEXT_LARGE = "text_24x24.gif";//$NON-NLS-1$
	/**
	 * the image for note attachment
	 */	
	public static final String DESC_NOTE_ATTACHMENT = "noteattachment.gif";//$NON-NLS-1$
	/**
	 * the image for large note attachment
	 */	
	public static final String DESC_NOTE_ATTACHMENT_LARGE = "noteattachment_24x24.gif";//$NON-NLS-1$

	public static final String IMAGE_ERROR = "error.gif"; //$NON-NLS-1$ 

	public static final String IMAGE_ARRANGE_SELECTED = ENABLED_PREFIX + "arrangeselected.gif"; //$NON-NLS-1$
	public static final String IMAGE_ARRANGE_SELECTED_DISABLED = DISABLED_PREFIX + "arrangeselected.gif"; //$NON-NLS-1$
	public static final String IMAGE_ARRANGE_ALL = ENABLED_PREFIX + "arrangeall.gif"; //$NON-NLS-1$
	public static final String IMAGE_ARRANGE_ALL_DISABLED = DISABLED_PREFIX + "arrangeall.gif"; //$NON-NLS-1$

	public static final String IMAGE_SHOW_HIDE_COMPARTMENTS_GROUP = ENABLED_PREFIX +  "show_compartments_group.gif"; //$NON-NLS-1$
	public static final String IMAGE_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED = DISABLED_PREFIX + "show_compartments_group.gif"; //$NON-NLS-1$

	public static final String IMAGE_SHOW_ALL_RESIZABLE_COMPARTMENTS = "all_comp_vis.gif"; //$NON-NLS-1$
	public static final String IMAGE_HIDE_ALL_RESIZABLE_COMPARTMENTS = "none_comp_vis.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_SHOW_HIDE_CONNECTOR_LABELS_GROUP = ENABLED_PREFIX +  "show_connector_group.gif"; //$NON-NLS-1$
	public static final String IMAGE_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED = DISABLED_PREFIX + "show_connector_group.gif"; //$NON-NLS-1$

	public static final String IMAGE_CHANGEROUTER_GROUP = ENABLED_PREFIX + "line_style_group.gif"; //$NON-NLS-1$
	public static final String IMAGE_CHANGEROUTER_GROUP_DISABLED = DISABLED_PREFIX + "line_style_group.gif"; //$NON-NLS-1$

	public static final String IMAGE_CHANGEROUTERACTION_RECTILINEAR = ENABLED_PREFIX + "rectilinear.gif"; //$NON-NLS-1$
	public static final String IMAGE_CHANGEROUTERACTION_RECTILINEAR_DISABLED = DISABLED_PREFIX + "rectilinear.gif"; //$NON-NLS-1$

	public static final String IMAGE_CHANGEROUTERACTION_OBLIQUE = ENABLED_PREFIX + "oblique.gif"; //$NON-NLS-1$
	public static final String IMAGE_CHANGEROUTERACTION_OBLIQUE_DISABLED = DISABLED_PREFIX + "oblique.gif"; //$NON-NLS-1$

	public static final String IMAGE_CHANGEROUTERACTION_TREE = ENABLED_PREFIX + "tree.gif"; //$NON-NLS-1$
	public static final String IMAGE_CHANGEROUTERACTION_TREE_DISABLED = DISABLED_PREFIX + "tree.gif"; //$NON-NLS-1$

	public static final String IMAGE_AUTOSIZE = ENABLED_PREFIX + "autosize.gif"; //$NON-NLS-1$
	public static final String IMAGE_AUTOSIZE_DISABLED = DISABLED_PREFIX + "autosize.gif"; //$NON-NLS-1$

	public static final String IMAGE_ZOOM_IN = "zoomplus.gif"; //$NON-NLS-1$
	public static final String IMAGE_ZOOM_OUT = "zoomminus.gif"; //$NON-NLS-1$
	public static final String IMAGE_ZOOM_100 = "zoom100.gif"; //$NON-NLS-1$
	public static final String IMAGE_ZOOM_TOFIT = "zoomtofit.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_SELECTALL = ENABLED_PREFIX + "selectall.gif"; //$NON-NLS-1$
	public static final String IMAGE_SELECTALL_DISABLED = DISABLED_PREFIX + "selectall.gif"; //$NON-NLS-1$

	public static final String IMAGE_SELECTSHAPES = ENABLED_PREFIX + "selectshapes.gif"; //$NON-NLS-1$
	public static final String IMAGE_SELECTSHAPES_DISABLED = DISABLED_PREFIX + "selectshapes.gif"; //$NON-NLS-1$

	public static final String IMAGE_SELECTCONNECTORS = ENABLED_PREFIX + "selectconnectors.gif"; //$NON-NLS-1$	
	public static final String IMAGE_SELECTCONNECTORS_DISABLED = DISABLED_PREFIX + "selectconnectors.gif"; //$NON-NLS-1$

	public static final String IMAGE_ALIGN = "aleft.gif"; //$NON-NLS-1$

	public static final String IMAGE_OUTLINE = "outline.gif"; //$NON-NLS-1$

	public static final String IMAGE_OVERVIEW = "overview.gif"; //$NON-NLS-1$

	public static final String IMAGE_BOLD = ENABLED_PREFIX + "bold.gif"; //$NON-NLS-1$
	public static final String IMAGE_BOLD_DISABLED = DISABLED_PREFIX + "bold.gif"; //$NON-NLS-1$

	public static final String IMAGE_ITALIC = ENABLED_PREFIX + "italic.gif"; //$NON-NLS-1$
	public static final String IMAGE_ITALIC_DISABLED = DISABLED_PREFIX + "italic.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_FONT_COLOR = ENABLED_PREFIX + "font_color.gif"; //$NON-NLS-1$
	public static final String IMAGE_FONT_COLOR_DISABLED = DISABLED_PREFIX + "font_color.gif"; //$NON-NLS-1$

	public static final String IMAGE_FILL_COLOR = ENABLED_PREFIX + "fill_color.gif"; //$NON-NLS-1$
	public static final String IMAGE_FILL_COLOR_DISABLED = DISABLED_PREFIX + "fill_color.gif"; //$NON-NLS-1$

	public static final String IMAGE_LINE_COLOR = ENABLED_PREFIX + "line_color.gif"; //$NON-NLS-1$
	public static final String IMAGE_LINE_COLOR_DISABLED = DISABLED_PREFIX + "line_color.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_VIEWPAGEBREAKS = ENABLED_PREFIX + "viewpagebreaks.gif"; //$NON-NLS-1$
	public static final String IMAGE_VIEWPAGEBREAKS_DISABLED = DISABLED_PREFIX + "viewpagebreaks.gif"; //$NON-NLS-1$
	
	public static final String DESC_ACTON_RECALCPAGEBREAKS = ENABLED_PREFIX + "recalcpagebreaks.gif"; //$NON-NLS-1$
	public static final String DESC_ACTON_RECALCPAGEBREAKS_DISABLED = DISABLED_PREFIX + "recalcpagebreaks.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_POPUPBAR = "popupbar.gif"; //$NON-NLS-1$
	public static final String IMAGE_POPUPBAR_PLUS = "popupbar_plus.gif";//$NON-NLS-1$
	
	public static final String IMAGE_SNAPBACK = "snapback.gif";//$NON-NLS-1$
	
	public static final String IMAGE_SHOW_PROPERTIES_VIEW = "properties_view.gif";//$NON-NLS-1$

	public static final String IMAGE_SHOW_CONNECTOR_LABELS = ENABLED_PREFIX + "showconnector.gif"; //$NON-NLS-1$
	public static final String IMAGE_SHOW_CONNECTOR_LABELS_DISABLED = DISABLED_PREFIX + "showconnector.gif"; //$NON-NLS-1$			
	public static final String IMAGE_HIDE_CONNECTOR_LABELS = ENABLED_PREFIX + "hideconnector.gif"; //$NON-NLS-1$
	public static final String IMAGE_HIDE_CONNECTOR_LABELS_DISABLED = DISABLED_PREFIX + "hideconnector.gif"; //$NON-NLS-1$		
		
	public static final String IMAGE_SORT_FILTER = ENABLED_PREFIX + "sortfilter.gif"; //$NON-NLS-1$
	public static final String IMAGE_SORT_FILTER_DISABLED = DISABLED_PREFIX + "sortfilter.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_BRING_TO_FRONT = "bring_to_front.gif"; //$NON-NLS-1$
	public static final String IMAGE_BRING_FORWARD  = "bring_forward.gif"; //$NON-NLS-1$
	public static final String IMAGE_SEND_TO_BACK   = "send_to_back.gif"; //$NON-NLS-1$
	public static final String IMAGE_SEND_BACWARD   = "send_backward.gif"; //$NON-NLS-1$

	public static final String IMAGE_MAKE_SAME_SIZE_BOTH = "size_to_control.gif"; //$NON-NLS-1$
	public static final String IMAGE_MAKE_SAME_SIZE_HEIGHT = "size_to_control_height.gif"; //$NON-NLS-1$
	public static final String IMAGE_MAKE_SAME_SIZE_WIDTH = "size_to_control_width.gif"; //$NON-NLS-1$
	
	public static final String IMAGE_COPY_APPEARANCE = ENABLED_PREFIX + "copy_appearance_properties.gif"; //$NON-NLS-1$
	public static final String IMAGE_COPY_APPEARANCE_DISABLED = DISABLED_PREFIX + "copy_appearance_properties.gif"; //$NON-NLS-1$	
	
	/**
	 * Singleton instance for the resource manager
	 */
	private static AbstractResourceManager resourceManager =
		new DiagramResourceManager();

	/**
	 * Constructor for DiagramResourceManager.
	 */
	private DiagramResourceManager() {
		super();
	}

	/**
	 * Return singleton instance of the resource manager
	 * @return AbstractResourceManager
	 */
	public static AbstractResourceManager getInstance() {
		return resourceManager;
	}

	/**
	 * A shortcut method to get localized string
	 * @param key - resource bundle key. 
	 * @return - localized string value or a key if the bundle does not contain
	 * 			  this entry
	 */
	public static String getI18NString(String key) {
		return getInstance().getString(key);
	}


	protected void initializeResources() {
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager#initializeUIResources()
	 */
	protected void initializeUIResources() {
		initializeMessageResources();
	}

    protected Plugin getPlugin() {
        return DiagramUIPlugin.getInstance();
    }

}
