/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.commands;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;

/**
 * Edit command to configure a new model element with the characteristics of its
 * element type.
 * 
 * @author ldamus
 */
public abstract class ConfigureElementCommand
	extends EditElementCommand {

	/**
	 * The element type.
	 */
	private final IElementType elementType;

	/**
	 * Constructs a new element configuration command for the
	 * <code>request</code>.
	 * 
	 * @param request
	 *            the element configuration request
	 */
	public ConfigureElementCommand(ConfigureRequest request) {

		super(request.getLabel(), request.getElementToConfigure(), request);

		this.elementType = request.getTypeToConfigure();
	}

	/**
	 * Gets the element type.
	 * 
	 * @return the element type
	 */
	public IElementType getElementType() {
		return elementType;
	}

}