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

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * This command is used to create a connection view between two editparts, when
 * only the view adapters are available at the time of creating the command. The
 * editparts are required to get the correct create connection command, so this
 * command defers getting the create connection command until execution time at
 * which point it can get the editparts from the editpart registry and the view
 * adapters.
 * 
 * @author cmahoney
 */
public class DeferredCreateConnectionViewCommand
	extends AbstractModelCommand {

	/** the element for the connection's semantic element */
	protected EObject element = null;

	/** the connection's semantic hint */
	protected String semanticHint = null;

	/** the source adapter from which a View can be retrieved */
	protected IAdaptable sourceViewAdapter;

	/**
	 * the target adapter from which a View can be retrieved
	 */
	protected IAdaptable targetViewAdapter;

	/** the graphical viewer used to get the editpart registry */
	protected EditPartViewer viewer;

	/** the command saved for undo and redo */
	private Command createConnectionCmd;

	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connections, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	protected PreferencesHint preferencesHint;

	/**
	 * Constructor for <code>DeferredCreateConnectionViewCommand</code>.
	 * 
	 * @param element
	 *            the connection's semantic element
	 * @param sourceViewAdapter
	 *            adapter from which the source view can be retrieved
	 * @param targetViewAdapter
	 *            adapter from which the target view can be retrieved
	 * @param viewer
	 *            the viewer used to get the editpart registry
	 */
	public DeferredCreateConnectionViewCommand(EObject element,
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			EditPartViewer viewer, PreferencesHint preferencesHint) {

		super("Deferred Create Connection View Command", null); //$NON-NLS-1$
		this.element = element;
		this.sourceViewAdapter = sourceViewAdapter;
		this.targetViewAdapter = targetViewAdapter;
		this.viewer = viewer;
		this.preferencesHint = preferencesHint;
	}

	/**
	 * Constructor for <code>DeferredCreateConnectionViewCommand</code>.
	 * Passing in the semanticHint allows for the creation of a connection view
	 * without a semantic element.
	 * 
	 * @param semanticHint
	 *            the connection's semantic hint
	 * @param sourceViewAdapter
	 *            adapter from which the source view can be retrieved
	 * @param targetViewAdapter
	 *            adapter from which the target view can be retrieved
	 * @param viewer
	 *            the viewer used to get the editpart registry
	 */
	public DeferredCreateConnectionViewCommand(String semanticHint,
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			EditPartViewer viewer, PreferencesHint preferencesHint) {

		super("Deferred Create Connection View Command", null); //$NON-NLS-1$
		this.semanticHint = semanticHint;
		this.sourceViewAdapter = sourceViewAdapter;
		this.targetViewAdapter = targetViewAdapter;
		this.viewer = viewer;
		this.preferencesHint = preferencesHint;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		if (viewer != null) {
			EditPart editpart = viewer.getRootEditPart().getContents();
			if (editpart instanceof IGraphicalEditPart) {
				View view = (View) ((IGraphicalEditPart) editpart).getModel();
				if (view != null) {
					IFile f = EObjectUtil.getWorkspaceFile(view);
					return f != null ? Collections.singletonList(f)
						: Collections.EMPTY_LIST;
				}
			}
		}
		return super.getAffectedObjects();
	}

	/**
	 * Finds the source and target editparts by extracting the views from the
	 * view adapaters and searching in the editpart viewer. Creates a connection
	 * view between the source and target.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		Map epRegistry = viewer.getEditPartRegistry();
		IGraphicalEditPart sourceEP = (IGraphicalEditPart) epRegistry
			.get(sourceViewAdapter.getAdapter(View.class));
		IGraphicalEditPart targetEP = (IGraphicalEditPart) epRegistry
			.get(targetViewAdapter.getAdapter(View.class));

		// If these are null, then the diagram's editparts may not
		// have been refreshed yet.
		Assert.isNotNull(sourceEP);
		Assert.isNotNull(targetEP);

		// If an element exists, create the view using the given
		// semantic element.
		// Else if no semantic element is provided
		// , use the String semanticHint to create a view
		if (element != null) {
			createConnectionCmd = CreateConnectionViewRequest.getCreateCommand(
				this.element, sourceEP, targetEP, preferencesHint);
		} else {
			ConnectionViewDescriptor viewDescriptor = new ConnectionViewDescriptor(
				null, this.semanticHint, preferencesHint);
			CreateConnectionViewRequest createRequest = new CreateConnectionViewRequest(
				viewDescriptor);
			createConnectionCmd = CreateConnectionViewRequest.getCreateCommand(
				createRequest, sourceEP, targetEP);
		}

		if (createConnectionCmd.canExecute()) {
			createConnectionCmd.execute();
		}
		viewer = null;// for garbage collection
		return newOKCommandResult();
	}

}
