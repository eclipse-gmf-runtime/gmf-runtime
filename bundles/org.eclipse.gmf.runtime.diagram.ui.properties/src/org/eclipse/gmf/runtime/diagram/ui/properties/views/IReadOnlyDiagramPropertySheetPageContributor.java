/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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