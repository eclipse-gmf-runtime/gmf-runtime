/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;


/**
 * 
 * Request that can optional have any UI suppressed.
 * @author choang
 * @canBeSeenBy %level1
 */
public interface SuppressibleUIRequest {

	public void setSuppressibleUI(boolean suppressUI);
	
	public boolean isUISupressed();
	
}
