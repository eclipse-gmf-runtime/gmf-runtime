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

package org.eclipse.gmf.runtime.common.ui.services.action.contributionitem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.IAction;

import org.eclipse.gmf.runtime.common.ui.action.IDisposableAction;

/**
 * An action registry used for action life cycle management
 * 
 * @author melaasar
 */
public class ActionRegistry {

	/**
	 * A hashmap that contains the actions.
	 */
	private Map map = new HashMap();

	/**
	 * Calls init on all actions which implement the {@link IDisposableAction} interface so they
	 * can do their initialization.
	 */
	public void init() {
		Iterator actions = getActions();
		while (actions.hasNext()) {
			IAction action = (IAction) actions.next();
			if (action instanceof IDisposableAction)
				 ((IDisposableAction) action).init();
		}
	}

	/**
	 * Calls dispose on all actions which implement the {@link IDisposableAction} interface so they
	 * can perform their own clean-up.
	 */
	public void dispose() {
		Iterator actions = getActions();
		while (actions.hasNext()) {
			IAction action = (IAction) actions.next();
			if (action instanceof IDisposableAction)
				 ((IDisposableAction) action).dispose();
		}
	}

	/**
	 * Returns the action with the given key. The key is the ID of the action
	 * (see {@link #registerAction(IAction)}and
	 * {@link #registerAction(String, IAction)}).
	 * 
	 * @param key
	 *            the key
	 * @return the action
	 */
	public IAction getAction(Object key) {
		return (IAction) map.get(key);
	}

	/**
	 * Returns an <code>Iterator</code> of all the actions.
	 * @return the iterator
	 */
	protected Iterator getActions() {
		return map.values().iterator();
	}

	/**
	 * Register an action with this registry.  The action must have an id associated with it, which
	 * will be used as the key to later obtain the action using {@link #getAction(Object)}. 
	 * @param action the action to be registered
	 */
	public void registerAction(IAction action) {
		registerAction(action.getId(), action);
	}

	/**
	 * Register an action with this registry using the given id. The string
	 * <code>id</code> will be used as the key to later obtain the action
	 * using {@link #getAction(Object)}.
	 * 
	 * @param id
	 *            the action ID
	 * @param action
	 *            the action
	 */
	public void registerAction(String id, IAction action) {
		map.put(id, action);
	}
}
