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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;

/**
 * An abstract implementation of MCommand.
 * 
 * @author rafikj
 */
public abstract class MSLAbstractCommand
	implements MCommand {

	protected MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	protected MSLAbstractCommand(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Returns the editing domain this command is associated with.
	 */
	public MSLEditingDomain getDomain() {
		return domain;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {
		domain = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#canExecute()
	 */
	public boolean canExecute() {
		return true;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#canUndo()
	 */
	public boolean canUndo() {
		return true;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getResult()
	 */
	public Collection getResult() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getLabel()
	 */
	public String getLabel() {
		return MSLConstants.EMPTY_STRING;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#getDescription()
	 */
	public String getDescription() {
		return MSLConstants.EMPTY_STRING;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#chain(org.eclipse.emf.common.command.Command)
	 */
	public final Command chain(Command command) {

		if (command instanceof MCommand)
			return chain((MCommand) command);

		return MSLUnexecutableCommand.INSTANCE;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOwner()
	 */
	public EObject getOwner() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getFeature()
	 */
	public EStructuralFeature getFeature() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getValue()
	 */
	public Object getValue() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldValue()
	 */
	public Object getOldValue() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCollection()
	 */
	public Collection getCollection() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getIndex()
	 */
	public int getIndex() {
		return -1;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOldIndex()
	 */
	public int getOldIndex() {
		return -1;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getIndices()
	 */
	public int[] getIndices() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#isCompound()
	 */
	public boolean isCompound() {
		return false;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getCommands()
	 */
	public List getCommands() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getParticipantTypes()
	 */
	public Set getParticipantTypes() {
		return Collections.EMPTY_SET;
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

		return chainedCommand;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#unwrap()
	 */
	public MCommand unwrap() {
		return this;
	}
}