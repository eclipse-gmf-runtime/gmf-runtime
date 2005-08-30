/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Singleton element type that has no <code>EClass</code>. Clients can
 * specialized this element type if they wish to declare types that do not have
 * a matching <code>EClass</code>.
 * 
 * @author ldamus
 */
public class NullElementType
	extends MetamodelType {

	/**
	 * Edit helper for the NullElementType that considers before and after
	 * advice, only.
	 */
	private static class NullEditHelper
		extends AbstractEditHelper {

		protected ICommand getInsteadCommand(IEditCommandRequest req) {
			return null;
		}
	}

	/**
	 * The ID.
	 */
	public static final String ID = "org.eclipse.gmf.runtime.emf.type.core.null"; //$NON-NLS-1$

	/**
	 * The singleton instance.
	 */
	private static NullElementType _instance;

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static NullElementType getInstance() {
		if (_instance == null) {
			_instance = new NullElementType();
		}
		return _instance;
	}

	/**
	 * Private constructor.
	 */
	private NullElementType() {
		super(ID, null, null, null, new NullEditHelper());
	}

}
