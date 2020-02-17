/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.emf.type.core.EditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Request to move a collections of model elements from one location to another.
 * The request can specify the target features that should be used to contain
 * each of the elements being moved.
 * <P>
 * If the target feature is not specified for a given element being moved, then
 * a default feature is found in the target according to the following rules:
 * <UL>
 * <LI>If the feature fomerly containing the moved element exists in the target
 * element, it will be used.</LI>
 * <LI>Otherwise, the first feature in the target that can contain the moved
 * element will be used.</LI>
 * </UL>
 * 
 * @author ldamus
 */
public class MoveRequest
    extends AbstractEditCommandRequest {

    /**
     * The map of <code>EObject</code>s to be moved. Keyed on
     * <code>EObject</code>. Each value is the <code>EReference</code>
     * feature in the target element into which the element should be moved.
     * <P>
     * If the feature is not specified for a given element, then a default
     * feature is found in the target.
     */
    private final Map elementsToMove;

    /**
     * The new container for the element to be moved.
     */
    private EObject targetContainer;

    /**
     * Constructs a new request to move a model element from one container to
     * another.
     * 
     * @param editingDomain
     *            the editing domain in which I am requesting to make model
     * @param targetContainer
     *            the target container
     * @param targetFeature
     *            the target feature
     * @param elementToMove
     *            the element to be moved
     */
    public MoveRequest(TransactionalEditingDomain editingDomain,
            EObject targetContainer, EReference targetFeature,
            EObject elementToMove) {

        super(editingDomain);
        this.targetContainer = targetContainer;
        this.elementsToMove = new HashMap();
        elementsToMove.put(elementToMove, targetFeature);
    }

    /**
     * Constructs a new request to move a model element from one container to
     * another.
     * 
     * @param editingDomain
     *            the editing domain in which I am requesting to make model
     * @param targetContainer
     *            the target container
     * @param elementToMove
     *            the element to be moved.
     */
    public MoveRequest(TransactionalEditingDomain editingDomain,
            EObject targetContainer, EObject elementToMove) {

        this(editingDomain, targetContainer, null, elementToMove);
    }

    /**
     * Constructs a new request to move a collection of model element into a new
     * container. The features in the target used to contain the moved elements
     * will be derived as follows:
     * <UL>
     * <LI>If the feature fomerly containing the moved element exists in the
     * target element, it will be used.</LI>
     * <LI>Otherwise, the first feature in the target that can contain the
     * moved element will be used.</LI>
     * </UL>
     * 
     * @param editingDomain
     *            the editing domain in which I am requesting to make model
     * @param targetContainer
     *            the target container
     * @param elementsToMove
     *            the list of <code>EObjects</code> to be moved.
     */
    public MoveRequest(TransactionalEditingDomain editingDomain,
            EObject targetContainer, List elementsToMove) {

        super(editingDomain);
        this.targetContainer = targetContainer;
        this.elementsToMove = new HashMap();

        for (Iterator i = elementsToMove.iterator(); i.hasNext();) {
            this.elementsToMove.put(i.next(), null);
        }
    }

    /**
     * Constructs a new request to move a collection of model element into
     * specific features of a new container.
     * 
     * @param editingDomain
     *            the editing domain in which I am requesting to make model
     * @param targetContainer
     *            the target container
     * @param elementsToMove
     *            the map of <code>EObjects</code> to <code>EReference</code>
     *            features to be moved.
     */
    public MoveRequest(TransactionalEditingDomain editingDomain,
            EObject targetContainer, Map elementsToMove) {

        super(editingDomain);
        this.targetContainer = targetContainer;
        this.elementsToMove = elementsToMove;
    }

    /**
     * Constructs a new request to move a model element from one container to
     * another. The editing domain is derived from the
     * <code>targetContainer</code>.
     * 
     * @param targetContainer
     *            the target container
     * @param targetFeature
     *            the target feature
     * @param elementToMove
     *            the element to be moved
     */
    public MoveRequest(EObject targetContainer, EReference targetFeature,
            EObject elementToMove) {

        this(TransactionUtil.getEditingDomain(targetContainer),
            targetContainer, targetFeature, elementToMove);
    }

    /**
     * Constructs a new request to move a model element from one container to
     * another. The editing domain is derived from the
     * <code>targetContainer</code>.
     * 
     * @param targetContainer
     *            the target container
     * @param elementToMove
     *            the element to be moved.
     */
    public MoveRequest(EObject targetContainer, EObject elementToMove) {

        this(TransactionUtil.getEditingDomain(targetContainer),
            targetContainer, null, elementToMove);
    }

    /**
     * Constructs a new request to move a collection of model element into a new
     * container. The features in the target used to contain the moved elements
     * will be derived as follows:
     * <UL>
     * <LI>If the feature fomerly containing the moved element exists in the
     * target element, it will be used.</LI>
     * <LI>Otherwise, the first feature in the target that can contain the
     * moved element will be used.</LI>
     * </UL>
     * <P>
     * The editing domain is derived from the <code>targetContainer</code>.
     * 
     * @param targetContainer
     *            the target container
     * @param elementsToMove
     *            the list of <code>EObjects</code> to be moved.
     */
    public MoveRequest(EObject targetContainer, List elementsToMove) {

        this(TransactionUtil.getEditingDomain(targetContainer),
            targetContainer, elementsToMove);
    }

    /**
     * Constructs a new request to move a collection of model element into
     * specific features of a new container. The editing domain is derived from
     * the <code>targetContainer</code>.
     * 
     * @param targetContainer
     *            the target container
     * @param elementsToMove
     *            the map of <code>EObjects</code> to <code>EReference</code>
     *            features to be moved.
     */
    public MoveRequest(EObject targetContainer, Map elementsToMove) {

        this(TransactionUtil.getEditingDomain(targetContainer),
            targetContainer, elementsToMove);
    }

    /**
     * Gets the map of elements to be moved. Each entry in the map consists of
     * an <code>EObject</code> key, which is the element to be moved to the
     * new target, and an <code>EReference</code> value, which is the feature
     * in the new target that should contain the moved element.
     * 
     * @return the map of elements to be moved
     */
    public Map getElementsToMove() {
        return elementsToMove;
    }

    /**
     * Sets the container into which the element will be moved.
     * 
     * @param targetContainer
     *            the target container
     */
    public void setTargetContainer(EObject targetContainer) {
        this.targetContainer = targetContainer;
    }

    /**
     * Gets the container into which the element will be moved.
     * 
     * @return the container into which the element will be moved
     */
    public EObject getTargetContainer() {
        return targetContainer;
    }

    /**
     * Sets the reference feature into which an element should be moved.
     * 
     * @param element
     *            the element to be moved
     * @param targetFeature
     *            the target feature
     */
    public void setTargetFeature(EObject element, EReference targetFeature) {
        getElementsToMove().put(element, targetFeature);
    }

    /**
     * Gets the feature in the target element that should contain
     * <code>element</code> after it is moved.
     * 
     * @param element
     *            the element to be moved
     * @return the feature that will contain the element in the target
     */
    public EReference getTargetFeature(EObject element) {
        return (EReference) getElementsToMove().get(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
     */
    public List getElementsToEdit() {

        if (targetContainer != null) {
            return Collections.singletonList(targetContainer);
        }

        return super.getElementsToEdit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
     */
    protected String getDefaultLabel() {
        return EMFTypeCoreMessages.Request_Label_Move;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
     */
    public Object getEditHelperContext() {
    	IClientContext context = getClientContext();
    	
    	if (context == null) {
    		return targetContainer;
    	} else {
    		return new EditHelperContext(targetContainer, context);
    	}
    }

}