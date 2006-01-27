/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.core.view.factories.ViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.emf.core.edit.MRunOption;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.Bendpoints;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This is the bas factory class for all connection views, it will 
 * create the view and decorate it using the default decorations
 * you can subclass it to add more decorations, or customize the 
 * way it looks, like adding new style
 * @see #createView(IAdaptable, View, String, int, boolean, String)
 * @see #decorateView(View, View, IAdaptable, String, int, boolean)
 * @see #createStyles()
 * @author mmostafa
 * 
 */

public class ConnectionViewFactory
	extends AbstractViewFactory implements ViewFactory {
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.core.view.factories.ViewFactory#createView(org.eclipse.core.runtime.IAdaptable, org.eclipse.gmf.runtime.notation.View, java.lang.String, int, boolean, java.lang.String)
	 */
	public View createView(final IAdaptable semanticAdapter,
			final View containerView, final String semanticHint,
			final int index, final boolean persisted,
			final PreferencesHint preferencesHint) {

		setPreferencesHint(preferencesHint);

		final Edge edge = NotationFactory.eINSTANCE.createEdge();
		edge.getStyles().addAll(createStyles(edge));
		edge.setBendpoints(createBendpoints());

		EObject semanticEl = semanticAdapter==null ? null : (EObject)semanticAdapter.getAdapter(EObject.class);
		if (semanticEl==null)
			// enforce a set to NULL
			edge.setElement(null);
		else if (requiresElement(semanticAdapter,containerView)){
			edge.setElement(semanticEl);
		}

		edge.setType(semanticHint);
		
		// decorate view assumes the view had been inserted already, so 
		// we had to call insert child before calling decorate view
		ViewUtil.insertChildView(containerView,edge, index, persisted);
		int options = MRunOption.UNCHECKED | MRunOption.SILENT;
		MEditingDomainGetter.getMEditingDomain(containerView).runWithOptions(new MRunnable() {

			public Object run() {
				//	decorate view had to run as a silent operation other wise
				//	it will generate too many events
				decorateView(containerView, edge, semanticAdapter,
					semanticHint, index, true);
				return null;
			}
		},options);
		return edge;
	}
	
	/**
	 * this method will create the default bend point on the connection, connections are
	 * created as Straight linem you can override this method in your own factory that 
	 * subclass this class and change this behavior by adding extra bend points or returning
	 * a new list of bend points. This method get called by @link #createView(IAdaptable, View, String, int, boolean) 
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
	 * This method is responsible for decorating the created view, it get called
	 * by the Factory method @link #createView(IAdaptable, View, String, int, boolean),
	 * it will intiliaze the view with the default preferences also it will create 
	 * the default elements of the <code>View</code> if it had any
	 * @param containerView the container of the view
	 * @param view the view itself
	 * @param element the semantic elemnent of the view (it could be null)
	 * @param semanticHint the semantic hint of the view
	 * @param index the index of the view
	 * @param persisted flag indicating the the view was created as persisted or not
	 */
	protected void decorateView(View containerView, View view, IAdaptable element,
			String semanticHint, int index, boolean persisted) {
		initializeFromPreferences(view);
	}
	

	/**
	 * this method is called by @link #createView(IAdaptable, View, String, int, boolean) to 
	 * create the styles for the view that will be created, you can override this 
	 * method in you factory sub class to provide additional styles
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles(View view) {
		List styles = new ArrayList();
		styles.add(NotationFactory.eINSTANCE.createConnectorStyle());
		return styles;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.factories.AbstractViewFactory#initializeFromPreferences(org.eclipse.gmf.runtime.notation.View)
	 */
	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);

		IPreferenceStore store = (IPreferenceStore) getPreferencesHint()
			.getPreferenceStore();

		ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getRoutingStyle_Routing(), Routing
			.get(store.getInt(IPreferenceConstants.PREF_LINE_STYLE)));
	}

}