/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.emf.transaction.TransactionalEditingDomain;


/**
 * @author mgoyal
 *
 */
public interface MEditingDomainElement {
	/**
	 * @return The editing domain for this diagram document
	 */
	TransactionalEditingDomain getEditingDomain();
}
