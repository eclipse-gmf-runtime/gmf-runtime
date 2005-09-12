/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView;
import org.eclipse.gmf.runtime.emf.core.edit.MRunOption;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;

/**
 * @author melaasar
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class AbstractNodeView extends AbstractView {

	/**
	 * Method NodeView.
	 * incarnation constructor
	 * @param state
	 */
	public AbstractNodeView(Object state) {
		super((Node) state);
	}

	/**
	 * Method NodeView.
	 * creation constructor
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	public AbstractNodeView(
		final IAdaptable semanticAdapter,
		final IContainerView containerView,
		final String semanticHint,
		final int index,
		boolean persisted) {
		Node node = createNode();
		setView(node);
		node.getStyles().addAll(createStyles());
		node.setLayoutConstraint(createLayoutConstraint());

		if (requiresElement(semanticAdapter,containerView)){
			if (semanticAdapter==null)
				// enforce a set to NULL
				setSemanticElement(null);
			else
				setSemanticElement((EObject)semanticAdapter.getAdapter(EObject.class));
		}
		setSemanticType(semanticHint);
		// decorate view assumes that the view had been inserted already, so 
		// we had to call insert child before calling decorate view
		insertChildView((AbstractView)containerView,node,index, persisted);
				
		
		int options = MRunOption.UNCHECKED | MRunOption.SILENT;
		MEditingDomainGetter.getMEditingDomain(((AbstractView)containerView).getViewElement()).runWithOptions(new MRunnable() {
			public Object run() {
				// decorate view had to run as a silent operation other wise
				//it will generate too many events 
				decorateView(containerView, semanticAdapter, semanticHint, index, true);
				return null;
			}
		}, options);
	}

	
	
	/**
	 * Method NodeView.
	 * creation constructor
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	protected void decorateView(IContainerView containerView, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted){
		initializeFromPreferences();
	}

	/**
	 * @return a new layout constraint for the view
	 */
	protected LayoutConstraint createLayoutConstraint() {
		return null;
	}

	/**
	 * @return
	 */
	Node getNode() {
		return (Node) getViewElement();
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#isPropertySupported(java.lang.Object)
	 */
	protected boolean isPropertySupported(EStructuralFeature feature, EClass featureClass) {
		LayoutConstraint constraint = getNode().getLayoutConstraint();
		if (constraint != null && featureClass.isInstance(constraint))
			return true;
		return super.isPropertySupported(feature, featureClass);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getPropertyValue(java.lang.Object)
	 */
	protected Object getPropertyValue(EStructuralFeature feature, EClass featureClass) {
		LayoutConstraint constraint = getNode().getLayoutConstraint();
		if (constraint != null && featureClass.isInstance(constraint))
			return constraint.eGet(feature);
		return super.getPropertyValue(feature, featureClass);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	protected void setPropertyValue(EStructuralFeature feature, EClass featureClass, Object value) {
		LayoutConstraint constraint = getNode().getLayoutConstraint();
		if (constraint != null & featureClass.isInstance(constraint)) {
			constraint.eSet(feature, value);
			return;
		}
		super.setPropertyValue(feature, featureClass, value);
	}
	
	protected Node createNode(){
		return NotationFactory.eINSTANCE.createNode();
	}

}
