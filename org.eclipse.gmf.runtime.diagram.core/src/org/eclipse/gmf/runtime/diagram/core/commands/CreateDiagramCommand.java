/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramStatusCodes;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Creates a diagram
 *
 * @author schafe
 */
public class CreateDiagramCommand
	extends AbstractModelCommand {

	private final String _diagramType;

	private EObject _semanticContext;
	
	private PreferencesHint _preferencesHint;

	/**
	 * creates a create diagram command.
	 * @param label command label
	 * @param anElementContext semantic element to contain the diagram
	 * @param aDiagramKindType diagram type ID
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateDiagramCommand(String label, EObject anElementContext,
		String aDiagramKindType, PreferencesHint preferencesHint) {
		super(label, null);
		assert null != anElementContext: "Null element context in CreateDiagramCommand";//$NON-NLS-1$		
		_semanticContext = anElementContext;
		_diagramType = aDiagramKindType;
		_preferencesHint = preferencesHint;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		// Create the new diagram element
		//IElementCollection contents = getOwnedDiagramCollection(createOwningElement(progressMonitor));
		Diagram diagram = ViewService.getInstance().createDiagram(
			new EObjectAdapter(getSemanticContext()), getDiagramType(), getPreferencesHint());

		return new CommandResult(new Status(IStatus.OK, getPluginId(),
			DiagramStatusCodes.OK, EMPTY_STRING, null), diagram);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#getPluginId()
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