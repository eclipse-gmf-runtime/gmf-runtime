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

/**
 * A basic Move command.
 * 
 * @author rafikj
 */
public final class MSLMoveCommand
	extends MSLBasicCommand {

	private Object value = null;

	private int index = -1;

	private int oldIndex = -1;

	/**
	 * Constructor.
	 */
	public MSLMoveCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Object value, int index, int oldIndex) {

		super(domain, owner, feature);

		this.value = value;
		this.index = index;
		this.oldIndex = oldIndex;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {

		EObject owner = resolveOwner();

		if (owner != null) {

			EList ownerList = (EList) owner.eGet(getFeature());

			if (oldIndex < ownerList.size())
				ownerList.move(oldIndex, resolveValue());
		}
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {

		EObject owner = resolveOwner();

		if (owner != null) {

			EList ownerList = (EList) owner.eGet(getFeature());

			if (index < ownerList.size())
				ownerList.move(index, resolveValue());
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getIndex()
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldIndex()
	 */
	public int getOldIndex() {
		return oldIndex;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getResult()
	 */
	public Collection getResult() {
		return Collections.singleton(value);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public final Type getType() {
		return MCommand.MOVE;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getValue()
	 */
	private Object resolveValue() {

		if (value instanceof EObject)
			value = resolve((EObject) value);

		return value;
	}
}