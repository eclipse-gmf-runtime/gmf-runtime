/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorPart;

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
		super(label, null);
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
		this(DiagramUIMessages.Command_openDiagram, element);
	}
	
	/**
	 * This command can only be executed if the element is a diagram.
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	*/
    public boolean canExecute() {
		return getElement() instanceof Diagram;
	}

	/**
	 * Create a new editor to display the corresponding diagram.
	 * <p>
	 * @see org.eclipse.gmf.runtime.common.core.sandbox.AbstractCommand2#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */

    protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {
        
		try {
			TransactionUtil.getEditingDomain(getElement()).runExclusive(new Runnable() {
				public void run() {
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
						DiagramUIPlugin.getPluginId(),
						DiagramUIStatusCodes.COMMAND_FAILURE,
						e.getMessage(),
						e));
		}
		return CommandResult.newOKCommandResult();
	}

    
    public boolean canUndo() {
		return false;
	}

    public boolean canRedo() {
		return false;
	}

    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        return null;
}
    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        return null;
    }

}
