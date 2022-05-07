/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.editpart;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.notation.View;

/**
 * <p>
 * This interface is <EM>not</EM> intended to be implemented by clients as new
 * methods may be added in the future. 
 * </p>
 *
 * @author cmahoney
 */
public interface IEditPartOperation extends IOperation {

	/**
	 * Gets the caching key.
	 * 
	 * @return a string to be used as the caching key
	 */
	public abstract String getCachingKey();

	/**
	 * gets the cached view.
	 * @return the notation View
	 */
	public abstract View getView();

}