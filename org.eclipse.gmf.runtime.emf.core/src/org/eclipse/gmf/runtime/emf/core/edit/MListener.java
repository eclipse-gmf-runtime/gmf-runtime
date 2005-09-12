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

package org.eclipse.gmf.runtime.emf.core.edit;

import java.util.List;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLEventBroker;

/**
 * <p>
 * MSL listeners are used to handle EMF events. To define a new listener, one
 * would subclass <code>MListener</code> and implement the abstract method
 * <code>onEvent</code>.
 * </p>
 * <p>
 * A listener can have only one filter associated with it. For the listener to
 * start listening, one must attach a filter to the listener and call
 * <code>startListening</code>.
 * </p>
 * 
 * @author rafikj
 */
public abstract class MListener {

	protected MEditingDomain domain = null;

	private MFilter filter = null;

	/**
	 * Constructor. Constructs a listener that listens on events associated with
	 * the MSL default editing domain.
	 */
	public MListener() {
		this(MEditingDomain.INSTANCE);
	}

	/**
	 * Constructor. Constructs a listener that listens on events associated with
	 * the MSL default editing domain and attaches a filter to the listener.
	 * <code>filter</code> must not be null.
	 * 
	 * @param filter
	 *            The filter.
	 */
	public MListener(MFilter filter) {
		this(MEditingDomain.INSTANCE, filter);
	}

	/**
	 * Constructor. Constructs a listener that listens on events associated with
	 * the given editing domain. <code>domain</code> must not be null.
	 */
	public MListener(MEditingDomain domain) {

		super();

		this.domain = (MSLEditingDomain) domain;
	}

	/**
	 * Constructor. Constructs a listener that listens on events associated with
	 * the given editing domain and attaches a filter to the listener.
	 * <code>filter</code> must not be null. <code>domain</code> must not be
	 * null.
	 * 
	 * @param filter
	 *            The filter.
	 */
	public MListener(MEditingDomain domain, MFilter filter) {

		this(domain);

		this.filter = filter;
	}

	/**
	 * Processes the list of events. <code>events</code> is a list of
	 * <code>Notification</code> objects.
	 * 
	 * @param events
	 *            The events to process.
	 */
	public abstract void onEvent(List events);

	/**
	 * Gets the filter.
	 * 
	 * @return The filter.
	 */
	public MFilter getFilter() {
		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            The filter.
	 * @return The old filter.
	 */
	public MFilter setFilter(MFilter filter) {

		MFilter oldFilter = this.filter;
		this.filter = filter;

		return oldFilter;
	}

	/**
	 * Starts listening.
	 */
	public void startListening() {

		if (domain == null)
			MSLEventBroker.addUniversalListener(this);
		else
			((MSLEditingDomain) domain).getEventBroker().addListener(this);
	}

	/**
	 * Stops listening.
	 */
	public void stopListening() {

		if (domain == null)
			MSLEventBroker.removeUniversalListener(this);
		else
			((MSLEditingDomain) domain).getEventBroker().removeListener(this);
	}
}