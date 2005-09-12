/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
