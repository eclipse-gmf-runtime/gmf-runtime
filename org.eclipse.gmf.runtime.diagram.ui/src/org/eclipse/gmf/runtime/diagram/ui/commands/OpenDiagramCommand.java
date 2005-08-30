/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.       		       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import com.ibm.xtools.notation.Diagram;

/**
 * Command to open a diagram.
 * 
 * @author jcorchis
 */

public class OpenDiagramCommand extends AbstractCommand {

	/** Remember the element to be opened. */
	private EObject _element = null;

	/**
	 * Create an instance.
	 * @param label command label
	 * @param element to be opened.
	 */
	public OpenDiagramCommand( String label, EObject element ) {
		super(label);
		setElement( element );
	}
	
	/**
	 * returns the element associated with that command
	 * @return the element associated with that command
	 */
	protected EObject getElement() {
		return _element;
	}
	
	
	/**
	 * set the element to open
	 * @param element the element to open 
	 */
	protected void setElement( EObject element ) {
		_element = element;
	}
	
	/**
	 * Create an instance.
	 * @param element to be opened.
	 */
	public OpenDiagramCommand( EObject element ) {
		this(PresentationResourceManager.getI18NString("Command.openDiagram"), element);//$NON-NLS-1$
	}
	
	/**
	 * This command can only be executed if the element is a diagram.
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	*/
	public boolean isExecutable() {
		return getElement() instanceof Diagram;
	}

	/**
	 * Create a new editor to display the corresponding diagram.
	 * <p>
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		try {
			MEditingDomainGetter.getMEditingDomain(getElement()).runAsRead(new MRunnable() {
				public Object run() {
					Diagram diagram = null;
					// Obtain the associated diagram if one exists.
					if (getElement() instanceof Diagram) {
						diagram = (Diagram) getElement();
					}
					// If a diagram element exists, open it in its own editor.
					if (diagram != null) {
						IDiagramEditorInput diagramInput = new DiagramEditorInput(diagram);
						IEditorPart editor = null;
						editor = EditorService.getInstance().openEditor(diagramInput);
						if (editor == null) {
							throw new UnsupportedOperationException();
						}
					}					
					return null;
				}
			});
		} catch (Exception e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
					DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
					"doExecute()", //$NON-NLS-1$
					e);
			return new CommandResult(
					new Status(
						IStatus.ERROR,
						getPluginId(),
						DiagramUIStatusCodes.COMMAND_FAILURE,
						e.getMessage(),
						e));
		}
		return newOKCommandResult();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return false;
	}

}
