/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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

