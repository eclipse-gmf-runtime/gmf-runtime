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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * An event command can send an event when done, undone or redone..
 * 
 * @author rafikj
 */
public class MSLEventCommand
	extends MSLAbstractCommand {

	private Notification doEvent = null;

	private Notification undoEvent = null;

	private Set participantTypes = null;

	/**
	 * Creates an event command that sends the same event when done, undone or
	 * redone.
	 */
	public static MCommand create(MSLEditingDomain domain, Notification doEvent) {
		return new MSLEventCommand(domain, doEvent);
	}

	/**
	 * Creates an event command that sends the different events when done,
	 * undone or redone.
	 */
	public static MCommand create(MSLEditingDomain domain,
			Notification doEvent, Notification undoEvent) {

		return new MSLEventCommand(domain, doEvent, undoEvent);
	}

	/**
	 * Constructor.
	 */
	private MSLEventCommand(MSLEditingDomain domain, Notification doEvent) {
		this(domain, doEvent, doEvent);
	}

	/**
	 * Constructor.
	 */
	private MSLEventCommand(MSLEditingDomain domain, Notification doEvent,
			Notification undoEvent) {

		super(domain);

		this.doEvent = doEvent;
		this.undoEvent = undoEvent;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {

		super.dispose();

		doEvent = null;
		undoEvent = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	public void execute() {
		domain.getEventBroker().addEvent(doEvent);
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {
		domain.getEventBroker().addEvent(undoEvent);
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {
		domain.getEventBroker().addEvent(doEvent);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getParticipantTypes()
	 */
	public Set getParticipantTypes() {

		if (participantTypes == null) {

			participantTypes = new HashSet();

			MObjectType type = getParticipantType(doEvent);

			if (type != null)
				participantTypes.add(type);

			if (doEvent != undoEvent) {

				type = getParticipantType(undoEvent);

				if (type != null)
					participantTypes.add(type);
			}
		}

		return participantTypes;
	}

	/**
	 * Gets the type of the notification notifier.
	 */
	private MObjectType getParticipantType(Notification notification) {

		MObjectType type = null;

		Object notifier = notification.getNotifier();

		if (notifier instanceof EObject)
			type = EObjectUtil.getType((EObject) notifier);

		return type;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public Type getType() {
		return MCommand.EVENT;
	}
}