/******************************************************************************
 * Copyright (c) 2004, 2005,  IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectState;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.CanonicalStyle;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Display;


/**
 * The base canonical editpolicy class.
 * This edit policy will register itself with the model server to receive
 * semantic events fired to its host editpart.  It will create, if necessary,
 * notation elements for all semantic elements inserted into the host
 * element or delete the notation element for the semantic element removed
 * from the host element.
 * <P>
 * This editpolicy will create the necessary notation element by simply
 * returning a {@link org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand}.
 * 
 * @see #refreshSemanticChildren()
 * @see #handleSemanticEvent(NotificationEvent) will create or delete notation elements
 * as required.
 * @see #getCreateViewCommand(CreateRequest)
 * @see #shouldDeleteView(View)
 * @author mhanner
 */
public abstract class CanonicalEditPolicy extends AbstractEditPolicy 
implements PropertyChangeListener {
	
	/** Runs the supplied commands asyncronously. */
	private static class AsyncCommand extends Command {
		private final CompoundCommand _cc;
		
		
		/**
		 * constructor
		 * @param label this command label
		 */
		public AsyncCommand(String label) {
			super(label);
			_cc = new CompoundCommand(label);
		}

		/**
		 * constructor
		 * @param cmd the command
		 */
		public AsyncCommand( Command cmd ) {
			this( cmd.getLabel() );
			add( cmd );
		}

		
		/**
		 * constructor
		 * @param cmd the command
		 */
		public AsyncCommand( ICommand cmd ) {
			this( cmd.getLabel() );
			add( cmd );
		}
		
		/**
		 * Executes the command asynchonously. 
		 * Calls {@link #doExecute}.
		 */
		public final void execute() {
				Display.getCurrent().asyncExec( new Runnable() {
					public void run() {
						AsyncCommand.this.doExecute();
					}
				} );
		}
		
		/**
		 * Return the command to be executed asynchronously.
		 * @return the command
		 */
		protected final CompoundCommand getCommand() {
			return _cc;
		}
		
		/** Executes the command. */
		protected void doExecute() {
			getCommand().execute(); 
		}
		
		/**
		 * Add supplied command to the list of commands to be executed.
		 * @param cmd command to add
		 */
		public void add( ICommand cmd ) {
			_cc.add( new EtoolsProxyCommand(cmd));
		}

		
		/**
		 * Add supplied command to the list of commands to be executed.
		 * @param cmd the command to add
		 */
		public void add( Command cmd ) {
			_cc.add( cmd );
		}
	}
	
	/** [semantic element, canonical editpolicy] regisrty map. */
	static Map _registry = new WeakHashMap();
	
	/** ModelServer Listener Identifiers. */	
	private static final String SEMANTIC_FILTER_ID = "SemanticFilterID";//$NON-NLS-1$
	
	/** enable refresh flag. */
	private boolean _enabled = true;
	
	/** flag signalling a refresh request made while the editpolicy was disabled. */
	private boolean _deferredRefresh = false;
	
	/** semantic listener. */
	private Map _listenerFilters;
		
	/** Adds <code>String.class</tt> adaptablity to return a factory hint. */
	protected static final class CanonicalElementAdapter extends EObjectAdapter {
		private String _hint;
		
		/**
		 * constructor
		 * @param element
		 * @param hint
		 */
		public CanonicalElementAdapter( EObject element, String hint ) {
			super(element);
			_hint = hint;
		}
		
		/** Adds <code>String.class</tt> adaptablity. */
		public Object getAdapter(Class adapter) { 
			if ( adapter.equals(String.class) ) {
				return _hint;
			}
			return super.getAdapter(adapter);
		}
	}
	
	/** Registier this editpolicy against its semantic host. */
	private void RegisterEditPolicy() {
		EObject semanticHost = getSemanticHost();
		Set set = (Set)_registry.get(semanticHost);
		if ( set == null ) {
			set = new HashSet();
			_registry.put( semanticHost, set );
		}
		set.add(this);
	}
	
	/** Unregisters this editpolicy from the cache. */
	private void UnregisterEditPolicy() {
		EObject semanticHost = null; 

		//
		// 1st - delete unspecified refs
		Set set = (Set)_registry.get(null);
		if ( set != null ) {
			set.remove(this);
			if ( set.isEmpty() ) {
				_registry.remove(semanticHost);
			}
		}
		
		//
		// reverse key lookup since the unregistering an editpolicy
		// typically means that the semantic element has been deleted.
		Iterator keys = _registry.keySet().iterator();
		while ( keys.hasNext() ) {
			EObject key = (EObject)keys.next();
			if ( ((Set)_registry.get(key)).contains(this)) {
				semanticHost = key;
				break;
			}
		}
		
		set = (Set)_registry.get(semanticHost);
		if ( set != null ) {
			set.remove(this);
			if ( set.isEmpty() ) {
				_registry.remove(semanticHost);
			}
		}
	}
	
	/**
	 * Returns the <code>Canonical EditPolicies</code> mapped to the supplied <i>element</i>. 
	 * Canonical EditPolicies are mapped to their {@link #getSemanticHost()} as
	 * the key.  A single key may have multiple editpolicies registered against it.
	 * @param element a semantic element
	 * @return a unmodifiable list of semantic editpolicies listening to the supplied element
	 */
	public static List getRegisteredEditPolicies( EObject element ) {
		List policies = new ArrayList();
		Collection policiesWithSemanticElements = (Collection) _registry.get(element);
		if (policiesWithSemanticElements != null) {
			policies.addAll(policiesWithSemanticElements);
		}
		Collection policiesWithNullSemanticElements = (Collection) _registry.get(null);
		if (policiesWithNullSemanticElements != null) {
			policies.addAll(policiesWithNullSemanticElements);
		}
		return Collections.unmodifiableList(policies);
	}
	
	/**
	 * Returns the <b>enabled </b> <code>Canonical EditPolicies</code> mapped
	 * to the supplied <i>element </i> that are an instance of the supplied
	 * <tt>clazz</tt>. Canonical EditPolicies are mapped to their
	 * {@link #getSemanticHost()}as the key. A single key may have multiple
	 * editpolicies registered against it.
	 * 
	 * @param element
	 *            a semantic element
	 * @param clazz
	 *            a class type
	 * @return an unmodifiable list of semantic editpolicies listening to the
	 *         supplied element
	 */
	public static List getRegisteredEditPolicies( EObject element, Class clazz ) {
		List registeredPolicies = new ArrayList();
		Iterator ceps = getRegisteredEditPolicies(element).iterator();
		while( ceps.hasNext() ) {
			CanonicalEditPolicy cep = (CanonicalEditPolicy)ceps.next();
			if ( cep.isEnabled() && clazz.isInstance(cep) ) {
				registeredPolicies.add(cep);
			}
		}
		return Collections.unmodifiableList(registeredPolicies);
	}
	
	/** Asserts that the supplied host is an {@link IGraphicalEditPart} instance. */
	public void setHost(EditPart host) {
		if ( !(host instanceof IGraphicalEditPart) ) {
			throw new IllegalArgumentException();
		}
		super.setHost(host);
	}

	/**
	 * @return <code>(IGraphicalEditPart)host()</code>.
	 */
	protected final IGraphicalEditPart host() {
		return (IGraphicalEditPart)getHost();
	}

	/**
	 * Return the host's semantic children. <BR>
	 * Sample useage:<BR>
	 * Composite State (Shape): <br>
	 * <pre>
	 *		IUML2State state =  (IUML2State)resolveSemanticElement() 
	 *      return state == null 
	 *			? Collections.EMPTY_LIST
	 *			: state.getRegions();
	 * </pre><BR>
	 * Region (ShapeCompartment): <BR>
	 *		IUML2Region regions = (IUML2Region)resolveSemanticElement();
	 *		return regions == null 
	 *			? Collections.EMPTY_LIST
	 *			: regions.getSubvertices();
	 * </pre><BR>
	 * 
	 * @return a list of semantic children.
	 */
	abstract protected List getSemanticChildrenList();
	
	/** 
	 * Returns the default factory hint. 
	 * @return <code>host().getView().getSemanticType()</code>
	 */
	protected String getDefaultFactoryHint() {
		return ((View)host().getModel()).getType();
	}
	
	/**
	 * Return a factory hint to assign to this element. The supplied
	 * default hint is used if no hint can be found.
	 * @see #getFactoryHint(IAdaptable)
	 * @param elementAdapter adapter that adapts to {@link EObject}.
	 * @param defaultHint a default factory hint (typically the host's factory hint).
	 * @return a factory hint.
	 */
	protected String getFactoryHint(
		IAdaptable elementAdapter,
		final String defaultHint) {
		String fh = getFactoryHint(elementAdapter);
		return fh == null ? defaultHint : fh;
	}

	/**
	 * Clients may override this method to return an appropriate factory
	 * hint for the supplied semantic element.  Returning <tt>null</tt> will
	 * set the factory hint to the host editpart's factory hint.
	 * @see #getFactoryHint(IAdaptable, String)
	 * @param elementAdapter adapter that adapts to {@link EObject}.
	 * @return <tt>null</tt>.
	 */
	protected String getFactoryHint(IAdaptable elementAdapter) {
		return null;
	}
	
	/**
	 * Deletes a list of views.  The views will be deleted <tt>iff</tt> their semantic
	 * element has also been deleted.
	 * @param views an iterator on a list of views.
	 * @return <tt>true</tt> if the host editpart should be refreshed; either one one of the supplied
	 * views was deleted or has been reparented.
	 */
	protected final boolean deleteViews( Iterator views ) {
		if ( !isEnabled() ) {
			return false;
		}
		final CompoundCommand cc = new CompoundCommand(PresentationResourceManager.getI18NString("DeleteCommand.Label"));//$NON-NLS-1$
		while (views.hasNext()) {
			View view = (View)views.next();
			if ( shouldDeleteView(view) ) {
				cc.add(getDeleteViewCommand(view));
			}
		} 
		
		boolean doDelete = !cc.isEmpty() && cc.canExecute();
		if ( doDelete ) {
			executeCommand(cc);
		}
		return doDelete;
	}
	
	/**
	 * gets a {@link Command} to delete the supplied  {@link View}.
	 * @param view view to use
	 * @return command
	 */
	protected Command getDeleteViewCommand(View view) {
		return new EtoolsProxyCommand(new DeleteCommand(view));
	}

	/**
	 * returns <tt>true</tt> to always delete a view if required.
	 * @param view to consider
	 * @return true or false
	 */
	protected boolean shouldDeleteView(View view) {
		return true;
	}
	
	/**
	 * Return a list of all the notation elements mapped to the supplied semantic element.
	 * @param element to use
	 * @return list of <code>View</code>s
	 */
	protected List getViewReferers(EObject element) {
		List views = new ArrayList();
		if (element != null) {
			EReference[] features = {NotationPackage.eINSTANCE
					.getView_Element()};
			views.addAll(EObjectUtil.getReferencers(element, features));
		}
		return views;
	}

	
	/**
	 * This method tries to locate the position that the view will be
	 * inserted into it's parent.  The position is determined by the position
	 * of the semantic element.  If the semantic element is not found the view
	 * will be appended to it's parent.
	 * 
	 * @param semanticChild
	 * @return position where the view should be inserted
	 */
	protected int getViewIndexFor(EObject semanticChild) {
		// The default implementation returns APPEND
		return ViewUtil.APPEND;
	}
	

	/**
	 * Creates a view facade element for each of the supplied semantic elements.
	 * @param eObjects list of semantic element
	 * @return a list of {@link IAdaptable} that adapt to {@link View}.
	 */
	protected final List createViews(List eObjects) {
		List descriptors = new ArrayList();
		Iterator elements = eObjects.iterator();
		while( elements.hasNext() ) {
			EObject element = (EObject)elements.next();
			if ( element != null ) {
				CreateViewRequest.ViewDescriptor descriptor = getViewDescriptor(element);
				descriptors.add(descriptor);
			}
		}
		
		if ( !descriptors.isEmpty() ) {
			//
			// create the request
			CreateViewRequest request = getCreateViewRequest(descriptors);
			
			//
			// get the command and execute it.
			Command cmd = getCreateViewCommand(request);
			if ( cmd != null && cmd.canExecute() ) {
				SetViewMutabilityCommand.makeMutable(new EObjectAdapter(host().getNotationView())).execute();
				executeCommand(cmd);
				List adapters = (List)request.getNewObject();
				makeViewsMutable(adapters);
				return adapters;
			}
		}
		return Collections.EMPTY_LIST;
	}
		
	/**
	 * Executes the supplied command inside an <code>unchecked action</code>
	 * @param cmd command that can be executed (i.e., cmd.canExecute() == true)
	 */
	protected void executeCommand( final Command cmd ) {
		MEditingDomainGetter.getMEditingDomain((View)getHost().getModel()).runAsUnchecked( new MRunnable() {
			public Object run() { 
				cmd.execute(); 
				return null;
			}
		});
	}

	/**
	 * Returns a {@link CreateCommand} for each view descriptor contained
	 * in the supplied request without forwarding create requests to the
	 * host editpart.
	 * @param request a create request
	 * @return command create view command(s)
	 */
	protected Command getCreateViewCommand(CreateRequest request) {
		Command cmd = host().getCommand(request);
		if (cmd == null) {
			assert request instanceof CreateViewRequest;
			CompositeCommand cc = new CompositeCommand(PresentationResourceManager.getI18NString("AddCommand.Label")); //$NON-NLS-1$
			Iterator descriptors = ((CreateViewRequest)request).getViewDescriptors().iterator();

			while (descriptors.hasNext()) {
				CreateViewRequest.ViewDescriptor descriptor =
					(CreateViewRequest.ViewDescriptor)descriptors.next();

				ICommand createCommand = getCreateViewCommand(descriptor);

				cc.compose(createCommand);
			}
			cmd = new EtoolsProxyCommand(cc.unwrap());
		}
		
		return cmd;
	}
	
	/**
	 * @param descriptor 
	 * @return ICommand to create a view given a descriptor
	 */
	protected ICommand getCreateViewCommand(CreateViewRequest.ViewDescriptor descriptor) {
		CreateCommand createCommand =
			new CreateCommand(
				descriptor, 
				(View)getHost().getModel());
		return createCommand;
	}
	
	/**
	 * Return a create view request.  
	 * @param descriptors a {@link CreateViewRequest.ViewDescriptor} list.
	 * @return a create request
	 */
	protected CreateViewRequest getCreateViewRequest( List descriptors ) {
		return new CreateViewRequest( descriptors );
	}
	
	/**
	 * Return a view descriptor.
	 * @param elementAdapter semantic element
	 * @param viewKind type of view to create
	 * @param hint factory hint
	 * @param index index
	 * @return a create <i>non-persisted</i> view descriptor
	 */
	protected CreateViewRequest.ViewDescriptor getViewDescriptor(
		IAdaptable elementAdapter,
		Class viewKind,
		String hint,
		int index) {

		return new CreateViewRequest.ViewDescriptor(
				elementAdapter,
				viewKind,
				hint,
				index, 
				false, 
				host().getDiagramPreferencesHint());
	}

	/**
	 * Convenience method to create a view descriptor.  Will call
	 * {@link #getViewDescriptor(IAdaptable, Class, String, int)}
	 * @param element semantic element.
	 * @return view descriptor
	 */
	protected CreateViewRequest.ViewDescriptor getViewDescriptor( EObject element ) {
		//
		// create the view descritor
		String factoryHint = getDefaultFactoryHint();
		IAdaptable elementAdapter =
			new CanonicalElementAdapter(element, factoryHint);
		
		int pos = getViewIndexFor(element);
		CreateViewRequest.ViewDescriptor descriptor = 
			getViewDescriptor(
				elementAdapter,
				Node.class,
				getFactoryHint(elementAdapter, factoryHint),
				pos);
		return descriptor;
	}
	
	/**
	 * Registers with the model server to receive semantic events targeted to 
	 * the host editpart. By default, this editpolicy will receive events fired
	 * to the semantic element mapped to the host editpart.  Clients wanting to 
	 * listen to another semantic element should override {@link #getSemanticHost()}
	 * @see #deactivate()
	 */
	public void activate() {
		EObject semanticHost = getSemanticHost();
		if ( semanticHost != null && !isActive() ) {
			addListenerFilter(SEMANTIC_FILTER_ID, this, semanticHost);
			
			// listen to persisted children to eliminate duplicate views.
			addListenerFilter(SEMANTIC_FILTER_ID, this, semanticHost);
			// add listener to host view (handle case when user changes "visibility" property)
			addListenerFilter("NotationListener_Visibility", //$NON-NLS-1$
							  this,
							  (View)getHost().getModel(),
							  NotationPackage.eINSTANCE.getView_Visible());//$NON-NLS-1$
			
			Style style = ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
			if ( style != null ) {
				addListenerFilter("NotationListener_DrawerStyle", this, style); //$NON-NLS-1$
			}
			style = ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
			if ( style != null ) {
				addListenerFilter("NotationListener_CanonicalStyle", this, style);  //$NON-NLS-1$
			}
			
			refresh();
		}
		RegisterEditPolicy();
	}

	/**
	 * Return <tt>true</tt> if the editpolicy is enabled and its host
	 * is visible; otherwise <tt>false</tt>.
	 * @return <tt>true</tt>
	 */
	public boolean isEnabled() {
		DrawerStyle dstyle = (DrawerStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
		boolean isCollapsed = dstyle == null ? false : dstyle.isCollapsed();
		
		if ( isCollapsed ) {
			return false;
		}
		
		CanonicalStyle style = getCanonicalStyle();
		boolean enabled = _enabled && ((View)host().getModel()).isVisible();
		
		return style == null 
			? enabled
			: style.isCanonical() && enabled;
	}

	/**
	 * Disables the editpolicy.  While disabled, the editpolicy
	 * will not perform any refreshes.
	 * @param enable 
	 */
	public void enableRefresh( boolean enable ) {
		_enabled = enable;
		if ( _enabled && _deferredRefresh ) {
			_deferredRefresh = false;
			refresh();
		}
	}
	
	/**
	 * Sets enable(aBoolean) on all the edit policies of the semantic host.
	 * @param enable
	 */
	public void setEnable( boolean enable ) {
		EObject sHost = getSemanticHost();
		List registeredPolicies = getRegisteredEditPolicies(sHost);

		CanonicalEditPolicy[] policies = new CanonicalEditPolicy[registeredPolicies.size()];
		registeredPolicies.toArray(policies);
		
		for ( int i = 0; i < policies.length; i++ ) {
			policies[i].enableRefresh(enable);
		}
	}
	
	/** 
	 * check is this edit policy is active or not
	 * @return <tt>true</tt> if the this editpart has already been activated;
	 * otherwise <tt>false</tt>.
	 */
	public final boolean isActive() {
		return _listenerFilters == null 
			? false
			: _listenerFilters.containsKey(SEMANTIC_FILTER_ID);
	}
	
	/**
	 * Return the semantic element to be <i>listened</i> to by this editpolicy.
	 * @return <code>host().getView().resolveSemanticElement()</code> by default.
	 */
	public EObject getSemanticHost() {
		return ViewUtil.resolveSemanticElement((View)host().getModel()); 
	}
	
	/**
	 * Unregisters all registered model server listeners.
	 * @see #activate()
	 */
    public void deactivate() {
    	if (_listenerFilters != null) {
    		Map listeners = new HashMap(_listenerFilters);
    		Iterator keys = listeners.keySet().iterator();
    		while (keys.hasNext()) {
    			String id = (String) keys.next();
    			removeListenerFilter(id);
    		}
    	}
    	
		UnregisterEditPolicy();
    }

	
    /**
	 * Adds a listener filter by adding the given listener to a passed notifier.
	 * The supplied <tt>listener</tt> will not be added to there is already a listener
	 * registered against the supplied <tt>filterId</tt>
	 * 
	 * @param filterId A unique filter id (within the same editpart instance)
	 * @param listener A listener instance
	 * @param notifier An element notifer to add the listener to
	 * @return <tt>true</tt> if the listener was added, otherwise <tt>false</tt>
	 * @throws NullPointerException if either <tt>filterId</tt> or <tt>listner</tt> parameters are <tt>null</tt>.
	 */
	protected boolean addListenerFilter(
		String filterId,
		PropertyChangeListener listener,
		EObject element) {
		if ( filterId == null || listener == null ) {
			throw new NullPointerException();
		}

		if (element != null) {
			if (_listenerFilters == null)
				_listenerFilters = new HashMap();
			
			if ( !_listenerFilters.containsKey(filterId)) {
				PresentationListener.getInstance().addPropertyChangeListener(element,listener);
				_listenerFilters.put(filterId, new Object[] { element, listener });
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a listener filter by adding the given listener to a passed notifier.
	 * The supplied <tt>listener</tt> will not be added to there is already a listener
	 * registered against the supplied <tt>filterId</tt>
	 * 
	 * @param filterId A unique filter id (within the same editpart instance)
	 * @param listener A listener instance
	 * @param notifier An element notifer to add the listener to
	 * @return <tt>true</tt> if the listener was added, otherwise <tt>false</tt>
	 * @throws NullPointerException if either <tt>filterId</tt> or <tt>listner</tt> parameters are <tt>null</tt>.
	 */
	protected boolean addListenerFilter(
		String filterId,
		PropertyChangeListener listener,
		EObject element,
		EStructuralFeature feature) {
		if ( filterId == null || listener == null ) {
			throw new NullPointerException();
		}

		if (element != null) {
			if (_listenerFilters == null)
				_listenerFilters = new HashMap();
			
			if ( !_listenerFilters.containsKey(filterId)) {
				PresentationListener.getInstance().addPropertyChangeListener(element,feature,listener);
				_listenerFilters.put(filterId, new Object[] { element,feature, listener });
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes a listener previously added with the given id
	 * @param filterId the filter id
	 */
	protected void removeListenerFilter(String filterId) {
		if (_listenerFilters == null)
			return;
		Object[] objects = (Object[]) _listenerFilters.remove(filterId);
		if (objects == null)
			return;
		if (objects.length>2){
			PresentationListener.getInstance().removePropertyChangeListener(
				(EObject)objects[0],(EStructuralFeature)objects[1],(PropertyChangeListener) objects[2]);
		}else {
			PresentationListener.getInstance().removePropertyChangeListener(
					(EObject)objects[0],(PropertyChangeListener) objects[1]);
		}
	}
	
	/**
	 * Event callback: filters out non IElementEvent events.
	 * @param event an event fired from the model server.
	 */
	public final void propertyChange(PropertyChangeEvent event) {
		if ( isHostStillValid() && event instanceof NotificationEvent ) {
			NotificationEvent ne = (NotificationEvent)event;
			EObject element = ne.getElement();
			if ( element == null  ) {
				return;
			}
			
			handleNotificationEvent(ne);
		}
	}
	
	/**
	 * Return <tt>true</tt> if the host is active and its view has not
	 * been deleted; otherwise <tt>false</tt>
	 * @return true or false
	 */
	protected final boolean isHostStillValid() {
		return host().isActive() && !EObjectUtil.getState((EObject)host().getModel()).equals(MObjectState.DETACHED);
	}

	/** 
	 * Will invoke {@link #refreshSemantic()} if
	 * {@link #shouldHandleSemanticEvent(NotificationEvent)} returns <tt>true</tt>.
	 * 
	 * @param event a semantic event.
	 * @deprecated use {@link CanonicalEditPolicy#handleNotificationEvent} instead
	 */
	protected void handleSemanticEvent(NotificationEvent event) {
		if ( shouldHandleSemanticEvent(event) ) {
			refresh();
		}
	}
		
	/**
	 * Return <tt>true</tt> is the event is triggered by adding to or
	 * removing from one of element's slots.
	 * 
	 * @param event a semantic event
	 * @return <tt>true</tt> if {@link #refreshSemantic()} should be invoked;
	 * otherwise <tt>false</tt>.
	 * @deprecated use {@link CanonicalEditPolicy#shouldHandleNotificationEvent} instead
	 */
	protected boolean shouldHandleSemanticEvent(NotificationEvent event) {
		return event.isElementAddedToSlot() || event.isElementRemovedFromSlot();
	}
	
	/**
	 * Handles <code>NotificationEvent</code> and resynchronizes the canonical
	 * container if the event should be handled.
	 * 
	 * @param event <code>NotificationEvent</code> to handle.
	 */
	protected void handleNotificationEvent(NotificationEvent event) {
		
		boolean shouldRefresh = false;
		if ( shouldHandleNotificationEvent(event) ) {
			if ( NotationPackage.eINSTANCE.getCanonicalStyle_Canonical() == event.getFeature() ) {
				CanonicalStyle style = (CanonicalStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
				if (style != null) {
					setEnable(style.isCanonical());
				}
			}
			shouldRefresh = true;
		}
		
		// This can be removed when all clients have migrated
		// to handleNotificationEvent
		if (shouldHandleSemanticEvent(event)) {
			handleSemanticEvent(event);
			return;
		}
		
		if (shouldRefresh)
			refresh();
	}
	
	/**
	 * Determines if the the <code>NotificationEvent</code> should be handled / processed
	 * by the editpolicy.
	 * 
	 * @param event <code>NotificationEvent</code> to check
	 * @return <code>true</code> if event should be handled, <code>false</code> otherwise.
	 */
	protected boolean shouldHandleNotificationEvent( NotificationEvent event) {
	  if ( NotationPackage.eINSTANCE.getDrawerStyle_Collapsed() == event.getFeature() || 
	 	   NotationPackage.eINSTANCE.getCanonicalStyle_Canonical() == event.getFeature() ||
	 	   NotationPackage.eINSTANCE.getView_Visible() == event.getFeature() ||
	 	   NotationPackage.eINSTANCE.getView_PersistedChildren() == event.getFeature()) {
		  return true;
	  }

	  EObject element = event.getElement();
	  return (!(element instanceof View) 
	         && (event.isElementAddedToSlot() 
	            || event.isElementRemovedFromSlot()));
	}

			
	/**
	 * Return the host's model children.
	 * @return list of <code>View</Code>s
	 */
	protected List getViewChildren() {
		return new ArrayList(((View)host().getModel()).getChildren());
		
	}

	/**
	 * Resynchronize the canonical container.
	 */
	public final void refresh() {
		try {
			if ( isEnabled() ) {
				// avoid re-entry
				boolean defRefresh = _deferredRefresh;
				_deferredRefresh = false;
				refreshSemantic();
				_deferredRefresh = defRefresh;
			}
			else {
				_deferredRefresh = true;
			}
		}
		catch ( Throwable t ) {
			String eMsg = PresentationResourceManager	.getI18NString("CanonicalEditPolicy.refresh.failed_ERROR_");//$NON-NLS-1$
			Log.error(DiagramUIPlugin.getInstance(), IStatus.WARNING,
					eMsg, t);
		}
	}

	/**
	 * Redirects the call to {@link #refreshSemanticChildren()}.
	 */
	protected void refreshSemantic() {
		List createdViews = refreshSemanticChildren();
		makeViewsImmutable(createdViews);
	}

	/**
	 * Sets state on views to allow for modification without changing their
	 * non-persisted status.
	 * 
	 * @param createdViews <code<>List</code> of view adapters that were created during the 
	 * {@link CanonicalEditPolicy#refreshSemantic()} operation
	 */
	final protected void makeViewsMutable(List createdViews) {
		if (createdViews != null && !createdViews.isEmpty()) {
			List viewAdapters = prepareAdapterList(createdViews);
			executeCommand(SetViewMutabilityCommand.makeMutable(viewAdapters));
		}
	}
	
	/**
	 * Sets views as being immutable, meaning that they are unmodifiable as 
	 * non-persisted views.  Any subsequent change to an immutable view will force 
	 * the view to be persisted.
	 * 
	 * @param createdViews <code<>List</code> of view adapters that were created during the 
	 * {@link CanonicalEditPolicy#refreshSemantic()} operation
	 */
	final protected void makeViewsImmutable(List createdViews) {
		if (createdViews != null && !createdViews.isEmpty()) {
			addListenersToContainers(createdViews);
			
			List viewAdapters = prepareAdapterList(createdViews);
			Command immutable = SetViewMutabilityCommand.makeImmutable(viewAdapters);
			AsyncCommand ac = new AsyncCommand(immutable);
			ac.execute();
		}
	}

	private void addListenersToContainers(List createdViews) {
		UniqueEList list = new UniqueEList();
		ListIterator li = createdViews.listIterator();
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof IAdaptable) {
				View view = (View)((IAdaptable)obj).getAdapter(View.class);
				if (view != null)
					list.add(view.eContainer());
			}
		}
		
		ListIterator liContainers = list.listIterator();
		while (liContainers.hasNext()) {
			View containerView = (View)liContainers.next();
			addListenerFilter("NotationListener_Container" + containerView.toString(), //$NON-NLS-1$
				  this,
				  containerView,
				  NotationPackage.eINSTANCE.getView_PersistedChildren()); 
		}
	}
	
	private List prepareAdapterList(List createdViews) {
		List viewAdapters = new ArrayList();
		viewAdapters.add( host() );
		ListIterator li = createdViews.listIterator();
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj != null) {
				if (!(obj instanceof IAdaptable) && obj instanceof EObject)
					viewAdapters.add(new EObjectAdapter((EObject)obj));
				else
					viewAdapters.add(obj);
			}
		}
		return viewAdapters;
	}
	
	/**
	 * Updates the set of children views so that it
	 * is in sync with the semantic children. This method is called 
	 * in response to notification from the model.
	 * <P>
	 * The update is performed by comparing the exising views with the set of
	 * semantic children returned from {@link #getViewChildren()}. Views whose
	 * semantic element no longer exists are {@link #deleteViews(Iterator) removed}. 
	 * New semantic children have their View {@link  #createViews(List)
	 * created}.  Subclasses must override <code>getSemanticChildren()</code>.
	 * <P>
	 * Unlike <code>AbstractEditPart#refreshChildren()</code>, this refresh will not
	 * reorder the view list to ensure both it and the semantic children are
	 * in the same order since it is possible that this edit policy will handle
	 * a specifc subset of the host's views.  
	 * <P>
	 * The host is refreshed if a view has created or deleted as a result of this
	 * refresh.
	 * 
	 * @return <code>List</code> of new <code>View</code> objects that were created as a result of 
	 * the synchronization
	 */
	protected final List refreshSemanticChildren() {
		
		// Don't try to refresh children if the semantic element
		// cannot be resolved.
		if (resolveSemanticElement() == null) {
			return Collections.EMPTY_LIST;		
		}
		
		//
		// current views
		List viewChildren = getViewChildren();
		List semanticChildren = new ArrayList(getSemanticChildrenList());

		List orphaned = cleanCanonicalSemanticChildren(viewChildren, semanticChildren);

		//
		// create a view for each remaining semantic element.
		List createdViews = Collections.EMPTY_LIST;
		if ( !semanticChildren.isEmpty() ) {
			createdViews = createViews( semanticChildren );
			
			for ( int i = 0; i < createdViews.size(); i++ ) {
				View createdView = (View)((IAdaptable)createdViews.get(i)).getAdapter(View.class);
				if (createdView == null) {
					String eMsg =
						MessageFormat.format(
							PresentationResourceManager.getI18NString(
							"CanonicalEditPolicy.create.view.failed_ERROR_"),//$NON-NLS-1$
							new Object[] { semanticChildren.get(i)});
					IllegalStateException ise =
						new IllegalStateException(eMsg);
					Log.error(
						DiagramUIPlugin.getInstance(),
						IStatus.ERROR,
						eMsg,
						ise);
					throw ise;
				}
			}
		}
		
		boolean changed = false;
		//
		// delete all the remaining oprphaned views
		if ( !orphaned.isEmpty() ) {
			changed = deleteViews(orphaned.iterator());
		}
		
		if (changed || createdViews.size() > 0)
			postProcessRefreshSemantic(createdViews);
		

		return createdViews;
	}

	/**
	 * Synchronizes the semanticChildren the viewChildren to discover if any of the semanticChildren
	 * don't have a corresponding view.  Any <code>semanticChildren</code> that do have a view are
	 * removed from the list.
	 * 
	 * @param viewChildren <code>List</code> of <code>View</code> elements that already exist in the container.
	 * @param semanticChildren <code>List</code> of semantic elements that are candidates for synchronization
	 * @return <code>List</code> of orphans views that should be deleted from the container.
	 */
	final protected List cleanCanonicalSemanticChildren(Collection viewChildren, Collection semanticChildren) {
		View viewChild;
		EObject semanticChild;
		Iterator viewChildrenIT = viewChildren.iterator();
		List orphaned = new ArrayList();
		Map viewToSemanticMap = new HashMap();
		while( viewChildrenIT.hasNext() ) {
			viewChild = (View)viewChildrenIT.next();
			semanticChild = viewChild.getElement();
			if (semanticChildren.contains(semanticChild)) {
				semanticChildren.remove(semanticChild);
				viewToSemanticMap.put(semanticChild, viewChild);
			}
			else {
				orphaned.add(viewChild);
			}
			
			View viewInMap = (View)viewToSemanticMap.get(semanticChild);
			if (viewInMap != null && !viewChild.equals(viewInMap)) { 
				if (viewInMap.isMutable()) {
					orphaned.remove(viewChild);
					orphaned.add(viewInMap);
					viewToSemanticMap.put(semanticChild, viewChild);
				}
			}
		}
		return orphaned;
	}
	
	/**
	 * Allow for post processing of the refresh semantic to set the view mutable
	 * state and allow subclasses to add functionality.
	 * 
	 * @param viewDescriptors <code>List</code> of IAdaptable that adapt to <code>View</code>
	 */
	protected void postProcessRefreshSemantic(List viewDescriptors) {
		// need to refresh host to create editparts so that dependent canonical editpolicies can synchronize as well.
		getHost().refresh(); 
	}
	
	/**
	 * Convenience method to return the host's semantic element.
	 * Same as calling <code>host().getView().resolveSemanticElement();</code>
	 * @return an {@link EObject}
	 */
	protected final EObject resolveSemanticElement() {
		return ViewUtil.resolveSemanticElement((View)host().getModel());
	}

	/**
	 * gets the canonical style that may be installed on the host shape compartment view.
	 * @return <code>CanonicalStyle</code>
	 */
	protected CanonicalStyle getCanonicalStyle() {
		return (CanonicalStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
	}				
}
