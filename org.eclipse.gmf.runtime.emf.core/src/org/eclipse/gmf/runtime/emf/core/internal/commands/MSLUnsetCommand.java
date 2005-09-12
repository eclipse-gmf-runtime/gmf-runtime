/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * A basic unset command.
 * 
 * @author rafikj
 */
public final class MSLUnsetCommand
	extends MSLBasicCommand {

	private Object value = null;

	private Object oldValue = null;

	private boolean wasSet = false;

	/**
	 * Constructor.
	 */
	public MSLUnsetCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Object value, Object oldValue,
			boolean wasSet) {

		super(domain, owner, feature);

		this.value = value;
		this.oldValue = oldValue;
		this.wasSet = wasSet;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {
		super.dispose();
		oldValue = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {

		if (needToModify()) {

			EObject owner = resolveOwner();

			if (owner != null) {

				EStructuralFeature feature = getFeature();

				oldValue = resolveOldValue();

				if (wasSet)
					owner.eSet(feature, oldValue);
				else
					owner.eUnset(feature);
			}
		}
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {

		if (needToModify()) {

			EObject owner = resolveOwner();

			if (owner != null) {

				EStructuralFeature feature = getFeature();

				value = resolveValue();

				owner.eUnset(feature);
			}
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldValue()
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getResult()
	 */
	public Collection getResult() {
		return Collections.singleton(getOwner());
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public Type getType() {
		return MCommand.SET;
	}

	/**
	 * Checks if feature has opposite and decides if there is aneed to modify.
	 */
	private boolean needToModify() {

		EStructuralFeature feature = getFeature();

		// ignore single features that have a many opposite.
		if (feature instanceof EReference) {

			EReference reference = (EReference) feature;

			EReference opposite = reference.getEOpposite();

			if ((!reference.isMany()) && (opposite != null)
				&& (opposite.isMany()))
				return false;
		}

		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getValue()
	 */
	private Object resolveValue() {

		if (value instanceof EObject)
			value = resolve((EObject) value);

		return value;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldValue()
	 */
	private Object resolveOldValue() {

		if (oldValue instanceof EObject)
			oldValue = resolve((EObject) oldValue);

		return oldValue;
	}
}