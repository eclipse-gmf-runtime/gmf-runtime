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

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;

/**
 * Provides utility methods for manipulating shortcuts.
 * 
 * @author ldamus
 */
public class ShortcutUtil {

	/**
	 * Gets the target element of the <code>shortcut</code>.
	 * 
	 * @param shortcut
	 *            the shortcut element
	 * @return the target of the shorcut, or <code>null</code> if it cannot be
	 *         found or resolved.
	 */
	public static EObject getShortcutTarget(EObject shortcut) {

		assert isShortcut(shortcut) : "EObject shortcut is not a shortcut"; //$NON-NLS-1$

		EObject result = null;

		List references = ((EAnnotation) shortcut).getReferences();

		if (references.size() > 0) {
			result = ProxyUtil.resolve((EObject) references.get(0));
		}

		return result;
	}

	/**
	 * Determines whether or not <code>element</code> is a shortcut.
	 * 
	 * @param element
	 *            the element to be tested.
	 * @return <code>true</code> if the element is a shortcut,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isShortcut(EObject element) {

		return element instanceof EAnnotation
			&& MSLConstants.SHORTCUT_ANNOTATION.equals(((EAnnotation) element)
				.getSource());
	}

	/**
	 * Gets the shortcut target equivalent of <code>element</code>. The
	 * shortcut target is in a form suitable to be stored in the model as the
	 * target of the new shortcut element.
	 * 
	 * @param element
	 *            the element to be translated.
	 * @return the translated element
	 */
	public static EObject[] getShortcutTargets(Object element) {
		IShortcutTargetAdapter adapter = (IShortcutTargetAdapter) Platform
			.getAdapterManager().loadAdapter(element,
				IShortcutTargetAdapter.class.getName());

		if (adapter != null) {
			return adapter.getShortcutTargets(element);
		}
		return null;
	}

	/**
	 * Determines if <code>element</code> can be translated into a form
	 * suitable to be stored as the target of the shortcut.
	 * 
	 * @param element
	 *            the element to be tested
	 * @return <code>true</code> if a shortcut target could be created using
	 *         this element, <code>false</code> otherwise.
	 */
	public static boolean hasShortcutTargets(Object element) {
		/*
		 * Use adapter factories to supply the necessary translation. Note that
		 * this could also have been achieved by using a service.
		 */
		IShortcutTargetAdapter adapter = (IShortcutTargetAdapter) Platform
			.getAdapterManager().loadAdapter(element,
				IShortcutTargetAdapter.class.getName());

		return adapter != null && adapter.hasShortcutTargets(element);
	}
}