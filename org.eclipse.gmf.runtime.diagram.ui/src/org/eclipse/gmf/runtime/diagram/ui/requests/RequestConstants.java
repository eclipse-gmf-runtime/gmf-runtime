/******************************************************************************
 * Copyright (c) 2002, 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;
 

/**
 * this class defines the request constants
 * <p>
 * This interface is <EM>not</EM> intended to be implemented by clients as new
 * methods may be added in the future. 
 * </p>
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
	 * constants for refresh connections request
	 */
	public static final String REQ_REFRESH_CONNECTIONS = "refresh_connections"; //$NON-NLS-1$
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
	 * constants for set all connection bendpoint request
	 */
	public static final String REQ_SET_ALL_BENDPOINT = "set_all_connection_bendpoint";	   //$NON-NLS-1$ 
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
	 * constants for toggle connection labels request
	 */
	public static final String 	REQ_TOGGLE_CONNECTION_LABELS = "toggle_connection_label"; //$NON-NLS-1$
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
	
	/**
	 * constant for child property change request
	 */
	public static final String REQ_CHILD_PROPERTY_CHANGE = "child_property_change"; //$NON-NLS-1$
	
	/**
	 * constant for show all compartments  request
	 */
	public static final String REQ_SHOW_ALL_COMPARTMENTS = "show_all_compartments"; //$NON-NLS-1$
    
	/**
	 * constant key for extended data in the DirectEditRequest for the initial character
	 */
	public static final String REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR = "directedit_extendeddata_initial_char"; //$NON-NLS-1$
}
 
