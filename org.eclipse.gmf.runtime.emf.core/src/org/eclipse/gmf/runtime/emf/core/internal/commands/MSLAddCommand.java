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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * A basic Add command.
 * 
 * @author rafikj
 */
public final class MSLAddCommand
	extends MSLBasicCommand {

	private Object value = null;

	private Collection collection = null;

	private int position = -1;

	/**
	 * Constructor for a single add command.
	 */
	public MSLAddCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Object value, int position) {

		super(domain, owner, feature);

		this.value = value;
		this.position = position;
	}

	/**
	 * Constructor for an add many command.
	 */
	public MSLAddCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Collection collection, int position) {

		super(domain, owner, feature);

		this.collection = collection;
		this.position = position;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {

		super.dispose();

		value = null;
		collection = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {

		EObject owner = resolveOwner();

		if (owner != null) {

			EList ownerList = (EList) owner.eGet(getFeature());

			value = resolveValue();

			if ((value != null) && (ownerList.contains(value)))
				ownerList.remove(value);

			else {

				collection = resolveCollection();

				if (collection != null)
					ownerList.removeAll(collection);
			}
		}
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {

		EObject owner = resolveOwner();

		if (owner != null) {

			EList ownerList = (EList) owner.eGet(getFeature());

			value = resolveValue();

			if (value != null)
				MSLUtil.addObject(ownerList, value, position);

			else {

				collection = resolveCollection();

				if (collection != null)
					MSLUtil.addObjects(ownerList, collection, position);
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
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCollection()
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getResult()
	 */
	public Collection getResult() {

		if (value != null)
			return Collections.singleton(value);

		else if (collection != null)
			return collection;

		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getIndex()
	 */
	public int getIndex() {
		return position;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public Type getType() {
		return MCommand.ADD;
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
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCollection()
	 */
	private Collection resolveCollection() {

		if (collection != null) {

			Collection newCollection = new ArrayList(collection.size());

			boolean replace = false;

			for (Iterator i = collection.iterator(); i.hasNext();) {

				Object object = (EObject) i.next();

				if (object instanceof EObject) {

					EObject resolvedObject = resolve((EObject) object);

					if (resolvedObject != object) {

						object = resolvedObject;
						replace = true;
					}
				}

				newCollection.add(object);
			}

			if (replace)
				collection = newCollection;
		}

		return collection;
	}
}