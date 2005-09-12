/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.services.action;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.AbstractActionFilterProvider;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIStatusCodes;

/**
 * The parent of all model action filter providers. Defines behavior to wrap
 * queries on this provider in read actions.
 * 
 * @author khussey
 *
 */
public abstract class AbstractModelActionFilterProvider
	extends AbstractActionFilterProvider {

	/**
	 * The internal result of querying this model action filter provider.
	 * 
	 */
	private boolean result = false;

	/**
	 * Constructs a new model action filter provider.
	 * 
	 */
	protected AbstractModelActionFilterProvider() {
		super();
	}

	/**
	 * Retrieves the value of the <code>result</code> instance variable.
	 * 
	 * @return The value of the <code>result</code> instance variable.
	 * 
	 */
	protected final boolean getResult() {
		return result;
	}

	/**
	 * Sets the <code>result</code> instance variable to the specified value.
	 * 
	 * @param result The new value for the <code>result</code> instance
	 *                variable.
	 * 
	 */
	protected final void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * Actually does the work of determining whether the specific attribute
	 * matches the state of the target object.
	 *
	 * @return <code>true</code> if the attribute matches; <code>false</code>
	 *          otherwise.
	 * @param target The target object.
	 * @param name The attribute name.
	 * @param value The attriute value.
	 * 
	 */
	protected abstract boolean doTestAttribute(Object target, String name,
		String value);

	/**
	 * Actually does the work of determining whether this provider provides the
	 * specified operation.
	 * 
	 * @return <code>true</code> if this provider provides the operation;
	 *          <code>false</code> otherwise.
	 * @param operation The operation in question.
	 * 
	 */
	protected abstract boolean doProvides(IOperation operation);

	/**
	 * Tests whether the specific attribute matches the state of the target
	 * object, as a read action.
	 *
	 * @return <code>true</code> if the attribute matches; <code>false</code>
	 *          otherwise.
	 * @param target The target object.
	 * @param name The attribute name.
	 * @param value The attriute value.
	 * 
	 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
	 * 
	 */
	public final boolean testAttribute(final Object target, final String name,
		final String value) {

		try {
			OperationUtil.runAsRead(new Runnable() {

				public void run() {
					try {
						setResult(doTestAttribute(target, name, value));
					} catch (Exception e) {
						Trace.catching(MslUIPlugin.getDefault(),
							MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
							"testAttribute", e); //$NON-NLS-1$
						Log.warning(MslUIPlugin.getDefault(),
							MslUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
								.getMessage(), e);
						RuntimeException cre = new RuntimeException(
							e);
						Trace.throwing(MslUIPlugin.getDefault(),
							MslUIDebugOptions.EXCEPTIONS_THROWING, getClass(),
							"testAttribute", cre); //$NON-NLS-1$
						throw cre;
					}
				}
			});
		} catch (MSLActionAbandonedException e) {
			// This is not expected to happen.
			Trace.trace(MslUIPlugin.getDefault(),
				MslUIDebugOptions.MODEL_OPERATIONS,
				"MSLActionAbandonedException"); //$NON-NLS-1$
		}

		return getResult();
	}

	/**
	 * Tests whether this provider provides the specified operation, as a read
	 * action.
	 * 
	 * @return <code>true</code> if this provider provides the operation;
	 *          <code>false</code> otherwise.
	 * @param operation The operation in question.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(IOperation)
	 * 
	 */
	public final boolean provides(final IOperation operation) {

		try {
			OperationUtil.runAsRead(new Runnable() {

				public void run() {
					try {
						setResult(doProvides(operation));
					} catch (Exception e) {
						Trace.catching(MslUIPlugin.getDefault(),
							MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
							"provides", e); //$NON-NLS-1$
						Log.warning(MslUIPlugin.getDefault(),
							MslUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
								.getMessage(), e);
						RuntimeException cre = new RuntimeException(
							e);
						Trace.throwing(MslUIPlugin.getDefault(),
							MslUIDebugOptions.EXCEPTIONS_THROWING, getClass(),
							"provides", cre); //$NON-NLS-1$
						throw cre;
					}
				}
			});
		} catch (MSLActionAbandonedException e) {
			// This is not expected to happen.
			Trace.trace(MslUIPlugin.getDefault(),
				MslUIDebugOptions.MODEL_OPERATIONS,
				"MSLActionAbandonedException"); //$NON-NLS-1$
		}

		return getResult();
	}

}
