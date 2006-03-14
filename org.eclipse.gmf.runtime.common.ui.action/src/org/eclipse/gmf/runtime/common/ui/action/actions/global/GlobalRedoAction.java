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

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.operations.RedoActionHandler;

/**
 * Global Redo Action.
 * <P>
 * Extension of the GMF {@link GlobalAction} class that delegates redo behaviour
 * to the undoable operation framework's {@link RedoActionHandler}.
 * <P>
 * The undo context can be set through {@link #setUndoContext(IUndoContext)}.
 * If it is not explicitly set, the undo context will be derived by adapting the
 * workbench part to {@link IUndoContext}.
 * 
 * @author vramaswa
 */
public final class GlobalRedoAction extends GlobalAction {

	/**
	 * My operation framework action handler delegate.
	 */
	private RedoActionHandler delegate;

	/**
	 * My undo context.
	 */
	private IUndoContext undoContext;

	/**
	 * Property change listener to listen for changes in my delegate.
	 */
	private IPropertyChangeListener listener;

	/**
	 * Initializes me with a workbench page.
	 * 
	 * @param workbenchPage
	 *            the page
	 */
	public GlobalRedoAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Initializes me with a workbench part.
	 * 
	 * @param workbenchPart
	 *            the part
	 */
	public GlobalRedoAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.REDO;
	}

	/**
	 * Extends the superclass implementation to update the operation history
	 * undo action handler to which I delegate.
	 */
	protected void setWorkbenchPart(IWorkbenchPart workbenchPart) {
		super.setWorkbenchPart(workbenchPart);
		initializeWithContext(getUndoContext());
	}

	/**
	 * Initializes me with a new undo <code>context</code>.
	 * 
	 * @param context
	 *            the undo context
	 */
	protected void initializeWithContext(IUndoContext context) {
		if (delegate != null) {
			delegate.removePropertyChangeListener(getDelegateListener());
			delegate.dispose();
			delegate = null;
		}

		if (context != null) {
			IWorkbenchPart part = getWorkbenchPart();

			if (part != null) {
				delegate = new RedoActionHandler(part.getSite(), context);
				delegate.setPruneHistory(true);
				delegate.addPropertyChangeListener(getDelegateListener());
			}
		}

		// force enablement update in UI
		boolean enabled = isEnabled();
		firePropertyChange(IAction.ENABLED, Boolean.valueOf(!enabled), Boolean
				.valueOf(enabled));
	}

	/**
	 * Gets my property change listener to listen for changes in my delegate.
	 */
	private IPropertyChangeListener getDelegateListener() {
		if (listener == null) {
			listener = new IPropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent event) {
					// propagate to my own listeners
					firePropertyChange(event.getProperty(),
							event.getOldValue(), event.getNewValue());
				}
			};
		}
		return listener;
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public ImageDescriptor getImageDescriptor() {
		if (delegate != null) {
			return delegate.getImageDescriptor();
		} else {
			return null;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public ImageDescriptor getDisabledImageDescriptor() {
		if (delegate != null) {
			return delegate.getDisabledImageDescriptor();
		} else {
			return null;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public ImageDescriptor getHoverImageDescriptor() {
		if (delegate != null) {
			return delegate.getHoverImageDescriptor();
		} else {
			return null;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public String getText() {
		if (delegate != null) {
			return delegate.getText();
		} else {
			return null;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public String getToolTipText() {
		if (delegate != null) {
			return delegate.getToolTipText();
		} else {
			return null;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public String getDescription() {
		if (delegate != null) {
			return delegate.getDescription();
		} else {
			return null;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public boolean isEnabled() {
		if (delegate != null) {
			return delegate.isEnabled();
		} else {
			return false;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public boolean isHandled() {
		if (delegate != null) {
			return delegate.isHandled();
		} else {
			return false;
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public void setChecked(boolean checked) {
		if (delegate != null) {
			delegate.setChecked(checked);
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		if (delegate != null) {
			delegate.run();
		}
	}

	/**
	 * Delegates to the operation framework action handler.
	 */
	public void refresh() {
		if (delegate != null) {
			delegate.update();
		}
		setText(getText());
	}

	/**
	 * Sets my undo context. It will no longer be derived from the workbench
	 * part.
	 * 
	 * @param undoContext
	 *            my undo context
	 */
	public final void setUndoContext(IUndoContext context) {
		this.undoContext = context;
		initializeWithContext(context);
	}

	/**
	 * Gets my undo context. If it has not been explicitly set, derives the undo
	 * context from my workbench part.
	 * 
	 * @return my undo context. May be <code>null</code> if no one has set my
	 *         undo context and my workbench part does not adapt to
	 *         {@link IUndoContext}.
	 */
	public final IUndoContext getUndoContext() {

		if (undoContext == null) {
			IWorkbenchPart part = getWorkbenchPart();

			if (part != null) {
				return (IUndoContext) part.getAdapter(IUndoContext.class);
			}
		}
		return undoContext;
	}

	/**
	 * Listens to the operation history events.
	 */
	protected boolean isOperationHistoryListener() {
		return true;
	}

	/**
	 * Disposes my delegate.
	 */
	public void dispose() {

		if (delegate != null) {
			delegate.removePropertyChangeListener(getDelegateListener());
			delegate.dispose();
			delegate = null;
		}

		super.dispose();
	}

}
