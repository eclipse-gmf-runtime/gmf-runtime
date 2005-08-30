/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.resources;

/**
 * Convenience interface for the marker type and its attributes
 * 
 * @author Michael Yee
 */
public interface IMarker {

	/** marker type */
	public static final String TYPE = "org.eclipse.gmf.runtime.common.ui.services.marker"; //$NON-NLS-1$

	/** Separator between multiple IDs in the {@link #ELEMENT_ID}attribute. */
	public static final String ELEMENT_ID_SEPARATOR = " "; //$NON-NLS-1$

	/*
	 * ====================================================================
	 * Marker attributes:
	 * ====================================================================
	 */
	/** element guid */
	public static final String ELEMENT_ID = "elementId"; //$NON-NLS-1$
}