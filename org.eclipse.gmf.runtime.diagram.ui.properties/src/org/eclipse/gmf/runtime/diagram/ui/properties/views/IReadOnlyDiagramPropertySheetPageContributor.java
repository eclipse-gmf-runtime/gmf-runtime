/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.views;

import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

/**
 * A interface to indicate that the property sheet pages should be read only
 * independant of the selected object and its related file status.
 * 
 * For example, this interface is implemented by topic and browse diagrams. Even
 * though the selected object can be modified, we want the properties to be read
 * only for these diagrams.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public interface IReadOnlyDiagramPropertySheetPageContributor
	extends ITabbedPropertySheetPageContributor {
	/*
	 * there are no methods
	 */
}