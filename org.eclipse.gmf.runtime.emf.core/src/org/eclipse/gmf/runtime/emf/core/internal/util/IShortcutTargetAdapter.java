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

package org.eclipse.gmf.runtime.emf.core.internal.util;

import org.eclipse.emf.ecore.EObject;


/**
 * Adapts objects to the form in which they need to be in order to be stored as
 * the target of a shortcut element in the model, and vice versa.
 * <P>
 * Adapter factories are registered for visualizer and modeler so that they can
 * independently provide the shortcut target EObject given an object selected in
 * the "Select Shortcut Target" dialog.
 * 
 * @author ldamus
 */
public interface IShortcutTargetAdapter {

	/**
	 * Answers whether or not the <code>targetObject</code> can be translated
	 * into a form in which it can be stored as the target of a shortcut element
	 * in the model.
	 * 
	 * @param targetObject
	 *            the object that may need to be adapted to a form suitable to
	 *            be stored in the model as the target of a shortcut to
	 *            <code>targetObject</code>.
	 * @return <code>true</code> if <code>targetObject</code> can be
	 *         translated into a suitable form, <code>false</code> otherwise.
	 */
	public boolean hasShortcutTargets(Object targetObject);

	/**
	 * Adapts objects to the form in which they need to be in order to be stored
	 * as the target of a shortcut element in the model.
	 * 
	 * @param targetObject
	 *            the object that may need to be adapted to a form suitable to
	 *            be stored in the model as the target of a shortcut to
	 *            <code>targetObject</code>.
	 * @return the EObject equivalents of <code>targetObject</code>, suitable
	 *         to be stored in the model as the target of a shortcut to
	 *         <code>targetObject</code>.
	 */
	public EObject[] getShortcutTargets(Object targetObject);

	/**
	 * Answers whether or not the <code>shortcutTarget</code> can be 
	 * opened by this adapter.
	 * @param shortcutTarget the shortcut target to be opened
	 * @return <code>true</code> if this adapter can handle openine
	 * the shortcut target, <code>false</code> otherwise.
	 */
	public boolean canOpenShortcutTarget(EObject shortcutTarget);

	/**
	 * Opens the shortcut target.
	 * @param shortcutTarget the shortcut target to be opened.
	 */
	public void openShortcutTarget(EObject shortcutTarget);

}