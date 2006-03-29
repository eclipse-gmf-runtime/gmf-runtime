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

package org.eclipse.gmf.runtime.common.ui.action.global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionPlugin;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionStatusCodes;
import org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalActionHandlerData;
import org.eclipse.gmf.runtime.common.ui.services.action.global.GlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.services.action.global.GlobalActionHandlerContext;
import org.eclipse.gmf.runtime.common.ui.services.action.global.GlobalActionHandlerService;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerProvider;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The abstract parent of all concrete global actions. A concrete global action
 * needs to override the <code>getActionId()</code> method. The concrete
 * global action could override the <code>createContext()</code> and
 * <code>createCompoundCommand()</code> methods.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class GlobalAction
	extends AbstractActionHandler {

	/**
	 * Associated IWorkbenchActionConstant if one exists
	 */
	private final String workbenchActionConstant = getActionId();

	/**
	 * Default label for this global action.
	 */
	private String defaultLabel;

	/**
	 * Creates a GlobalAction.
	 * 
	 * @param workbenchPart
	 *            The part associated with this action
	 */
	public GlobalAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);

		assert null != workbenchPart;

		/* Disable the action when it is created */
		setEnabled(false);
	}

	/**
	 * Creates a GlobalAction.
	 * 
	 * @param workbenchPage
	 *            The part associated with this action
	 */
	public GlobalAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);

		assert null != workbenchPage;

		/* Disable the action when it is created */
		setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		Vector list = new Vector();

		/* Get the handler data */
		List handlerInfo = getGlobalActionHandlerData();
		for (Iterator i = handlerInfo.iterator(); i.hasNext();) {
			/* get the next element */
			GlobalActionHandlerData data = (GlobalActionHandlerData) i.next();

			/* Get the command */
			ICommand command = data.getHandler().getCommand(data.getContext());
			if (command != null) {
				list.addElement(command);
			}
		}

		if (list.size() <= 0) {
			return;
		}

		/* Create the composite operation */
		IUndoableOperation operation = createCompositeCommand(list).reduce();
        try {
            IStatus status = getOperationHistory()
                .execute(operation, progressMonitor, null);
            
    		if (!status.isOK()) {
    			/* log status error */
    			Log.log(CommonUIActionPlugin.getDefault(), status);
    		}
        } catch (ExecutionException e) {
            Trace.catching(CommonUIActionPlugin.getDefault(),
                CommonUIActionDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "doRun", e); //$NON-NLS-1$
            Log.error(CommonUIActionPlugin.getDefault(),
                CommonUIActionStatusCodes.ACTION_FAILURE, e
                    .getLocalizedMessage(), e);
        }

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		boolean enable = false;
		try {
			/* Get the handler data */
			List handlerInfo = getGlobalActionHandlerData();

			// Reset the label to the default
			if (defaultLabel != null) {
				setText(defaultLabel);
			}

			/* Check the handlers for enablement status */
			for (Iterator i = handlerInfo.iterator(); i.hasNext();) {
				/* Get the next element */
				GlobalActionHandlerData data = (GlobalActionHandlerData) i
					.next();

				/* Check the enablement */
				if (data.getHandler().canHandle(data.getContext())) {
					if (!enable) {
						enable = true;
					}
				}

				/* Update the label, if appropriate */
				if (handlerInfo.size() == 1) {
					String label = data.getHandler()
						.getLabel(data.getContext());
					if (label != null) {
						setText(label);
					}
				}
			}
		} catch (Throwable exception) {
			enable = false;
			Trace.catching(CommonUIActionPlugin.getDefault(),
				CommonUIActionDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"refresh", exception); //$NON-NLS-1$

			IStatus status = new Status(IStatus.WARNING, CommonUIActionPlugin
				.getPluginId(), CommonUIActionStatusCodes.GENERAL_UI_FAILURE, String
				.valueOf(exception.getMessage()), exception);

			Log.log(CommonUIActionPlugin.getDefault(), status);
		}

		/* Set the enablement of the action */
		setEnabled(enable);
	}

	/**
	 * Returns the <code>GlobalActionId</code> handled by this action
	 * 
	 * @return int
	 */
	public abstract String getActionId();

    /**
     * Returns a <code>CompositeCommand</code> whose undo context is derived from my workbench part.
     * 
     * @param commands a list of commands to compose into a <code>CompositeCommand</code>
     * @return the CompositeCommand
     */
    protected CompositeCommand createCompositeCommand(List commands) {
        assert null != commands;
        
        CompositeCommand result = new CompositeCommand(getLabel(), commands);
        IUndoContext undoContext = getUndoContext();
        
        if (undoContext != null) {
            result.addContext(undoContext);
        }
        return result;
    }
    
    /**
     * Gets the undo context from my workbench part. May be <code>null</code>.
     * 
     * @return my undo context
     */
    protected IUndoContext getUndoContext() {
        IWorkbenchPart part = getWorkbenchPart();

        if (part != null) {
            return (IUndoContext) part.getAdapter(IUndoContext.class);
        }
        return null;
    }


	/**
     * Returns a <code>IGlobalActionContext</code>
     * 
     * @return IGlobalActionContext
     */
	protected IGlobalActionContext createContext() {
		/* Create the global action context */
		return new GlobalActionContext(getWorkbenchPart(), getSelection(),
			getLabel(), getActionId());
	}

	/**
	 * Returns a list of <code>GlobalActionHandlerData</code>. Handles
	 * different types of selections
	 * 
	 * @return List
	 */
	protected List getGlobalActionHandlerData() {
		/* Check if the selection is a text selection */
		if (getSelection() instanceof ITextSelection) {
			return getGlobalActionHandlerData((ITextSelection) getSelection());
		} else if (getSelection() instanceof IStructuredSelection) {
			return getGlobalActionHandlerData((IStructuredSelection) getSelection());
		}

		return new ArrayList();
	}

	/**
	 * Returns a list of <code>GlobalActionHandlerData</code> for a given list
	 * of element types
	 * 
	 * @param listOfElementTypes
	 *            list of unique element types
	 * @return List
	 */
	private List getGlobalActionHandlerData(List listOfElementTypes) {
		assert null != listOfElementTypes;

		/* Get the global action handler for unique element types */
		ArrayList listOfHandlers = new ArrayList();
		Iterator iterator = listOfElementTypes.iterator();
		while (iterator.hasNext()) {
			/* Get the element type */
			Class clazz = (Class) iterator.next();

			/* Create the global action handler context */
			GlobalActionHandlerContext context = new GlobalActionHandlerContext(
				getWorkbenchPart(), getActionId(), clazz, false);

			/* Get the handler */
			IGlobalActionHandler handler = GlobalActionHandlerService
				.getInstance().getGlobalActionHandler(context);

			/* Get a compatible one if no handler is found for a direct match */
			if (handler == null) {
				/* Create the global action handler context */
				context = new GlobalActionHandlerContext(getWorkbenchPart(),
					getActionId(), clazz, true);

				/* Get the handler */
				handler = GlobalActionHandlerService.getInstance()
					.getGlobalActionHandler(context);
			}

			/* Add to the list */
			if (handler != null && !listOfHandlers.contains(handler)) {
				listOfHandlers.add(handler);
			}
		}

		/* Create the global action handler data and add it to the list */
		ArrayList handlerData = new ArrayList();
		IGlobalActionContext actionContext = createContext();
		for (int i = 0; i < listOfHandlers.size(); i++) {
			/* Get the next handler */
			IGlobalActionHandler handler = (IGlobalActionHandler) listOfHandlers
				.get(i);

			/* Create the global action handler data */
			handlerData
				.add(new GlobalActionHandlerData(handler, actionContext));
		}

		/* Return the handler data */
		return handlerData;
	}

	/**
	 * Returns a list of <code>GlobalActionHandlerData</code> for selection of
	 * type <code>IStructuredSelection</code>. This methods queries the
	 * <code>GlobalActionHandlerService</code> for all the global action
	 * handlers associated with this action.
	 * 
	 * @param selection
	 *            The <code>IStructuredSelection</code>
	 * @return List
	 */
	private List getGlobalActionHandlerData(IStructuredSelection selection) {
		assert null != selection;

		/* Create a unique list of element types */
		ArrayList listOfElementTypes = new ArrayList();

		if (selection.isEmpty()) {
			// Use the NullElementType to signify that global action handlers
			// should be found that provide regardless of the selected types.
			listOfElementTypes.add(IGlobalActionHandlerProvider.NullElementType.class);

		} else {
			/* Get the selection as an object array */
			Object[] array = selection.toArray();

			for (int i = 0; i < array.length; i++) {
				if (!listOfElementTypes.contains(array[i].getClass())) {
					listOfElementTypes.add(array[i].getClass());
				}
			}
		}

		/* Get the global action handler for unique element types */
		return getGlobalActionHandlerData(listOfElementTypes);
	}

	/**
	 * Returns a list of <code>GlobalActionHandlerData</code> for selection of
	 * type <code>ITextSelection</code>. This methods queries the
	 * <code>GlobalActionHandlerService</code> for all the global action
	 * handlers associated with this action.
	 * 
	 * @param selection
	 *            The <code>ITextSelection</code>
	 * @return List
	 */
	private List getGlobalActionHandlerData(ITextSelection selection) {
		assert null != selection;

		/* Get the element type */
		Class clazz = selection.getClass();

		/* Create a unique list of element types */
		ArrayList listOfElementTypes = new ArrayList();
		listOfElementTypes.add(clazz);

		/* Get the global action handler for unique element types */
		return getGlobalActionHandlerData(listOfElementTypes);
	}

	/**
	 * Returns the workbenchActionConstant.
	 * 
	 * @return String
	 */
	protected String getWorkbenchActionConstant() {
		return workbenchActionConstant;
	}

	/**
	 * Returns a list with a GlobalActionHandlerData object containing a context
	 * of Object. You can have getObjectContextGlobalActionHandlerData call this
	 * instead.
	 * 
	 * @return List with a GlobalActionHandlerData object containing a context
	 *         of Object
	 */
	protected List getObjectContextGlobalActionHandlerData() {
		GlobalActionHandlerContext context = new GlobalActionHandlerContext(
			getWorkbenchPart(), getActionId(), Object.class, false);

		IGlobalActionHandler globalActionHandler = GlobalActionHandlerService
			.getInstance().getGlobalActionHandler(context);

		if (globalActionHandler == null) {
			//an error may occur, OK because someone is playing with the xml
			return new ArrayList();
		}

		GlobalActionHandlerData data = new GlobalActionHandlerData(
			globalActionHandler, createContext());

		ArrayList list = new ArrayList();
		list.add(data);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init() {
		super.init();
		defaultLabel = getLabel();
	}

}