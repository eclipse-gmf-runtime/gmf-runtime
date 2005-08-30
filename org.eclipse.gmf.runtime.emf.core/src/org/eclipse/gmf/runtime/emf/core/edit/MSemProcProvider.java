/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.edit;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * <p>
 * MSL SemProc Providers are used to provide model modifications in response to events.
 * To define a new SemProc, one
 * would subclass <code>MSemProcPRovider</code> and implement the abstract method
 * <code>onEvent</code>.
 * </p>
 * <p>
 * A SemProc provider can have only one filter associated with it. For the provider to
 * start providing for semproc, one must attach a filter to the provider and call
 * <code>startListening</code>.
 * </p>
 * 
 * @author mgoyal
 */
public abstract class MSemProcProvider extends MListener {

	/**
	 * Constructor. Constructs a semproc provider that provides 
	 * on events associated with the MSL default editing domain.
	 */
	public MSemProcProvider() {
		super(MEditingDomain.INSTANCE);
	}

	/**
	 * Constructor. Constructs a semproc provider that provides 
	 * on events associated with the MSL default editing domain
	 * and attaches a filter to the SemProc Provider.
	 * 
	 * <code>filter</code> must not be null.
	 * 
	 * @param filter The filter.
	 */
	public MSemProcProvider(MFilter filter) {
		super(MEditingDomain.INSTANCE, filter);
	}

	/**
	 * Constructor. Constructs a semproc provider that provides
	 * on events associated with the given editing domain. 
	 * <code>domain</code> must not be null.
	 */
	public MSemProcProvider(MEditingDomain domain) {
		super(domain);
		assert domain != null;
	}

	/**
	 * Constructor. Constructs a semproc provider that provides 
	 * on events associated with the given editing domain and 
	 * attaches a filter to the listener.
	 * 
	 * <code>filter</code> must not be null. 
	 * <code>domain</code> must not be null.
	 * 
	 * @param filter The filter.
	 */
	public MSemProcProvider(MEditingDomain domain, MFilter filter) {
		super(domain, filter);
		assert domain != null;
		assert filter != null;
	}

	/**
	 * Starts listening.
	 */
	public void startListening() {
		((MSLEditingDomain) domain).getEventBroker().addSemProcProvider(this);
	}

	/**
	 * Stops listening.
	 */
	public void stopListening() {
		((MSLEditingDomain) domain).getEventBroker().removeSemProcProvider(this);
	}
}
