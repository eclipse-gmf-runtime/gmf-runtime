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


package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This interface consolidates all of the getter methods for supported MSL EMF
 * commands. Where a method is defined on this interface that is not applicable
 * for a given implementation class, it will return null.
 * 
 * @author nbennett
 */
public interface MCommand
	extends Command {

	/**
	 * Type-safe enumeration class of all EMF Command Types currently supported
	 * by the MSL command generation infrastructure.
	 */

	public static class Type {

		private static int nextOridnal = 0;

		private int ordinal = nextOridnal;

		private Type() {
			super();
			nextOridnal++;
		}

		public boolean equals(Type other) {
			return this.ordinal == other.ordinal;
		}

		public int hashCode() {
			return ordinal;
		}
	}

	/**
	 * Type-safe enumerated value representing the <code>MSLAddCommand</code>
	 */
	public static final Type ADD = new Type();

	/**
	 * Type-safe enumerated value representing the
	 * <code>TraversableCompoundCommand</code>
	 */
	public static final Type COMPOUND = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLMoveCommand</code>
	 */
	public static final Type MOVE = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLRemoveCommand</code>
	 */
	public static final Type REMOVE = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLSetCommand</code>
	 */
	public static final Type SET = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLEventCommand</code>
	 */
	public static final Type EVENT = new Type();

	/**
	 * Type-safe enumerated value representing the
	 * <code>MSLUnexecutableCommand</code>
	 */
	public static final Type UNEXECUTABLE = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLPSMCommand</code>
	 */
	public static final Type PSM = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLProxyCommand</code>
	 */
	public static final Type PROXY = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLResourceAddCommand</code>
	 */
	public static final Type RESOURCE_ADD = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLResourceRemoveCommand</code>
	 */
	public static final Type RESOURCE_REMOVE = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLResourceMoveCommand</code>
	 */
	public static final Type RESOURCE_MOVE = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLSeparateCommand</code>
	 */
	public static final Type SEPARATE = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLAbsorbCommand</code>
	 */
	public static final Type ABSORB = new Type();

	/**
	 * Type-safe enumerated value representing the <code>MSLAbandonActionCommand</code>
	 */
	public static final Type ABANDON_ACTION = new Type();

	/**
	 * Returns the type of this MCommand as defined by the MCommand.Type static
	 * inner class.
	 */
	public Type getType();

	/**
	 * This returns the owner object upon which the command will act. It could
	 * be null in the case that we are dealing with an
	 * {@link org.eclipse.emf.common.util.EList}.
	 */
	public EObject getOwner();

	/**
	 * This returns the feature of the owner object upon the command will act.
	 * It could be null, in the case that we are dealing with an
	 * {@link org.eclipse.emf.common.util.EList}.
	 */
	public EStructuralFeature getFeature();

	/**
	 * This returns the value being operated on.
	 */
	public Object getValue();

	/**
	 * This returns the old value of the feature which must be restored during
	 * undo.
	 */
	public Object getOldValue();

	/**
	 * This returns the collection of objects being operated on.
	 */
	public Collection getCollection();

	/**
	 * This returns the position at which the objects will be operated on.
	 */
	public int getIndex();

	/**
	 * This returns the orginal position to which the object will be moved upon
	 * undo.
	 */
	public int getOldIndex();

	/**
	 * These returns the indices at which to reinsert removed objects during an
	 * undo so as to achieve the original list order.
	 */
	public int[] getIndices();

	/**
	 * This returns true if the command is compound.
	 */
	public boolean isCompound();

	/**
	 * This returns the commands list of a compound command.
	 */
	public List getCommands();

	/**
	 * Returns a list of the MObjectTypes of the participating elements of this
	 * command
	 */
	public Set getParticipantTypes();

	/**
	 * Returns one of three values:
	 * {@link org.eclipse.gmf.runtime.emf.core.internal.commands.MSLUnexecutableCommand#INSTANCE},
	 * if there are no commands, the one command, if there is exactly one
	 * command, or <code>this</code>, if there are multiple commands; this
	 * command is {@link org.eclipse.emf.common.command.Command#dispose}d in
	 * the first two cases. You should only unwrap a compound command if you
	 * created it for that purpose, e.g.,
	 * 
	 * <pre>
	 * MSLCompoundCommand subcommands = new MSLCompoundCommand();
	 * subcommands.append(x);
	 * if (condition)
	 * 	subcommands.append(y);
	 * MCommand result = subcommands.unwrap();
	 * </pre>
	 * 
	 * is a good way to create an efficient accumulated result.
	 * 
	 * @return the unwapped command.
	 */
	public MCommand unwrap();

	/**
	 * Returns a command that represents the composition of this command with
	 * the given command. The resulting command may just be this, if this
	 * command is capabable of composition. Otherwise, it will be a new command
	 * created to compose the two.
	 * <p>
	 * Instead of the following pattern of usage
	 * 
	 * <pre>
	 * MCommand result = x;
	 * if (condition)
	 * 	result = result.chain(y);
	 * </pre>
	 * 
	 * you should consider using a
	 * {@link org.eclipse.gmf.runtime.emf.core.internal.commands.MSLCompoundCommand}
	 * and using
	 * {@link org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#unwrap()}
	 * to optimize the result:
	 * 
	 * <pre>
	 * MSLCompoundCommand subcommands = new MSLCompoundCommand();
	 * subcommands.append(x);
	 * if (condition)
	 * 	subcommands.append(y);
	 * MCommand result = subcommands.unwrap();
	 * </pre>
	 * 
	 * This gives you more control over how the compound command composes it's
	 * result and affected objects.
	 * 
	 * @param command
	 *            the command to chain.
	 * @return a command that represents the composition of this command with
	 *         the given command.
	 */
	MCommand chain(MCommand command);
}