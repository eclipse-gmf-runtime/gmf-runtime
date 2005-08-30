/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.command.CommandManagerChangeEvent;
import org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.util.PartListenerAdapter;
import org.eclipse.gmf.runtime.common.ui.util.StatusLineUtil;

/**
 * The abstract parent of all concrete action handlers that execute commands.
 * Logging and exception handling are done in a uniform way in the
 * <code>run()</code> method. Concrete subclasses must provide a definition of
 * the <code>doRun()</code> method to gather any required input and execute a
 * command. As an implementer of the <code>IRepeatableAction</code> interface,
 * this class implements the <code>isRepeatable()</code> method to return
 * <code>true</code> if it is enabled, and implements the
 * <code>repeat()</code> method to run itself. Subclasses that aren't
 * repeatable or require special repeat behavior must override the default
 * implementations of these interface methods.
 * 
 * This action handler supports life cycle methods by implementing the
 * <code>IDisposableAction</code> interface. Therefore, clients need to call
 * the <code>init()</code> method to initialize the action, and the
 * <code>dispose()</code> method when the action is no longer needed.
 * 
 * @author khussey
 */
public abstract class AbstractActionHandler
	extends Action
	implements IDisposableAction, IRepeatableAction, ISelectionChangedListener,
	ICommandManagerChangeListener, IPropertyListener {

	/**
	 * Flag to indicate whether or not this action has been set up.
	 */
	private boolean setup;

	/**
	 * My disposed state.
	 */
	private boolean disposed;

	/**
	 * The workbench part to which this action handler applies.
	 */
	private IWorkbenchPart workbenchPart;

	/**
	 * The workbench page this action is associated to
	 */
	private IWorkbenchPage workbenchPage;

	/**
	 * The part listener of this action
	 */
	private IPartListener partListener;

	/**
	 * Constructs a new action handler for the specified workbench part.
	 * 
	 * @param workbenchPart
	 *            The workbench part to which this action handler applies.
	 */
	protected AbstractActionHandler(IWorkbenchPart workbenchPart) {
		super();

		assert null != workbenchPart : "null workbenchPart"; //$NON-NLS-1$

		setWorkbenchPart(workbenchPart);

		this.workbenchPage = workbenchPart.getSite().getPage();

		// This is needed for backward compatibility in case the creator
		// of the action (using this constructor) did not dispose of it
		this.partListener = new PartListenerAdapter() {

			/**
			 * when the part closes, remove the listener to the workbench page
			 * and remove all listeners.
			 */
			public void partClosed(IWorkbenchPart part) {
				if (getWorkbenchPart() == part) {
					dispose();
				}
			}
		};
		workbenchPage.addPartListener(partListener);
	}

	/**
	 * Constructs a new action handler that gets its workbench part by listening
	 * to the given workbench page
	 * 
	 * @param workbenchPage
	 *            The workbench page associated with this action handler
	 */
	protected AbstractActionHandler(final IWorkbenchPage workbenchPage) {
		super();

		assert null != workbenchPage : "null workbenchPage"; //$NON-NLS-1$

		this.workbenchPage = workbenchPage;

		this.partListener = new PartListenerAdapter() {

			/**
			 * Listens to part activation and updates the active workbench
			 */
			public void partActivated(IWorkbenchPart part) {
				setWorkbenchPart(part);
				if (part != null)
					refresh();
			}
		};
		workbenchPage.addPartListener(partListener);
	}

	/**
	 * The basic implementation sets the workbenchpart if not already set and
	 * refreshes the action if the current part is not null.
	 * <P>
	 * Any subclass that overrided this method should ensure that the disposed
	 * state of this action is maintained by calling
	 * <code>setDisposed(false)</code> or calling <code>super.init()</code>.
	 */
	public void init() {

		setDisposed(false);

		if (getWorkbenchPart() == null)
			setWorkbenchPart(getWorkbenchPage().getActivePart());
		if (getWorkbenchPart() != null)
			refresh();
	}

	/**
	 * Default implementation of dispose. Any subclass that overrided this
	 * method should ensure that the disposed state of this action is maintained
	 * by calling <code>setDisposed(true)</code> or calling
	 * <code>super.dispose()</code>.
	 */
	public void dispose() {
		setWorkbenchPart(null);

		if (partListener != null && workbenchPage != null) {
			workbenchPage.removePartListener(partListener);
			workbenchPage = null;
			partListener = null;
		}
		setDisposed(true);
	}

	/**
	 * Sets the current workbencgPart
	 * 
	 * @param workbenchPart
	 *            The current workbenchPart
	 */
	protected void setWorkbenchPart(IWorkbenchPart workbenchPart) {
		if (getWorkbenchPart() == workbenchPart)
			return;

		if (getWorkbenchPart() != null) {
			if (isSelectionListener()) {
				ISelectionProvider provider = getWorkbenchPart().getSite()
					.getSelectionProvider();
				if (provider != null) {
					provider.removeSelectionChangedListener(this);
				}
			}
			if (isPropertyListener()) {
				getWorkbenchPart().removePropertyListener(this);
			}
			if (isCommandStackListener()) {
				getCommandManager().removeCommandManagerChangeListener(this);
			}
		}

		this.workbenchPart = workbenchPart;

		if (workbenchPart != null) {
			if (isSelectionListener()) {
				ISelectionProvider provider = getWorkbenchPart().getSite()
					.getSelectionProvider();
				if (provider != null) {
					provider.addSelectionChangedListener(this);
				}
			}
			if (isPropertyListener()) {
				getWorkbenchPart().addPropertyListener(this);
			}
			if (isCommandStackListener()) {
				getCommandManager().addCommandManagerChangeListener(this);
			}
		}
	}

	/**
	 * Retrieves the value of the <code>workbenchPart</code> instance
	 * variable.
	 * 
	 * @return The value of the <code>workbenchPart</code> instance variable.
	 */
	protected final IWorkbenchPart getWorkbenchPart() {
		return workbenchPart;
	}

	/**
	 * Retrieves the action manager for this action handler from its workbench
	 * part.
	 * 
	 * @return The action manager for this action handler.
	 */
	protected ActionManager getActionManager() {
		ActionManager manager = null;
		IWorkbenchPart wbPart = getWorkbenchPart();
		if (wbPart != null) {
			manager = (ActionManager) wbPart.getAdapter(
				ActionManager.class);			
		}
		
		return null == manager ? ActionManager.getDefault()
			: manager;
	}

	/**
	 * Retrieves the command manager for this action handler from its action
	 * manager.
	 * 
	 * @return The command manager for this action handler.
	 */
	protected CommandManager getCommandManager() {
		return getActionManager().getCommandManager();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		getActionManager().run(this);
	}

	/**
	 * Runs this action handler.
	 */
	public void run(IProgressMonitor progressMonitor) {
		if (isSetup() || !needsSetup()) {
			try {
				StatusLineUtil.outputErrorMessage(getWorkbenchPart(),
					StringStatics.BLANK);
				doRun(progressMonitor);
			} catch (Exception e) {
				handle(e);
			}
			setSetup(false);
		} else {
			throw new IllegalStateException(
				"action must be setup before it is run"); //$NON-NLS-1$
		}
	}

	/**
	 * Runs this action handler, passing the triggering SWT event.
	 * 
	 * @param event
	 *            The SWT event which triggered this action being run.
	 */
	public void runWithEvent(Event event) {
		getActionManager().run(this);
	}

	/**
	 * Notifies this action handler that the selection has changed.
	 * 
	 * @param event
	 *            Event object describing the change.
	 */
	public final void selectionChanged(SelectionChangedEvent event) {
		refresh();
	}

	/**
	 * Property change event handler; does nothing by default. Subclasses should
	 * override if they are interested in handling property change events.
	 */
	public void propertyChanged(Object source, int propId) {
		// Do nothing by default
	}
	
	/**
	 * Retrieves the label for this action handler.
	 * 
	 * @return The label for this action handler.
	 */
	public String getLabel() {
		return getText();
	}

	/**
	 * Retrieves the current selection.
	 * 
	 * @return The current selection.
	 */
	protected ISelection getSelection() {
		ISelection selection = null;
		ISelectionProvider selectionProvider = getWorkbenchPart().getSite()
			.getSelectionProvider();

		if (selectionProvider != null) {
			selection = selectionProvider.getSelection();
		}

		return (selection != null) ? selection
			: StructuredSelection.EMPTY;
	}

	/**
	 * Retrieves the current structured selection.
	 * 
	 * @return The current structured selection.
	 */
	protected IStructuredSelection getStructuredSelection() {
		IStructuredSelection selection = null;
		ISelectionProvider selectionProvider = null;
		if (getWorkbenchPart() != null) {
			selectionProvider = getWorkbenchPart().getSite()
				.getSelectionProvider();
		}

		if (selectionProvider != null
			&& selectionProvider.getSelection() instanceof IStructuredSelection) {
			selection = (IStructuredSelection) selectionProvider.getSelection();
		}
		return (selection != null) ? selection
			: StructuredSelection.EMPTY;
	}

	/**
	 * Retrieves a Boolean indicating whether this action handler can be
	 * repeated.
	 * 
	 * @return <code>true</code> if this action handler is enabled;
	 *         <code>false</code> otherwise.
	 */
	public boolean isRepeatable() {
		return isEnabled();
	}

	/**
	 * Retrieves a Boolean indicating whether this action handler can be run.
	 * 
	 * @return <code>true</code> if this action handler is enabled;
	 *         <code>false</code> otherwise.
	 */
	public boolean isRunnable() {
		return isEnabled();
	}

	/**
	 * Retrieves a Boolean indicating whether this action handler is interested
	 * in selection events.
	 * 
	 * @return <code>true</code> if this action handler is interested;
	 *         <code>false</code> otherwise.
	 */
	protected boolean isSelectionListener() {
		return false;
	}

	/**
	 * Answers whether of not this action handler is interested in property
	 * change events.
	 * <P>
	 * This default implementation always returns <code>false</code>.
	 * Subclasses must override if they are interested in property change
	 * events.
	 * 
	 * @return <code>true</code> if this action handler is interested;
	 *         <code>false</code> otherwise.
	 */
	protected boolean isPropertyListener() {
		return false;
	}
	
	/**
	 * Retrieves a Boolean indicating whether this action handler is interested
	 * in commnd stack changed events.
	 * 
	 * @return <code>true</code> if this action handler is interested;
	 *         <code>false</code> otherwise.
	 */
	protected boolean isCommandStackListener() {
		return false;
	}

	/**
	 * Re-runs this action handler.
	 */
	public void repeat(IProgressMonitor progressMonitor) {
		run(progressMonitor);
	}

	/**
	 * Handles the specified exception.
	 * 
	 * @param exception
	 *            The exception to be handled.
	 */
	protected void handle(Exception exception) {
		Trace.catching(CommonUIPlugin.getDefault(),
			CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
			"handle", exception); //$NON-NLS-1$

		IStatus status = new Status(IStatus.ERROR,
			CommonUIPlugin.getPluginId(), CommonUIStatusCodes.ACTION_FAILURE,
			String.valueOf(exception.getMessage()), exception);

		Log.log(CommonUIPlugin.getDefault(), status);
		openErrorDialog(status);
	}

	/**
	 * Opens an error dialog for the specified status object.
	 * 
	 * @param status
	 *            The status object for which to open an error dialog.
	 *  
	 */
	protected void openErrorDialog(IStatus status) {
		ErrorDialog.openError(getWorkbenchPart().getSite().getShell(),
			removeMnemonics(getLabel()), null, status);
	}

	/**
	 * Performs the actual work when this action handler is run. Subclasses must
	 * override this method to do some work.
	 * 
	 * @param progressMonitor
	 *            the progress monitor for tracking the progress of this action
	 *            when it is run.
	 */
	protected abstract void doRun(IProgressMonitor progressMonitor);


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#getWorkIndicatorType()
	 */
	public WorkIndicatorType getWorkIndicatorType() {
		return WorkIndicatorType.BUSY;
	}

	/**
	 * Returns the part listener
	 * 
	 * @return The part listener
	 */
	protected IPartListener getPartListener() {
		return partListener;
	}

	/**
	 * Returns the workbench page
	 * 
	 * @return The workbench page
	 */
	protected IWorkbenchPage getWorkbenchPage() {
		return workbenchPage;
	}

	/**
	 * Handles an event indicating that a command manager has changed.
	 * 
	 * @param event
	 *            The command manager change event to be handled.
	 */
	public final void commandManagerChanged(CommandManagerChangeEvent event) {
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#isDisposed()
	 */
	public boolean isDisposed() {
		return disposed;
	}

	/**
	 * Sets my disposed state.
	 * 
	 * @param b
	 *            <code>true</code> if I am disposed, <code>false</code>
	 *            otherwise.
	 */
	protected void setDisposed(boolean b) {
		disposed = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#setup()
	 */
	public boolean setup() {
		setSetup(true);
		return true;
	}

	/**
	 * Returns the setup state of this action.
	 * 
	 * @return <code>true</code> if the action has been setup,
	 *         <code>false</code> otherwise.
	 */
	public boolean isSetup() {
		return setup;
	}

	/**
	 * Sets the setup state of this action.
	 * 
	 * @param setup
	 *            <code>true</code> if the action has been setup,
	 *            <code>false</code> otherwise.
	 */
	protected void setSetup(boolean setup) {
		this.setup = setup;
	}

	/**
	 * Answers whether or not this action should be setup before it is run.
	 * Subclasses should override if they provide vital behaviour in the setup
	 * method.
	 * 
	 * @return <code>true</code> if the action has a setup, <code>false</code>
	 *         otherwise.
	 */
	protected boolean needsSetup() {
		return false;
	}
	
}