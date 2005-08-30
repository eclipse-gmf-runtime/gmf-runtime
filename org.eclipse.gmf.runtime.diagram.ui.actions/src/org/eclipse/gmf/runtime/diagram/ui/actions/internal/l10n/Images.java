/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.actions.*
 */
public class Images {

	/**
	 * Enabled subdirectory off of root icon directory
	 */
	private static final String ENABLED_PREFIX = "elcl16/"; //$NON-NLS-1$

	/**
	 * Disabled subdirectory off of root icon directory
	 */
	private static final String DISABLED_PREFIX = "dlcl16/"; //$NON-NLS-1$

	// shared images start with ICON_
	public static final Image ICON_BLANK_LISTITEM;

	public static final Image ICON_ERROR;

	public static final ImageDescriptor DESC_ACTION_ALIGN;

	public static final ImageDescriptor DESC_ACTION_HIDE_ALL_RESIZABLE_COMPARTMENTS;
	public static final ImageDescriptor DESC_ACTION_SHOW_ALL_RESIZABLE_COMPARTMENTS;

	public static final ImageDescriptor DESC_ACTION_ARRANGE_ALL;

	public static final ImageDescriptor DESC_ACTION_ARRANGE_ALL_DISABLED;

	public static final ImageDescriptor DESC_ACTION_ARRANGE_SELECTED;

	public static final ImageDescriptor DESC_ACTION_ARRANGE_SELECTED_DISABLED;

	public static final ImageDescriptor DESC_ACTION_AUTOSIZE;

	public static final ImageDescriptor DESC_ACTION_AUTOSIZE_DISABLED;

	public static final ImageDescriptor DESC_ACTION_FILL_COLOR;

	public static final ImageDescriptor DESC_ACTION_FILL_COLOR_DISABLED;

	public static final ImageDescriptor DESC_ACTION_FONT_COLOR;

	public static final ImageDescriptor DESC_ACTION_FONT_COLOR_DISABLED;

	public static final ImageDescriptor DESC_ACTION_LINE_COLOR;

	public static final ImageDescriptor DESC_ACTION_LINE_COLOR_DISABLED;

	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP;

	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED;

	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP;

	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED;

	public static final ImageDescriptor DESC_ACTION_COPY_APPEARANCE;

	public static final ImageDescriptor DESC_ACTION_COPY_APPEARANCE_DISABLED;

	public static final ImageDescriptor DESC_ACTION_BOLD;

	public static final ImageDescriptor DESC_ACTION_BOLD_DISABLED;

	public static final ImageDescriptor DESC_ACTION_ITALIC;

	public static final ImageDescriptor DESC_ACTION_ITALIC_DISABLED;

	public static final ImageDescriptor DESC_ACTION_SHOW_CONNECTOR_LABELS;

	public static final ImageDescriptor DESC_ACTION_SHOW_CONNECTOR_LABELS_DISABLED;

	public static final ImageDescriptor DESC_ACTION_HIDE_CONNECTOR_LABELS;

	public static final ImageDescriptor DESC_ACTION_HIDE_CONNECTOR_LABELS_DISABLED;

	public static final ImageDescriptor DESC_ACTION_VIEWPAGEBREAKS;

	public static final ImageDescriptor DESC_ACTION_VIEWPAGEBREAKS_DISABLED;

	public static final ImageDescriptor DESC_ACTON_RECALCPAGEBREAKS;

	public static final ImageDescriptor DESC_ACTON_RECALCPAGEBREAKS_DISABLED;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTER_GROUP;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTER_GROUP_DISABLED;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR_DISABLED;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_OBLIQUE;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_OBLIQUE_DISABLED;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_TREE;

	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_TREE_DISABLED;

	public static final ImageDescriptor DESC_ACTION_SELECTALL;

	public static final ImageDescriptor DESC_ACTION_SELECTALL_DISABLED;

	public static final ImageDescriptor DESC_ACTION_SELECTSHAPES;

	public static final ImageDescriptor DESC_ACTION_SELECTSHAPES_DISABLED;

	public static final ImageDescriptor DESC_ACTION_SELECTCONNECTORS;

	public static final ImageDescriptor DESC_ACTION_SELECTCONNECTORS_DISABLED;

	public static final ImageDescriptor DESC_ACTION_MAKE_SAME_SIZE_BOTH;

	public static final ImageDescriptor DESC_ACTION_MAKE_SAME_SIZE_HEIGHT;

	public static final ImageDescriptor DESC_ACTION_MAKE_SAME_SIZE_WIDTH;

	public static final ImageDescriptor DESC_ACTION_SNAPBACK;

	public static final ImageDescriptor DESC_ACTION_SORT_FILTER;

	public static final ImageDescriptor DESC_ACTION_SORT_FILTER_DISABLED;

	public static final ImageDescriptor DESC_ACTION_BRING_TO_FRONT;

	public static final ImageDescriptor DESC_ACTION_BRING_FORWARD;

	public static final ImageDescriptor DESC_ACTION_SEND_TO_BACK;

	public static final ImageDescriptor DESC_ACTION_SEND_BACWARD;

	static {
		// shared images
		ICON_BLANK_LISTITEM = create("blank.gif"); //$NON-NLS-1$ 

		ICON_ERROR = create("error.gif"); //$NON-NLS-1$
		

		DESC_ACTION_ARRANGE_SELECTED = createDescriptor(ENABLED_PREFIX
			+ "arrangeselected.gif"); //$NON-NLS-1$
		DESC_ACTION_ARRANGE_SELECTED_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "arrangeselected.gif"); //$NON-NLS-1$

		DESC_ACTION_ARRANGE_ALL = createDescriptor(ENABLED_PREFIX
			+ "arrangeall.gif"); //$NON-NLS-1$
		DESC_ACTION_ARRANGE_ALL_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "arrangeall.gif"); //$NON-NLS-1$

		DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP = createDescriptor(ENABLED_PREFIX
			+ "show_compartments_group.gif"); //$NON-NLS-1$
		DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "show_compartments_group.gif"); //$NON-NLS-1$
		
		DESC_ACTION_SHOW_ALL_RESIZABLE_COMPARTMENTS = createDescriptor("all_comp_vis.gif"); //$NON-NLS-1$

		DESC_ACTION_HIDE_ALL_RESIZABLE_COMPARTMENTS = createDescriptor("none_comp_vis.gif"); //$NON-NLS-1$

		DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP = createDescriptor(ENABLED_PREFIX
			+ "show_connector_group.gif"); //$NON-NLS-1$
		DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "show_connector_group.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTER_GROUP = createDescriptor(ENABLED_PREFIX
			+ "line_style_group.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTER_GROUP_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "line_style_group.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR = createDescriptor(ENABLED_PREFIX
			+ "rectilinear.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "rectilinear.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTERACTION_OBLIQUE = createDescriptor(ENABLED_PREFIX
			+ "oblique.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTERACTION_OBLIQUE_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "oblique.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTERACTION_TREE = createDescriptor(ENABLED_PREFIX
			+ "tree.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTERACTION_TREE_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "tree.gif"); //$NON-NLS-1$

		DESC_ACTION_AUTOSIZE = createDescriptor(ENABLED_PREFIX + "autosize.gif"); //$NON-NLS-1$
		DESC_ACTION_AUTOSIZE_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "autosize.gif"); //$NON-NLS-1$

		DESC_ACTION_SELECTALL = createDescriptor(ENABLED_PREFIX
			+ "selectall.gif"); //$NON-NLS-1$
		DESC_ACTION_SELECTALL_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "selectall.gif"); //$NON-NLS-1$

		DESC_ACTION_SELECTSHAPES = createDescriptor(ENABLED_PREFIX
			+ "selectshapes.gif"); //$NON-NLS-1$
		DESC_ACTION_SELECTSHAPES_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "selectshapes.gif"); //$NON-NLS-1$

		DESC_ACTION_SELECTCONNECTORS = createDescriptor(ENABLED_PREFIX
			+ "selectconnectors.gif"); //$NON-NLS-1$	
		DESC_ACTION_SELECTCONNECTORS_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "selectconnectors.gif"); //$NON-NLS-1$

		DESC_ACTION_ALIGN = createDescriptor("aleft.gif"); //$NON-NLS-1$

		DESC_ACTION_BOLD = createDescriptor(ENABLED_PREFIX + "bold.gif"); //$NON-NLS-1$
		DESC_ACTION_BOLD_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "bold.gif"); //$NON-NLS-1$

		DESC_ACTION_ITALIC = createDescriptor(ENABLED_PREFIX + "italic.gif"); //$NON-NLS-1$
		DESC_ACTION_ITALIC_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "italic.gif"); //$NON-NLS-1$

		DESC_ACTION_FONT_COLOR = createDescriptor(ENABLED_PREFIX
			+ "font_color.gif"); //$NON-NLS-1$
		DESC_ACTION_FONT_COLOR_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "font_color.gif"); //$NON-NLS-1$

		DESC_ACTION_FILL_COLOR = createDescriptor(ENABLED_PREFIX
			+ "fill_color.gif"); //$NON-NLS-1$
		DESC_ACTION_FILL_COLOR_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "fill_color.gif"); //$NON-NLS-1$

		DESC_ACTION_LINE_COLOR = createDescriptor(ENABLED_PREFIX
			+ "line_color.gif"); //$NON-NLS-1$
		DESC_ACTION_LINE_COLOR_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "line_color.gif"); //$NON-NLS-1$

		DESC_ACTION_VIEWPAGEBREAKS = createDescriptor(ENABLED_PREFIX
			+ "viewpagebreaks.gif"); //$NON-NLS-1$
		DESC_ACTION_VIEWPAGEBREAKS_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "viewpagebreaks.gif"); //$NON-NLS-1$

		DESC_ACTON_RECALCPAGEBREAKS = createDescriptor(ENABLED_PREFIX
			+ "recalcpagebreaks.gif"); //$NON-NLS-1$
		DESC_ACTON_RECALCPAGEBREAKS_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "recalcpagebreaks.gif"); //$NON-NLS-1$

		DESC_ACTION_SNAPBACK = createDescriptor("snapback.gif");//$NON-NLS-1$

		DESC_ACTION_SHOW_CONNECTOR_LABELS = createDescriptor(ENABLED_PREFIX
			+ "showconnector.gif"); //$NON-NLS-1$
		DESC_ACTION_SHOW_CONNECTOR_LABELS_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "showconnector.gif"); //$NON-NLS-1$			
		DESC_ACTION_HIDE_CONNECTOR_LABELS = createDescriptor(ENABLED_PREFIX
			+ "hideconnector.gif"); //$NON-NLS-1$
		DESC_ACTION_HIDE_CONNECTOR_LABELS_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "hideconnector.gif"); //$NON-NLS-1$		

		DESC_ACTION_SORT_FILTER = createDescriptor(ENABLED_PREFIX
			+ "sortfilter.gif"); //$NON-NLS-1$
		DESC_ACTION_SORT_FILTER_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "sortfilter.gif"); //$NON-NLS-1$

		DESC_ACTION_BRING_TO_FRONT = createDescriptor("bring_to_front.gif"); //$NON-NLS-1$
		DESC_ACTION_BRING_FORWARD = createDescriptor("bring_forward.gif"); //$NON-NLS-1$
		DESC_ACTION_SEND_TO_BACK = createDescriptor("send_to_back.gif"); //$NON-NLS-1$
		DESC_ACTION_SEND_BACWARD = createDescriptor("send_backward.gif"); //$NON-NLS-1$

		DESC_ACTION_MAKE_SAME_SIZE_BOTH = createDescriptor("size_to_control.gif"); //$NON-NLS-1$
		DESC_ACTION_MAKE_SAME_SIZE_HEIGHT = createDescriptor("size_to_control_height.gif"); //$NON-NLS-1$
		DESC_ACTION_MAKE_SAME_SIZE_WIDTH = createDescriptor("size_to_control_width.gif"); //$NON-NLS-1$

		DESC_ACTION_COPY_APPEARANCE = createDescriptor(ENABLED_PREFIX
			+ "copy_appearance_properties.gif"); //$NON-NLS-1$
		DESC_ACTION_COPY_APPEARANCE_DISABLED = createDescriptor(DISABLED_PREFIX
			+ "copy_appearance_properties.gif"); //$NON-NLS-1$	

	}

	static private Image create(String filename) {
		return DiagramActionsResourceManager.getInstance()
			.createImage(filename);
	}

	static private ImageDescriptor createDescriptor(String filename) {
		return DiagramActionsResourceManager.getInstance()
			.createImageDescriptor(filename);
	}
}