/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;

/**
 * Command to destroy a reference from one element to another.
 * 
 * @author ldamus
 */
public class DestroyReferenceCommand
	extends EditElementCommand {

	/**
	 * The element that contains the reference.
	 */
	private EObject container;

	/**
	 * The feature in the <code>container</code> that contains the reference.
	 */
	private EReference containingFeature;

	/**
	 * The referenced object.
	 */
	private EObject referencedObject;

	/**
	 * Constructs a new command to destroy a model element.
	 * 
	 * @param request
	 *            the destroy element requestO
	 */
	public DestroyReferenceCommand(DestroyReferenceRequest request) {

		super(request.getLabel(), request.getContainer(), request);
		this.container = request.getContainer();
		this.containingFeature = request.getContainingFeature();
		this.referencedObject = request.getReferencedObject();
	}

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		List features = new ArrayList();

		if (getContainingFeature() == null) {
			features.addAll(getContainer().eClass().getEAllReferences());

		} else {
			features.add(getContainingFeature());
		}

		for (Iterator i = features.iterator(); i.hasNext();) {
			EReference nextReference = (EReference) i.next();

			if (nextReference.isMany()) {
				Collection referenceCollection = (Collection) getContainer()
					.eGet(nextReference);

				if (referenceCollection.contains(getReferencedObject())) {
					referenceCollection.remove(getReferencedObject());
				}

			} else if (getContainer().eGet(nextReference) == getReferencedObject()) {
				getContainer().eSet(nextReference, null);
			}
		}
		return newOKCommandResult();
	}

	/**
	 * Gets element that contains the reference.
	 * 
	 * @return the element that contains the reference
	 */
	protected EObject getContainer() {
		return container;
	}

	/**
	 * Gets the feature in the <code>container</code> that contains the
	 * reference.
	 * 
	 * @return the feature in the <code>container</code> that contains the
	 *         reference.
	 */
	protected EReference getContainingFeature() {
		return containingFeature;
	}

	/**
	 * Gets the referenced object.
	 * 
	 * @return the referenced object.
	 */
	protected EObject getReferencedObject() {
		return referencedObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.commands.core.internal.commands.EditElementCommand#isExecutable()
	 */
	public boolean isExecutable() {
		return getReferencedObject() != null && getContainer() != null;
	}

}