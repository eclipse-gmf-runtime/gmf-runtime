/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.command.CommandManagerChangeEvent;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.XtoolsProxyCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;

/**
 * @author sshaw
 * 
 * <p>
 * Editted by Tauseef A, Israr Feb 7, 2003 Included a protected get method for
 * the edit domain.
 * </p>
 */
public class DiagramCommandStack
	extends CommandStack {

	private Map stackToManager = new HashMap();

	private IDiagramEditDomain editDomain;

	private Command cmdRecent = null;

	private CommandManager commandManager;

	/**
	 * constructor
	 * @param editDomain the editing domain assoicated with this stack
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
		//The removal of the listener here is done to avoid multiple 
		//commandchangelisteners added to the commandmanager.
		//Tauseef Israr
		removeCommandStackListener(csl);	
		ICommandManagerChangeListener cmcl = new ICommandManagerChangeListener() {
			public void commandManagerChanged(CommandManagerChangeEvent event) {
				if (csl != null)
					csl.commandStackChanged(event);
			}
		};

		stackToManager.put(csl, cmcl);
		getCommandManager().addCommandManagerChangeListener(cmcl);
	}

	/**
	 * Returns <code>true</code> if there is a Command to redo.
	 * 
	 * @return <code>true</code> if there is a Command to redo.
	 */
	public boolean canRedo() {
		return getCommandManager().canRedo();
	}

	/**
	 * Returns <code>true</code> if the last Command executed can be undone.
	 * 
	 * @return <code>true</code> if the last Command executed can be undone.
	 */
	public boolean canUndo() {
		return getCommandManager().canUndo();
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
	 * @param command The Command to execute.
	 * @param progressMonitor
	 */
	public void execute(Command command, IProgressMonitor progressMonitor) {
		if (command == null || !command.canExecute())
			return;
		execute(getICommand(command), progressMonitor);
	}


	/**
	 * exectus a the supplied command
	 * @param command the command to execute
	 */
	protected void execute(ICommand command) {
		execute(command, null);
	}

	/**
	 * executes the supplied command
	 * @param command the command to exectue
	 * @param progressMonitor
	 */
	protected void execute(ICommand command, IProgressMonitor progressMonitor) {
		if (progressMonitor != null)
			getCommandManager().execute(command, progressMonitor);
		else
			getCommandManager().execute(command);
	}

	/**
	 * A method to convert a GEF Command into Xtools ICommand
	 * 
	 * @param command
	 * @return <code>ICommand</code>
	 */
	public static ICommand getICommand(Command command) {
		if (command instanceof CompoundCommand) {
			CompositeCommand cc = new CompositeCommand(command.getLabel());
			Object[] subCommands = ((CompoundCommand) command).getChildren();
			for (int i = 0; i < subCommands.length; i++) {
				cc.compose(getICommand((Command) subCommands[i]));
			}
			return cc.unwrap();
		}
		if (command instanceof EtoolsProxyCommand) {
			return getICommand(((EtoolsProxyCommand) command).getICommand());
		}
		return new XtoolsProxyCommand(command);
	}

	/**
	 * A method to remove redundancies from an Xtools ICommand
	 * @param command 
	 * @return <code>ICommand</code>
	 */
	public static ICommand getICommand(ICommand command) {

//		if (command instanceof CompositeModelActionCommand) {
//			CompositeModelActionCommand cc = new CompositeModelActionCommand(
//				command.getLabel());
//			List subCommands = ((CompositeCommand) command).getCommands();
//			for (int i = 0; i < subCommands.size(); i++) {
//				cc.compose(getICommand((ICommand) subCommands.get(i)));
//			}
//			return cc;
//		}

		if (command instanceof CompositeModelCommand) {
			CompositeModelCommand cc = new CompositeModelCommand(command
				.getLabel());
			List subCommands = ((CompositeCommand) command).getCommands();
			for (int i = 0; i < subCommands.size(); i++) {
				cc.compose(getICommand((ICommand) subCommands.get(i)));
			}
			return cc.unwrap();
		}

		if (command instanceof CompositeCommand) {
			CompositeCommand cc = new CompositeCommand(command.getLabel());
			List subCommands = ((CompositeCommand) command).getCommands();
			for (int i = 0; i < subCommands.size(); i++) {
				cc.compose(getICommand((ICommand) subCommands.get(i)));
			}
			return cc.unwrap();
		}
		if (command instanceof XtoolsProxyCommand) {
			return getICommand(((XtoolsProxyCommand) command).getCommand());
		}
		return command;
	}

	/**
	 * Clears both the undo and redo stacks, then sends a notification to any
	 * object listening to the CommandStack.
	 */
	public void flush() {
		getCommandManager().clear();
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
		if (getCommandManager().canRedo()) {
			Command emptyCmd = new Command() {
				// empty
			};

			emptyCmd.setLabel(getCommandManager().getRedoLabel());
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
		if (getCommandManager().canUndo()) {
			Command emptyCmd = new Command() {
				//empty
			};

			emptyCmd.setLabel(getCommandManager().getUndoLabel());
			return emptyCmd;
		}

		return null;
	}

	/**
	 * Executes the last undone Command.
	 */
	public void redo() {
		cmdRecent = getRedoCommand();
		getCommandManager().redo();
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
			ICommandManagerChangeListener cmcl = (ICommandManagerChangeListener) stackToManager
				.get(csl);
			if (cmcl != null)
				getCommandManager().removeCommandManagerChangeListener(cmcl);
			// mgoyal: removing from stack manager
			stackToManager.remove(csl);
		}
	}

	/**
	 * Undoes the last executed Command.
	 */
	public void undo() {
		cmdRecent = getUndoCommand();
		getCommandManager().undo();
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
	 * Returns the commandManager.
	 * 
	 * @return CommandManager
	 */
	protected CommandManager getCommandManager() {
		if (commandManager == null)
			return CommandManager.getDefault();
		return commandManager;
	}

	/**
	 * Sets the commandManager.
	 * 
	 * @param commandManager
	 *            The commandManager to set
	 */
	protected void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	/**
	 * Gets the return values of the given executed command
	 * 
	 * @param c The command
	 * @return A collection of values returned by the given command
	 */
	public static Collection getReturnValues(Command c) {
		if (c instanceof CompoundCommand) {
			CompoundCommand cc = (CompoundCommand)c;
			List l = new ArrayList(cc.size());
			for (Iterator i = cc.getCommands().iterator(); i.hasNext();)
				l.addAll(getReturnValues((Command) i.next()));
			return l;
			
		} 
		else if ( c instanceof EtoolsProxyCommand ) {
			return getReturnValues((EtoolsProxyCommand)c);
		}
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * gets the return the values for the supplied command.
	 * @param cmd command to use
	 * @return a collection of return values
	 */
	public static Collection getReturnValues( EtoolsProxyCommand cmd ) {
		return getReturnValues( cmd.getICommand() );
	}

	/**
	 * gets the return the values for the supplied command.
	 * @param cmd command to use
	 * @return a collection of return values
	 */
	public static Collection getReturnValues( XtoolsProxyCommand cmd ) {
		return getReturnValues( cmd.getCommand() );
	}

	/**
	 * gets the return the values for the supplied command.
	 * @param cmd command to use
	 * @return a collection of return values
	 */	
	public static Collection getReturnValues( ICommand cmd ) {
		if ( cmd instanceof CompositeCommand ) {
			CompositeCommand cc = (CompositeCommand)cmd;
			List l = new ArrayList();
			for (Iterator i = cc.getCommands().iterator(); i.hasNext();)
				l.addAll(getReturnValues((ICommand) i.next()));
			return l;
		}
		else if ( cmd instanceof XtoolsProxyCommand ) {	//
			// Need to recurse into the proxy command(s) since they
			// will not have set the XtoolsProxyCommand result
			// This Could be moved into XtoolsProxyCommand but
			// #getCommandResult() can no longer be final.
			return getReturnValues((XtoolsProxyCommand)cmd);
		}
		else {
			CommandResult r = cmd.getCommandResult(); 
			Object o = r != null ? r.getReturnValue() : null;
			if (o instanceof Collection) {
				return (Collection) o;
			}
			else if (o != null) {
				return Collections.singletonList(o);
			}
		}
		return Collections.EMPTY_LIST;
		
	}

}