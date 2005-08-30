/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredCreateConnectorViewCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.PresentationNotationType;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import com.ibm.xtools.notation.Node;

/**
 * Concrete implemention of AttachedShapeAction which attaches a new Notational Note to the
 * targeted shapes.
 * 
 * @author jcorchis 
 */
public class AddNoteAction extends AttachedShapeAction {

	/**
	 * Constructor
	 * @param page the active workbenchPage. 
	 */
	public AddNoteAction(IWorkbenchPage page) {
		super(page);

	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init() {
		super.init();
		setText(DiagramActionsResourceManager.getI18NString("AddNoteAction.ActionLabelText")); //$NON-NLS-1$
		setId(ActionIds.ACTION_ADD_NOTELINK);
		setToolTipText(DiagramActionsResourceManager.getI18NString("AddNoteAction.ActionToolTipText")); //$NON-NLS-1$
		setImageDescriptor(
			DiagramActionsResourceManager.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.DESC_NOTE_ATTACHMENT));
		setHoverImageDescriptor(getImageDescriptor());
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getSelectedObjects().isEmpty())
			return true;
		List parts = getSelectedObjects();
		for (int i = 0; i < parts.size(); i++) {
			Object o = parts.get(i);
			if (!(o instanceof INodeEditPart))
				return false;
			else{
				INodeEditPart nodeEditPart = (INodeEditPart)o;
				if (!(nodeEditPart.canAttachNote())){
					return false;
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		List selectedEditParts = getSelectedObjects();
		IDiagramWorkbenchPart editor = getDiagramWorkbenchPart();

		// note request
		ViewDescriptor viewDescriptor =
			new ViewDescriptor(null, Node.class, PresentationNotationType.NOTE.getSemanticHint(), getPreferencesHint());
		CreateViewRequest noteRequest = new CreateViewRequest(viewDescriptor);
		
		noteRequest.setLocation(getLocation(selectedEditParts));
		
		IGraphicalEditPart primaryPart = (IGraphicalEditPart) selectedEditParts.get(0);
		if (primaryPart instanceof ConnectionEditPart) {
			primaryPart = (IGraphicalEditPart) ((ConnectionEditPart) primaryPart).getSource();
		}		

		Command createNoteCmd =
			getContainer(primaryPart).getCommand(noteRequest);

		// note view adapter
		IAdaptable noteViewAdapter =
			(IAdaptable) ((List)noteRequest.getNewObject()).get(0);

		// create the note attachments commands
		CompositeCommand noteAttachmentCC =
			new CompositeCommand(getToolTipText());

		Iterator iter = selectedEditParts.iterator();
		while (iter.hasNext()) {
			IGraphicalEditPart targetEditPart =
				(IGraphicalEditPart) iter.next();

			noteAttachmentCC.compose(
				new DeferredCreateConnectorViewCommand(
					Properties.NOTEATTACHMENT,
					noteViewAdapter,
					new EObjectAdapter((EObject)targetEditPart.getModel()),
					editor.getDiagramGraphicalViewer(),
					getPreferencesHint()));
		}

		CompoundCommand cc =
			new CompoundCommand(getToolTipText());
		cc.add(createNoteCmd);
		cc.add(new EtoolsProxyCommand(noteAttachmentCC));

		editor.getDiagramEditDomain().getDiagramCommandStack().execute(cc);
		
		selectAddedObject(editor.getDiagramGraphicalViewer(), noteRequest);
	}

}
