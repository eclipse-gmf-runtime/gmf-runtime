/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.services.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 *
 * Base of view creation operations
 */
public abstract class CreateViewOperation implements IOperation {

	/** The semantic adapter */
	private final IAdaptable semanticAdapter;
	
	/** child view factory hint */
	private final String semanticHint;
	
	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connections, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private final PreferencesHint preferencesHint;

	/**
	 * Method CreateViewOperation.
	 * @param semanticRef adapts to <code<IReference</code>
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	protected CreateViewOperation(
		IAdaptable semanticAdapter,
		String semanticHint, PreferencesHint preferencesHint) {
		
		this.semanticAdapter = semanticAdapter;		
		this.semanticHint = semanticHint;
		this.preferencesHint = preferencesHint;
	}

	/**
	 * Method getSemanticAdapter.
	 * @return IAdaptable
	 */
	public final IAdaptable getSemanticAdapter() {
		return semanticAdapter;
	}

	/**
	 * Returns the view kind class
	 * 
	 * @return class of the view kind
	 */
	public abstract Class getViewKind();

	/**
	 * Method extractContainerView.
	 * Return the supplied view's container. This method requires that the supplied
	 * view be <b>incarnated</b> in order to get the container.
	 * @param view
	 * @return IContainerView
	 */
	static final View extractContainerView(View view) {
		/*return (IContainerView) ViewService.getInstance().incarnateView(
			ViewUtil.getContainerView(view));*/
		EObject container = view.eContainer();
		if (container instanceof View)
			return (View)container;
		return null;
	}

	/**
	 * Method extractSemanticHint.
	 * Return the supplied view's semantic hint
	 * @param view
	 * @return String
	 */
	static final String extractSemanticHint(View view) {
		return view.getType();
	}

	/**
	 * Method extractSemanticAdapter.
	 * @param umlView
	 * @return IAdaptable
	 */
	static final IAdaptable extractSemanticAdapter(View view) {
		EObject element = view.getElement();
		if (element  != null)
			return  new EObjectAdapter(element);
		return null;
	}

	/**
	 * Method getSemanticHint.
	 * @return String
	 */
	public final String getSemanticHint() {
		return semanticHint;
	}

	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	public final PreferencesHint getPreferencesHint() {
		return preferencesHint;
	}
	
	private String cachingKey;
	private static final String dummyHint = "dummy";  //$NON-NLS-1$

	public String getCachingKey() {
		if (cachingKey == null)
			cachingKey = determineCachingKey();
		return cachingKey;
	}
	
	private String determineCachingKey() {
		String type = getSemanticHint();
		if (type != null && type.length() > 0)
			return type;
		
		String classId = getSemanticEClassId();
		if (classId != null)
			return classId;
		
		return dummyHint;
	}

	/** Return the supplied view's underlying notation element's eClass */
	private String getSemanticEClassId() {
		if (semanticAdapter == null)
			return null;
		EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
		if (eObject != null)
			return PackageUtil.getID(EMFCoreUtil.getProxyClass(eObject));
		IElementType type = (IElementType) semanticAdapter.getAdapter(IElementType.class);
		if (type != null)
			PackageUtil.getID(type.getEClass());
		return null;
	}

}
