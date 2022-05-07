/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectionEndsCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * 
 * A request to create new <code>IView</code> (s)
 * 
 * To instantiate this request, clients have to create a
 * <code>ConnectionViewDescriptor</code> or a list of
 * <code>ConnectionViewDescriptor</code> s filling it with view creation
 * parameters. The <code>ConnectionViewDescriptor</code> is a inner class to
 * this request
 * 
 * The request object can be used to obtain a view creation command from a
 * target <code>EditPart</code> Once such command is executed, the request
 * cannot be reused again to create another view. A different instance of the
 * reqyest has to be used instead
 * 
 * @author melaasar
 * 
 */
public class CreateConnectionViewRequest
	extends CreateConnectionRequest {

	/**
	 * A specialized view descriptor for connection views
	 */
	public static class ConnectionViewDescriptor
		extends ViewDescriptor {

		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the
		 *            appropriate preference store from which to retrieve
		 *            diagram preference values. The preference hint is mapped
		 *            to a preference store in the preference registry <@link
		 *            DiagramPreferencesRegistry>.
		 */
		public ConnectionViewDescriptor(IAdaptable elementAdapter,
				PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, preferencesHint);
		}
        
        /**
         * Constructor.
         * 
         * @param elementAdapter
         * @param persisted
         *            indicates if ths connector will be created as a persisted
         *            connector or transient connector
         * @param preferencesHint
         *            The preference hint that is to be used to find the
         *            appropriate preference store from which to retrieve
         *            diagram preference values. The preference hint is mapped
         *            to a preference store in the preference registry <@link
         *            DiagramPreferencesRegistry>.
         */
        public ConnectionViewDescriptor(IAdaptable elementAdapter,
                boolean persisted, PreferencesHint preferencesHint) {
            super(elementAdapter, Edge.class,persisted, preferencesHint);
        }

		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the
		 *            appropriate preference store from which to retrieve
		 *            diagram preference values. The preference hint is mapped
		 *            to a preference store in the preference registry <@link
		 *            DiagramPreferencesRegistry>.
		 */
		public ConnectionViewDescriptor(IAdaptable elementAdapter,
				String semanticHint, PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, preferencesHint);
		}
        
        /**
         * Constructor.
         * 
         * @param elementAdapter
         * @param semanticHint
         * @param persisted
         *            Indicates if the connector will be created as a transient
         *            or persisted connector 
         * @param preferencesHint
         *            The preference hint that is to be used to find the
         *            appropriate preference store from which to retrieve
         *            diagram preference values. The preference hint is mapped
         *            to a preference store in the preference registry <@link
         *            DiagramPreferencesRegistry>.
         */
        public ConnectionViewDescriptor(IAdaptable elementAdapter,
                String semanticHint,boolean persisted, PreferencesHint preferencesHint) {
            super(elementAdapter, Edge.class, semanticHint,persisted, preferencesHint);
        }

		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param index
		 */
		public ConnectionViewDescriptor(IAdaptable elementAdapter,
				String semanticHint, int index, PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, index,
				preferencesHint);
		}

		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param index
		 * @param persisted
		 *            flag to indicate if this view will be persisted or not
		 */
		public ConnectionViewDescriptor(IAdaptable elementAdapter,
				String semanticHint, int index, boolean persisted,
				PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, index, persisted,
				preferencesHint);
		}
	}

	/**
	 * The connection view descriptor set by the user
	 */
	private ConnectionViewDescriptor connectionViewDescriptor;

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            a semantic element
	 */
	public CreateConnectionViewRequest(EObject element,
			PreferencesHint preferencesHint) {
		this(new ConnectionViewDescriptor(new EObjectAdapter(element),
			preferencesHint));
	}
    
    /**
     * Constructor.
     * 
     * @param element
     *            a semantic element
     * @param boolean
     *            indicate if the connection will be persisted or not
     */
    public CreateConnectionViewRequest(EObject element,
            boolean persisted, PreferencesHint preferencesHint) {
        this(new ConnectionViewDescriptor(new EObjectAdapter(element),
            persisted,preferencesHint));
    }

	/**
	 * Constructor.
	 * 
	 * @param ViewDescriptor
	 *            a view descriptor
	 */
	public CreateConnectionViewRequest(ConnectionViewDescriptor ViewDescriptor) {
		Assert.isNotNull(ViewDescriptor);
		this.connectionViewDescriptor = ViewDescriptor;
	}

	/**
	 * Gets the descriptor for the connection view to be created.
	 * 
	 * @return the descriptor
	 */
	public ConnectionViewDescriptor getConnectionViewDescriptor() {
		return connectionViewDescriptor;
	}

	/**
	 * An <code>IAdaptable</code> object that adapts to <code>IView</code>
	 * .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObject()
	 */
	public Object getNewObject() {
		return getConnectionViewDescriptor();
	}

	/**
	 * The type is a <code>IAdaptable</code> object that adapters to
	 * <code>IView</code> .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObjectType()
	 */
	public Object getNewObjectType() {
		return IAdaptable.class;
	}

	/**
	 * The factory mechanism is not used
	 * 
	 * @throws UnsupportedOperationException
	 */
	protected CreationFactory getFactory() {
		throw new UnsupportedOperationException(
			"The Factory mechanism is not used"); //$NON-NLS-1$
	}

	/**
	 * The factory mechanism is not used
	 */

	public void setFactory(CreationFactory factory) {
		throw new UnsupportedOperationException(
			"The Factory mechanism is not used"); //$NON-NLS-1$
	}

	/**
	 * Method getCreateCommand.
	 * 
	 * @param element
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(EObject element,
			EditPart sourceEditPart, EditPart targetEditPart,
			PreferencesHint preferencesHint) {

		Assert.isNotNull(element);
		Assert.isNotNull(sourceEditPart);
		Assert.isNotNull(targetEditPart);
        boolean transientTargetOrSource = hasTransientSourceOrTarget(sourceEditPart, targetEditPart);

		CreateConnectionViewRequest request = new CreateConnectionViewRequest(
			element,!transientTargetOrSource, preferencesHint);

		request.setSourceEditPart(sourceEditPart);
		request.setTargetEditPart(targetEditPart);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEditPart.getCommand(request);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		return targetEditPart.getCommand(request);
	}

    private static boolean hasTransientSourceOrTarget(EditPart sourceEditPart, EditPart targetEditPart) {
        boolean transientTargetOrSource = hasTransientView(sourceEditPart);
        if (!transientTargetOrSource){
            transientTargetOrSource = hasTransientView(targetEditPart);
        }
        return transientTargetOrSource;
    }
    
    private static boolean hasTransientSourceOrTarget(IAdaptable sourceView,IAdaptable targetView) {
        boolean transientTargetOrSource = hasTransientView(sourceView);
        if (!transientTargetOrSource){
            transientTargetOrSource = hasTransientView(targetView);
        }
        return transientTargetOrSource;
    }

    private static boolean hasTransientView(EditPart sourceEditPart) {
        boolean transientTargetOrSource = false;
        if (sourceEditPart.getModel() instanceof View){
            View srcView = (View)sourceEditPart.getModel();
            if (ViewUtil.isTransient(srcView)){
                transientTargetOrSource = true;
            }
        }
        return transientTargetOrSource;
    }
    
    private static boolean hasTransientView(IAdaptable adaptable) {
        View view = (View)adaptable.getAdapter(View.class);
        if (view !=null){
            if (ViewUtil.isTransient(view)){
                return true;
            }
        }
        return false;
    }

	/**
	 * Method getCreateCommand Gets the command given a request, source and
	 * target edit parts. (No semantic element required.)
	 * 
	 * @param request
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @return <code>Command</code>
	 */
	public static Command getCreateCommand(CreateConnectionViewRequest request,
			EditPart sourceEditPart, EditPart targetEditPart) {

		Assert.isNotNull(request);
		Assert.isNotNull(sourceEditPart);
		Assert.isNotNull(targetEditPart);

		request.setSourceEditPart(sourceEditPart);
		request.setTargetEditPart(targetEditPart);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEditPart.getCommand(request);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		return targetEditPart.getCommand(request);
	}

	/**
	 * getCreateCommand.
	 * 
	 * @param elementAdapter
	 * @param sourceViewAdapter
	 * @param targetViewAdapter
	 * @param diagramEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(IAdaptable elementAdapter,
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			DiagramEditPart diagramEditPart, PreferencesHint preferencesHint) {

        Diagram diagram = diagramEditPart.getDiagramView().getDiagram();
        boolean transientTargetOrSource = hasTransientSourceOrTarget(sourceViewAdapter, targetViewAdapter);
		CreateCommand createCommand = new CreateCommand(diagramEditPart.getEditingDomain(),
			new ConnectionViewDescriptor(elementAdapter,!transientTargetOrSource, preferencesHint),
            diagram);

		IAdaptable viewAdapter = (IAdaptable) createCommand.getCommandResult()
			.getReturnValue();
        
        TransactionalEditingDomain editingDomain = diagramEditPart.getEditingDomain();

		SetConnectionEndsCommand sceCommand = new SetConnectionEndsCommand(editingDomain, 
			DiagramUIMessages.Commands_SetConnectionEndsCommand_Source);
		sceCommand.setEdgeAdaptor(viewAdapter);
		sceCommand.setNewSourceAdaptor(sourceViewAdapter);
		sceCommand.setNewTargetAdaptor(targetViewAdapter);

		CompositeTransactionalCommand cc = new CompositeTransactionalCommand(diagramEditPart.getEditingDomain(), null);
		cc.compose(createCommand);
		cc.compose(sceCommand);
		return new ICommandProxy(cc);
	}

	/**
	 * Method getCreateCommand.
	 * 
	 * @param viewDescriptor
	 * @param sourceViewAdapter
	 * @param targetViewAdapter
	 * @param diagramEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(ViewDescriptor viewDescriptor,
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			DiagramEditPart diagramEditPart) {

        Diagram diagram = diagramEditPart.getDiagramView().getDiagram();
		CreateCommand createCommand = new CreateCommand(diagramEditPart.getEditingDomain(), viewDescriptor,
            diagram);
		IAdaptable viewAdapter = (IAdaptable) createCommand.getCommandResult()
			.getReturnValue();
        TransactionalEditingDomain editingDomain = diagramEditPart.getEditingDomain();
		SetConnectionEndsCommand sceCommand = new SetConnectionEndsCommand(editingDomain, 
			DiagramUIMessages.Commands_SetConnectionEndsCommand_Source);
		sceCommand.setEdgeAdaptor(viewAdapter);
		sceCommand.setNewSourceAdaptor(sourceViewAdapter);
		sceCommand.setNewTargetAdaptor(targetViewAdapter);
		CompositeTransactionalCommand cc = new CompositeTransactionalCommand(diagramEditPart.getEditingDomain(), null);
		cc.compose(createCommand);
		cc.compose(sceCommand);
		return new ICommandProxy(cc);
	}
}
