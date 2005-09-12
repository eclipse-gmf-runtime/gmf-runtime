/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.icon;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.ProviderChangeEvent;

/**
 * A custom provider changed event for handling icon and text changes
 * 
 * @author myee
 */
public class LabelChangedEvent
	extends ProviderChangeEvent {

	/**
	 * Indicates if the event is affecting the children of the affected elements
	 */
	private boolean childrenAffecting = false;

	/**
	 * The affected elements
	 */
	private final Object[] elements;

	/**
	 * Constructor for LabelChangedEvent
	 * 
	 * @param source
	 *            the provider source
	 * @param elements
	 *            the affected elements
	 */
	public LabelChangedEvent(IProvider source, Object[] elements) {
		super(source);
		this.elements = elements;
	}

	/**
	 * Constructor for LabelChangedEvent
	 * 
	 * @param source
	 *            the provider source
	 * @param elements
	 *            the affected elements
	 * @param childrenAffecting
	 *            Indicates if the event is affecting the children of the
	 *            affected elements
	 */
	public LabelChangedEvent(IProvider source, Object[] elements,
			boolean childrenAffecting) {
		this(source, elements);
		this.childrenAffecting = childrenAffecting;
	}

	/**
	 * Returns the affected elements
	 * 
	 * @return the elements.
	 */
	public Object[] getElements() {
		return elements;
	}

	/**
	 * @return The indicator if the event is affecting the children of the
	 *         affected elements
	 */
	public boolean isChildrenAffecting() {
		return childrenAffecting;
	}
}