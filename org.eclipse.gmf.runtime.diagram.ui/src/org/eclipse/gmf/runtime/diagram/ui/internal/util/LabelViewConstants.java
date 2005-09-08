/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.util;


/**
 * @author mmostafa
 * this interface defines all label View constants
 */
public interface LabelViewConstants {
	/** 
	 * Percentage location for labels that are located
	 * relative to the source end of a <code>Connector</code>
	 */
	public static final int SOURCE_LOCATION = 15;
	/** 
	 * Percentage location for labels that are located
	 * relative to the target of a <code>Connector</code>
	 */
	public static final int TARGET_LOCATION = 85;
	/** 
	 * Percentage location for labels that are located
	 * relative to the middle of a <code>Connector</code>
	 */
	public static final int MIDDLE_LOCATION = 50;	
}
