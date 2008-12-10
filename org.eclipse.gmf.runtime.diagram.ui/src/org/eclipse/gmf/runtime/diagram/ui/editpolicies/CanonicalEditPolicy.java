/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationUtil;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.util.EditPartUtil;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.CanonicalStyle;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;


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
 * @author mhanner, mmostafa
 */
public abstract class CanonicalEditPolicy extends AbstractEditPolicy 
implements NotificationListener {
	
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
            // do not use Display.getCurrent() this mthod could be invoked
            // on a non ui thread
            PlatformUI.getWorkbench().getDisplay().asyncExec( new Runnable() {
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
			_cc.add( new ICommandProxy(cmd));
		}

		
		/**
		 * Add supplied command to the list of commands to be executed.
		 * @param cmd the command to add
		 */
		public void add( Command cmd ) {
			_cc.add( cmd );
		}
	}
	
	/** [semantic element, canonical editpolicy] registry map. */
	static Map<EObject, Set<CanonicalEditPolicy>> _registry = new WeakHashMap<EObject, Set<CanonicalEditPolicy>>();
	
	/** ModelServer Listener Identifiers. */	
	private static final String SEMANTIC_FILTER_ID = "SemanticFilterID";//$NON-NLS-1$
	
	/** enable refresh flag. */
	private boolean _enabled = true;
	
	/** flag signaling a refresh request made while the editpolicy was disabled. */
	private boolean _deferredRefresh = false;
	
	/** semantic listener. */
	private Map<String, Object[]> _listenerFilters;
		
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
	
	/** Register this editpolicy against its semantic host. */
	private void RegisterEditPolicy() {
		EObject semanticHost = getSemanticHost();
		Set<CanonicalEditPolicy> set = _registry.get(semanticHost);
		if ( set == null ) {
			set = new HashSet<CanonicalEditPolicy>();
			_registry.put( semanticHost, set );
		}
		set.add(this);
	}
	
	/** Unregisters this editpolicy from the cache. */
	private void UnregisterEditPolicy() {
		EObject semanticHost = null; 

		// 1st - delete unspecified refs
		Set<CanonicalEditPolicy> set = _registry.get(null);
		if ( set != null ) {
			set.remove(this);
			if ( set.isEmpty() ) {
				_registry.remove(semanticHost);
			}
		}
		
		// reverse key lookup since the unregistering an editpolicy
		// typically means that the semantic element has been deleted.
		for(EObject key : _registry.keySet()) {
			if(_registry.get(key).contains(this)) {
				semanticHost = key;
				break;
			}
		}
		
		set = _registry.get(semanticHost);
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
	public static List<CanonicalEditPolicy> getRegisteredEditPolicies( EObject element ) {
		List<CanonicalEditPolicy> policies = new ArrayList<CanonicalEditPolicy>();
		Collection<CanonicalEditPolicy> policiesWithSemanticElements = _registry.get(element);
		if (policiesWithSemanticElements != null) {
			policies.addAll(policiesWithSemanticElements);
		}
		Collection<CanonicalEditPolicy> policiesWithNullSemanticElements = _registry.get(null);
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
	public static List<CanonicalEditPolicy> getRegisteredEditPolicies( EObject element, Class clazz ) {
		List<CanonicalEditPolicy> registeredPolicies = new ArrayList<CanonicalEditPolicy>();
		
		for(CanonicalEditPolicy cep : getRegisteredEditPolicies(element)) {
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
	 * @return a list of semantic children.
	 */
	abstract protected List<EObject> getSemanticChildrenList();
	
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
	protected final boolean deleteViews( Iterator<View> views ) {
		if ( !isEnabled() ) {
			return false;
		}
		final CompoundCommand cc = new CompoundCommand(DiagramUIMessages.DeleteCommand_Label);
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
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		return new ICommandProxy(new DeleteCommand(editingDomain, view));
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
	protected List<View> getViewReferers(EObject element) {
		List<View> views = new ArrayList<View>();
		if (element != null) {
			EReference[] features = {NotationPackage.eINSTANCE
					.getView_Element()};
			views.addAll(EMFCoreUtil.getReferencers(element, features));
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
	 * Creates a <code>View</code> element for each of the supplied semantic elements.
	 * @param eObjects list of semantic element
	 * @return a list of {@link IAdaptable} that adapt to {@link View}.
	 */
	protected final List<IAdaptable> createViews(List<EObject> eObjects) {
		List<ViewDescriptor> descriptors = new ArrayList<ViewDescriptor>();
		
		for(EObject element : eObjects) {
			if ( element != null ) {
				descriptors.add(getViewDescriptor(element));
			}
		}
		
		if ( !descriptors.isEmpty() ) {
			// create the request
			CreateViewRequest request = getCreateViewRequest(descriptors);
			
			// get the command and execute it.
			Command cmd = getCreateViewCommand(request);
			if ( cmd != null && cmd.canExecute() ) {
				SetViewMutabilityCommand.makeMutable(new EObjectAdapter(host().getNotationView())).execute();
				executeCommand(cmd);
				List<IAdaptable> adapters = (List<IAdaptable>)request.getNewObject();
				return adapters;
			}
		}
		return Collections.emptyList();
	}
    	
	/**
	 * Executes the supplied command inside an <code>unchecked action</code>
	 * @param cmd command that can be executed (i.e., cmd.canExecute() == true)
	 */
	protected void executeCommand( final Command cmd ) {
        Map<String, Boolean> options = null;
        EditPart ep = getHost();
        boolean isActivating = true;
        // use the viewer to determine if we are still initializing the diagram
        // do not use the DiagramEditPart.isActivating since ConnectionEditPart's
        // parent will not be a diagram edit part
        EditPartViewer viewer = ep.getViewer();
        if (viewer instanceof DiagramGraphicalViewer){
            isActivating = ((DiagramGraphicalViewer)viewer).isInitializing();
        }
        
       
        if (isActivating||
            !EditPartUtil.isWriteTransactionInProgress((IGraphicalEditPart)getHost(), false, false))
            options = Collections.singletonMap(Transaction.OPTION_UNPROTECTED,
                Boolean.TRUE);
  
        AbstractEMFOperation operation = new AbstractEMFOperation(
			((IGraphicalEditPart) getHost()).getEditingDomain(),
			StringStatics.BLANK, options) {

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {

				cmd.execute();

				return Status.OK_STATUS;
			}
		};
		try {
			operation.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"executeCommand", e); //$NON-NLS-1$
			Log.warning(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
				"executeCommand", e); //$NON-NLS-1$
		}
	}

	/**
	 * Returns a {@link CreateCommand} for each view descriptor contained
	 * in the supplied request without forwarding create requests to the
	 * host editpart.
	 * @param request a create request
	 * @return command create view command(s)
	 */
	protected Command getCreateViewCommand(CreateRequest request) {
        assert request instanceof CreateViewRequest;

        CompositeCommand cc = new CompositeCommand(DiagramUIMessages.AddCommand_Label); 
        Command cmd = host().getCommand(request);

        if (cmd == null) {
            for(ViewDescriptor descriptor : ((CreateViewRequest)request).getViewDescriptors()) {
                ICommand createCommand = getCreateViewCommand(descriptor);
                cc.compose(createCommand);
            }
        } else {
            cc.compose(new CommandProxy(cmd));
            
            for(ViewDescriptor descriptor : ((CreateViewRequest)request).getViewDescriptors()) {
            	cc.compose(new CommandProxy(SetViewMutabilityCommand.makeMutable(descriptor)));
            }
        }
        return new ICommandProxy(cc.reduce());
	}
	
	/**
	 * @param descriptor 
	 * @return ICommand to create a view given a descriptor
	 */
	protected ICommand getCreateViewCommand(CreateViewRequest.ViewDescriptor descriptor) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		CreateCommand createCommand =
            new CreateCommand(editingDomain,
				descriptor, 
				(View)getHost().getModel());
        CompositeCommand cmd = new CompositeCommand(DiagramUIMessages.AddCommand_Label);
        cmd.compose(createCommand);
        cmd.compose(new CommandProxy(SetViewMutabilityCommand.makeMutable(descriptor)));
		return cmd;
	}
	
	/**
	 * Return a create view request.  
	 * @param descriptors a {@link CreateViewRequest.ViewDescriptor} list.
	 * @return a create request
	 */
	protected CreateViewRequest getCreateViewRequest( List<ViewDescriptor> descriptors ) {
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
		// create the view descriptor
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
			// add listener to host view (handle case when user changes "visibility" property)
			addListenerFilter("NotationListener_Visibility", //$NON-NLS-1$
							  this,
							  (View)getHost().getModel(),
							  NotationPackage.eINSTANCE.getView_Visible());
			
			Style style = ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
			if ( style != null ) {
				addListenerFilter("NotationListener_DrawerStyle", this, style); //$NON-NLS-1$
			}
			style = ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
			if ( style != null ) {
				addListenerFilter("NotationListener_CanonicalStyle", this, style);  //$NON-NLS-1$
			}
			
			refreshOnActivate();
		}
		RegisterEditPolicy();
	}

	/**
	 * Refresh that is called on activate of the editpolicy to ensure that all relevant editparts
	 * can receive canonically created connections.
	 */
	protected void refreshOnActivate() {
		refresh();
	}

	/**
	 * Return <tt>true</tt> if the editpolicy is enabled and its host
	 * is visible; otherwise <tt>false</tt>.
	 * @return <tt>true</tt>
	 */
	public boolean isEnabled() {
        // if the editing domain is null then there is no point in enabling the edit policy
        // the editing domain could be null because the view is detached or if the host is detached
        if ( TransactionUtil.getEditingDomain((EObject)getHost().getModel())==null){
            return false;
        }
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
		List<CanonicalEditPolicy> registeredPolicies = getRegisteredEditPolicies(sHost);

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
    		Map<String, Object[]> listeners = new HashMap<String, Object[]>(_listenerFilters);
    		
    		for(String id: listeners.keySet()) {
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
		NotificationListener listener,
		EObject element) {
		if ( filterId == null || listener == null ) {
			throw new NullPointerException();
		}

		if (element != null) {
			if (_listenerFilters == null)
				_listenerFilters = new HashMap<String, Object[]>();
			
			if ( !_listenerFilters.containsKey(filterId)) {
				getDiagramEventBroker().addNotificationListener(element,listener);
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
		NotificationListener listener,
		EObject element,
		EStructuralFeature feature) {
		if ( filterId == null || listener == null ) {
			throw new NullPointerException();
		}

		if (element != null) {
			if (_listenerFilters == null)
				_listenerFilters = new HashMap<String, Object[]>();
			
			if ( !_listenerFilters.containsKey(filterId)) {
				getDiagramEventBroker().addNotificationListener(element,feature,listener);
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
		if (_listenerFilters == null) {
			return;
		}
		Object[] objects = (Object[]) _listenerFilters.remove(filterId);
		if (objects == null) {
			return;
		}
		if (objects.length > 2) {
			getDiagramEventBroker().removeNotificationListener(
				(EObject) objects[0], (EStructuralFeature) objects[1],
				(NotificationListener) objects[2]);
		} else {
			getDiagramEventBroker().removeNotificationListener(
				(EObject) objects[0], (NotificationListener) objects[1]);
		}
	}
	
	/**
	 * Event callback: filters out non IElementEvent events.
	 * @param event an event fired from the model server.
	 */
	public final void notifyChanged(Notification notification) {
		if ( isHostStillValid()) {
			Object element = notification.getNotifier();
			if ( element == null  ) {
				return;
			}
			
			handleNotificationEvent(notification);
		}
	}
	
	/**
	 * Return <tt>true</tt> if the host is active and its view has not
	 * been deleted; otherwise <tt>false</tt>
	 * @return true or false
	 */
	protected final boolean isHostStillValid() {
		if (!host().isActive()) {
			return false;
		}

		// is it detached?
		EObject eObject = (EObject) host().getModel();
		if (eObject != null && eObject.eResource() == null
			&& !eObject.eIsProxy()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Handles <code>NotificationEvent</code> and resynchronizes the canonical
	 * container if the event should be handled.
	 * 
	 * @param event <code>NotificationEvent</code> to handle.
	 */
	protected void handleNotificationEvent(Notification event) {
		
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
	protected boolean shouldHandleNotificationEvent(Notification event) {
	  if ( NotationPackage.eINSTANCE.getDrawerStyle_Collapsed() == event.getFeature() || 
	 	   NotationPackage.eINSTANCE.getCanonicalStyle_Canonical() == event.getFeature() ||
	 	   NotationPackage.eINSTANCE.getView_Visible() == event.getFeature() ||
	 	   NotationPackage.eINSTANCE.getView_PersistedChildren() == event.getFeature()) {
		  return true;
	  }

	  Object element = event.getNotifier();
      if (element instanceof EObject && !(element instanceof View)){
          boolean addOrDelete = (NotificationUtil.isElementAddedToSlot(event) 
                  || NotificationUtil.isElementRemovedFromSlot(event));
          EStructuralFeature feature = getFeatureToSynchronize();
          if (feature!=null){
              if (feature.equals(event.getFeature()) && 
                  (addOrDelete||NotificationUtil.isSlotModified(event))){
                      return true;
              }
              return false;
          }
          Set<EStructuralFeature> features = getFeaturesToSynchronize();
          if (features!=null && !features.isEmpty()){
              if (features.contains(event.getFeature())&&
                  (addOrDelete||NotificationUtil.isSlotModified(event))){
                  return true;
              }
              return false;
          }
          
          // just for backward compatibility will not be needed when all clients migrate
          if (addOrDelete){
                  return true;
          }
      }
      return false;      
	}

			
	/**
     * Return the host's model children.
     * @return list of <code>View</Code>s
     */
    protected List<View> getViewChildren() {
        return getViewChildren((View) host().getModel());
    }

    /**
     * Return the host's model children. This is a recursive method that handles
     * groups.
     * 
     * @param view
     *            the view to find the children for
     * @return list of children views with groups removed.
     */
    private List<View> getViewChildren(View view) {
        ArrayList<View> list = new ArrayList<View>();
        
        for(View child : (EList<View>)view.getChildren()) {
            if (child instanceof Node
                    && ViewType.GROUP.equals(((Node) child).getType())) {
                list.addAll(getViewChildren(child));
            } else {
                list.add(child);
            }        	
        }
        
        return list;
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
			String eMsg = DiagramUIMessages.CanonicalEditPolicy_refresh_failed_ERROR_;
			Log.error(DiagramUIPlugin.getInstance(), IStatus.WARNING,
					eMsg, t);
		}
	}

	/**
	 * Redirects the call to {@link #refreshSemanticChildren()}.
	 */
	protected void refreshSemantic() {
		List<IAdaptable> createdViews = refreshSemanticChildren();
		makeViewsImmutable(createdViews);
	}

	/**
	 * Sets state on views to allow for modification without changing their
	 * non-persisted status.
	 * 
	 * @param createdViews <code<>List</code> of view adapters that were created during the 
	 * {@link CanonicalEditPolicy#refreshSemantic()} operation
	 */
	final protected void makeViewsMutable(List<IAdaptable> createdViews) {
		if (createdViews != null && !createdViews.isEmpty()) {
			List<IAdaptable> viewAdapters = prepareAdapterList(createdViews);
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
	final protected void makeViewsImmutable(List<IAdaptable> createdViews) {
		if (createdViews != null && !createdViews.isEmpty()) {
			addListenersToContainers(createdViews);
			
			List<IAdaptable> viewAdapters = prepareAdapterList(createdViews);
			Command immutable = SetViewMutabilityCommand.makeImmutable(viewAdapters);
			AsyncCommand ac = new AsyncCommand(immutable);
			ac.execute();
		}
	}

	private void addListenersToContainers(List<IAdaptable> createdViews) {
		UniqueEList<View> list = new UniqueEList<View>();
		for(IAdaptable obj : createdViews) {
			View view = (View)obj.getAdapter(View.class);
			if (view != null) {
				list.add((View)view.eContainer());
			}
		}
		
		for(View containerView : list) {
			addListenerFilter("NotationListener_Container_" + containerView.getClass().getName() + '@' + Integer.toHexString(containerView.hashCode()), //$NON-NLS-1$
				  this,
				  containerView,
				  NotationPackage.eINSTANCE.getView_PersistedChildren());
		}
	}
	
	private List<IAdaptable> prepareAdapterList(List<IAdaptable> createdViews) {
		List<IAdaptable> viewAdapters = new ArrayList<IAdaptable>();
		viewAdapters.add( host() );
		
		ListIterator<IAdaptable> li = createdViews.listIterator();
		while (li.hasNext()) {
			IAdaptable adapter = li.next();
			
			if (adapter != null) {
				if (!(adapter instanceof IAdaptable) && adapter instanceof EObject) {
					viewAdapters.add(new EObjectAdapter((EObject)adapter));
				} else {
					viewAdapters.add(adapter);
				}
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
	protected final List<IAdaptable> refreshSemanticChildren() {
		
		// Don't try to refresh children if the semantic element
		// cannot be resolved.
		if (resolveSemanticElement() == null) {
			return Collections.emptyList();		
		}
		
		// current views
		List<View> viewChildren = getViewChildren();
		List<EObject> semanticChildren = new ArrayList<EObject>(getSemanticChildrenList());

		List<View> orphaned = cleanCanonicalSemanticChildren(viewChildren, semanticChildren);
		boolean changed = false;
		//
		// delete all the remaining orphaned views
		if ( !orphaned.isEmpty() ) {
			changed = deleteViews(orphaned.iterator());
		}
		
		//
		// create a view for each remaining semantic element.
		List<IAdaptable> createdViews = Collections.emptyList();
		if ( !semanticChildren.isEmpty() ) {
			createdViews = createViews( semanticChildren );
			
			for ( int i = 0; i < createdViews.size(); i++ ) {
				View createdView = (View)((IAdaptable)createdViews.get(i)).getAdapter(View.class);
				if (createdView == null) {
					String eMsg =
						NLS
						.bind(
							DiagramUIMessages.CanonicalEditPolicy_create_view_failed_ERROR_,
							semanticChildren.get(i));
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
		
		if (changed || createdViews.size() > 0) {
			postProcessRefreshSemantic(createdViews);
		}
		

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
	final protected List<View> cleanCanonicalSemanticChildren(Collection<? extends View> viewChildren, Collection<EObject> semanticChildren) {
		EObject semanticChild;
		
		List<View> orphaned = new ArrayList<View>();
		Map<EObject, View> viewToSemanticMap = new HashMap<EObject, View>();
		
		for(View viewChild : viewChildren) {
			
			semanticChild = viewChild.getElement();
			if (!isOrphaned(semanticChildren, viewChild)) {
				semanticChildren.remove(semanticChild);
				viewToSemanticMap.put(semanticChild, viewChild);
			}
			else {
				orphaned.add(viewChild);
			}
			
			View viewInMap = viewToSemanticMap.get(semanticChild);
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
     * Decide if the passed view is orphaned or not
     * 
     * @param semanticChildren
     *            semantic children to check against
     * @param view
     *            the view that should be checked
     * @return true if orphaned other wise false
     */
    protected boolean isOrphaned(Collection<EObject> semanticChildren, View view) {
        return !semanticChildren.contains(view.getElement());
    }
	
	/**
	 * Allow for post processing of the refresh semantic to set the view mutable
	 * state and allow subclasses to add functionality.
	 * 
	 * @param viewDescriptors <code>List</code> of IAdaptable that adapt to <code>View</code>
	 */
	protected void postProcessRefreshSemantic(List<IAdaptable> viewDescriptors) {
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
	
    /**
     * Gets the diagram event broker from the editing domain.
     * 
     * @return the diagram event broker
     */
    private DiagramEventBroker getDiagramEventBroker() {
        TransactionalEditingDomain theEditingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        if (theEditingDomain != null) {
            return DiagramEventBroker.getInstance(theEditingDomain);
        }
        return null;
    }

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.parts.NotificationForEditPartsListener#getViewer()
	 */
	final public EditPartViewer getViewer() {
		return getHost().getViewer();
	}
    
    /**
     * This method should be overridden by sub classes to provide the features the canonical edit policy
     * will use to synchronize the views with the semantic element
     * This method should be overridden only if the edit policy synchronizes more than one EStructuralFeature  
     * @return Set of EStructuralFeature features
     */

    protected Set<EStructuralFeature> getFeaturesToSynchronize(){
        return Collections.emptySet();
    }
    
    /**
     * This method should be overridden by sub classes to provide the feature the canonical edit policy
     * will use to synchronize the views with the semantic element
     * This method should be overridden only if the edit policy synchronizes only one EStructuralFeature  
     * @return  EStructuralFeature 
     */
    protected EStructuralFeature getFeatureToSynchronize(){
        return null;
    }
    
    /**
     * Determines if this editpolicy would create a view for the supplied 
     * semantic element.  The default implementation will return <tt>true</tt>
     * if the supplied <tt>eObject</tt> is contained in {@link #getSemanticChildrenList()}.
     * @param eObject a semantic element
     * @return <tt>true</tt> if this policy would create a view; 
     * <tt>false</tt> otherwise.
     */
    public boolean canCreate( EObject eObject ) { 
        return eObject == null 
            ? false
            : getSemanticChildrenList().contains(eObject);
    }

}
