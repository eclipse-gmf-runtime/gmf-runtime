/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardContentsHelper;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.XtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ClipboardCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.CopyCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.PasteViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DiagramProvidersPlugin;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.ui.properties.actions.PropertyPageViewAction;
import com.ibm.xtools.notation.Edge;
import com.ibm.xtools.notation.View;

/**
 * Class that implements the <code>IGlobalActionHandler</code> interface.
 * Contains behaviour common to presentation.
 * 
 * @author Vishy Ramaswamy
 */
public class PresentationGlobalActionHandler
	extends AbstractGlobalActionHandler {

	/** Remember the "open" commands associated with the selected edit parts. */
	private ICommand openCommand = null;

	/**
	 * Constructor for PresentationGlobalActionHandler.
	 */
	public PresentationGlobalActionHandler() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		/* Check if the active part is a IDiagramWorkbenchPart */
		IWorkbenchPart part = cntxt.getActivePart();
		if (!(part instanceof IDiagramWorkbenchPart)) {
			return null;
		}

		/* Get the model operation context */
		IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) part;

		/* Create a command */
		ICommand command = null;

		/* Check the action id */
		String actionId = cntxt.getActionId();
		if (actionId.equals(GlobalActionId.DELETE)) {
			CompoundCommand deleteCC = getDeleteCommand(cntxt);			
			/* Set the command */
			if (deleteCC != null && deleteCC.canExecute())
				command = new XtoolsProxyCommand(deleteCC);
		} else if (actionId.equals(GlobalActionId.COPY)) {
			command = getCopyCommand(cntxt, diagramPart, false);
		} else if (actionId.equals(GlobalActionId.CUT)) {
			command = getCutCommand(cntxt, diagramPart);
		} else if (actionId.equals(GlobalActionId.OPEN)) {
			// Open command: use the previously cached command.
			command = openCommand;
		} else if (actionId.equals(GlobalActionId.PASTE)) {

			PasteViewRequest pasteReq = createPasteViewRequest();

			/* Get the selected edit parts */
			Object[] objects = ((IStructuredSelection) cntxt.getSelection())
				.toArray();

			if (objects.length == 1) {
				/* Send the request to the currently selected part */
				Command paste = ((EditPart) objects[0]).getCommand(pasteReq);
				if (paste != null) {
					/* Set the command */
					CommandStack cs = diagramPart.getDiagramEditDomain()
						.getDiagramCommandStack();
					cs.execute(paste);
					selectAddedObject(diagramPart.getDiagramGraphicalViewer(),
						DiagramCommandStack.getReturnValues(paste));
					return null;
				}
			}
		} else if (actionId.equals(GlobalActionId.SAVE)) {
			part.getSite().getPage().saveEditor((IEditorPart) diagramPart,
				false);
		} else if (actionId.equals(GlobalActionId.PROPERTIES)) {
			new PropertyPageViewAction().run();
		}

		return command;
	}

	/**
	 * Returns a command to copy the context's selection to the clipboard.
	 * 
	 * @param cntxt
	 *            the <code>IGlobalActionContext</code> from which the label
	 *            and selection are retrieved.
	 * @param diagramPart
	 *            the <code>IDiagramWorkbenchPart</code> from which the
	 *            diagram is retrieved.
	 * @param isUndoable
	 *            true if this command should be undoable/redoable; false
	 *            otherwise
	 * @return the copy command
	 */
	protected ICommand getCopyCommand(IGlobalActionContext cntxt,
			IDiagramWorkbenchPart diagramPart, final boolean isUndoable) {

		return new CopyCommand(cntxt.getLabel(), diagramPart.getDiagram(),
			getSelectedViews(cntxt.getSelection())) {

			public boolean isUndoable() {
				return isUndoable;
			}

			public boolean isRedoable() {
				return isUndoable;
			}

			protected CommandResult doUndo() {
				return isUndoable ? newOKCommandResult()
					: super.doUndo();
			}

			protected CommandResult doRedo() {
				return isUndoable ? newOKCommandResult()
					: super.doRedo();
			}
		};
	}
	
	/**
	 * Returns a command to copy the context's selection to the clipboard and to delete it.
	 * 
	 * @param cntxt
	 *            the <code>IGlobalActionContext</code> from which the label
	 *            and selection are retrieved.
	 * @param diagramPart
	 *            the <code>IDiagramWorkbenchPart</code> from which the
	 *            diagram is retrieved.
	 * @return the cut command
	 */
	protected ICommand getCutCommand(IGlobalActionContext cntxt, IDiagramWorkbenchPart diagramPart) {
		CompositeModelCommand cut = new CompositeModelCommand(cntxt.getLabel());

		// Add a copy command - the cut must be undoable/redoable
		cut.compose(getCopyCommand(cntxt, diagramPart, true));

		/* Get the selected edit parts */
		Object[] objects = ((IStructuredSelection) cntxt.getSelection())
			.toArray();
		for (int i = 0; i < objects.length; i++) {
			/* Get the next part */
			EditPart editPart = (EditPart) objects[i];

			/* Create the delete request */
			GroupRequest deleteReq = new GroupRequest(
				RequestConstants.REQ_DELETE);

			/* Send the request to the edit part */
			Command deleteCommand = editPart.getCommand(deleteReq);

			/* Add to the compound command */
			if (deleteCommand != null) {
				cut.compose(new XtoolsProxyCommand(deleteCommand));
			}
		}

		if (!cut.isEmpty() && cut.isExecutable())
			return cut;
		
		return null;
	}

	/**
	 * Creates and returns the appropriate <code>PasteViewRequest</code> that is
	 * to be used to get the appropriate paste <code>Command</code> from the <code>EditPart</code>.
	 * The returned <code>PasteViewRequest</code> contains data from the clipboard
	 * 
	 * @return PasteViewRequest 
	 */
	protected PasteViewRequest createPasteViewRequest() {
		PasteViewRequest pasteReq;
		ICustomData[] data = ClipboardManager.getInstance().getClipboardData(
			ClipboardCommand.DRAWING_SURFACE,
			ClipboardContentsHelper.getInstance());
		if (data == null) {
			data = ClipboardManager.getInstance().getClipboardData(
				ClipboardManager.COMMON_FORMAT,
				ClipboardContentsHelper.getInstance());
		}
		/* Create the paste request */
		pasteReq = new PasteViewRequest(data);
		return pasteReq;
	}
	
	/**
	 * Returns appropriate delete command for this context.
	 * 
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler
	 * @return CompoundCommand command
	 */
	private CompoundCommand getDeleteCommand(IGlobalActionContext cntxt){
		/* Create the delete request */
		GroupRequest deleteReq = new GroupRequest(
			RequestConstants.REQ_DELETE);

		CompoundCommand deleteCC = new CompoundCommand(cntxt.getLabel());

		CompositeModelCommand compositeCommand = new CompositeModelCommand(cntxt.getLabel());
		/* Get the selected edit parts */
		Object[] objects = ((IStructuredSelection) cntxt.getSelection())
			.toArray();
		for (int i = 0; i < objects.length; i++) {
			/* Get the next part */
			EditPart editPart = (EditPart) objects[i];

			/* Send the request to the edit part */
			Command command = editPart.getCommand(deleteReq);
			if (command != null)
				compositeCommand.compose(new XtoolsProxyCommand(command));
			//deleteCC.add(editPart.getCommand(deleteReq));
		}
		if (compositeCommand.getCommands().size() > 0){
			deleteCC.add(new EtoolsProxyCommand(compositeCommand));
		}
			
		return deleteCC;
	}
	
	
	private boolean isContainedInViews(List views, View view) {
		while (view != null) {
			if (views.contains(view)) {
				return true;
			}
			if(view.eContainer() instanceof View)
				view = (View)view.eContainer();
			else 
				break;
		}
		return false;
	}

	/**
	 * Returns the selected <code>View</code> objects, only if the selection is an <code>IStructuredSelection</code>.
	 * and only the <code>View</code> object of an <code>INodeEditPart</code> or a <code>ShapeEditPart</code>
	 * @param sel the selection from which to extract the View objects
	 * @return List the selected View. Could be empty if the selection doesn't contain proper edit parts, 
	 * or, could be the original if the selection is not an <code>IStructuredSelection</code>
	 */
	protected List getSelectedViews(ISelection sel) {
		final ArrayList views = new ArrayList();
		final ArrayList editParts = new ArrayList();

		/* Check if the selection is a structured selection */
		if (!(sel instanceof IStructuredSelection)) {
			return views;
		}

		/* Get the relevant Views */
		for (Iterator i = ((IStructuredSelection) sel).iterator(); i.hasNext();) {
			Object object = i.next();

			if (!((object instanceof INodeEditPart) || (object instanceof ShapeEditPart))){
				continue;
			}

			View view = (object instanceof IAdaptable) ? (View) ((IAdaptable) object)
				.getAdapter(View.class)
				: null;
			
			/* Make sure that view is not deleted */
			if (view != null && view.eResource()!=null) {
				views.add(view);
				editParts.add(object);
			}
		}

		/* Get rid of dangling connectors */
		try {
			MEditingDomainGetter.getMEditingDomain(views).runAsRead(new MRunnable() {

				public Object run() {
					ArrayList objects = (ArrayList) views.clone();
					for (Iterator i = objects.iterator(); i.hasNext();) {
						Object object = i.next();
						if (object instanceof Edge) {
							Edge view = (Edge) object;
							View fromView = view.getSource();
							View toView = view.getTarget();
							if (fromView == null || toView == null
								|| !isContainedInViews(views, fromView) || !isContainedInViews(views, toView)) {
								views.remove(view);
							}
						}
					}
					return null;
				}
			});
		} catch (Exception e) {
			Trace.catching(DiagramProvidersPlugin.getInstance(),
				DiagramActionsDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"getSelectedViews()", //$NON-NLS-1$
				e);
		}

		/* Make sure that the selection contains atleast one IShapeView */
		boolean doesSelectionContainAShapeView = false;
		for (Iterator i = editParts.iterator(); i.hasNext();) {
			if (i.next() instanceof ShapeEditPart) {
				doesSelectionContainAShapeView = true;
				break;
			}
		}

		/* Clear the selection if no shape views are present */
		if (!doesSelectionContainAShapeView) {
			views.clear();
		}
		return views;
	}

	/**
	 * Checks to determine if the selected edit part can be opened.
	 * <p>
	 * In order to truly verify that the edit part can be opened, the
	 * corresponding "open" command must be obtained and tested for execution.
	 * This command can then be cached for the getCommand() method.
	 * <p>
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler
	 * @return boolean - indicates an "open" command exists and can be executed
	 */
	private boolean canOpen(IGlobalActionContext cntxt) {
		// Reset any information about the elements to be opened.
		boolean canOpenAll = true;
		openCommand = new CompositeCommand(cntxt.getLabel());

		// Since all edit parts are associated with the smae edit domain,
		// get the model operation context from the first selected edit part.
		Object[] objects = ((IStructuredSelection) cntxt.getSelection())
			.toArray();

		// For all of the selected objects until one cannot be opened
		for (int i = 0; canOpenAll && i < objects.length; i++) {
			if (objects[i] instanceof EditPart) {
				// Get the edit part's command for the "open" request.
				EditPart editPart = (EditPart) objects[i];
				Request request = new Request(RequestConstants.REQ_OPEN);
				Command cmd = editPart.getCommand(request);

				// If a command is not available or cannot be executed,
				if (cmd == null || !cmd.canExecute()) {
					// Do not allow any of the selected parts to be opened.
					canOpenAll = false;
				}

				// else add the command to the composite command.
				else {
					openCommand.compose(new XtoolsProxyCommand(cmd));
				}
			}

		}

		if (!canOpenAll) {
			openCommand = null;
		}
		return canOpenAll;
	}

	/**
	 * Checks if the selected IViews can be deleted.
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler 
	 * @return boolean indicating the enablement of the delete action
	 */
	private boolean canDelete(IGlobalActionContext cntxt) {
		
		//The selectedObjects are not in a container
 		//that is set to canonical, so do a regular check that the 
 		//command is not null.
 		return getCommand(cntxt) != null;
	}
	
	/**
	 * Checks if the selected IViews can be copied to the clipboard
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler 
	 * @return boolean indicating the enablement of the copy action
	 */
	protected boolean canCopy(IGlobalActionContext cntxt) {
		List elements = getSelectedViews(cntxt.getSelection());
		/* Make sure the selection is not empty */
		if (elements.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * Checks if the selected IViews can be cut
	 * 
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler 
	 * @return boolean indicating the enablement of the cut action
	 */
	protected boolean canCut(IGlobalActionContext cntxt) {
		String actionId = cntxt.getActionId();
		if (actionId.equals(GlobalActionId.CUT)) {
			ICommand command = getCommand(cntxt);
			if (command != null && command.isExecutable()) {
				return canCopy(cntxt);
			}
		}
		return false;
	}

	/**
	 * Checks if the paste can occur
	 * 
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler 
	 * @return boolean indicating the enablement of the paste action
	 */	
	protected boolean canPaste(IGlobalActionContext cntxt) {
		/* Check if the clipboard has data for the drawing surface */
		return ClipboardManager.getInstance().doesClipboardHaveData(
			ClipboardCommand.DRAWING_SURFACE,
			ClipboardContentsHelper.getInstance())
			|| (ClipboardManager.getInstance().doesClipboardHaveData(
				ClipboardManager.COMMON_FORMAT, ClipboardContentsHelper
					.getInstance()));
	}

	/**
	 * Checks if the selected IElements will allow a print
	 * 
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler 
	 * @return boolean indicating the enablement of the print action
	 */
	private boolean canPrint() {
		return true;
	}

	/**
	 * Checks if the selected IElements will allow a save. Save should only be
	 * enabled when no shapes are selected to avoid clutter on the context menu
	 * and if the editor is dirty.
	 * 
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler 
	 * @return boolean indicating the enablement of the save action
	 */
	protected boolean canSave(IGlobalActionContext cntxt) {
		IWorkbenchPart part = cntxt.getActivePart();
		if (part instanceof IDiagramWorkbenchPart
			&& part instanceof IEditorPart && ((IEditorPart) part).isDirty()) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(final IGlobalActionContext cntxt) {
		boolean result = false;

		/* Check if the active part is a IDiagramWorkbenchPart */
		IWorkbenchPart part = cntxt.getActivePart();
		if (!(part instanceof IDiagramWorkbenchPart)) {
			return false;
		}

		/* Check if the selection is a structured selection */
		if (!(cntxt.getSelection() instanceof IStructuredSelection)) {
			return result;
		}

		/* Check the action id */
		String actionId = cntxt.getActionId();
		if (actionId.equals(GlobalActionId.DELETE)) {
			result = canDelete(cntxt);
		} else if (actionId.equals(GlobalActionId.COPY)) {
			result = canCopy(cntxt);
		} else if (actionId.equals(GlobalActionId.CUT)) {
			result = canCut(cntxt);
		} else if (actionId.equals(GlobalActionId.OPEN)) {
			result = canOpen(cntxt);
		} else if (actionId.equals(GlobalActionId.PASTE)) {
			result = canPaste(cntxt);
		} else if (actionId.equals(GlobalActionId.PRINT)) {
			result = canPrint();
		} else if (actionId.equals(GlobalActionId.SAVE)) {
			result = canSave(cntxt);
		} else if (actionId.equals(GlobalActionId.PROPERTIES)) {
			result = true;
		}

		return result;
	}

	/**
	 * Select the newly added shape view by default.
	 * 
	 * @param viewer the viewer owning the edit parts to be selected
	 * @param objects the collection of object from which to extract the <code>EditPart</code>
	 * to select 
	 */
	protected void selectAddedObject(EditPartViewer viewer, Collection objects) {
		final List editparts = new ArrayList();
		for (Iterator i = objects.iterator(); i.hasNext();) {
			Object editPart = getEditPart(viewer, i.next());
			if (editPart != null)
				editparts.add(editPart);
		}
		if (!editparts.isEmpty()) {
			viewer.setSelection(new StructuredSelection(editparts));
		}
	}
	
	private EditPart getEditPart(EditPartViewer viewer, Object object){
		if (object instanceof View) {
			return (EditPart)viewer.getEditPartRegistry().get(object);			
		}else if(object instanceof IAdaptable){
			return (EditPart)viewer.getEditPartRegistry().get(
				((IAdaptable) object).getAdapter(View.class));			
		}
		return null;
	}
}