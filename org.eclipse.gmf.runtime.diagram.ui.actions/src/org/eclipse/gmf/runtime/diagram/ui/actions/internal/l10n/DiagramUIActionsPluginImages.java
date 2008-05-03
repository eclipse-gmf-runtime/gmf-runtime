/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n;

import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly. The public
 * strings represent images that will be cached and can be retrieved using
 * {@link #get(String)} which should <b>not</b> be disposed by the client.
 *
 * @author cmahoney
 */
public class DiagramUIActionsPluginImages {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$

	/**
	 * Enabled subdirectory off of root icon directory
	 */
	private static final String PREFIX_ENABLED = PREFIX_ROOT + "elcl16/"; //$NON-NLS-1$

	/**
	 * Disabled subdirectory off of root icon directory
	 */
	private static final String PREFIX_DISABLED = PREFIX_ROOT + "dlcl16/"; //$NON-NLS-1$

	// Image descriptors.

	public static final ImageDescriptor DESC_NOTE_ATTACHMENT = create(PREFIX_ROOT + "noteattachment.gif");//$NON-NLS-1$
	
	public static final ImageDescriptor DESC_ARRANGE_SELECTED = create(PREFIX_ENABLED
		+ "arrangeselected.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARRANGE_SELECTED_DISABLED = create(PREFIX_DISABLED
		+ "arrangeselected.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ARRANGE_ALL = create(PREFIX_ENABLED
		+ "arrangeall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARRANGE_ALL_DISABLED = create(PREFIX_DISABLED
		+ "arrangeall.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SHOW_HIDE_COMPARTMENTS_GROUP = create(PREFIX_ENABLED
		+ "show_compartments_group.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED = create(PREFIX_DISABLED
		+ "show_compartments_group.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_SHOW_ALL_RESIZABLE_COMPARTMENTS = create(PREFIX_ROOT + "all_comp_vis.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_HIDE_ALL_RESIZABLE_COMPARTMENTS = create(PREFIX_ROOT + "none_comp_vis.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SHOW_HIDE_CONNECTION_LABELS_GROUP = create(PREFIX_ENABLED
		+ "show_connector_group.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SHOW_HIDE_CONNECTION_LABELS_GROUP_DISABLED = create(PREFIX_DISABLED
		+ "show_connector_group.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTER_GROUP = create(PREFIX_ENABLED
		+ "line_style_group.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTER_GROUP_DISABLED = create(PREFIX_DISABLED
		+ "line_style_group.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTERACTION_RECTILINEAR = create(PREFIX_ENABLED
		+ "rectilinear.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTERACTION_RECTILINEAR_DISABLED = create(PREFIX_DISABLED
		+ "rectilinear.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTERACTION_OBLIQUE = create(PREFIX_ENABLED
		+ "oblique.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTERACTION_OBLIQUE_DISABLED = create(PREFIX_DISABLED
		+ "oblique.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTERACTION_TREE = create(PREFIX_ENABLED
		+ "tree.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTERACTION_TREE_DISABLED = create(PREFIX_DISABLED
		+ "tree.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_AUTOSIZE = create(PREFIX_ENABLED + "autosize.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_AUTOSIZE_DISABLED = create(PREFIX_DISABLED
		+ "autosize.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SELECTALL = create(PREFIX_ENABLED
		+ "selectall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SELECTALL_DISABLED = create(PREFIX_DISABLED
		+ "selectall.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SELECTSHAPES = create(PREFIX_ENABLED
		+ "selectshapes.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SELECTSHAPES_DISABLED = create(PREFIX_DISABLED
		+ "selectshapes.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SELECTCONNECTIONS = create(PREFIX_ENABLED
		+ "selectconnectors.gif"); //$NON-NLS-1$	
	public static final ImageDescriptor DESC_SELECTCONNECTIONS_DISABLED = create(PREFIX_DISABLED
		+ "selectconnectors.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ALIGN = create(PREFIX_ROOT + "aleft.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_BOLD = create(PREFIX_ENABLED + "bold.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_BOLD_DISABLED = create(PREFIX_DISABLED
		+ "bold.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ITALIC = create(PREFIX_ENABLED + "italic.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ITALIC_DISABLED = create(PREFIX_DISABLED
		+ "italic.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_FONT_COLOR = create(PREFIX_ENABLED
		+ "font_color.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_FONT_COLOR_DISABLED = create(PREFIX_DISABLED
		+ "font_color.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_FILL_COLOR = create(PREFIX_ENABLED
		+ "fill_color.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_FILL_COLOR_DISABLED = create(PREFIX_DISABLED
		+ "fill_color.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_LINE_COLOR = create(PREFIX_ENABLED
		+ "line_color.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_COLOR_DISABLED = create(PREFIX_DISABLED
		+ "line_color.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_VIEWPAGEBREAKS = create(PREFIX_ENABLED
		+ "viewpagebreaks.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_VIEWPAGEBREAKS_DISABLED = create(PREFIX_DISABLED
		+ "viewpagebreaks.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_RECALCPAGEBREAKS = create(PREFIX_ENABLED
		+ "recalcpagebreaks.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_RECALCPAGEBREAKS_DISABLED = create(PREFIX_DISABLED
		+ "recalcpagebreaks.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SNAPBACK = create(PREFIX_ROOT + "snapback.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SHOW_CONNECTION_LABELS = create(PREFIX_ENABLED
		+ "showconnector.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SHOW_CONNECTION_LABELS_DISABLED = create(PREFIX_DISABLED
		+ "showconnector.gif"); //$NON-NLS-1$			
	public static final ImageDescriptor DESC_HIDE_CONNECTION_LABELS = create(PREFIX_ENABLED
		+ "hideconnector.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_HIDE_CONNECTION_LABELS_DISABLED = create(PREFIX_DISABLED
		+ "hideconnector.gif"); //$NON-NLS-1$		

	public static final ImageDescriptor DESC_SORT_FILTER = create(PREFIX_ENABLED
		+ "sortfilter.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SORT_FILTER_DISABLED = create(PREFIX_DISABLED
		+ "sortfilter.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_BRING_TO_FRONT = create(PREFIX_ENABLED + "bring_to_front.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_BRING_FORWARD = create(PREFIX_ENABLED + "bring_forward.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SEND_TO_BACK = create(PREFIX_ENABLED + "send_to_back.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SEND_BACWARD = create(PREFIX_ENABLED + "send_backward.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_MAKE_SAME_SIZE_BOTH = create(PREFIX_ROOT + "size_to_control.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_MAKE_SAME_SIZE_HEIGHT = create(PREFIX_ROOT + "size_to_control_height.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_MAKE_SAME_SIZE_WIDTH = create(PREFIX_ROOT + "size_to_control_width.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_COPY_APPEARANCE = create(PREFIX_ENABLED
		+ "copy_appearance_properties.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_COPY_APPEARANCE_DISABLED = create(PREFIX_DISABLED
		+ "copy_appearance_properties.gif"); //$NON-NLS-1$	

    public static final ImageDescriptor DESC_GROUP = create(PREFIX_ENABLED + "group.gif"); //$NON-NLS-1$
    public static final ImageDescriptor DESC_GROUP_DISABLED = create(PREFIX_DISABLED + "group.gif"); //$NON-NLS-1$
    public static final ImageDescriptor DESC_UNGROUP = create(PREFIX_ENABLED + "ungroup.gif"); //$NON-NLS-1$
    public static final ImageDescriptor DESC_UNGROUP_DISABLED = create(PREFIX_DISABLED + "ungroup.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_LEFT = create(PREFIX_ENABLED
			+ "alignleft.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_LEFT_DISABLED = create(PREFIX_DISABLED
			+ "alignleft.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_CENTER = create(PREFIX_ENABLED
			+ "aligncenter.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_CENTER_DISABLED = create(PREFIX_DISABLED
			+ "aligncenter.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_RIGHT = create(PREFIX_ENABLED
			+ "alignright.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_RIGHT_DISABLED = create(PREFIX_DISABLED
			+ "alignright.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_LINE_WIDTH = create(PREFIX_ENABLED
			+ "line_width.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_DISABLED = create(PREFIX_DISABLED
			+ "line_width.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_ONE = create(PREFIX_ENABLED
			+ "line_width_one.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_ONE_DISABLED = create(PREFIX_DISABLED
			+ "line_width_one.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_TWO = create(PREFIX_ENABLED
			+ "line_width_two.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_TWO_DISABLED = create(PREFIX_DISABLED
			+ "line_width_two.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_THREE = create(PREFIX_ENABLED
			+ "line_width_three.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_THREE_DISABLED = create(PREFIX_DISABLED
			+ "line_width_three.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_FOUR = create(PREFIX_ENABLED
			+ "line_width_four.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_FOUR_DISABLED = create(PREFIX_DISABLED
			+ "line_width_four.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_FIVE = create(PREFIX_ENABLED
			+ "line_width_five.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_WIDTH_FIVE_DISABLED = create(PREFIX_DISABLED
			+ "line_width_five.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_ARROW_TYPE = create(PREFIX_ENABLED
			+ "arrow_type.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_DISABLED = create(PREFIX_DISABLED
			+ "arrow_type.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_NONE = create(PREFIX_ENABLED
			+ "arrow_type_none.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_NONE_DISABLED = create(PREFIX_DISABLED
			+ "arrow_type_none.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_OPEN = create(PREFIX_ENABLED
			+ "arrow_type_open.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_OPEN_DISABLED = create(PREFIX_DISABLED
			+ "arrow_type_open.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_SOLID = create(PREFIX_ENABLED
			+ "arrow_type_solid.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARROW_TYPE_SOLID_DISABLED = create(PREFIX_DISABLED
			+ "arrow_type_solid.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_LINE_TYPE = create(PREFIX_ENABLED
			+ "line_type.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DISABLED = create(PREFIX_DISABLED
			+ "line_type.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_SOLID = create(PREFIX_ENABLED
			+ "line_type_solid.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_SOLID_DISABLED = create(PREFIX_DISABLED
			+ "line_type_solid.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DASH = create(PREFIX_ENABLED
			+ "line_type_dash.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DASH_DISABLED = create(PREFIX_DISABLED
			+ "line_type_dash.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DOT = create(PREFIX_ENABLED
			+ "line_type_dot.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DOT_DISABLED = create(PREFIX_DISABLED
			+ "line_type_dot.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DASH_DOT = create(PREFIX_ENABLED
			+ "line_type_dash_dot.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DASH_DOT_DISABLED = create(PREFIX_DISABLED
			+ "line_type_dash_dot.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DASH_DOT_DOT = create(PREFIX_ENABLED
			+ "line_type_dash_dot_dot.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_TYPE_DASH_DOT_DOT_DISABLED = create(PREFIX_DISABLED
			+ "line_type_dash_dot_dot.gif"); //$NON-NLS-1$

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(DiagramActionsPlugin
			.getPluginId(), imageName);
	}
}
