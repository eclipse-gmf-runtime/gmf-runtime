/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramStatusCodes;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Creates a diagram
 *
 * @author schafe
 */
public class CreateDiagramCommand
	extends AbstractTransactionalCommand {

	private final String _diagramType;

	private EObject _semanticContext;
	
	private PreferencesHint _preferencesHint;

	/**
	 * creates a create diagram command.
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label command label
	 * @param anElementContext semantic element to contain the diagram
	 * @param aDiagramKindType diagram type ID
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateDiagramCommand(TransactionalEditingDomain editingDomain, String label, EObject anElementContext,
		String aDiagramKindType, PreferencesHint preferencesHint) {
		super(editingDomain, label, null);
		assert null != anElementContext: "Null element context in CreateDiagramCommand";//$NON-NLS-1$		
		_semanticContext = anElementContext;
		_diagramType = aDiagramKindType;
		_preferencesHint = preferencesHint;
	}

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		// Create the new diagram element
		//IElementCollection contents = getOwnedDiagramCollection(createOwningElement(progressMonitor));
		Diagram diagram = ViewService.getInstance().createDiagram(
			new EObjectAdapter(getSemanticContext()), getDiagramType(), getPreferencesHint());
        int severity = IStatus.OK;
        if (diagram==null)
            severity = IStatus.ERROR;
		return new CommandResult(new Status(severity, getPluginId(),
			DiagramStatusCodes.OK, StringStatics.BLANK, null), diagram);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand2#getPluginId()
	 */
	protected String getPluginId() {
		return DiagramPlugin.getPluginId();
	}

	/**
	 * gives access to the diagram type.
	 * @return DiagramKind diagramType
	 */
	protected String getDiagramType() {
		return _diagramType;
	}

	/**
	 * gives access to the semantic context
	 * @return the semantic context
	 */
	protected EObject getSemanticContext() {
		return _semanticContext;

	}

	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint() {
		return _preferencesHint;

	}
}