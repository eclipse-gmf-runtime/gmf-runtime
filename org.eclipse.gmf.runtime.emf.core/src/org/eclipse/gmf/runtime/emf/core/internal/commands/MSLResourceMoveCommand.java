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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * Command encapsulating a move within some list feature of a resource.
 * 
 * @author Christian W. Damus (cdamus)
 * @author rafikj
 */
public final class MSLResourceMoveCommand
	extends MSLResourceCommand {

	private Object value = null;

	private int index = -1;

	private int oldIndex = -1;

	/**
	 * Constructor.
	 */
	public MSLResourceMoveCommand(MSLEditingDomain domain, Resource owner,
			int feature, Object value, int index, int oldIndex) {

		super(domain, owner, feature);

		this.value = value;
		this.index = index;
		this.oldIndex = oldIndex;
	}

	public void undo() {

		Resource owner = getResource();

		if (owner != null) {

			EList ownerList = (EList) eGet(owner, getFeatureID());

			if (oldIndex < ownerList.size())
				ownerList.move(oldIndex, resolveValue());
		}
	}

	public void redo() {

		Resource owner = getResource();

		if (owner != null) {

			EList ownerList = (EList) eGet(owner, getFeatureID());

			if (index < ownerList.size())
				ownerList.move(index, resolveValue());
		}
	}

	public Object getValue() {
		return value;
	}

	public int getIndex() {
		return index;
	}

	public int getOldIndex() {
		return oldIndex;
	}

	public Collection getResult() {
		return Collections.singleton(value);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public final Type getType() {
		return MCommand.RESOURCE_MOVE;
	}

	private Object resolveValue() {

		if (value instanceof EObject)
			value = resolve((EObject) value);

		return value;
	}
}