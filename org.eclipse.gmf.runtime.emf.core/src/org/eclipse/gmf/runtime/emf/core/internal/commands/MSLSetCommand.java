/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * A basic set command.
 * 
 * @author rafikj
 */
public final class MSLSetCommand
	extends MSLBasicCommand {

	private Object value = null;

	private Object oldValue = null;

	private int position = -1;

	private boolean wasSet = false;

	/**
	 * Constructor.
	 */
	public MSLSetCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Object value, Object oldValue,
			int position, boolean wasSet) {

		super(domain, owner, feature);

		this.value = value;
		this.oldValue = oldValue;
		this.position = position;
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
		EObject owner = resolveOwner();

		if (owner != null) {

			EStructuralFeature feature = getFeature();

			EList ownerList = (feature.isMany()) ? ((EList) owner
				.eGet(feature))
				: null;

			oldValue = resolveOldValue();

			if (ownerList == null) {

				if (wasSet)
					owner.eSet(feature, oldValue);
				else
					owner.eUnset(feature);

			} else {

				if (ownerList.contains(oldValue))
					ownerList.remove(oldValue);

				value = resolveValue();

				if (ownerList.contains(value))
					ownerList.remove(value);

				MSLUtil.addObject(ownerList, oldValue, position);
			}
		}
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {
		EObject owner = resolveOwner();

		if (owner != null) {

			EStructuralFeature feature = getFeature();

			EList ownerList = (feature.isMany()) ? ((EList) owner
				.eGet(feature))
				: null;

			value = resolveValue();

			if (ownerList == null)
				owner.eSet(feature, value);

			else {

				if (ownerList.contains(value))
					ownerList.remove(value);

				oldValue = resolveOldValue();

				if (ownerList.contains(oldValue))
					ownerList.remove(oldValue);

				MSLUtil.addObject(ownerList, value, position);
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