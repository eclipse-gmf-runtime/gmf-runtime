/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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