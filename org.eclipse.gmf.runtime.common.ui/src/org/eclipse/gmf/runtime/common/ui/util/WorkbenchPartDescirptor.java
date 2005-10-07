/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
 * A standard implementation of <code>IWorkbenchPartDescriptor</code> interface
 * @author melaasar
 * @deprecated use org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartDescriptor
 */
public class WorkbenchPartDescirptor extends WorkbenchPartDescriptor {

	public WorkbenchPartDescirptor(String partId, Class partClass, IWorkbenchPage partPage) {
		super(partId, partClass, partPage);
	}

}
