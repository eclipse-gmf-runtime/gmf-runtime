/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;
 
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * A request to create an element and its view
 * @author melaasar
 */
public class CreateViewAndElementRequest extends CreateViewRequest {

	/**
	 * An extended view descriptor that takes an <code>ElementDescriptor</code> 
	 * instead of <code>IAdaptable</code> as the element adapter
	 */
	public static class ViewAndElementDescriptor extends ViewDescriptor {
		/**
		 * Constructor for ViewAndElementDescriptor.
		 * @param requestAdapter
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ViewAndElementDescriptor(CreateElementRequestAdapter requestAdapter, PreferencesHint preferencesHint) {
			super(requestAdapter, preferencesHint);
		}

		/**
		 * Constructor for ViewAndElementDescriptor.
		 * @param requestAdapter
		 * @param viewkind
		 */
		public ViewAndElementDescriptor(
			CreateElementRequestAdapter requestAdapter,
			Class viewkind,
			PreferencesHint preferencesHint) {
			super(requestAdapter, viewkind, preferencesHint);
		}

		/**
		 * Constructor for ViewAndElementDescriptor.
		 * @param requestAdapter
		 * @param viewkind
		 * @param semanticHint
		 */
		public ViewAndElementDescriptor(
			CreateElementRequestAdapter requestAdapter,
			Class viewkind,
			String semanticHint, 
			PreferencesHint preferencesHint) {
			super(requestAdapter, viewkind, semanticHint, preferencesHint);
		}

		/**
		 * Constructor for ViewAndElementDescriptor.
		 * @param requestAdapter
		 * @param viewKind
		 * @param semanticHint
		 * @param index
		 */
		public ViewAndElementDescriptor(
			CreateElementRequestAdapter requestAdapter,
			Class viewKind,
			String semanticHint,
			int index, 
			PreferencesHint preferencesHint) {
			super(requestAdapter, viewKind, semanticHint, index, preferencesHint);
		}

		/**
		 * Method getElementDescriptor.
		 * @return ElementDescriptor
		 */
		public CreateElementRequestAdapter getCreateElementRequestAdapter() {
			return (CreateElementRequestAdapter) getElementAdapter();
		}
	}

	
	/**
	 * Constructor for CreateViewAndElementRequest.
	 * @param viewAndElementDescriptor
	 */
	public CreateViewAndElementRequest(ViewAndElementDescriptor viewAndElementDescriptor) {
		super(RequestConstants.REQ_CREATE, viewAndElementDescriptor);
	}
	
	/**
	 * Constructor for CreateViewAndElementRequest.
	 * @param type
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateViewAndElementRequest(IElementType type, PreferencesHint preferencesHint) {
		super(
			new ViewAndElementDescriptor(
				new CreateElementRequestAdapter(
					new CreateElementRequest(type)), preferencesHint));
	}
	
	/**
	 * Constructor for CreateViewAndElementRequest.
	 * @param type
	 * @param context
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateViewAndElementRequest(IElementType type, EObject context, PreferencesHint preferencesHint) {
		super(
			new ViewAndElementDescriptor(
				new CreateElementRequestAdapter(
					new CreateElementRequest(context, type)), preferencesHint));		
	}

	/**
	 * Method getViewAndElementDescriptor.
	 * @return ViewAndElementDescriptor
	 */
	public ViewAndElementDescriptor getViewAndElementDescriptor() {
		return (ViewAndElementDescriptor) getViewDescriptors().get(0);
	}

}
