/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.palette;

/**
 * An interface that adds identity to a palette entry
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 */
public interface PaletteIdentity {

	/**
	 * Returns the id of a palette entry
	 * 
	 * @return The id of the palette entry
	 */
	String getId();
}

