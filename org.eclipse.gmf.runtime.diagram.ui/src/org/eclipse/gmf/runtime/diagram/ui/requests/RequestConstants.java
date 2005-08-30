/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;
 
/*
 * @canBeSeenBy %partners
 */
/**
 * this class defines the request constants
 * @author sshaw
 *
 */
public interface RequestConstants extends org.eclipse.gef.RequestConstants {

	/**
	 * constants for auto size request
	 */
	public static final String 	REQ_AUTOSIZE = "autosize"; //$NON-NLS-1$
    /**
	 * constants for refresh request
	 */
	public static final String	REQ_REFRESH = "refresh";		   //$NON-NLS-1$
    /**
	 * constants for refresh connectors request
	 */
	public static final String REQ_REFRESH_CONNECTORS = "refresh_connectors"; //$NON-NLS-1$
    /**
	 * constants for drag request
	 */
	public static final String REQ_DRAG = "drag";//$NON-NLS-1$
    /**
	 * constants for drop request
	 */
	public static final String 	REQ_DROP = "drop";//$NON-NLS-1$
    /**
	 * constants for drop objects request
	 */
	public static final String 	REQ_DROP_OBJECTS = "drop_objects";  //$NON-NLS-1$
    /**
	 * constants for paste request
	 */
	public static final String 	REQ_PASTE = "paste";  //$NON-NLS-1$ 
    /**
	 * constants for property change request
	 */
	public static final String 	REQ_PROPERTY_CHANGE = "property_change";		   //$NON-NLS-1$
	
    /**
	 * constants for wrap semantic  request
	 */
	public static final String REQ_SEMANTIC_WRAPPER = "wrap semantic request"; //$NON-NLS-1$
    /**
	 * constants for set all connector ben point request
	 */
	public static final String REQ_SET_ALL_BENDPOINT = "set_all_connector_bendpoint";	   //$NON-NLS-1$ 
    /**
	 * constants for arrange radial request
	 */
	public static final String REQ_ARRANGE_RADIAL = "arrange_radial";			   //$NON-NLS-1$
    /**
	 * constants for arrange deferred request
	 */
	public static final String 	REQ_ARRANGE_DEFERRED = "arrange_deferred";			   //$NON-NLS-1$		
    /**
	 * constants for move deferred request
	 */
	public static final String REQ_MOVE_DEFERRED = "move_shape_deferred";		   //$NON-NLS-1$
    /**
	 * constants for apply appearance properties  request
	 */
	public static final String 	REQ_APPLY_APPEARANCE_PROPERTIES = "apply_appearance_properties"; //$NON-NLS-1$		
    /**
	 * constants for show related elements request
	 */
	public static final String 	REQ_SHOW_RELATED_ELEMENTS = "show_related_elements"; //$NON-NLS-1$
    /**
	 * constants for show/hide relationships
	 */
	public static final String 	REQ_SHOWHIDE_RELATIONSHIPS = "showhide_relationships"; //$NON-NLS-1$		
    /**
	 * constants for recalculate page breaks request
	 */
	public static final String 	REQ_RECALCULATE_PAGEBREAKS = "recalculate_pagebreaks"; //$NON-NLS-1$
    /**
	 * constants for sort filter compartment
	 */
	public static final String 	REQ_SORT_FILTER_COMPARTMENT = "sort_filter_compartment"; //$NON-NLS-1$
    /**
	 * constants for sort filter content request
	 */
	public static final String REQ_SORT_FILTER_CONTENT = "sort_filter_content"; //$NON-NLS-1$
    /**
	 * constants for filter compartment items
	 */
	public static final String 	REQ_FILTER_COMPARTMENT_ITEMS = "filter_compartment_items"; //$NON-NLS-1$
    /**
	 * constants for sort copartment items request
	 */
	public static final String 	REQ_SORT_COMPARTMENT_ITEMS = "sort_compartment_items"; //$NON-NLS-1$	
    /**
	 * constants for change sort filter request
	 */
	public static final String REQ_CHANGE_SORT_FILTER = "change_sort_filter"; //$NON-NLS-1$
    /**
	 * constants for show as alternate view  request
	 */
	public static final String REQ_SHOW_AS_ALTERNATE_VIEW = "show_as_alternate_view"; //$NON-NLS-1$
    /**
	 * constants for snap back request
	 */
	public static final String 	REQ_SNAP_BACK = "snap_back"; //$NON-NLS-1$
    /**
	 * constants for toggle connector labels request
	 */
	public static final String 	REQ_TOGGLE_CONNECTOR_LABELS = "toggle_connector_label"; //$NON-NLS-1$
    /**
	 * constants for toggle canonical mode request
	 */
	public static final String 	REQ_TOGGLE_CANONICAL_MODE = "toggle_canonical_mode";//$NON-NLS-1$
    /**
	 * constants for insert semantic request
	 */
	public static final String 	REQ_INSERT_SEMANTIC = "insert_semantic"; //$NON-NLS-1$
    /**
	 * constants for duplicate  request
	 */
	public static final String REQ_DUPLICATE = "duplicate"; //$NON-NLS-1$
		
}
 
