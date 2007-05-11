/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.requests.TargetRequest;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionBendpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PropertyHandlerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ToggleCanonicalModeCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IContainedEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.SanpToHelperUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ConnectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ConnectionLineSegEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.TreeConnectionBendpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpolicy.EditPolicyService;
import org.eclipse.gmf.runtime.diagram.ui.internal.type.NotationTypeUtil;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.diagram.ui.util.EditPartUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.OrthogonalRouter;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.GraphicalEditPolicyEx;
import org.eclipse.gmf.runtime.gef.ui.internal.l10n.Cursors;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.SelectConnectionEditPartTracker;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.JumpLinkStatus;
import org.eclipse.gmf.runtime.notation.JumpLinkType;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.gmf.runtime.notation.Smoothness;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.IActionFilter;

/**
 * the base edit part that controls <code>Edge</code> views, it is the basic
 * controller for the connection's view
 * 
 * @author mmostafa
 */
abstract public class ConnectionEditPart
    extends AbstractConnectionEditPart
    implements IGraphicalEditPart, PropertyChangeListener, IContainedEditPart,
    IPrimaryEditPart, NotificationListener {

    /** A map of listener filters ids to filter data */
    private Map listenerFilters;

    /** Used for registering and unregistering the edit part */
    private String elementGuid;

    /**
     * Flag to indicate if the edit part is in edit mode
     */
    private boolean isEditable = true;

    /** Used for accessibility. */
    protected AccessibleEditPart accessibleEP;

    /**
     * Cache the editing domain after it is retrieved.
     */
    private TransactionalEditingDomain editingDomain;

     
    /**
     * Cache the font data when a font is created so that it can be
     * disposed later.
     */
    private FontData cachedFontData;
    
    /**
	 * Cache the answer to whether or not this is a semantic connection after it
	 * is retrieved.
	 */
    private Boolean semanticConnection;
    
    /** counter that tracs the recursive depth of the getCommand() method. */
    private static volatile int GETCOMMAND_RECURSIVE_COUNT = 0;
    
    /** A list of editparts who's canonical editpolicies are to be temporarily disabled. */
    private static Set _disableCanonicalEditPolicyList = new HashSet();
    
    /**
     * gets a property change command for the passed property, using both of the
     * old and new values
     * 
     * @param property
     *            the property associated with the command
     * @param oldValue
     *            the old value associated with the command
     * @param newValue
     *            the new value associated with the command
     * @return a command
     */
    protected Command getPropertyChangeCommand(Object property,
            Object oldValue, Object newValue) {
        // by default return null, which means there is no special command to
        // change the property
        return null;
    }
    
    

    protected void addChild(EditPart child, int index) {
        super.addChild(child, index);
        if (child instanceof GraphicalEditPart){
            GraphicalEditPart gEP = (GraphicalEditPart)child;
            boolean editMode = isEditModeEnabled(); 
            if (editMode != gEP.isEditModeEnabled()){
                if (editMode)
                    gEP.enableEditMode();
                else
                    gEP.disableEditMode();
            }
        }
    }



    /**
     * Register the adapters for the standard properties.
     */
    static {
        registerAdapters();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate() {
    	
    	if (isActive()) {
            return;
        }
    	
        addNotationalListeners();

        EObject semanticProxy = ((View) getModel()).getElement();
        EObject semanticElement = EMFCoreUtil.resolve(getEditingDomain(),
            semanticProxy);

        if (semanticElement != null)
            addSemanticListeners();
        else if (semanticProxy != null) {
            addListenerFilter("SemanticProxy", this, semanticProxy); //$NON-NLS-1$
        }
        super.activate();
    }

    /**
     * Adds a listener filter by adding the given listener to a passed notifier
     * 
     * @param filterId
     *            A unique filter id (within the same editpart instance)
     * @param listener
     *            A listener instance
     * @param notifier
     *            An element notifer to add the listener to
     */
    protected void addListenerFilter(String filterId,
            NotificationListener listener, EObject element) {

        if (element == null)
            return;

    	assert filterId != null;
    	assert listener != null;

        if (listenerFilters == null)
            listenerFilters = new HashMap();

        getDiagramEventBroker().addNotificationListener(element, listener);
        listenerFilters.put(filterId, new Object[] {element, listener});
    }

    /**
     * Adds a listener filter by adding the given listener to a passed notifier
     * 
     * @param filterId
     *            A unique filter id (within the same editpart instance)
     * @param listener
     *            A listener instance
     * @param notifier
     *            An element notifer to add the listener to
     */
    protected void addListenerFilter(String filterId,
            NotificationListener listener, EObject element,
            EStructuralFeature feature) {

        if (element == null)
            return;

    	assert filterId != null;
    	assert listener != null;

        if (listenerFilters == null)
            listenerFilters = new HashMap();

        getDiagramEventBroker().addNotificationListener(element, feature,
            listener);
        listenerFilters
            .put(filterId, new Object[] {element, feature, listener});
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
     */
    final protected EditPart createChild(Object model) {
        return EditPartService.getInstance()
            .createGraphicEditPart((View) model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createConnection(java.lang.Object)
     */
    final protected org.eclipse.gef.ConnectionEditPart createConnection(
            Object connectionView) {
        return (org.eclipse.gef.ConnectionEditPart) createChild(connectionView);
    }

    /**
     * Overridden to support editpolicies installed programmatically and via the
     * <code>EditPolicyService</code>. Subclasses should override
     * <code>createDefaultEditPolicies()</code>.
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    final protected void createEditPolicies() {
        createDefaultEditPolicies();
        EditPolicyService.getInstance().createEditPolicies(this);
    }

    /**
     * Should be overridden to install editpolicies programmatically.
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createDefaultEditPolicies() {
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
            new SemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.PROPERTY_HANDLER_ROLE,
            new PropertyHandlerEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
            new org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE,
            new ConnectionEditPolicy());
        installBendpointEditPolicy();
        installEditPolicy(EditPolicyRoles.DECORATION_ROLE,
            new DecorationEditPolicy());
        installEditPolicy(EditPolicyRoles.CONNECTION_LABELS_ROLE,
            new ConnectionLabelsEditPolicy());

        installEditPolicy(EditPolicyRoles.SNAP_FEEDBACK_ROLE,
            new SnapFeedbackPolicy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.EditPart#deactivate()
     */
   public void deactivate() {
	   
	   if (!isActive()) {
           return;
       }
	   
        boolean wasActive = isActive();
        super.deactivate();
        if (listenerFilters != null && wasActive != isActive()) {
            for (Iterator i = listenerFilters.keySet().iterator(); i.hasNext();) {
                Object[] obj = (Object[]) listenerFilters.get(i.next());
                if (obj.length > 2) {
                    getDiagramEventBroker().removeNotificationListener(
                        (EObject) obj[0], (EStructuralFeature) obj[1],
                        (NotificationListener) obj[2]);
                } else {
                    getDiagramEventBroker().removeNotificationListener(
                        (EObject) obj[0], (NotificationListener) obj[1]);
                }
            }
        }
    }

    public void removeNotify() {
        super.removeNotify();
        
        if (cachedFontData != null) {
            getResourceManager().destroyFont(
                FontDescriptor.createFrom(cachedFontData));
            cachedFontData = null;
        }
    }

    /**
     * executes the passed command
     * 
     * @param command
     *            the command to execute
     */
    protected void executeCommand(Command command) {
        getEditDomain().getCommandStack().execute(command);
    }

    /**
     * a function that registers this provider with the Eclipse AdapterManager
     * as an IView and an IActionFilter adapter factory for the
     * IGraphicalEditPart nodes
     * 
     */
    static private void registerAdapters() {
        Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

            /**
             * @see org.eclipse.core.runtime.IAdapterFactory
             */
            public Object getAdapter(Object adaptableObject, Class adapterType) {

                IGraphicalEditPart gep = (IGraphicalEditPart) adaptableObject;

                if (adapterType == IActionFilter.class) {
                    return ActionFilterService.getInstance();
                } else if (adapterType == View.class) {
                    return gep.getModel();
                }
                return null;
            }

            /**
             * @see org.eclipse.core.runtime.IAdapterFactory
             */
            public Class[] getAdapterList() {
                return new Class[] {IActionFilter.class, View.class};
            }

        }, IGraphicalEditPart.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()
     */
    protected AccessibleEditPart getAccessibleEditPart() {
        if (accessibleEP == null) {
            accessibleEP = new AccessibleGraphicalEditPart() {

                private String getSemanticName() {
                    EObject semanticElement = resolveSemanticElement();

                    if (semanticElement != null) {
                        String name = semanticElement.getClass().getName();
                        int startIndex = name.lastIndexOf('.') + 1;
                        int endIndex = name.lastIndexOf("Impl"); //$NON-NLS-1$
                        return name.substring(startIndex, endIndex);
                    }

                    return DiagramUIMessages.Accessible_Connection_Label;
                }

                public void getName(AccessibleEvent e) {

                    EditPart sourceEP = getSource();
                    EditPart targetEP = getTarget();

                    // Get the Connection Name
                    String connectionName = getSemanticName();

                    // Get the Source Name
                    String sourceName = null;
                    if (sourceEP != null) {
                        AccessibleEditPart aEP = (AccessibleEditPart) sourceEP
                            .getAdapter(AccessibleEditPart.class);
                        AccessibleEvent event = new AccessibleEvent(this);
                        aEP.getName(event);
                        sourceName = event.result;
                    }

                    // Get the Target Name
                    String targetName = null;
                    if (targetEP != null) {
                        AccessibleEditPart aEP = (AccessibleEditPart) targetEP
                            .getAdapter(AccessibleEditPart.class);
                        AccessibleEvent event = new AccessibleEvent(this);
                        aEP.getName(event);
                        targetName = event.result;
                    }

                    if (sourceName != null && targetName != null) {
                        e.result = NLS
                            .bind(
                                DiagramUIMessages.Accessible_Connection_From_Source_To_Target,
                                new Object[] {connectionName, sourceName,
                                    targetName});
                    } else if (sourceName != null) {
                        e.result = NLS
                            .bind(
                                DiagramUIMessages.Accessible_Connection_From_Source,
                                new Object[] {connectionName, sourceName});
                    } else if (targetName != null) {
                        e.result = NLS.bind(
                            DiagramUIMessages.Accessible_Connection_To_Target,
                            new Object[] {connectionName, targetName});
                    } else {
                        e.result = connectionName;
                    }
                }
            };
        }
        return accessibleEP;
    }

    /**
     * Adds the ability to adapt to this editpart's view class.
     */
    public Object getAdapter(Class key) {
        Object adapter = Platform.getAdapterManager().getAdapter(this, key);
        if (adapter != null) {
            return adapter;
        }

        if (key == SnapToHelper.class) {
            return SanpToHelperUtil.getSnapHelper(this);
        }

        Object model = getModel();

        if (View.class.isAssignableFrom(key) && key.isInstance(model)) {
            return model;
        }

        if (model != null && model instanceof View) {
            // Adapt to semantic element
            EObject semanticObject = ViewUtil
                .resolveSemanticElement((View) model);
            if (key.isInstance(semanticObject)) {
                return semanticObject;
            }else if (key.isInstance(model)) {
                return model;
            }
        }
        return super.getAdapter(key);
    }

    /**
     * Method getChildBySemanticHint.
     * 
     * @param semanticHint
     * @return IGraphicalEditPart
     */
    public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
        if (getModel() != null) {
            View view = ViewUtil.getChildBySemanticHint((View) getModel(),
                semanticHint);
            if (view != null)
                return (IGraphicalEditPart) getViewer().getEditPartRegistry()
                    .get(view);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.EditPart#getCommand(org.eclipse.gef.Request)
     */
    public Command getCommand(Request _request) {
        if (!isEditModeEnabled()) {
            return UnexecutableCommand.INSTANCE;
        }

        Command cmd = null;
        try {
            GETCOMMAND_RECURSIVE_COUNT++;
            final Request request = _request;
            try {
                cmd = (Command) getEditingDomain().runExclusive(
                    new RunnableWithResult.Impl() {

                        public void run() {
                            setResult(ConnectionEditPart.super
                                .getCommand(request));
                        }
                    });
            } catch (InterruptedException e) {
                Trace.catching(DiagramUIPlugin.getInstance(),
                    DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                    "getCommand", e); //$NON-NLS-1$
                Log.error(DiagramUIPlugin.getInstance(),
                    DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
                    "getCommand", e); //$NON-NLS-1$
            }
            

            if ( cmd != null ) {
                _disableCanonicalEditPolicyList.addAll(disableCanonicalFor(_request));
            }
            
            GETCOMMAND_RECURSIVE_COUNT--;
            
            if ( GETCOMMAND_RECURSIVE_COUNT == 0 ) {
                if ( cmd != null 
                        && !_disableCanonicalEditPolicyList.isEmpty() ) {
                    CompoundCommand cc = new CompoundCommand();
                    cc.setLabel( cmd.getLabel() );
                    ToggleCanonicalModeCommand tcmd = 
                        ToggleCanonicalModeCommand.getToggleCanonicalModeCommand(_disableCanonicalEditPolicyList, false);
                    cc.add( tcmd );
                    cc.add( cmd );
                    ToggleCanonicalModeCommand tcmd2 = ToggleCanonicalModeCommand.getToggleCanonicalModeCommand(tcmd, true);
                    if (tcmd2 != null) {
                        tcmd2.setDomain(getEditingDomain());
                    }
                    cc.add( tcmd2 );
                    _disableCanonicalEditPolicyList.clear();
                    return cc.unwrap();
                }
            }
        }
        catch( RuntimeException t ) {
            GETCOMMAND_RECURSIVE_COUNT = 0;
            throw t;
        }
        return cmd;
    }
    
    /**
     * Return a list of editparts who's canonical editpolicies should be disabled
     * prior to executing the commands associated to the supplied request.
     * This implementation will return the editpart honoring a <code>SemanticWrapperRequest</code>
     * and a <code>CreateConnectionViewRequest</code>'s source and target editparts.
     *
     * @param request a request that has returned a command.
     * @return list of editparts.
     */
    protected Collection disableCanonicalFor( final Request request ) {
        //
        // not the most generic of solution; however, it will have to do for now...
        //
        // Alternate solutions
        // 1. common request interface on all the requests
        //  IRequest#getAffectedEditParts
        //
        // 2. Traverse down the command and collect of the ICommand#getAffectedObjects()
        //  -- this requires that all our commands properly set this value.
        
        Set hosts = new HashSet();
        if ( (request instanceof EditCommandRequestWrapper)  
                || request instanceof TargetRequest
                || request instanceof DropRequest ) {
            hosts.add(this);
            hosts.add(getParent());
        }
        if((request instanceof ReconnectRequest)) {
            ReconnectRequest reconnect = (ReconnectRequest)request;
            hosts.add(this);
            hosts.add(getParent());
            if(reconnect.getTarget() != null) {
                EditPart target  = reconnect.getTarget();
                addEditPartAndParent(hosts, target);
            }
            if(reconnect.getConnectionEditPart() != null) {
                org.eclipse.gef.ConnectionEditPart connectionEditPart = reconnect.getConnectionEditPart();
                if(connectionEditPart.getSource() != null) {
                    EditPart srcEP = connectionEditPart.getSource();
                    addEditPartAndParent(hosts, srcEP);
                }
                if(connectionEditPart.getTarget() != null) {
                    EditPart trgEP = connectionEditPart.getTarget();
                    addEditPartAndParent(hosts, trgEP);
                }
            }
        }
        if ((request instanceof CreateConnectionRequest) ) {
            CreateConnectionRequest ccvr = (CreateConnectionRequest)request;
            hosts.add(this);
            hosts.add(getParent());
            if ( ccvr.getSourceEditPart() != null ) {
                hosts.add( ccvr.getSourceEditPart());
                hosts.add( ccvr.getSourceEditPart().getParent());
            }
            if ( ccvr.getTargetEditPart() != null ) {
                hosts.add( ccvr.getTargetEditPart());
                hosts.add( ccvr.getTargetEditPart().getParent());
            }
        }
        if ((request instanceof GroupRequest)) {
            List parts = ((GroupRequest)request).getEditParts();
            hosts.add(this);
            hosts.add(getParent());
        
            Iterator editparts = parts == null ? Collections.EMPTY_LIST.iterator() : parts.iterator();  
            while ( editparts.hasNext() ) {
                EditPart ep = (EditPart)editparts.next();
                addEditPartAndParent(hosts, ep);
            }
        }
        
        /////////////////////////////////////////////////////////////
        // This following behavior is specific to BorderItemEditPart and
        // AbstractBorderItemEditPart, but we do not want to allow clients to
        // override this method so we do not want to make it protected.
        
        if (this instanceof IBorderItemEditPart) {
            if ((request instanceof CreateConnectionViewRequest)) {
                CreateConnectionViewRequest ccvr = (CreateConnectionViewRequest) request;
                if (ccvr.getSourceEditPart() instanceof IBorderItemEditPart) {
                    hosts.add(ccvr.getSourceEditPart().getParent().getParent());
                }
                if (ccvr.getTargetEditPart() instanceof IBorderItemEditPart) {
                    hosts.add(ccvr.getTargetEditPart().getParent().getParent());
                }
            }
        }
        /////////////////////////////////////////////////////////////

        return hosts;
    }
    
    private void addEditPartAndParent(Set hosts, EditPart editPart) {
        hosts.add(editPart);
        hosts.add(editPart.getParent());
    }

    
    

    /**
     * Convenience method returning the editpart's Diagram, the Diagam that owns
     * the edit part
     * 
     * @return the diagram
     */
    protected Diagram getDiagramView() {
        return (Diagram) getRoot().getContents().getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getPrimaryView()
     */
    public View getPrimaryView() {
        for (EditPart parent = this; parent != null; parent = parent
            .getParent())
            if (parent instanceof IPrimaryEditPart)
                return (View) parent.getModel();
        return null;
    }

    /**
     * Convenience method returning the editpart's edit domain. Same as calling
     * <code>getRoot().getViewer().getEditDomain()</code>
     * 
     * @return the edit domain
     */
    protected EditDomain getEditDomain() {
        return getRoot().getViewer().getEditDomain();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getDiagramEditDomain()
     */
    public IDiagramEditDomain getDiagramEditDomain() {
        return (IDiagramEditDomain) getEditDomain();
    }

    /**
     * Return this editpart's view (model) children.
     * 
     * @return list of views.
     */
    protected List getModelChildren() {
        return ((View) getModel()).getChildren();
    }

    /**
     * Convenience method to retreive the value for the supplied value from the
     * editpart's associated view element. Same as calling
     * <code> ViewUtil.getStructuralFeatureValue(getNotationView(),feature)</code>.
     */
    public Object getStructuralFeatureValue(EStructuralFeature feature) {
        return ViewUtil.getStructuralFeatureValue((View) getModel(), feature);
    }

    /**
     * try to resolve the semantic element and Return the resolven element; if
     * the element is unresolvable or null it will return null
     * 
     * @return non proxy EObject or NULL
     */
    public EObject resolveSemanticElement() {
        EObject eObj = ((View) getModel()).getElement();
        if (eObj == null) {
            return null;
        }

        if (!eObj.eIsProxy()) {
            return eObj;
        }

        try {
            return (EObject) getEditingDomain().runExclusive(
                new RunnableWithResult.Impl() {

                    public void run() {
                        setResult(ViewUtil
                            .resolveSemanticElement((View) getModel()));
                    }
                });
        } catch (InterruptedException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "resolveSemanticElement", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
                "resolveSemanticElement", e); //$NON-NLS-1$
            return null;
        }
    }

    /**
     * Walks up the editpart hierarchy to find and return the
     * <code>TopGraphicEditPart</code> instance.
     */
    public TopGraphicEditPart getTopGraphicEditPart() {
        EditPart editPart = this;
        while (editPart instanceof IGraphicalEditPart) {
            if (editPart instanceof TopGraphicEditPart)
                return (TopGraphicEditPart) editPart;
            editPart = editPart.getParent();
        }
        return null;
    }

    /**
     * Return the editpart's associated Notation View.
     * 
     * @return <code>View</code>, the associated view or null if there is no
     *         associated Notation View
     */
    public View getNotationView() {
        Object model = getModel();
        if (model instanceof View)
            return (View) model;
        return null;
    }

    /**
     * Handles the passed property changed event only if the editpart's view is
     * not deleted
     */
    public final void propertyChange(PropertyChangeEvent event) {
        if (isActive())
            handlePropertyChangeEvent(event);
    }

    /**
     * Handles the property changed event. Clients should override to respond to
     * the specific notification events they are interested.
     * 
     * Note: This method may get called on a non-UI thread. Clients should
     * either ensure that their code is thread safe and/or doesn't make
     * unsupported calls (i.e. Display.getCurrent() ) assuming they are on the
     * main thread. Alternatively if this is not possible, then the client can
     * wrap their handler within the Display.synchExec runnable to ensure
     * synchronization and subsequent execution on the main thread.
     * 
     * @param event
     *            the <code>Notification</code> object that is the property
     *            changed event
     */
    protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(
            Connection.PROPERTY_CONNECTION_ROUTER)) {
            installRouter();
        }
    }

    /**
     * Method reactivateSemanticModel. This method reactivates the edit part's
     * emantic model by: 1- removing semantic listeners 2- adding semantic
     * listeners if the semantic reference is resolvable 3- Refreshing it
     * 
     * This method is called in response to IView's Properties.ID_SEMANTICREF
     * property change event However, it will only work under the following
     * assumptions: 1- The old and new semantic models are compatible in their
     * kind 2- The deltas between old and new semantic models do not affect
     * notation 3- Connections are not refereshed since they are maintained by
     * the diagram
     */
    public void reactivateSemanticModel() {
        removeSemanticListeners();
        if (resolveSemanticElement() != null)
            addSemanticListeners();
        refresh();
    }

    /** Finds an editpart given a starting editpart and an EObject */
    public EditPart findEditPart(EditPart epBegin, EObject theElement) {
        if (theElement == null) {
            return null;
        }
        EditPart epStart = null;
        if (epBegin == null) {
            epStart = this;
        } else {
            epStart = epBegin;
        }

        final View view = (View) ((IAdaptable) epStart).getAdapter(View.class);

        if (view != null) {
            EObject el = ViewUtil.resolveSemanticElement(view);

            if ((el != null) && el.equals(theElement)) {
                return epStart;
            }
        }

        ListIterator childLI = epStart.getChildren().listIterator();
        while (childLI.hasNext()) {
            EditPart epChild = (EditPart) childLI.next();

            EditPart elementEP = findEditPart(epChild, theElement);
            if (elementEP != null) {
                return elementEP;
            }
        }
        return null;
    }

    /**
     * Refresh the editpart's figure foreground colour.
     */
    protected void refreshForegroundColor() {
        LineStyle style = (LineStyle) getPrimaryView().getStyle(
            NotationPackage.Literals.LINE_STYLE);
        if (style != null)
            setForegroundColor(DiagramColorRegistry.getInstance().getColor(
                new Integer(style.getLineColor())));
    }

    /**
     * Refresh the editpart's figure visibility.
     */
    protected void refreshVisibility() {
        setVisibility(((View) getModel()).isVisible());
    }

    /**
     * Removes a listener previously added with the given id
     * 
     * @param filterId
     *            the filiter ID
     */
    protected void removeListenerFilter(String filterId) {
        if (listenerFilters == null)
            return;

        Object[] objects = (Object[]) listenerFilters.get(filterId);
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
        listenerFilters.remove(filterId);
    }

    /**
     * sets the forefround color of the editpart's figure
     * 
     * @param color
     *            the color
     */
    protected void setForegroundColor(Color color) {
        getFigure().setForegroundColor(color);
    }

    /**
     * Sets the passed feature if possible on this editpart's view to the passed
     * value.
     * 
     * @param feature
     *            the feature to use
     * @param value
     *            the value of the property being set
     */
    public void setStructuralFeatureValue(EStructuralFeature feature,
            Object value) {
        ViewUtil.setStructuralFeatureValue((View) getModel(), feature, value);
    }

    /**
     * sets the edit part's visibility
     * 
     * @param vis
     *            the new visibilty value
     */
    protected void setVisibility(boolean vis) {
        if (!vis && getSelected() != SELECTED_NONE)
            getViewer().deselect(this);
        getFigure().setVisible(vis);
        getFigure().revalidate();
    }

    /**
     * This method adds all listeners to the notational world (views, figures,
     * editpart...etc) Override this method to add more notational listeners
     * down the hierarchy
     */
    protected void addNotationalListeners() {
        addListenerFilter("View", this, (View) getModel());//$NON-NLS-1$
        getFigure().addPropertyChangeListener(
            Connection.PROPERTY_CONNECTION_ROUTER, this);
    }

    /**
     * This method adds all listeners to the semantic element behind this
     * EditPart Override this method to add more semantic listeners down the
     * hierarchy This method is called only if the semantic element is
     * resolvable
     */
    protected void addSemanticListeners() {
        addListenerFilter("SemanticModel",//$NON-NLS-1$
            this, resolveSemanticElement());
    }

    /**
     * This method removes all listeners to the notational world (views,
     * figures, editpart...etc) Override this method to remove notational
     * listeners down the hierarchy
     */
    protected void removeNotationalListeners() {
        getFigure().removePropertyChangeListener(
            Connection.PROPERTY_CONNECTION_ROUTER, this);
        removeListenerFilter("View");//$NON-NLS-1$
    }

    /**
     * This method removes all listeners to the semantic element behind this
     * EditPart Override this method to remove semantic listeners down the
     * hierarchy
     */
    protected void removeSemanticListeners() {
        removeListenerFilter("SemanticModel");//$NON-NLS-1$
    }

    /**
     * @see org.eclipse.gef.EditPart#addNotify()
     */
    public void addNotify() {
        super.addNotify();
        installRouter();
    }

    /**
     * a static array of appearance property ids applicable to the connections
     */
    protected static final String[] appearanceProperties = new String[] {
        Properties.ID_FONTNAME, Properties.ID_FONTSIZE, Properties.ID_FONTBOLD,
        Properties.ID_FONTITALIC, Properties.ID_FONTCOLOR,
        Properties.ID_LINECOLOR};

    /**
     * construcotr
     * 
     * @param view ,
     *            the view the edit part will own
     */
    public ConnectionEditPart(View view) {
        setModel(view);
    }

    /**
     * Method createConnectionFigure.
     * 
     * @return a <code>Connection</code> figure
     */
    abstract protected Connection createConnectionFigure();

    final protected IFigure createFigure() {
        return createConnectionFigure();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.EditPart#refresh()
     */
    public void refresh() {
        if (getSource() != null && getTarget() != null) {
            try {
                getEditingDomain().runExclusive(new Runnable() {

                    public void run() {
                        ConnectionEditPart.super.refresh();
                        EditPolicyIterator i = getEditPolicyIterator();
                        while (i.hasNext()) {
                            EditPolicy policy = i.next();
                            if (policy instanceof GraphicalEditPolicyEx) {
                                ((GraphicalEditPolicyEx) policy).refresh();
                            }
                        }
                    }
                });
            } catch (InterruptedException e) {
                Trace.catching(DiagramUIPlugin.getInstance(),
                    DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                    "refresh", e); //$NON-NLS-1$
                Log.error(DiagramUIPlugin.getInstance(),
                    DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
                    "refresh", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * utility method to get the <code>Edge</code> view
     * 
     * @return the <code>Edge</code>
     */
    protected Edge getEdge() {
        return (Edge) getModel();
    }

    /*
     * @see AbstractEditPart#getDragTracker(Request)
     */
    public DragTracker getDragTracker(Request req) {
        return new SelectConnectionEditPartTracker(this);
    }

    /**
     * give access to the source of the edit part's Edge
     * 
     * @return the source
     */
    protected Object getModelSource() {
        return getEdge().getSource();
    }

    /**
     * give access to the target of the edit part's Edge
     * 
     * @return the target
     */
    protected Object getModelTarget() {
        return getEdge().getTarget();
    }

    /**
     * installes a router on the edit part, depending on the
     * <code>RoutingStyle</code>
     */
    protected void installRouter() {
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        RoutingStyle style = (RoutingStyle) ((View) getModel())
            .getStyle(NotationPackage.Literals.ROUTING_STYLE);

        if (style != null && cLayer instanceof ConnectionLayerEx) {

            ConnectionLayerEx cLayerEx = (ConnectionLayerEx) cLayer;
            Routing routing = style.getRouting();
            if (Routing.MANUAL_LITERAL == routing) {
                getConnectionFigure().setConnectionRouter(
                    cLayerEx.getObliqueRouter());
            } else if (Routing.RECTILINEAR_LITERAL == routing) {
                getConnectionFigure().setConnectionRouter(
                    cLayerEx.getRectilinearRouter());
            } else if (Routing.TREE_LITERAL == routing) {
                getConnectionFigure().setConnectionRouter(
                    cLayerEx.getTreeRouter());
            }

        }

        refreshRouterChange();
    }

    /**
     * refresh the pendpoints owned by the EditPart's <code>Edge</code>
     */
    protected void refreshBendpoints() {
        RelativeBendpoints bendpoints = (RelativeBendpoints) getEdge()
            .getBendpoints();
        List modelConstraint = bendpoints.getPoints();
        List figureConstraint = new ArrayList();
        for (int i = 0; i < modelConstraint.size(); i++) {
            org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint wbp = (org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint) modelConstraint
                .get(i);
            RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
            rbp.setRelativeDimensions(new Dimension(wbp.getSourceX(), wbp
                .getSourceY()), new Dimension(wbp.getTargetX(), wbp
                .getTargetY()));
            rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
            figureConstraint.add(rbp);
        }
        getConnectionFigure().setRoutingConstraint(figureConstraint);
    }

    private void installBendpointEditPolicy() {
        if (getConnectionFigure().getConnectionRouter() instanceof ForestRouter) {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
                new TreeConnectionBendpointEditPolicy());
        } else if (getConnectionFigure().getConnectionRouter() instanceof OrthogonalRouter) {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
                new ConnectionLineSegEditPolicy());
        } else {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
                new ConnectionBendpointEditPolicy());
        }

        EditPartUtil.synchronizeRunnableToMainThread(this, new Runnable() {

            public void run() {
                if (getConnectionFigure().getConnectionRouter() instanceof ForestRouter) {
                    getConnectionFigure().setCursor(Cursors.CURSOR_SEG_MOVE);
                } else if (getConnectionFigure().getConnectionRouter() instanceof OrthogonalRouter) {
                    getConnectionFigure().setCursor(Cursors.CURSOR_SEG_MOVE);
                } else {
                    getConnectionFigure().setCursor(Cursors.CURSOR_SEG_ADD);
                }
            };
        });
    }

    /**
     * Method refreshRouterChange.
     */
    protected void refreshRouterChange() {
        refreshBendpoints();
        installBendpointEditPolicy();
    }

    /**
     * Method refreshSmoothness.
     */
    protected void refreshSmoothness() {
        Connection connection = getConnectionFigure();
        if (!(connection instanceof PolylineConnectionEx))
            return;

        PolylineConnectionEx poly = (PolylineConnectionEx) connection;
        RoutingStyle style = (RoutingStyle) ((View) getModel())
            .getStyle(NotationPackage.Literals.ROUTING_STYLE);
        if (style != null) {
            Smoothness smoothness = style.getSmoothness();

            if (Smoothness.LESS_LITERAL == smoothness) {
                poly.setSmoothness(PolylineConnectionEx.SMOOTH_LESS);
            } else if (Smoothness.NORMAL_LITERAL == smoothness) {
                poly.setSmoothness(PolylineConnectionEx.SMOOTH_NORMAL);
            } else if (Smoothness.MORE_LITERAL == smoothness) {
                poly.setSmoothness(PolylineConnectionEx.SMOOTH_MORE);
            } else if (Smoothness.NONE_LITERAL == smoothness) {
                poly.setSmoothness(PolylineConnectionEx.SMOOTH_NONE);
            }
        }
    }

    /**
     * Method refreshJumplinks.
     */
    protected void refreshJumplinks() {
        Connection connection = getConnectionFigure();
        if (!(connection instanceof PolylineConnectionEx))
            return;

        PolylineConnectionEx poly = (PolylineConnectionEx) connection;
        RoutingStyle style = (RoutingStyle) ((View) getModel())
            .getStyle(NotationPackage.Literals.ROUTING_STYLE);

        JumpLinkStatus status = JumpLinkStatus.NONE_LITERAL;
        JumpLinkType type = JumpLinkType.SEMICIRCLE_LITERAL;
        boolean reverse = false;
        if (style != null) {
            status = style.getJumpLinkStatus();
            type = style.getJumpLinkType();
            reverse = style.isJumpLinksReverse();
        }

        int jumpType = 0;
        if (JumpLinkStatus.BELOW_LITERAL == status) {
            jumpType = PolylineConnectionEx.JUMPLINK_FLAG_BELOW;
        } else if (JumpLinkStatus.ABOVE_LITERAL == status) {
            jumpType = PolylineConnectionEx.JUMPLINK_FLAG_ABOVE;
        } else if (JumpLinkStatus.ALL_LITERAL == status) {
            jumpType = PolylineConnectionEx.JUMPLINK_FLAG_ALL;
        }

        boolean bCurved = type.equals(JumpLinkType.SEMICIRCLE_LITERAL);
        boolean bAngleIn = !type.equals(JumpLinkType.SQUARE_LITERAL);
        boolean bOnBottom = reverse;

        poly.setJumpLinks(jumpType != 0);
        poly.setJumpLinksStyles(jumpType, bCurved, bAngleIn, bOnBottom);
    }

    /**
     * Method refreshRoutingStyles.
     */
    protected void refreshRoutingStyles() {
        Connection connection = getConnectionFigure();
        if (!(connection instanceof PolylineConnectionEx))
            return;

        PolylineConnectionEx poly = (PolylineConnectionEx) connection;

        RoutingStyle style = (RoutingStyle) ((View) getModel())
            .getStyle(NotationPackage.Literals.ROUTING_STYLE);
        if (style != null) {

            boolean closestDistance = style.isClosestDistance();
            boolean avoidObstruction = style.isAvoidObstructions();

            poly.setRoutingStyles(closestDistance, avoidObstruction);
            
			if (avoidObstruction)
				installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,null);
			else
				installBendpointEditPolicy();

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    protected void refreshVisuals() {
        super.refreshVisuals();
        refreshVisibility();
        refreshForegroundColor();
        refreshRoutingStyles();
        refreshSmoothness();
        refreshJumplinks();
        refreshBendpoints();
        refreshFont();
    }

    /**
     * Refresh the editpart's figure font.
     */
    protected void refreshFont() {
        FontStyle style = (FontStyle) getPrimaryView().getStyle(
            NotationPackage.Literals.FONT_STYLE);
        if (style != null) {
            setFont(new FontData(style.getFontName(), style.getFontHeight(),
                (style.isBold() ? SWT.BOLD
                    : SWT.NORMAL) | (style.isItalic() ? SWT.ITALIC
                    : SWT.NORMAL)));
        }
    }

    /**
     * Sets the font to the label. This method could be overriden to change the
     * font data of the font overrides typically look like this: super.setFont(
     * new FontData( fontData.getName(), fontData.getHeight(),
     * fontData.getStyle() <| &> SWT.????));
     * 
     * @param fontData
     *            the font data
     */
    protected void setFont(FontData fontData) {
        if (cachedFontData != null && cachedFontData.equals(fontData)) {
            // the font was previously set and has not changed; do nothing.
            return;
        }

        try {
            Font newFont = getResourceManager().createFont(
                FontDescriptor.createFrom(fontData));
            getFigure().setFont(newFont);
            getFigure().repaint();

            if (cachedFontData != null) {
                getResourceManager().destroyFont(
                    FontDescriptor.createFrom(cachedFontData));
            }
            cachedFontData = fontData;
        } catch (DeviceResourceException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "setFont", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING, "setFont", e); //$NON-NLS-1$
        }
    }

    /**
     * Returns an array of the appearance property ids applicable to the
     * receiver. Fro this type it is Properties.ID_FONT,
     * Properties.ID_FONTCOLOR, Properties.ID_LINECOLOR
     * 
     * @return - an array of the appearane property ids applicable to the
     *         receiver
     */
    protected String[] getAppearancePropertyIDs() {
        return appearanceProperties;
    }

    /**
     * Perform a request by executing a command from the target editpart of the
     * request For the Direct_Edit request, we need to show up an editor first
     * 
     * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
     */
    public void performRequest(Request request) {
        if (!isEditModeEnabled()) {
            return;
        }

        if (RequestConstants.REQ_DIRECT_EDIT == request.getType()) {
            performDirectEditRequest(request);
        } else {
            EditPart targetEditPart = getTargetEditPart(request);
            if (targetEditPart != null) {
                Command command = targetEditPart.getCommand(request);
                if (command != null) {
                    getDiagramEditDomain().getDiagramCommandStack().execute(
                        command);
                    return;
                }
            }
        }
    }

    /**
     * Performs a direct edit request (usually by showing some type of editor)
     * 
     * @param request
     *            the direct edit request
     */
    protected void performDirectEditRequest(Request request) {
        try {
            EditPart primaryChildEditPart = (EditPart) getEditingDomain()
                .runExclusive(new RunnableWithResult.Impl() {

                    public void run() {
                        setResult(getPrimaryChildEditPart());
                    }
                });
            if (primaryChildEditPart != null) {
                primaryChildEditPart.performRequest(request);
            }

        } catch (InterruptedException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "performDirectEditRequest", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
                "performDirectEditRequest", e); //$NON-NLS-1$
        }

    }

    /**
     * @see org.eclipse.gef.EditPart#understandsRequest(org.eclipse.gef.Request)
     */
    public boolean understandsRequest(Request req) {
        return RequestConstants.REQ_DIRECT_EDIT == req.getType()
            || super.understandsRequest(req);
    }

    /** Adds a [ref, editpart] mapping to the EditPartForElement map. */
    protected void registerModel() {
        super.registerModel();

        // Save the elements Guid to use during unregister
        EObject ref = ((View) getModel()).getElement();
        if (ref == null) {
            return;
        }

        elementGuid = EMFCoreUtil.getProxyID(ref);

        ((IDiagramGraphicalViewer) getViewer()).registerEditPartForElement(
            elementGuid, this);
    }

    /** Remove this editpart from the EditPartForElement map. */
    protected void unregisterModel() {
        super.unregisterModel();

        ((IDiagramGraphicalViewer) getViewer()).unregisterEditPartForElement(
            elementGuid, this);
    }

    /**
     * Handles the case where the semantic reference has changed.
     */
    protected final void handleMajorSemanticChange() {
        if (getSource() instanceof GraphicalEditPart
            && getTarget() instanceof GraphicalEditPart) {
            ((GraphicalEditPart) getSource()).refreshSourceConnection(this);
            ((GraphicalEditPart) getTarget()).refreshTargetConnection(this);
        }
    }

    /**
     * Refreshes a child editpart by removing it and refreshing children
     * 
     * @param child
     */
    final void refreshChild(GraphicalEditPart child) {
        removeChild(child);
        refreshChildren();
    }

    /**
     * check if there is a canonical edit policy installed on the edit part or
     * not
     * 
     * @return <tt>true</tt> if a canonical editpolicy has been installed on
     *         this editpart; otherwise <tt>false</tt>
     */
    public final boolean isCanonical() {
        return getEditPolicy(EditPolicyRoles.CANONICAL_ROLE) != null;
    }

    /**
     * checks if the edit part's figure is visible or not
     * 
     * @return <tt>true</tt> if the editpart's figure is visible;
     *         <tt>false</tt> otherwise.
     */
    public boolean isSelectable() {
        return getFigure().isVisible();
    }

    /*
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#disableEditMode()
     */
    public void disableEditMode() {
        if (isEditable == false) {
            return;
        }

        List l = getSourceConnections();
        int size = l.size();
        for (int i = 0; i < size; i++) {
            Object obj = l.get(i);
            if (obj instanceof IEditableEditPart) {
                ((IEditableEditPart) obj).disableEditMode();
            }
        }

        List c = getChildren();
        size = c.size();
        for (int i = 0; i < size; i++) {
            Object obj = c.get(i);
            if (obj instanceof IEditableEditPart) {
                ((IEditableEditPart) obj).disableEditMode();
            }
        }

        isEditable = false;
    }

    /*
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#enableEditMode()
     */
    public void enableEditMode() {
        if (isEditable) {
            return;
        }
        isEditable = true;
        List c = getChildren();
        int size = c.size();
        for (int i = 0; i < size; i++) {
            Object obj = c.get(i);
            if (obj instanceof IEditableEditPart) {
                ((IEditableEditPart) obj).enableEditMode();
            }
        }

        List l = getSourceConnections();
        size = l.size();
        for (int i = 0; i < size; i++) {
            Object obj = l.get(i);
            if (obj instanceof IEditableEditPart) {
                ((IEditableEditPart) obj).enableEditMode();
            }
        }
    }

    /*
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#isEditModeEnabled()
     */
    public boolean isEditModeEnabled() {
        // protect against deadlock - don't allow any action while write
        // transaction
        // is active on another thread
        if (EditPartUtil.isWriteTransactionInProgress(this, true, true))
            return false;
        return isEditable;
    }

    /*
     * @see org.eclipse.gef.EditPart#showSourceFeedback(org.eclipse.gef.Request)
     */
    public void showSourceFeedback(Request request) {
        if (!isEditModeEnabled()) {
            return;
        }

        super.showSourceFeedback(request);
    }

    /*
     * @see org.eclipse.gef.EditPart#showTargetFeedback(org.eclipse.gef.Request)
     */
    public void showTargetFeedback(Request request) {
        if (!isEditModeEnabled()) {
            return;
        }

        super.showTargetFeedback(request);
    }

    /*
     * @see org.eclipse.gef.EditPart#eraseSourceFeedback(org.eclipse.gef.Request)
     */
    public void eraseSourceFeedback(Request request) {
        if (!isEditModeEnabled()) {
            return;
        }

        super.eraseSourceFeedback(request);
    }

    /*
     * @see org.eclipse.gef.EditPart#eraseTargetFeedback(org.eclipse.gef.Request)
     */
    public void eraseTargetFeedback(Request request) {
        if (!isEditModeEnabled()) {
            return;
        }

        super.eraseTargetFeedback(request);
    }

    /**
     * this method will return the primary child EditPart inside this edit part
     * 
     * @return the primary child view inside this edit part
     */
    public EditPart getPrimaryChildEditPart() {
        if (getChildren().size() > 0)
            return (EditPart) getChildren().get(0);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getDiagramPreferencesHint()
     */
    public PreferencesHint getDiagramPreferencesHint() {
        RootEditPart root = getRoot();
        if (root instanceof IDiagramPreferenceSupport) {
            return ((IDiagramPreferenceSupport) root).getPreferencesHint();
        }
        return PreferencesHint.USE_DEFAULTS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener#notifyChanged(org.eclipse.emf.common.notify.Notification)
     */
    public void notifyChanged(Notification notification) {
        if (isActive()) {
            handleNotificationEvent(notification);
        }
    }

    /**
     * Handles the property changed event
     * 
     * @param event
     *            the property changed event
     */
    protected void handleNotificationEvent(Notification event) {
        Object feature = event.getFeature();
        if (NotationPackage.Literals.VIEW__PERSISTED_CHILDREN.equals(
            feature)
            || NotationPackage.Literals.VIEW__TRANSIENT_CHILDREN.equals(
                feature)) {
            refreshChildren();
        } else if (NotationPackage.Literals.VIEW__VISIBLE.equals(feature)) {
            Object notifier = event.getNotifier();
            if (notifier == getModel())
                setVisibility(((Boolean) event.getNewValue()).booleanValue());
            // Reactivating in response to semantic model reference change
            // However, we need to verify that the event belongs to this
            // editpart's view
        } else if (NotationPackage.Literals.ROUTING_STYLE__ROUTING.equals(
            feature)) {
            installRouter();
        } else if (NotationPackage.Literals.ROUTING_STYLE__SMOOTHNESS
            .equals(feature)
            || NotationPackage.Literals.ROUTING_STYLE__AVOID_OBSTRUCTIONS
                .equals(feature)
            || NotationPackage.Literals.ROUTING_STYLE__CLOSEST_DISTANCE
                .equals(feature)
            || NotationPackage.Literals.ROUTING_STYLE__JUMP_LINK_STATUS
                .equals(feature)
            || NotationPackage.Literals.ROUTING_STYLE__JUMP_LINK_TYPE.equals(
                feature)
            || NotationPackage.Literals.ROUTING_STYLE__JUMP_LINKS_REVERSE
                .equals(feature)) {
            refreshVisuals();
        } else if (NotationPackage.Literals.LINE_STYLE__LINE_COLOR.equals(
            feature)) {
            Integer c = (Integer) event.getNewValue();
            setForegroundColor(DiagramColorRegistry.getInstance().getColor(c));
        } else if (NotationPackage.Literals.RELATIVE_BENDPOINTS__POINTS
            .equals(feature)) {
            refreshBendpoints();
        } else if (event.getFeature() == NotationPackage.Literals
            .VIEW__ELEMENT
            && ((EObject) event.getNotifier()) == getNotationView()){
            handleMajorSemanticChange();
       } else if (event.getEventType() == EventType.UNRESOLVE
                && event.getNotifier() == ((View) getModel()).getElement())
                handleMajorSemanticChange();
    }

    /**
     * @return <code>IMapMode</code> that allows for the coordinate mapping
     *         from device to logical units.
     */
    protected IMapMode getMapMode() {
        RootEditPart root = getRoot();
        if (root instanceof DiagramRootEditPart) {
            DiagramRootEditPart dgrmRoot = (DiagramRootEditPart) root;
            return dgrmRoot.getMapMode();
        }

        return MapModeUtil.getMapMode();
    }

    /**
     * Derives my editing domain from my diagram element. Subclasses may
     * override.
     */
    public TransactionalEditingDomain getEditingDomain() {
        if (editingDomain == null) {
            // try to get the editing domain for the model
            editingDomain = TransactionUtil.getEditingDomain(getModel());

            if (editingDomain == null) {
                // try to get the editing domain from the diagram view
                editingDomain = TransactionUtil
                    .getEditingDomain(getDiagramView());
            }
        }
        return editingDomain;
    }

    /**
     * Gets the diagram event broker from the editing domain.
     * 
     * @return the diagram event broker
     */
    private DiagramEventBroker getDiagramEventBroker() {
        TransactionalEditingDomain theEditingDomain = getEditingDomain();
        if (theEditingDomain != null) {
            return DiagramEventBroker.getInstance(theEditingDomain);
        }
        return null;
    }
    
    
    public Object getPreferredValue(EStructuralFeature feature) {
        Object preferenceStore = getDiagramPreferencesHint()
            .getPreferenceStore();
        if (preferenceStore instanceof IPreferenceStore) {            
            if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()) {
                
                return FigureUtilities.RGBToInteger(PreferenceConverter
                    .getColor((IPreferenceStore) preferenceStore,
                        IPreferenceConstants.PREF_LINE_COLOR));
                
            } else if (feature == NotationPackage.eINSTANCE
                .getFontStyle_FontColor()) {
                
                return FigureUtilities.RGBToInteger(PreferenceConverter
                    .getColor((IPreferenceStore) preferenceStore,
                        IPreferenceConstants.PREF_FONT_COLOR));
                
            }
        }
        return getStructuralFeatureValue(feature);
    }
    
    
    /**
     * Gets the resource manager to remember the resources allocated for this
     * graphical viewer. All resources will be disposed when the graphical
     * viewer is closed if they have not already been disposed.
     * 
     * @return the resource manager
     */
    protected ResourceManager getResourceManager() {
        EditPartViewer viewer = getViewer();
        if (viewer instanceof DiagramGraphicalViewer) {
            return ((DiagramGraphicalViewer) viewer).getResourceManager();
        }
        return JFaceResources.getResources();
    } 
    
    /**
	 * Answers whether or not this connection represents a part of the semantic
	 * model.
	 * 
	 * @return <code>true</code> if this connection has semantic meaning,
	 *         <code>false</code> otherwise.
	 */
	public boolean isSemanticConnection() {

		if (semanticConnection == null) {
			if (getEdge() != null && (getEdge().getElement() != null
					|| !NotationTypeUtil.hasNotationType(getEdge()))) {
				semanticConnection = Boolean.TRUE;
			} else {
				semanticConnection = Boolean.FALSE;
			}
		}
		return semanticConnection.booleanValue();
	}
	
	/**
	 * Clear the semantic connection value when the model changes.
	 */
	public void setModel(Object model) {
		super.setModel(model);
		semanticConnection = null;
	}
  
}
