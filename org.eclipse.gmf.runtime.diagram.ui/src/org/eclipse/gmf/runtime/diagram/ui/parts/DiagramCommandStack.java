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

package org.eclipse.gmf.runtime.diagram.ui.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.ConnectionHandleTool;
import org.eclipse.swt.widgets.Display;

/**
 * GEF command stack that delegates to an {@link IOperationHistory}.
 * 
 * @author sshaw
 * @author Tauseef A, Israr
 * @author ldamus
 */
public class DiagramCommandStack
    extends CommandStack {

    private Map stackToManager = new HashMap();

    private IDiagramEditDomain editDomain;

    private Command cmdRecent = null;

    private IOperationHistory delegate;

    private IUndoContext undoContext;

    private final class HistoryEventObject
        extends EventObject {

        private final OperationHistoryEvent event;

        private HistoryEventObject(OperationHistoryEvent event) {
            super(event.getHistory());
            this.event = event;
        }

        /**
         * Gets my operation history event.
         * 
         * @return my operation history event.
         */
        public OperationHistoryEvent getOperationHistoryEvent() {
            return event;
        }
    }

    /**
     * Initializes me with my diagram edit domain and undo context.
     * 
     * @param editDomain
     *            the editing domain assoicated with this stack
     * @param undoContext
     *            my undo context
     */
    public DiagramCommandStack(IDiagramEditDomain editDomain) {
        this.editDomain = editDomain;
    }

    /**
     * Adds a listener to this CommandStack.
     * 
     * @param listener
     *            The Object listening to this CommandStack.
     */
    public void addCommandStackListener(CommandStackListener listener) {

        final CommandStackListener csl = listener;
        // The removal of the listener here is done to avoid multiple
        // commandchangelisteners added to the commandmanager.
        // Tauseef Israr
        removeCommandStackListener(csl);

        IOperationHistoryListener cmcl = new IOperationHistoryListener() {

            public void historyNotification(OperationHistoryEvent event) {
                if (csl != null) {
                    csl.commandStackChanged(new HistoryEventObject(event));
                }
            }
        };

        stackToManager.put(csl, cmcl);
        getOperationHistory().addOperationHistoryListener(cmcl);
    }

    /**
     * Returns <code>true</code> if there is a Command to redo.
     * 
     * @return <code>true</code> if there is a Command to redo.
     */
    public boolean canRedo() {
        return getOperationHistory().canRedo(getUndoContext());
    }

    /**
     * Returns <code>true</code> if the last Command executed can be undone.
     * 
     * @return <code>true</code> if the last Command executed can be undone.
     */
    public boolean canUndo() {
        return getOperationHistory().canUndo(getUndoContext());
    }

    /**
     * Executes the given Command if it can execute.
     * 
     * @param command
     *            The Command to execute.
     */
    public void execute(Command command) {
        execute(command, null);
    }

    /**
     * Executes the given Command if it can execute.
     * 
     * @param command
     *            The Command to execute.
     * @param progressMonitor
     */
    public void execute(Command command, IProgressMonitor progressMonitor) {
        if (command == null || !command.canExecute())
            return;
        execute(getICommand(command), progressMonitor);
    }

    /**
     * exectus a the supplied command
     * 
     * @param command
     *            the command to execute
     */
    protected void execute(ICommand command) {
        execute(command, null);
    }

    /**
     * executes the supplied command
     * 
     * @param command
     *            the command to exectue
     * @param progressMonitor
     */
    protected void execute(ICommand command, IProgressMonitor progressMonitor) {

        if (progressMonitor != null) {
            DiagramGraphicalViewer viewer = getDiagramGraphicalViewer();

            if (viewer != null && Display.getCurrent() == null)
                viewer.enableUpdates(false);

            try {
                command.addContext(getUndoContext());
                getOperationHistory().execute(command, progressMonitor, null);

            } catch (ExecutionException e) {
                Trace.catching(DiagramUIPlugin.getInstance(),
                    DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
                    getClass(), "execute", e); //$NON-NLS-1$
                Log.error(DiagramUIPlugin.getInstance(),
                    DiagramUIStatusCodes.COMMAND_FAILURE, "execute", e); //$NON-NLS-1$
            } finally {
                if (viewer != null)
                    viewer.enableUpdates(true);
            }
        } else {
            try {
                command.addContext(getUndoContext());
                getOperationHistory().execute(command,
                    new NullProgressMonitor(), null);

            } catch (ExecutionException e) {
                Trace.catching(DiagramUIPlugin.getInstance(),
                    DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
                    getClass(), "execute", e); //$NON-NLS-1$
                Log.error(DiagramUIPlugin.getInstance(),
                    DiagramUIStatusCodes.COMMAND_FAILURE, "execute", e); //$NON-NLS-1$
            }
        }

    }
    
    private DiagramGraphicalViewer getDiagramGraphicalViewer() {
        IDiagramEditDomain ded = getDiagramEditDomain();
        DiagramGraphicalViewer viewer = null;

        if (ded instanceof DiagramEditDomain) {
            IDiagramWorkbenchPart dgrmWP = ((DiagramEditDomain) ded)
                .getDiagramEditorPart();
            if (dgrmWP != null) {
                IDiagramGraphicalViewer dgv = ((DiagramEditDomain) ded)
                    .getDiagramEditorPart().getDiagramGraphicalViewer();
                if (dgv instanceof DiagramGraphicalViewer)
                    viewer = (DiagramGraphicalViewer) dgv;
            }
        }
        return viewer;
    }

    /**
     * Converts a GEF {@link Command} into a GMF {@link ICommand}
     * 
     * @param command
     *            the GEF command
     * @return the GMF command
     */
    public static ICommand getICommand(Command command) {

        if (command instanceof CompoundCommand) {

            CompositeCommand composite = new CompositeCommand(
                command.getLabel());
            Object[] subCommands = ((CompoundCommand) command).getChildren();

            for (int i = 0; i < subCommands.length; i++) {
                composite.compose(getICommand((Command) subCommands[i]));
            }
            return composite.reduce();
        }

        if (command instanceof EtoolsProxyCommand) {
            return getICommand(((EtoolsProxyCommand) command).getICommand());
        }

        return new CommandProxy(command);
    }

    /**
     * Removes redundancies from <code>command</code> by stripping out layers
     * of command wrappers used to accomodate the use of GEF commands on an
     * {@link IOperationHistory} and {@link ICommand}s on the GEF
     * {@link CommandStack}.
     * 
     * @param command
     *            the command to be processed
     * @return a command representing the simplified form of the input command.
     *         May be a new command.
     */
    public static ICommand getICommand(ICommand command) {

        ICommand result = command;

        if (command instanceof ICompositeCommand) {
            // process composite command
            List processedCommands = new ArrayList();

            ICompositeCommand composite = (ICompositeCommand) command;

            if (!composite.isEmpty()) {

                for (Iterator i = composite.iterator(); i.hasNext();) {
                    IUndoableOperation nextOperation = (IUndoableOperation) i
                        .next();

                    // remove the next child from the composite
                    i.remove();

                    // convert any GEF commands to GMF commands
                    if (nextOperation instanceof ICommand) {
                        ICommand nextCommand = (ICommand) nextOperation;
                        processedCommands.add(getICommand(nextCommand));

                    } else {
                        processedCommands.add(nextOperation);
                    }
                }

                // add all the children back
                for (Iterator i = processedCommands.iterator(); i.hasNext();) {
                    composite.add((IUndoableOperation) i.next());
                }

                // reduce to the simplest equivalent form
                result = composite.reduce();
            }

        } else if (command instanceof CommandProxy) {
        	// process GEF command proxy
            return getICommand(((CommandProxy) command).getCommand());
        }

        return result;
    }

    /**
     * Flushes my undo context from my delegate operation history.
     */
    public void flush() {
        getOperationHistory().dispose(getUndoContext(), true, true, false);
        super.flush();
    }
    
    /**
     * Flushes my operation history delegate.
     */
    public void dispose() {
        super.dispose();
        flush();
    }

    /**
     * Returns the most recently executed command.
     * 
     * @return The most recently executed command.
     */
    public Command getMostRecentCommand() {
        return cmdRecent;
    }

    /**
     * getRedoCommand Returns the command at the top of the redo stack.
     * 
     * @see org.eclipse.gef.commands.CommandStack#getRedoCommand()
     */
    public Command getRedoCommand() {
        if (getOperationHistory().canRedo(getUndoContext())) {
            Command emptyCmd = new Command() {
                // empty
            };

            IUndoableOperation redo = getOperationHistory().getRedoOperation(
                getUndoContext());
            emptyCmd.setLabel(redo.getLabel());
            return emptyCmd;
        }

        return null;
    }

    /**
     * getUndoCommand() Returns the next command to be undone.
     * 
     * @see org.eclipse.gef.commands.CommandStack#getUndoCommand()
     */
    public Command getUndoCommand() {
        if (getOperationHistory().canUndo(getUndoContext())) {
            Command emptyCmd = new Command() {
                // empty
            };

            IUndoableOperation undo = getOperationHistory().getUndoOperation(
                getUndoContext());
            emptyCmd.setLabel(undo.getLabel());
            return emptyCmd;
        }

        return null;
    }

    /**
     * Executes the last undone Command.
     */
    public void redo() {
        cmdRecent = getRedoCommand();

        try {
            getOperationHistory().redo(getUndoContext(),
                new NullProgressMonitor(), null);

        } catch (ExecutionException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
                ConnectionHandleTool.class, "redo", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.COMMAND_FAILURE, "redo", e); //$NON-NLS-1$
        }
    }

    /**
     * Removes the given CommandStackListener.
     * 
     * @param listener
     *            The object to be removed from the list of listeners.
     */
    public void removeCommandStackListener(CommandStackListener listener) {
        final CommandStackListener csl = listener;

        if (csl != null) {
            IOperationHistoryListener historyListener = (IOperationHistoryListener) stackToManager
                .get(csl);

            if (historyListener != null) {
                getOperationHistory().removeOperationHistoryListener(
                    historyListener);
            }
            // mgoyal: removing from stack manager
            stackToManager.remove(csl);
        }
    }

    /**
     * Undoes the last executed Command.
     */
    public void undo() {
        cmdRecent = getUndoCommand();

        try {
            getOperationHistory().undo(getUndoContext(),
                new NullProgressMonitor(), null);

        } catch (ExecutionException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
                ConnectionHandleTool.class, "undo", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.COMMAND_FAILURE, "undo", e); //$NON-NLS-1$
        }
    }

    /**
     * Returns the editDomain.
     * 
     * @return IDiagramEditDomain
     */
    protected IDiagramEditDomain getDiagramEditDomain() {
        return editDomain;
    }

    /**
     * Gets my operation history delegate.
     * 
     * @return my operation history delegate
     */
    protected IOperationHistory getOperationHistory() {

        if (delegate == null) {
            delegate = OperationHistoryFactory.getOperationHistory();
        }
        return delegate;
    }

    /**
     * Sets my operation history delegate.
     * 
     * @param operationHistory
     *            my operation history delegate
     */
    public void setOperationHistory(IOperationHistory operationHistory) {
        this.delegate = operationHistory;
    }

    /**
     * Gets the return values of the given executed command
     * 
     * @param c
     *            The command
     * @return A collection of values returned by the given command
     */
    public static Collection getReturnValues(Command c) {
        if (c instanceof CompoundCommand) {
            CompoundCommand cc = (CompoundCommand) c;
            List l = new ArrayList(cc.size());
            for (Iterator i = cc.getCommands().iterator(); i.hasNext();)
                l.addAll(getReturnValues((Command) i.next()));
            return l;

        } else if (c instanceof EtoolsProxyCommand) {
            return getReturnValues((EtoolsProxyCommand) c);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * gets the return the values for the supplied command.
     * 
     * @param cmd
     *            command to use
     * @return a collection of return values
     */
    public static Collection getReturnValues(EtoolsProxyCommand cmd) {
        return getReturnValues(cmd.getICommand());
    }


    
    
    /**
     * gets the return the values for the supplied command.
     * @param cmd command to use
     * @return a collection of return values
     */
    public static Collection getReturnValues( CommandProxy cmd ) {
        return getReturnValues( cmd.getCommand() );
    }

    /**
     * gets the return the values for the supplied command.
     * 
     * @param cmd
     *            command to use
     * @return a collection of return values
     */
    public static Collection getReturnValues(ICommand cmd) {
        
        if (cmd instanceof ICompositeCommand) {
            ICompositeCommand cc = (ICompositeCommand) cmd;
            List l = new ArrayList();
            for (Iterator i = cc.iterator(); i.hasNext();)
                l.addAll(getReturnValues((ICommand) i.next()));
            return l;
            
        } else if ( cmd instanceof CommandProxy ) { //
            // Need to recurse into the proxy command(s) since they
            // will not have set the CommandProxy result
            // This Could be moved into CommandProxy but
            // #getCommandResult() can no longer be final.
            return getReturnValues((CommandProxy)cmd);
        
        } else {
            CommandResult r = cmd.getCommandResult();
            Object o = r != null ? r.getReturnValue()
                : null;
            
            if (o instanceof Collection) {
                return (Collection) o;
                
            } else if (o != null) {
                return Collections.singletonList(o);
            }
        }
        
        return Collections.EMPTY_LIST;
    }

    /**
     * Gets my undo context. I add my context to all commands executed through
     * me.
     * 
     * @return my undo context
     */
    public IUndoContext getUndoContext() {

        if (undoContext == null) {
            undoContext = new ObjectUndoContext(this);
        }
        return undoContext;
    }
    
    /**
     * Sets my undo context.
     * 
     * @param undoContext
     *            my undo context
     */
    public void setUndoContext(IUndoContext undoContext) {
        this.undoContext = undoContext;
    }

}