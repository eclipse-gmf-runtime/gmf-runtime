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

package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart;

import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * Abstract operation that simply stores the associated view and provides an
 * accessor method to all subclasses.
 * 
 * @author gsturov
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public abstract class EditPartOperation
	implements IOperation {

	/** cached caching key */
	private String cachingKey;

	/**
	 * Gets the caching key.
	 * 
	 * @return a string to be used as the caching key
	 */
	public String getCachingKey() {
		if (cachingKey == null)
			cachingKey = determineCachingKey();
		return cachingKey;
	}

	/**
	 * Determines the caching key for this operation.
	 * 
	 * @return a string to be used as the caching key
	 */
	protected abstract String determineCachingKey();

}

