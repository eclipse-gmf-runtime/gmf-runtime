/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.internal.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * This interface enables Xtools clients to handle the navigation when the user
 * goes to (double clicks) a marker that the client has defined. The client can
 * either perform the necessary feedback in their provider or can delegate the
 * operation via an applicable mechanism (e.g., a GEF request).
 * 
 * @author Kevin Cornell
 */
public interface IMarkerNavigationProvider
	extends IProvider {

	/**
	 * Perform feedback for marker navigation.
	 * 
	 * @param editor
	 *            the editor opened with the associated resource
	 * @param marker
	 *            the marker reference
	 */
	public void gotoMarker(IEditorPart editor, IMarker marker);

}