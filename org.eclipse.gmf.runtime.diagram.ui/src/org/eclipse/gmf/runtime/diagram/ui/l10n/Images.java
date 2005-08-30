/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.l10n;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/*
 * @canBeSeenBy %partners
 */
/**
 * this class will define the icons and image descriptos for actions
 * @author sshaw
 *
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
	
	/**
	 * icon for blank list item
	 */
	public static final Image ICON_BLANK_LISTITEM;
	/**
	 * the error icon
	 */
	public static final Image ICON_ERROR;
	/**
	 * Image descriptor for action arrange selected
	 */
	public static final ImageDescriptor DESC_ACTION_ARRANGE_SELECTED;
    /**
	 * Image descriptor for action arrange selected disabled
	 */
	public static final ImageDescriptor DESC_ACTION_ARRANGE_SELECTED_DISABLED;
    /**
	 * Image descriptor for action arrange all
	 */
	public static final ImageDescriptor DESC_ACTION_ARRANGE_ALL;
    /**
	 * Image descriptor for action arrange all disabled
	 */
	public static final ImageDescriptor DESC_ACTION_ARRANGE_ALL_DISABLED;
    /**
	 * Image descriptor for action show hide copartments group
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP;
	/**
	 * Image descriptor for action show hide copartments group disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED;	

	/**
	 * Image descriptor for action show all resizable compartments
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_ALL_RESIZABLE_COMPARTMENTS;
	/**
	 * Image descriptor for action hide all resizable compartments;
	 */
	public static final ImageDescriptor DESC_ACTION_HIDE_ALL_RESIZABLE_COMPARTMENTS;
	/**
	 * Image descriptor for action show hide connector labels group
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP;
	/**
	 * Image descriptor for action show hide connector labels group disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED;
	/**
	 * Image descriptor for action changerouter group
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTER_GROUP;
	/**
	 * Image descriptor for action changerouter group disabled
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTER_GROUP_DISABLED;
	/**
	 * Image descriptor for action changerouteraction rectilinear
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR;
	/**
	 * Image descriptor for action changerouteraction rectilinear disabled
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR_DISABLED;
	/**
	 * Image descriptor for action changerouteraction oblique
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_OBLIQUE;
	/**
	 * Image descriptor for action changerouteraction oblique_disabled
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_OBLIQUE_DISABLED;
	/**
	 * Image descriptor for action changerouteraction tree
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_TREE;
	/**
	 * Image descriptor for action changerouteraction tree disabled
	 */
	public static final ImageDescriptor DESC_ACTION_CHANGEROUTERACTION_TREE_DISABLED;
	/**
	 * Image descriptor for action autosize
	 */
	public static final ImageDescriptor DESC_ACTION_AUTOSIZE;
	/**
	 * Image descriptor for action autosize disabled
	 */
	public static final ImageDescriptor DESC_ACTION_AUTOSIZE_DISABLED;
	/**
	 * Image descriptor for action zoom in
	 */
	public static final ImageDescriptor DESC_ACTION_ZOOM_IN;
	/**
	 * Image descriptor for action zoom out
	 */
	public static final ImageDescriptor DESC_ACTION_ZOOM_OUT;
	/**
	 * Image descriptor for action zoom 100
	 */
	public static final ImageDescriptor DESC_ACTION_ZOOM_100;
	/**
	 * Image descriptor for action zoom to fit
	 */
	public static final ImageDescriptor DESC_ACTION_ZOOM_TOFIT;
	/**
	 * Image descriptor for action select all
	 */
	public static final ImageDescriptor DESC_ACTION_SELECTALL;
	/**
	 * Image descriptor for action select all disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SELECTALL_DISABLED;
	/**
	 * Image descriptor for action select shapes
	 */
	public static final ImageDescriptor DESC_ACTION_SELECTSHAPES;
	/**
	 * Image descriptor for action select shapes disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SELECTSHAPES_DISABLED;
	/**
	 * Image descriptor for action select connectors
	 */
	public static final ImageDescriptor DESC_ACTION_SELECTCONNECTORS;
	/**
	 * Image descriptor for action select connectors disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SELECTCONNECTORS_DISABLED;
	/**
	 * Image descriptor for action align
	 */
	public static final ImageDescriptor DESC_ACTION_ALIGN;
	/**
	 * Image descriptor for action outline
	 */
	public static final ImageDescriptor DESC_ACTION_OUTLINE;
	/**
	 * Image descriptor for action overview
	 */
	public static final ImageDescriptor DESC_ACTION_OVERVIEW;
	/**
	 * Image descriptor for action bold
	 */
	public static final ImageDescriptor DESC_ACTION_BOLD;
	/**
	 * Image descriptor for action bold disabled
	 */
	public static final ImageDescriptor DESC_ACTION_BOLD_DISABLED;
	/**
	 * Image descriptor for action italic
	 */
	public static final ImageDescriptor DESC_ACTION_ITALIC;
	/**
	 * Image descriptor for action italic disabled
	 */
	public static final ImageDescriptor DESC_ACTION_ITALIC_DISABLED;
	/**
	 * Image descriptor for action font color
	 */
	public static final ImageDescriptor DESC_ACTION_FONT_COLOR;
	/**
	 * Image descriptor for action font color disabled
	 */
	public static final ImageDescriptor DESC_ACTION_FONT_COLOR_DISABLED;
	/**
	 * Image descriptor for action fill color
	 */
	public static final ImageDescriptor DESC_ACTION_FILL_COLOR;
	/**
	 * Image descriptor for action fill color disabled
	 */
	public static final ImageDescriptor DESC_ACTION_FILL_COLOR_DISABLED;
	/**
	 * Image descriptor for action line color
	 */
	public static final ImageDescriptor DESC_ACTION_LINE_COLOR;
	/**
	 * Image descriptor for action line color disabled
	 */
	public static final ImageDescriptor DESC_ACTION_LINE_COLOR_DISABLED;
	/**
	 * Image descriptor for action view page breaks disabled
	 */
	public static final ImageDescriptor DESC_ACTION_VIEWPAGEBREAKS;	
	/**
	 * Image descriptor for action view page breaks disabled
	 */
	public static final ImageDescriptor DESC_ACTION_VIEWPAGEBREAKS_DISABLED;	
	/**
	 * Image descriptor for action recalc page breaks disabled
	 */
	public static final ImageDescriptor DESC_ACTON_RECALCPAGEBREAKS;	
	/**
	 * Image descriptor for action recalc page breaks disabled
	 */
	public static final ImageDescriptor DESC_ACTON_RECALCPAGEBREAKS_DISABLED;
	/**
	 * Image descriptor for action action bar
	 */
	public static final ImageDescriptor DESC_ACTION_ACTIONBAR;
	/**
	 * Image descriptor for action action bar inv
	 */
	public static final ImageDescriptor DESC_ACTION_ACTIONBAR_INV;
	/**
	 * Image descriptor for action snape back
	 */
	public static final ImageDescriptor DESC_ACTION_SNAPBACK;
	/**
	 * Image descriptor for action actionbar plus
	 */
	public static final ImageDescriptor DESC_ACTION_ACTIONBAR_PLUS;
	/**
	 * Image descriptor for action show peoperties view
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_PROPERTIES_VIEW;
	/**
	 * Image descriptor for action show connector labels
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_CONNECTOR_LABELS;
	/**
	 * Image descriptor for action show connector labels disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SHOW_CONNECTOR_LABELS_DISABLED;	
	/**
	 * Image descriptor for action hide connector labels
	 */
	public static final ImageDescriptor DESC_ACTION_HIDE_CONNECTOR_LABELS;
	/**
	 * Image descriptor for action hide connector labels disabled
	 */
	public static final ImageDescriptor DESC_ACTION_HIDE_CONNECTOR_LABELS_DISABLED;	
	/**
	 * Image descriptor for action copy appearance
	 */
	public static final ImageDescriptor DESC_ACTION_COPY_APPEARANCE;
	/**
	 * Image descriptor for action copy appearance disabled
	 */
	public static final ImageDescriptor DESC_ACTION_COPY_APPEARANCE_DISABLED;	
	
	/**
	 * Image descriptor for action bring to front
	 */
	public static final ImageDescriptor DESC_ACTION_BRING_TO_FRONT;
	/**
	 * Image descriptor for action bring forward
	 */
	public static final ImageDescriptor DESC_ACTION_BRING_FORWARD;
	/**
	 * Image descriptor for action send to back
	 */
	public static final ImageDescriptor DESC_ACTION_SEND_TO_BACK;
	/**
	 * Image descriptor for action send backward
	 */
	public static final ImageDescriptor DESC_ACTION_SEND_BACWARD;

	/**
	 * Image descriptor for action make same size both
	 */
	public static final ImageDescriptor DESC_ACTION_MAKE_SAME_SIZE_BOTH;
	/**
	 * Image descriptor for action make same size height
	 */
	public static final ImageDescriptor DESC_ACTION_MAKE_SAME_SIZE_HEIGHT;
	/**
	 * Image descriptor for action make same size width
	 */
	public static final ImageDescriptor DESC_ACTION_MAKE_SAME_SIZE_WIDTH;
	
	/**
	 * Image descriptor for action sort filter
	 */
	public static final ImageDescriptor DESC_ACTION_SORT_FILTER;
	/**
	 * Image descriptor for action sort filter disabled
	 */
	public static final ImageDescriptor DESC_ACTION_SORT_FILTER_DISABLED;


	static {
		// shared images
		ICON_BLANK_LISTITEM = create("blank.gif"); //$NON-NLS-1$ 

		ICON_ERROR = create("error.gif"); //$NON-NLS-1$ 

		DESC_ACTION_ARRANGE_SELECTED = createDescriptor(ENABLED_PREFIX + "arrangeselected.gif"); //$NON-NLS-1$
		DESC_ACTION_ARRANGE_SELECTED_DISABLED = createDescriptor(DISABLED_PREFIX + "arrangeselected.gif"); //$NON-NLS-1$

		DESC_ACTION_ARRANGE_ALL = createDescriptor(ENABLED_PREFIX + "arrangeall.gif"); //$NON-NLS-1$
		DESC_ACTION_ARRANGE_ALL_DISABLED = createDescriptor(DISABLED_PREFIX + "arrangeall.gif"); //$NON-NLS-1$

		DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP = createDescriptor(ENABLED_PREFIX +  "show_compartments_group.gif"); //$NON-NLS-1$
		DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED = createDescriptor(DISABLED_PREFIX + "show_compartments_group.gif"); //$NON-NLS-1$

		DESC_ACTION_SHOW_ALL_RESIZABLE_COMPARTMENTS = createDescriptor("all_comp_vis.gif"); //$NON-NLS-1$

		DESC_ACTION_HIDE_ALL_RESIZABLE_COMPARTMENTS = createDescriptor("none_comp_vis.gif"); //$NON-NLS-1$
		
		DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP = createDescriptor(ENABLED_PREFIX +  "show_connector_group.gif"); //$NON-NLS-1$
		DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED = createDescriptor(DISABLED_PREFIX + "show_connector_group.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTER_GROUP = createDescriptor(ENABLED_PREFIX + "line_style_group.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTER_GROUP_DISABLED = createDescriptor(DISABLED_PREFIX + "line_style_group.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR = createDescriptor(ENABLED_PREFIX + "rectilinear.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTERACTION_RECTILINEAR_DISABLED = createDescriptor(DISABLED_PREFIX + "rectilinear.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTERACTION_OBLIQUE = createDescriptor(ENABLED_PREFIX + "oblique.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTERACTION_OBLIQUE_DISABLED = createDescriptor(DISABLED_PREFIX + "oblique.gif"); //$NON-NLS-1$

		DESC_ACTION_CHANGEROUTERACTION_TREE = createDescriptor(ENABLED_PREFIX + "tree.gif"); //$NON-NLS-1$
		DESC_ACTION_CHANGEROUTERACTION_TREE_DISABLED = createDescriptor(DISABLED_PREFIX + "tree.gif"); //$NON-NLS-1$

		DESC_ACTION_AUTOSIZE = createDescriptor(ENABLED_PREFIX + "autosize.gif"); //$NON-NLS-1$
		DESC_ACTION_AUTOSIZE_DISABLED = createDescriptor(DISABLED_PREFIX + "autosize.gif"); //$NON-NLS-1$

		DESC_ACTION_ZOOM_IN = createDescriptor("zoomplus.gif"); //$NON-NLS-1$
		DESC_ACTION_ZOOM_OUT = createDescriptor("zoomminus.gif"); //$NON-NLS-1$
		DESC_ACTION_ZOOM_100 = createDescriptor("zoom100.gif"); //$NON-NLS-1$
		DESC_ACTION_ZOOM_TOFIT = createDescriptor("zoomtofit.gif"); //$NON-NLS-1$
		
		DESC_ACTION_SELECTALL = createDescriptor(ENABLED_PREFIX + "selectall.gif"); //$NON-NLS-1$
		DESC_ACTION_SELECTALL_DISABLED = createDescriptor(DISABLED_PREFIX + "selectall.gif"); //$NON-NLS-1$

		DESC_ACTION_SELECTSHAPES = createDescriptor(ENABLED_PREFIX + "selectshapes.gif"); //$NON-NLS-1$
		DESC_ACTION_SELECTSHAPES_DISABLED = createDescriptor(DISABLED_PREFIX + "selectshapes.gif"); //$NON-NLS-1$

		DESC_ACTION_SELECTCONNECTORS = createDescriptor(ENABLED_PREFIX + "selectconnectors.gif"); //$NON-NLS-1$	
		DESC_ACTION_SELECTCONNECTORS_DISABLED = createDescriptor(DISABLED_PREFIX + "selectconnectors.gif"); //$NON-NLS-1$

		DESC_ACTION_ALIGN = createDescriptor("aleft.gif"); //$NON-NLS-1$

		DESC_ACTION_OUTLINE = createDescriptor("outline.gif"); //$NON-NLS-1$

		DESC_ACTION_OVERVIEW = createDescriptor("overview.gif"); //$NON-NLS-1$

		DESC_ACTION_BOLD = createDescriptor(ENABLED_PREFIX + "bold.gif"); //$NON-NLS-1$
		DESC_ACTION_BOLD_DISABLED = createDescriptor(DISABLED_PREFIX + "bold.gif"); //$NON-NLS-1$

		DESC_ACTION_ITALIC = createDescriptor(ENABLED_PREFIX + "italic.gif"); //$NON-NLS-1$
		DESC_ACTION_ITALIC_DISABLED = createDescriptor(DISABLED_PREFIX + "italic.gif"); //$NON-NLS-1$
		
		DESC_ACTION_FONT_COLOR = createDescriptor(ENABLED_PREFIX + "font_color.gif"); //$NON-NLS-1$
		DESC_ACTION_FONT_COLOR_DISABLED = createDescriptor(DISABLED_PREFIX + "font_color.gif"); //$NON-NLS-1$

		DESC_ACTION_FILL_COLOR = createDescriptor(ENABLED_PREFIX + "fill_color.gif"); //$NON-NLS-1$
		DESC_ACTION_FILL_COLOR_DISABLED = createDescriptor(DISABLED_PREFIX + "fill_color.gif"); //$NON-NLS-1$

		DESC_ACTION_LINE_COLOR = createDescriptor(ENABLED_PREFIX + "line_color.gif"); //$NON-NLS-1$
		DESC_ACTION_LINE_COLOR_DISABLED = createDescriptor(DISABLED_PREFIX + "line_color.gif"); //$NON-NLS-1$
		
		DESC_ACTION_VIEWPAGEBREAKS = createDescriptor(ENABLED_PREFIX + "viewpagebreaks.gif"); //$NON-NLS-1$
		DESC_ACTION_VIEWPAGEBREAKS_DISABLED = createDescriptor(DISABLED_PREFIX + "viewpagebreaks.gif"); //$NON-NLS-1$
		
		DESC_ACTON_RECALCPAGEBREAKS = createDescriptor(ENABLED_PREFIX + "recalcpagebreaks.gif"); //$NON-NLS-1$
		DESC_ACTON_RECALCPAGEBREAKS_DISABLED = createDescriptor(DISABLED_PREFIX + "recalcpagebreaks.gif"); //$NON-NLS-1$
		
		DESC_ACTION_ACTIONBAR = createDescriptor("actionbar.gif"); //$NON-NLS-1$
		DESC_ACTION_ACTIONBAR_INV = createDescriptor("actionbar_inv.gif"); //$NON-NLS-1$
		
		DESC_ACTION_SNAPBACK = createDescriptor("snapback.gif");//$NON-NLS-1$
		
		DESC_ACTION_ACTIONBAR_PLUS = createDescriptor("actionbar_plus.gif");//$NON-NLS-1$

		DESC_ACTION_SHOW_PROPERTIES_VIEW = createDescriptor("properties_view.gif");//$NON-NLS-1$

		DESC_ACTION_SHOW_CONNECTOR_LABELS = createDescriptor(ENABLED_PREFIX + "showconnector.gif"); //$NON-NLS-1$
		DESC_ACTION_SHOW_CONNECTOR_LABELS_DISABLED = createDescriptor(DISABLED_PREFIX + "showconnector.gif"); //$NON-NLS-1$			
		DESC_ACTION_HIDE_CONNECTOR_LABELS = createDescriptor(ENABLED_PREFIX + "hideconnector.gif"); //$NON-NLS-1$
		DESC_ACTION_HIDE_CONNECTOR_LABELS_DISABLED = createDescriptor(DISABLED_PREFIX + "hideconnector.gif"); //$NON-NLS-1$		
			
		DESC_ACTION_SORT_FILTER = createDescriptor(ENABLED_PREFIX + "sortfilter.gif"); //$NON-NLS-1$
		DESC_ACTION_SORT_FILTER_DISABLED = createDescriptor(DISABLED_PREFIX + "sortfilter.gif"); //$NON-NLS-1$
		
		DESC_ACTION_BRING_TO_FRONT = createDescriptor("bring_to_front.gif"); //$NON-NLS-1$
		DESC_ACTION_BRING_FORWARD  = createDescriptor("bring_forward.gif"); //$NON-NLS-1$
		DESC_ACTION_SEND_TO_BACK   = createDescriptor("send_to_back.gif"); //$NON-NLS-1$
		DESC_ACTION_SEND_BACWARD   = createDescriptor("send_backward.gif"); //$NON-NLS-1$

		DESC_ACTION_MAKE_SAME_SIZE_BOTH = createDescriptor("size_to_control.gif"); //$NON-NLS-1$
		DESC_ACTION_MAKE_SAME_SIZE_HEIGHT = createDescriptor("size_to_control_height.gif"); //$NON-NLS-1$
		DESC_ACTION_MAKE_SAME_SIZE_WIDTH = createDescriptor("size_to_control_width.gif"); //$NON-NLS-1$
		
		DESC_ACTION_COPY_APPEARANCE = createDescriptor(ENABLED_PREFIX + "copy_appearance_properties.gif"); //$NON-NLS-1$
		DESC_ACTION_COPY_APPEARANCE_DISABLED = createDescriptor(DISABLED_PREFIX + "copy_appearance_properties.gif"); //$NON-NLS-1$	

	}

	static private Image create(String filename) {
		return PresentationResourceManager.getInstance().createImage(filename);
	}

	static private ImageDescriptor createDescriptor(String filename) {
		return PresentationResourceManager.getInstance().createImageDescriptor(
			filename);
	}
}
