/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.ui.IWorkbenchPage;

/**
 * A descriptor for a workbench part using its id, class and page
 * @author melaasar
 */
public interface IWorkbenchPartDescriptor {
	
	/**
	 * Gets the id of the workbench part
	 * 
	 * @return The id of the workbench part
	 */
	public String getPartId();
	
	/**
	 * Gets the class of the workbench part
	 * 
	 * @return The class of the workbench part
	 */
	public Class getPartClass();
	
	/**
	 * Gets the page of the workbench part
	 * 
	 * @return The page of the workbench part
	 */
	public IWorkbenchPage getPartPage();

}
