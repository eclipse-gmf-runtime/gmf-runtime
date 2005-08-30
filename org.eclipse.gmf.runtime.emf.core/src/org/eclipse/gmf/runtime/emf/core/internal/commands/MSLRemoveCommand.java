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
 * A basic Remove command.
 * 
 * @author rafikj
 */
public final class MSLRemoveCommand
	extends MSLBasicCommand {

	private Object oldValue = null;

	private Collection collection = null;

	private int position = -1;

	private int[] positions = null;

	/**
	 * Constructor.
	 */
	public MSLRemoveCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Object value, int position) {

		super(domain, owner, feature);

		this.position = position;
		this.oldValue = value;

		getParticipantTypes();
	}

	/**
	 * Constructor.
	 */
	public MSLRemoveCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, Collection collection, int[] positions) {

		super(domain, owner, feature);

		this.positions = positions;
		this.collection = collection;

		if ((collection != null)
			&& ((positions == null) || (positions.length == 0))) {

			this.positions = new int[collection.size()];

			for (int i = 0; i < this.positions.length; i++)
				this.positions[i] = i;
		}

		getParticipantTypes();
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {

		super.dispose();

		oldValue = null;
		collection = null;
		positions = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {

		EObject owner = resolveOwner();

		if (owner != null) {

			EList ownerList = (EList) owner.eGet(getFeature());

			oldValue = resolveOldValue();

			if (oldValue != null)
				MSLUtil.addObject(ownerList, oldValue, position);

			else {

				collection = resolveCollection();

				if (collection != null)
					MSLUtil.addObjects(ownerList, collection, positions);
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

			oldValue = getOldValue();

			if ((oldValue != null) && (ownerList.contains(oldValue)))
				ownerList.remove(oldValue);

			else {

				collection = getCollection();

				if (collection != null)
					ownerList.removeAll(collection);
			}
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldValue()
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCollection()
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getIndex()
	 */
	public int getIndex() {
		return position;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getIndices()
	 */
	public int[] getIndices() {
		return positions;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getResult()
	 */
	public Collection getResult() {

		if (oldValue != null)
			return Collections.singleton(oldValue);

		else if (collection != null)
			return collection;

		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public final Type getType() {
		return MCommand.REMOVE;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldValue()
	 */
	private Object resolveOldValue() {

		if (oldValue instanceof EObject)
			oldValue = resolve((EObject) oldValue);

		return oldValue;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCollection()
	 */
	private Collection resolveCollection() {

		if (collection != null) {

			Collection newCollection = new ArrayList(collection.size());

			boolean replace = false;

			for (Iterator i = collection.iterator(); i.hasNext();) {

				Object oldObject = (EObject) i.next();

				if (oldObject instanceof EObject) {

					EObject resolvedOldObject = resolve((EObject) oldObject);

					if (resolvedOldObject != oldObject) {

						oldObject = resolvedOldObject;
						replace = true;
					}
				}

				newCollection.add(oldObject);
			}

			if (replace)
				collection = newCollection;
		}

		return collection;
	}
}