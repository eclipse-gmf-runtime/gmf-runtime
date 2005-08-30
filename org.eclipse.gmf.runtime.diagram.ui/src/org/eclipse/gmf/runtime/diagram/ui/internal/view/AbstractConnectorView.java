/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IView;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MRunOption;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.Anchor;
import org.eclipse.gmf.runtime.notation.Bendpoints;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;

/*
 * @canBeSeenBy %partners
 */
public abstract class AbstractConnectorView
	extends AbstractView
	implements IConnectorView {

	/**
	 * Method ConnectorView.
	 * incarnation constructor
	 * @param state
	 */
	protected AbstractConnectorView(Object state) {
		super((Edge) state);

	}

	/**
	 * <i>create</i> constructor.
	 * @param semanticAdapter semanitc element
	 * @param containerView parent element
	 * @param semanticHint a factory hint
	 * @param index position with parent's child collection
	 * @param persisted persisted flag
	 */
	protected AbstractConnectorView(final IAdaptable semanticAdapter,
		final IContainerView containerView,
		final String semanticHint,final int index,
		final boolean persisted) {
		super(NotationFactory.eINSTANCE.createEdge());
		Edge edge = getEdge();
		edge.getStyles().addAll(createStyles());
		edge.setBendpoints(createBendpoints());

		if (requiresElement(semanticAdapter, containerView)) {
			if (semanticAdapter == null)
				// enforce a set to NULL
				setSemanticElement(null);
			else
				setSemanticElement((EObject) semanticAdapter
					.getAdapter(EObject.class));
		}
		setSemanticType(semanticHint);

		// decorate view assumes the view had been inserted already, so 
		// we had to call insert child before calling decorate view
		insertChildView((AbstractView)containerView,edge, index, persisted);
		int options = MRunOption.UNCHECKED | MRunOption.SILENT;
		MEditingDomainGetter.getMEditingDomain(((AbstractView)containerView).getViewElement()).runWithOptions(new MRunnable() {
			public Object run() {
				//	decorate view had to run as a silent operation other wise
				//	it will generate too many events
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
	protected void decorateView(IContainerView containerView,
		IAdaptable semanticAdapter, String semanticHint, int index,
		boolean persisted) {
		initializeFromPreferences();
	}

	/**
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected Bendpoints createBendpoints() {
		RelativeBendpoints bendpoints = NotationFactory.eINSTANCE
			.createRelativeBendpoints();
		List points = new ArrayList(2);
		points.add(new RelativeBendpoint());
		points.add(new RelativeBendpoint());
		bendpoints.setPoints(points);
		return bendpoints;
	}

	/**
	 * Method getNConnectorView.
	 * gets the Notation view casted into NConnectorView
	 * @return NConnectorView
	 */
	Edge getEdge() {
		return (Edge) getViewElement();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#getFromView()
	 */
	public IView getSourceView() {
		return incarnateView(getEdge().getSource());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#getToView()
	 */
	public IView getTargetView() {
		return incarnateView(getEdge().getTarget());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#setFromView(IConnectableView)
	 */
	public void setSourceView(IView view) {
		if (view != null) {
			View sView = ((AbstractView) view).getViewElement();
			ViewUtil.persistElement(sView);
			getEdge().setSource(sView);
		} else
			getEdge().setSource(null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#setToView(IConnectableView)
	 */
	public void setTargetView(IView view) {
		if (view != null) {
			View tView = ((AbstractView) view).getViewElement();
			ViewUtil.persistElement(tView);
			getEdge().setTarget(tView);
		} else
			getEdge().setTarget(null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#getSourceAnchor()
	 */
	public Anchor getSourceAnchor() {
		return getEdge().getSourceAnchor();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#getTargetAnchor()
	 */
	public Anchor getTargetAnchor() {
		return getEdge().getTargetAnchor();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#setSourceAnchor(org.eclipse.gmf.runtime.notation.Anchor)
	 */
	public void setSourceAnchor(Anchor anchor) {
		getEdge().setSourceAnchor(anchor);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#setTargetAnchor(org.eclipse.gmf.runtime.notation.Anchor)
	 */
	public void setTargetAnchor(Anchor anchor) {
		getEdge().setTargetAnchor(anchor);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IConnectorView#getBendpoints()
	 */
	public Bendpoints getBendpoints() {
		return getEdge().getBendpoints();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractView#createStyles()
	 */
	protected List createStyles() {
		List styles = new ArrayList();
		styles.add(NotationFactory.eINSTANCE.createConnectorStyle());
		return styles;
	}
}