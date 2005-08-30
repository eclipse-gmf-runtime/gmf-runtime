/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A command used to optionally create a new view and new element. This command
 * is used when it is not known at command creation time whether or not an
 * element should be created as well. For example, when creating a connection to
 * an unspecified target, did the user want to
 * <li>create a new element for the target (view and element)?</li>
 * <li>use an existing element and its view already on the diagram (nothing
 * created)?</li>
 * <li>use an existing element and add a new view to the diagram (view only)?
 * 
 * <p>
 * Note: This command will pop up a dialog box if the element exists already and
 * there is a view for it on the diagram to ask the user what they want to do.
 * </p>
 * 
 * @author cmahoney
 */
public class CreateViewAndOptionallyElementCommand
	extends AbstractCommand {

	/**
	 * Adapts to the element, if null at command execution time, an element is
	 * to be created.
	 */
	private IAdaptable elementAdapter;

	/** The location to create the new view. */
	private Point location;

	/** The container editpart to send the view request to. */
	private IGraphicalEditPart containerEP;

	/** The command executed, saved for undo/redo. */
	private Command command = null;

	/** The result to be returned from which the new view can be retrieved. */
	private ObjectAdapter resultAdapter = new ObjectAdapter();
	
	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connectors, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private PreferencesHint preferencesHint;

	/**
	 * Creates a new <code>CreateViewAndOptionallyElementCommand</code>.
	 * 
	 * @param elementAdapter
	 *            Adapts to the element, if null at command execution time, an
	 *            element is to be created.
	 * @param containerEP
	 *            The container edit part, where the view request is sent.
	 * @param location
	 *            The location to create the new view. If null, a default
	 *            location is used
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateViewAndOptionallyElementCommand(IAdaptable elementAdapter,
		IGraphicalEditPart containerEP, Point location, PreferencesHint preferencesHint) {
		super(PresentationResourceManager.getI18NString("CreateCommand.Label")); //$NON-NLS-1$

		setElementAdapter(elementAdapter);
		setContainerEP(containerEP);
		if (location != null) {
			setLocation(location);
		} else {
			setLocation(getContainerEP().getFigure().getBounds().getTopRight()
				.translate(100, 100));
		}
		setPreferencesHint(preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		if (containerEP != null) {
			View view = (View)containerEP.getModel();
			if (view != null) {
				IFile f = EObjectUtil.getWorkspaceFile(view);
				return f != null ? Collections.singletonList(f) : Collections.EMPTY_LIST;
			}
		}
		return super.getAffectedObjects();
	}
	
	/**
	 * Searches the container editpart to see if the element passed in already
	 * has a view.
	 * 
	 * @param element
	 * @return IView the view if found; or null
	 */
	protected View getExistingView(EObject element) 
	{
		IGraphicalEditPart theTarget =(IGraphicalEditPart)getContainerEP().findEditPart(null, element);
		if(theTarget != null)
			return (View)theTarget.getModel();
		return null;
		
	}
	
	/**
	 * <li>If the element adapter is empty, this command creates a new element
	 * and view for it.</li>
	 * <li>If the element adapter is not empty, and a view for this element
	 * exists in the container, this command will prompt the user to see if they
	 * want to use the existing view or create a new view for the element and
	 * then execute accordingly.</li>
	 * <li>If the element adapter is not empty, and a view for this element
	 * does not exist in the container, this command will create a new element
	 * and view.</li>
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		CreateViewRequest createRequest;

		// Create the element first, if one does not exist.
		EObject element = (EObject) getElementAdapter().getAdapter(
			EObject.class);
		if (element == null) {
			IElementType type = (IElementType) getElementAdapter()
				.getAdapter(IElementType.class);
			if (type == null) {
				return newErrorCommandResult(getLabel());
			}
			createRequest = CreateViewRequestFactory
				.getCreateShapeRequest(type, getPreferencesHint());
		} else {
			createRequest = new CreateViewRequest(
				new CreateViewRequest.ViewDescriptor(
					new EObjectAdapter(element), getPreferencesHint()));
		}
		createRequest.setLocation(getLocation());

		if (createRequest != null) {
			IGraphicalEditPart target = (IGraphicalEditPart) getContainerEP().getTargetEditPart(createRequest);
			if ( target != null ) {
				Command theCmd = target.getCommand(createRequest);
				setCommand(theCmd);

				View theExistingView = getExistingView(element);
				if(theExistingView != null)
				{
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.YES | SWT.NO);

					messageBox.setText(PresentationResourceManager.getI18NString("CreateViewAndOptionallyElementCommand.ViewExists.Title"));//$NON-NLS-1$
					messageBox.setMessage(MessageFormat.
						format(PresentationResourceManager.getI18NString("CreateViewAndOptionallyElementCommand.ViewExists.Message"), //$NON-NLS-1$
						new Object[] {EObjectUtil.getName(element)}));
					int iResult = messageBox.open();
					if(iResult == SWT.YES)
					{
						setResult(new EObjectAdapter(theExistingView));
						return newOKCommandResult(getResult());
					}
				}
				// Fall-thru and create a new view
				if (getCommand().canExecute()) {
					ICommand xtoolsCommand = DiagramCommandStack.getICommand(getCommand());
					xtoolsCommand.execute(progressMonitor);					
					if (progressMonitor.isCanceled()) {
						return newCancelledCommandResult();
					}else if (!(xtoolsCommand.getCommandResult().getStatus().isOK())){
						return xtoolsCommand.getCommandResult();
					}				
					Object obj = ((List) createRequest.getNewObject()).get(0);										
					setResult((IAdaptable) obj);
					return newOKCommandResult(getResult());
				}
			}
		}
		containerEP = null;// to allow garbage collection
		return newErrorCommandResult(getLabel());
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return getCommand() != null && getCommand().canUndo();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return getCommand() != null && getCommand().canExecute();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		if (getCommand() != null) {
			getCommand().redo();
		}
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		if (getCommand() != null) {
			getCommand().undo();
		}
		return newOKCommandResult();
	}

	/**
	 * @return the adapter from which the view can be retrieved.
	 */
	public IAdaptable getResult() {
		return resultAdapter;
	}

	/**
	 * Sets the result to adapt to the view passed in.
	 * @param viewAdapter
	 */
	protected void setResult(IAdaptable viewAdapter) {
		View view = (View) viewAdapter.getAdapter(View.class);
		resultAdapter.setObject(view);
	}

	/**
	 * Gets the elementAdapter.
	 * @return Returns the elementAdapter.
	 */
	protected IAdaptable getElementAdapter() {
		return elementAdapter;
	}

	/**
	 * Sets the elementAdapter.
	 * @param elementAdapter The elementAdapter to set.
	 */
	protected void setElementAdapter(IAdaptable elementAdapter) {
		this.elementAdapter = elementAdapter;
	}

	/**
	 * Gets the location.
	 * @return Returns the location.
	 */
	protected Point getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * @param location The location to set.
	 */
	protected void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Gets the containerEP.
	 * @return Returns the containerEP.
	 */
	protected IGraphicalEditPart getContainerEP() {
		return containerEP;
	}

	/**
	 * Sets the containerEP.
	 * @param containerEP The containerEP to set.
	 */
	protected void setContainerEP(IGraphicalEditPart containerEP) {
		this.containerEP = containerEP;
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
		return preferencesHint;
	}
	
	/**
	 * Sets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @param preferencesHint the preferences hint
	 */
	protected void setPreferencesHint(PreferencesHint preferencesHint) {
		this.preferencesHint = preferencesHint;
	}

	/**
	 * Gets the command.
	 * @return Returns the command.
	 */
	protected Command getCommand() {
		return command;
	}

	/**
	 * Sets the command.
	 * @param command The command to set.
	 */
	protected void setCommand(Command command) {
		this.command = command;
	}
}
