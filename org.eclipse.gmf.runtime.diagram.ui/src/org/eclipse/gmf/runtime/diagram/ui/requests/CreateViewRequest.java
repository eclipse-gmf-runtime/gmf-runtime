/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * A request to create new <code>IView</code> (s)
 * 
 * To instantiate this request, clients have to create a <code>ViewDescriptor</code>
 * or a list of <code>ViewDescriptor</code> s filling it with view creation
 * parameters. The <code>ViewDescriptor</code> is a inner class to this
 * request
 * 
 * The request object can be used to obtain a view creation command from a
 * target <code>EditPart</code> Once such command is executed, the request
 * cannot be reused again to create another view. A different instance of the
 * reqyest has to be used instead
 *  
 * @author melaasar
 * 
 */
public class CreateViewRequest extends CreateRequest {

	/**
	 * A view descriptor that contains attributes needed to create the view The
	 * class is also a mutable adapter that initially adapts to nothing, but
	 * can adapt to <code> IView </code> after the view has been created (by
	 * executing the creation command returned from edit policies in response
	 * to this request) This is how GEF works!!
	 */
	public static class ViewDescriptor implements IAdaptable {
		/** the element adapter */
		private final IAdaptable elementAdapter;

		/** the view kind */
		private final Class viewKind;

		/** the semantic hint */
		private final String semanticHint;

		/** the index */
		private final int index;

		/** The underlying view element */
		private View view;

		/** persisted view flag. */
		private boolean _persisted;
		
		/**
		 * The hint used to find the appropriate preference store from which general
		 * diagramming preference values for properties of shapes, connections, and
		 * diagrams can be retrieved. This hint is mapped to a preference store in
		 * the {@link DiagramPreferencesRegistry}.
		 */
		private PreferencesHint preferencesHint;
		
		/**
		 * Creates a new view descriptor using element adapter
		 * 
		 * @param elementAdapter the element adapter referened by the view
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ViewDescriptor(IAdaptable elementAdapter, PreferencesHint preferencesHint) {
			this(elementAdapter, Node.class, preferencesHint);
		}

		/**
		 * Creates a new view descriptor using element adapter and a view kind
		 * 
		 * @param elementAdapter the element adapter referened by the view
		 * @param viewkind the kind of the view to be created (a concrete class
		 *            derived from IView)
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ViewDescriptor(IAdaptable elementAdapter, Class viewkind, PreferencesHint preferencesHint) {
			this(elementAdapter, viewkind, "", preferencesHint); //$NON-NLS-1$
		}
        
        /**
         * Creates a new view descriptor using element adapter and a view kind
         * 
         * @param elementAdapter the element adapter referened by the view
         * @param viewkind the kind of the view to be created (a concrete class
         *            derived from IView)
         * @param persisted
         *            indicates if the view will be created as a persisted
         *            view or transient 
         * @param preferencesHint
         *            The preference hint that is to be used to find the appropriate
         *            preference store from which to retrieve diagram preference
         *            values. The preference hint is mapped to a preference store in
         *            the preference registry <@link DiagramPreferencesRegistry>.
         */
        public ViewDescriptor(IAdaptable elementAdapter, Class viewkind,boolean persisted, PreferencesHint preferencesHint) {
            this(elementAdapter, viewkind, "",persisted, preferencesHint); //$NON-NLS-1$
        }

		/**
		 * Creates a new view descriptor using element adapter, a view kind and
		 * a factory hint
		 * 
		 * @param elementAdapter the element adapter referened by the view
		 * @param viewkind the kind of the view to be created 
		 * @param semanticHint the semantic hint of the view
         * @param preferencesHint
         *            The preference hint that is to be used to find the appropriate
         *            preference store from which to retrieve diagram preference
         *            values. The preference hint is mapped to a preference store in
         *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ViewDescriptor(
			IAdaptable elementAdapter,
			Class viewkind,
			String semanticHint,
			PreferencesHint preferencesHint) {
			this(elementAdapter, viewkind, semanticHint, ViewUtil.APPEND, preferencesHint);
		}
        
        /**
         * Creates a new view descriptor using element adapter, a view kind and
         * a factory hint
         * 
         * @param elementAdapter the element adapter referened by the view
         * @param viewkind the kind of the view to be created 
         * @param semanticHint the semantic hint of the view
         * @param persisted
         *            indicates if the view will be created as a persisted
         *            view or transient 
         * @param preferencesHint
         *            The preference hint that is to be used to find the appropriate
         *            preference store from which to retrieve diagram preference
         *            values. The preference hint is mapped to a preference store in
         *            the preference registry <@link DiagramPreferencesRegistry>.
         */
        public ViewDescriptor(
            IAdaptable elementAdapter,
            Class viewkind,
            String semanticHint,
            boolean persisted,
            PreferencesHint preferencesHint) {
            this(elementAdapter, viewkind, semanticHint, ViewUtil.APPEND,persisted, preferencesHint);
        }

		/**
		 * Creates a new view descriptor using the supplied element adapter, 
		 * view kind, factory hint and index.
		 * <P>
		 * Same as calling <code>new ViewDescriptor(elementAdapter, viewKind, factoryHint, index, true);</code>
		 * @param elementAdapter the element adapter referened by the view
		 * @param viewKind the kind of the view to be created (a concrete class derived from View)
		 * @param factoryHint the semantic hint of the view
		 * @param index the index of the view in its parent's children collection
		 */
		public ViewDescriptor(
			IAdaptable elementAdapter,
			Class viewKind,
			String factoryHint,
			int index,
			PreferencesHint preferencesHint) {
			this(elementAdapter, viewKind, factoryHint, index, true, preferencesHint);
		}

		/**
		 * Creates a new view descriptor using the supplied element adapter, 
		 * view kind, factory hint, index and persistence flag.
		 * @param elementAdapter the element adapter referened by the view
		 * @param viewKind the kind of the view to be created (a concrete class derived from View)
		 * @param semanticHint the semantic hint of the view
		 * @param index the index of the view in its parent's children collection
		 * @param persisted set <true> to create a persisted (attached) view; 
		 * <tt>false</tt> to create a detached (non-persisted) view.
		 */
		public ViewDescriptor(
			IAdaptable elementAdapter,
			Class viewKind,
			String semanticHint,
			int index,
			boolean persisted,
			PreferencesHint preferencesHint) {

			Assert.isNotNull(viewKind);
			Assert.isTrue(index >= ViewUtil.APPEND);

			this.elementAdapter = elementAdapter;
			this.viewKind = viewKind;
			this.semanticHint = semanticHint;
			this.index = index;
			_persisted = persisted;
			this.preferencesHint = preferencesHint;
		}

		/**
		 * Adapts to IView
		 * 
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
		 */
		public Object getAdapter(Class adapter) {
			if (adapter.isInstance(view))
				return view;
			return null;
		}

		/**
		 * Method setView.
		 * 
		 * @param view
		 */
		public void setView(View view) {
			this.view = view;
		}
		/**
		 * Method setPersisted.
		 * 
		 * @param persisted
		 */
		public void setPersisted( boolean persisted ) {
			_persisted = persisted;
		}
		
		/**
		 * Method getelementAdapter.
		 * 
		 * @return IAdaptable
		 */
		public IAdaptable getElementAdapter() {
			return elementAdapter;
		}
		
		/**
		 * Method getViewKind.
		 * 
		 * @return Class
		 */
		public Class getViewKind() {
			return viewKind;
		}

		/**
		 * Method getSemanticHint.
		 * 
		 * @return String
		 */
		public String getSemanticHint() {
			return semanticHint;
		}

		/**
		 * Method getIndex.
		 * 
		 * @return int
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * Return <tt>true</tt> if the view will be persisted; otherwise <tt>false</tt>
		 * @return <code>true</code> or <code>false</code>
		 */
		public final boolean isPersisted() {
			return _persisted;
		}

		/**
		 * Gets the preferences hint that is to be used to find the appropriate
		 * preference store from which to retrieve diagram preference values. The
		 * preference hint is mapped to a preference store in the preference
		 * registry <@link DiagramPreferencesRegistry>.
		 * 
		 * @return the preferences hint
		 */
		public PreferencesHint getPreferencesHint() {
			return preferencesHint;
		}
	}

	/**
	 * The view descriptors set by the user
	 */
	private List<ViewDescriptor> viewDescriptors;

	/**
	 * Convenience constructor for CreateViewRequest using a <code>IElement</code>
	 * 
	 * @param element a semantic element
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateViewRequest(EObject element, PreferencesHint preferencesHint) {
		this(new ViewDescriptor(new EObjectAdapter(element), preferencesHint));
	}

	/**
	 * Constructor for CreateViewRequest using a <code>ViewDescriptor</code>
	 * 
	 * @param viewDescriptor a view descriptor
	 */
	public CreateViewRequest(ViewDescriptor viewDescriptor) {
		this(Collections.singletonList(viewDescriptor));
	}

	/**
	 * Constructor for CreateViewRequest using a list of <code>ViewDescriptor</code>
	 * s
	 * 
	 * @param viewDescriptors a list of view descriptors
	 */
	public CreateViewRequest(List<ViewDescriptor> viewDescriptors) {
		Assert.isNotNull(viewDescriptors);
		this.viewDescriptors = viewDescriptors;
		setLocation(new Point(-1,-1));
	}

	/**
	 * Constructor for CreateViewRequest using a request type and a <code>ViewDescriptor</code>
	 * 
	 * @param type request type
	 * @param viewDescriptor a view descriptor
	 */
	public CreateViewRequest(Object type, ViewDescriptor viewDescriptor) {
		this(type, Collections.singletonList(viewDescriptor));
	}

	/**
	 * Constructor for CreateViewRequest using a request type and list of
	 * <code>ViewDescriptor</code> s
	 * 
	 * @param type the request type
	 * @param viewDescriptors a list of view descriptors
	 */
	public CreateViewRequest(Object type, List<ViewDescriptor> viewDescriptors) {
		super(type);
		Assert.isNotNull(viewDescriptors);
		this.viewDescriptors = viewDescriptors;
		setLocation(new Point(-1,-1));
	}

	/**
	 * Returns the viewDescriptors list
	 * 
	 * @return List of <code>ViewDescriptor</code> s
	 */
	public List<ViewDescriptor> getViewDescriptors() {
		return viewDescriptors;
	}

	/**
	 * A list of <code>IAdaptable</code> objects that adapt to <code>View</code>
	 * .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObject()
	 */
	public Object getNewObject() {
		return getViewDescriptors();
	}

	/**
	 * The type is a List of <code>IAdaptable</code> objects that adapt to
	 * <code>IView</code> .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObjectType()
	 */
	public Object getNewObjectType() {
		return List.class;
	}

	/**
	 * The factory mechanism is not used
	 * @throws UnsupportedOperationException
	 */
	protected CreationFactory getFactory() {
		throw new UnsupportedOperationException("The Factory mechanism is not used"); //$NON-NLS-1$
	}

	/**
	 * The factory mechanism is not used
	 */
	public void setFactory(CreationFactory factory) {
		throw new UnsupportedOperationException("The Factory mechanism is not used"); //$NON-NLS-1$
	}
}
