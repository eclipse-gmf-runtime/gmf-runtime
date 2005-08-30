/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
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
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * Command encapsulating an addition to some list feature of a resource.
 * 
 * @author Christian W. Damus (cdamus)
 * @author rafikj
 */
public final class MSLResourceAddCommand
	extends MSLResourceCommand {

	private Object value = null;

	private Collection collection = null;

	private int position = -1;

	/**
	 * Constructor for a single add command.
	 */
	public MSLResourceAddCommand(MSLEditingDomain domain, Resource owner,
			int feature, Object value, int position) {

		super(domain, owner, feature);

		this.value = value;
		this.position = position;
	}

	/**
	 * Constructor for an add many command.
	 */
	public MSLResourceAddCommand(MSLEditingDomain domain, Resource owner,
			int feature, Collection collection, int position) {

		super(domain, owner, feature);

		this.collection = collection;
		this.position = position;
	}

	public void dispose() {

		super.dispose();

		value = null;
		collection = null;
	}

	public void undo() {

		Resource owner = getResource();

		if (owner != null) {

			EList ownerList = (EList) eGet(owner, getFeatureID());

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

	public void redo() {

		Resource owner = getResource();

		if (owner != null) {

			EList ownerList = (EList) eGet(owner, getFeatureID());

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

	public Object getValue() {
		return value;
	}

	public Collection getCollection() {
		return collection;
	}

	public Collection getResult() {

		if (value != null)
			return Collections.singleton(value);

		else if (collection != null)
			return collection;

		return Collections.EMPTY_LIST;
	}

	public int getIndex() {
		return position;
	}

	public Type getType() {
		return MCommand.RESOURCE_ADD;
	}

	private Object resolveValue() {

		if (value instanceof EObject)
			value = resolve((EObject) value);

		return value;
	}

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