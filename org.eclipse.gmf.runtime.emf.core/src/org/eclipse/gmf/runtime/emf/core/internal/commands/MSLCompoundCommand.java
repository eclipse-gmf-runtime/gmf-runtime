/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.emf.common.command.Command;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * A compound comamnd maintains a collection of commands.
 * 
 * @author rafikj
 */
public class MSLCompoundCommand
	extends MSLAbstractCommand {

	private List commands = null;

	private MSLAbandonActionCommand abandonCommand = null;
	
	private Set participantTypes = null;

	/**
	 * Constructor.
	 */
	protected MSLCompoundCommand(MSLEditingDomain domain) {
		super(domain);
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {

		super.dispose();

		for (Iterator cmds = getCommands().listIterator(); cmds.hasNext();) {

			((Command) cmds.next()).dispose();
			cmds.remove();
		}

		commands = null;
		participantTypes = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	public void execute() {

		for (ListIterator i = getCommands().listIterator(); i.hasNext();) {

			Command command = (Command) i.next();
			command.execute();
		}
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {

		for (ListIterator i = getCommands().listIterator(getCommands().size()); i
			.hasPrevious();) {

			Command command = (Command) i.previous();
			command.undo();
		}
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {

		for (ListIterator i = getCommands().listIterator(); i.hasNext();) {

			Command command = (Command) i.next();
			command.redo();
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#chain(org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand)
	 */
	public MCommand chain(MCommand command) {

		MSLCompoundCommand chainedCommand = new MSLCompoundCommand(domain) {

			public MCommand chain(MCommand c) {
				append(c);
				return this;
			}
		};

		chainedCommand.append(this);
		chainedCommand.append(command);

		return chainedCommand;
	}

	/**
	 * Appends a command to the collection.
	 */
	public void append(MCommand command) {

		getCommands().add(command);

		Set types = command.getParticipantTypes();

		getParticipantTypes().addAll(types);
		
		if (command instanceof MSLAbandonActionCommand) {
			abandonCommand = (MSLAbandonActionCommand) command;
		}
	}
	
	/**
	 * Queries whether I include a command to abandon the action.
	 * 
	 * @return <code>true</code> if I include an abandon command;
	 *     <code>false</code>, otherwise
	 */
	public boolean isAbandon() {
		return abandonCommand != null;
	}
	
	/**
	 * If I am an {@link #isAbandon() abandon} compound, returns my contained
	 * abandon command.
	 * 
	 * @return the abandon command, or <code>null</code> if none
	 * 
	 * @see #isAbandon()
	 */
	public MSLAbandonActionCommand getAbandonCommand() {
		return abandonCommand;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#unwrap()
	 */
	public MCommand unwrap() {

		switch (getCommands().size()) {

			case 0:
				dispose();
				return MSLUnexecutableCommand.INSTANCE;

			case 1:
				MCommand result = (MCommand) getCommands().remove(0);
				dispose();
				return result;

			default:
				return this;
		}
	}

	/**
	 * Is the collection empty?
	 */
	public boolean isEmpty() {
		return (commands == null) || (getCommands().isEmpty());
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#isCompound()
	 */
	public boolean isCompound() {
		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCommands()
	 */
	public List getCommands() {

		commands = commands == null ? new ArrayList()
			: commands;

		return commands;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getParticipantTypes()
	 */
	public Set getParticipantTypes() {

		participantTypes = participantTypes == null ? new HashSet()
			: participantTypes;

		return participantTypes;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public final Type getType() {
		return MCommand.COMPOUND;
	}
}