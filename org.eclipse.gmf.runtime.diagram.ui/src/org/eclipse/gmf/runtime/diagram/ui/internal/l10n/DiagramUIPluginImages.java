/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.l10n;

import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly. The public
 * strings represent images that will be cached and can be retrieved using
 * {@link #get(String)} which should <b>not</b> be disposed by the client.
 *
 * @author cmahoney
 */
public class DiagramUIPluginImages {

	/**
	 * The icons root directory.
	 */
	static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$

	/**
	 * Enabled subdirectory off of root icon directory
	 */
	private static final String PREFIX_ENABLED = PREFIX_ROOT + "elcl16/"; //$NON-NLS-1$

	/**
	 * Disabled subdirectory off of root icon directory
	 */
	private static final String PREFIX_DISABLED = PREFIX_ROOT + "dlcl16/"; //$NON-NLS-1$	

	// Cached images that can be retrieved using the get method. The
	// corresponding image descriptor must be initialized using the
	// createAndCache() method.
			
	public static final String IMG_ZOOM_IN = PREFIX_ROOT + "zoomplus.gif"; //$NON-NLS-1$
	
	public static final String IMG_HANDLE_EXPAND = PREFIX_ROOT + "collapsed.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_COLLAPSE = PREFIX_ROOT + "expanded.gif"; //$NON-NLS-1$
	
	public static final String IMG_HANDLE_INCOMING_WEST = PREFIX_ROOT + "handle_incoming_west.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_OUTGOING_WEST = PREFIX_ROOT + "handle_outgoing_west.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_INCOMING_EAST = PREFIX_ROOT + "handle_incoming_east.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_OUTGOING_EAST = PREFIX_ROOT + "handle_outgoing_east.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_INCOMING_SOUTH = PREFIX_ROOT + "handle_incoming_south.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_OUTGOING_SOUTH = PREFIX_ROOT + "handle_outgoing_south.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_INCOMING_NORTH = PREFIX_ROOT + "handle_incoming_north.gif"; //$NON-NLS-1$
	public static final String IMG_HANDLE_OUTGOING_NORTH = PREFIX_ROOT + "handle_outgoing_north.gif"; //$NON-NLS-1$

	public static final String IMG_POPUPBAR = PREFIX_ROOT + "popupbar.gif"; //$NON-NLS-1$
	public static final String IMG_POPUPBAR_PLUS = PREFIX_ROOT + "popupbar_plus.gif"; //$NON-NLS-1$

	// Image descriptors.

	public static final ImageDescriptor DESC_HANDLE_COLLAPSE = createAndCache(IMG_HANDLE_COLLAPSE);
	public static final ImageDescriptor DESC_HANDLE_EXPAND = createAndCache(IMG_HANDLE_EXPAND);
	
	public static final ImageDescriptor DESC_HANDLE_INCOMING_WEST = createAndCache(IMG_HANDLE_INCOMING_WEST);
	public static final ImageDescriptor DESC_HANDLE_OUTGOING_WEST = createAndCache(IMG_HANDLE_OUTGOING_WEST);
	public static final ImageDescriptor DESC_HANDLE_INCOMING_EAST = createAndCache(IMG_HANDLE_INCOMING_EAST);
	public static final ImageDescriptor DESC_HANDLE_OUTGOING_EAST = createAndCache(IMG_HANDLE_OUTGOING_EAST);
	public static final ImageDescriptor DESC_HANDLE_INCOMING_SOUTH = createAndCache(IMG_HANDLE_INCOMING_SOUTH);
	public static final ImageDescriptor DESC_HANDLE_OUTGOING_SOUTH = createAndCache(IMG_HANDLE_OUTGOING_SOUTH);
	public static final ImageDescriptor DESC_HANDLE_INCOMING_NORTH = createAndCache(IMG_HANDLE_INCOMING_NORTH);
	public static final ImageDescriptor DESC_HANDLE_OUTGOING_NORTH = createAndCache(IMG_HANDLE_OUTGOING_NORTH);
	
	public static final ImageDescriptor DESC_ARRANGE_SELECTED = create(PREFIX_ENABLED + "arrangeselected.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARRANGE_SELECTED_DISABLED = create(PREFIX_DISABLED + "arrangeselected.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARRANGE_ALL = create(PREFIX_ENABLED + "arrangeall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ARRANGE_ALL_DISABLED = create(PREFIX_DISABLED + "arrangeall.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SHOW_HIDE_COMPARTMENTS_GROUP = create(PREFIX_ENABLED +  "show_compartments_group.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED = create(PREFIX_DISABLED + "show_compartments_group.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SHOW_ALL_RESIZABLE_COMPARTMENTS = create(PREFIX_ROOT + "all_comp_vis.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_HIDE_ALL_RESIZABLE_COMPARTMENTS = create(PREFIX_ROOT + "none_comp_vis.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_SHOW_HIDE_CONNECTOR_LABELS_GROUP = create(PREFIX_ENABLED +  "show_connector_group.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED = create(PREFIX_DISABLED + "show_connector_group.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTER_GROUP = create(PREFIX_ENABLED + "line_style_group.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTER_GROUP_DISABLED = create(PREFIX_DISABLED + "line_style_group.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTERACTION_RECTILINEAR = create(PREFIX_ENABLED + "rectilinear.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTERACTION_RECTILINEAR_DISABLED = create(PREFIX_DISABLED + "rectilinear.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTERACTION_OBLIQUE = create(PREFIX_ENABLED + "oblique.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTERACTION_OBLIQUE_DISABLED = create(PREFIX_DISABLED + "oblique.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHANGEROUTERACTION_TREE = create(PREFIX_ENABLED + "tree.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHANGEROUTERACTION_TREE_DISABLED = create(PREFIX_DISABLED + "tree.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_AUTOSIZE = create(PREFIX_ENABLED + "autosize.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_AUTOSIZE_DISABLED = create(PREFIX_DISABLED + "autosize.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ZOOM_IN = createAndCache(IMG_ZOOM_IN);
	public static final ImageDescriptor DESC_ZOOM_OUT = create(PREFIX_ROOT + "zoomminus.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ZOOM_100 = create(PREFIX_ROOT + "zoom100.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ZOOM_TOFIT = create(PREFIX_ROOT + "zoomtofit.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_SELECTALL = create(PREFIX_ENABLED + "selectall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SELECTALL_DISABLED = create(PREFIX_DISABLED + "selectall.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SELECTSHAPES = create(PREFIX_ENABLED + "selectshapes.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SELECTSHAPES_DISABLED = create(PREFIX_DISABLED + "selectshapes.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SELECTCONNECTORS = create(PREFIX_ENABLED + "selectconnectors.gif"); //$NON-NLS-1$	
	public static final ImageDescriptor DESC_SELECTCONNECTORS_DISABLED = create(PREFIX_DISABLED + "selectconnectors.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ALIGN = create(PREFIX_ROOT + "aleft.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_OUTLINE = create(PREFIX_ROOT + "outline.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_OVERVIEW = create(PREFIX_ROOT + "overview.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_BOLD = create(PREFIX_ENABLED + "bold.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_BOLD_DISABLED = create(PREFIX_DISABLED + "bold.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ITALIC = create(PREFIX_ENABLED + "italic.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ITALIC_DISABLED = create(PREFIX_DISABLED + "italic.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_FONT_COLOR = create(PREFIX_ENABLED + "font_color.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_FONT_COLOR_DISABLED = create(PREFIX_DISABLED + "font_color.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_FILL_COLOR = create(PREFIX_ENABLED + "fill_color.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_FILL_COLOR_DISABLED = create(PREFIX_DISABLED + "fill_color.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_LINE_COLOR = create(PREFIX_ENABLED + "line_color.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_LINE_COLOR_DISABLED = create(PREFIX_DISABLED + "line_color.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_VIEWPAGEBREAKS = create(PREFIX_ENABLED + "viewpagebreaks.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_VIEWPAGEBREAKS_DISABLED = create(PREFIX_DISABLED + "viewpagebreaks.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_ACTON_RECALCPAGEBREAKS = create(PREFIX_ENABLED + "recalcpagebreaks.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_ACTON_RECALCPAGEBREAKS_DISABLED = create(PREFIX_DISABLED + "recalcpagebreaks.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_POPUPBAR = createAndCache(IMG_POPUPBAR);
	public static final ImageDescriptor DESC_POPUPBAR_PLUS = createAndCache(IMG_POPUPBAR_PLUS);
	
	public static final ImageDescriptor DESC_SNAPBACK = create(PREFIX_ROOT + "snapback.gif");//$NON-NLS-1$
	
	public static final ImageDescriptor DESC_SHOW_PROPERTIES_VIEW = create(PREFIX_ROOT + "properties_view.gif");//$NON-NLS-1$

	public static final ImageDescriptor DESC_SHOW_CONNECTOR_LABELS = create(PREFIX_ENABLED + "showconnector.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SHOW_CONNECTOR_LABELS_DISABLED = create(PREFIX_DISABLED + "showconnector.gif"); //$NON-NLS-1$			
	public static final ImageDescriptor DESC_HIDE_CONNECTOR_LABELS = create(PREFIX_ENABLED + "hideconnector.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_HIDE_CONNECTOR_LABELS_DISABLED = create(PREFIX_DISABLED + "hideconnector.gif"); //$NON-NLS-1$		
		
	public static final ImageDescriptor DESC_SORT_FILTER = create(PREFIX_ENABLED + "sortfilter.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SORT_FILTER_DISABLED = create(PREFIX_DISABLED + "sortfilter.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_MAKE_SAME_SIZE_BOTH = create(PREFIX_ROOT + "size_to_control.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_MAKE_SAME_SIZE_HEIGHT = create(PREFIX_ROOT + "size_to_control_height.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_MAKE_SAME_SIZE_WIDTH = create(PREFIX_ROOT + "size_to_control_width.gif"); //$NON-NLS-1$
	
	public static final ImageDescriptor DESC_COPY_APPEARANCE = create(PREFIX_ENABLED + PREFIX_ROOT + "copy_appearance_properties.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_COPY_APPEARANCE_DISABLED = create(PREFIX_DISABLED + PREFIX_ROOT + "copy_appearance_properties.gif"); //$NON-NLS-1$	
	
	public static final ImageDescriptor DESC_SHAPECURSOR_MASK = create(PREFIX_ENABLED + "shapecursor_mask.bmp");//$NON-NLS-1$
	public static final ImageDescriptor DESC_SHAPECURSOR_SOURCE = create(PREFIX_ENABLED + "shapecursor_source.bmp");//$NON-NLS-1$	
	
	public static final ImageDescriptor DESC_NOSHAPECURSOR_MASK = create(PREFIX_DISABLED + "noshapecursor_mask.bmp");//$NON-NLS-1$
	public static final ImageDescriptor DESC_NOSHAPECURSOR_SOURCE = create(PREFIX_DISABLED + "noshapecursor_source.bmp");//$NON-NLS-1$	
	
	public static final ImageDescriptor DESC_CONNECTION_CURSOR_MASK = create(PREFIX_ENABLED + "connectcursor_mask.bmp");//$NON-NLS-1$
	public static final ImageDescriptor DESC_CONNECTION_CURSOR_SOURCE = create(PREFIX_ENABLED + "connectcursor_source.bmp");//$NON-NLS-1$	
	public static final ImageDescriptor DESC_NO_CONNECTION_CURSOR_MASK = create(PREFIX_DISABLED + "noconnectcursor_mask.bmp");//$NON-NLS-1$
	public static final ImageDescriptor DESC_NO_CONNECTION_CURSOR_SOURCE = create(PREFIX_DISABLED + "noconnectcursor_source.bmp");//$NON-NLS-1$	

	public static final ImageDescriptor DESC_CHECKBOX_SELECTED = create(PREFIX_ROOT + "checkboxselected.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_CHECKBOX_CLEARED = create(PREFIX_ROOT + "checkboxcleared.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_UP_PATH = create(PREFIX_ROOT + "CollectionUp.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_DOWN_PATH = create(PREFIX_ROOT + "CollectionDown.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SORT_ARROW_UP = create(PREFIX_ROOT + "sm_arrow_up.gif"); //$NON-NLS-1$
	public static final ImageDescriptor DESC_SORT_ARROW_DN = create(PREFIX_ROOT + "sm_arrow_dn.gif"); //$NON-NLS-1$
	
	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(DiagramUIPlugin
			.getPluginId(), imageName);
	}
	
	/**
	 * Creates the image descriptor from the filename given and caches it in the
	 * plugin's image registry.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor createAndCache(String imageName) {
		ImageDescriptor result = create(imageName);
		DiagramUIPlugin.getInstance().getImageRegistry().put(imageName, result);
		return result;
	}
	
	/**
	 * Gets an image from the image registry. This image should not be disposed
	 * of, that is handled in the image registry. The image descriptor must have
	 * previously been cached in the image registry. The cached images for the
	 * public image names defined in this file can be retrieved using this
	 * method.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the image or null if it has not been cached in the registry
	 */
	public static Image get(String imageName) {
		return DiagramUIPlugin.getInstance().getImageRegistry().get(imageName);
	}

}
