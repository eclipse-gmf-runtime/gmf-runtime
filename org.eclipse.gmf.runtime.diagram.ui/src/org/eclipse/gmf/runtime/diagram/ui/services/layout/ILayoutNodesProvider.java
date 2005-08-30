/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.ILayoutNodesProviderBase;

/**
 * The interface for providers of the layout provider extension point.
 * 
 * Implement this interface to contribute an extension to the
 * "layoutProviders" extension point.
 * The layoutNodes(List, boolean, IAdaptable) method call is 
 * invoked by internal presentation code for "arrange" actions.
 * 
 * Consideration of dependencies has to be done when choosing the priority
 * of the provider.  
 * The layout operation is executed on the first provider 
 * (in descending order of priority) that is found to provide the operation.
 * 
 * Implementors are not expected to start read/write operations.
 * 
 * @author schafe, sshaw 
 */
public interface ILayoutNodesProvider extends ILayoutNodesProviderBase {

	/**
	 * the default layout type
	 * @deprecated replace with {@link LayoutType.DEFAULT}
	 */
	public static String DEFAULT_LAYOUT = LayoutType.DEFAULT;
	/**
	 * the radial layout type 
	 * @deprecated replace with {@link LayoutType.RADIAL}
	 */
	public static String RADIAL_LAYOUT = LayoutType.RADIAL;
	 
}
