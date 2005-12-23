/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * The MSL listens to EMF notifications and populates the command stack on the
 * fly. The following class generates commands based on notifications received
 * from EMF.
 * 
 * @author rafikj
 */
public class MSLCommandGenerator {

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLCommandGenerator(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Adds a command to command stack given an EMF notification.
	 */
	public void generateCommand(Notification notification) {

		if (domain.getUndoStack().isUndoInProgress())
			return;

		if (domain.getUndoStack().isRedoInProgress())
			return;

		if (domain.getUndoStack().isAbandonInProgress())
			return;

		if (notification.getNotifier() instanceof Resource) {
			generateResourceCommand(notification);
			return;
		}
		
		int eventType = notification.getEventType();

		if (eventType == EventTypes.CREATE)
			return;

		Object feature = notification.getFeature();

		if (feature == null)
			return;

		if (feature instanceof EStructuralFeature) {

			EStructuralFeature structuralFeature = (EStructuralFeature) feature;

			// ignore non changeable features.
			if (!structuralFeature.isChangeable())
				return;

			EObject notifier = (EObject) notification.getNotifier();

			Object newValue = notification.getNewValue();
			Object oldValue = notification.getOldValue();

			int position = notification.getPosition();

			boolean wasSet = notification.wasSet();

			switch (eventType) {

				case Notification.SET: {
					MSLUtil.execute(domain,
						new MSLSetCommand(domain, notifier, structuralFeature,
							newValue, oldValue, position, wasSet));

					break;
				}

				case Notification.UNSET: {
					MSLUtil.execute(domain,
						new MSLUnsetCommand(domain, notifier,
							structuralFeature, newValue, oldValue, wasSet));

					break;
				}

				case Notification.ADD: {
					MSLUtil.execute(domain, new MSLAddCommand(domain, notifier,
						structuralFeature, newValue, position));

					break;
				}

				case Notification.ADD_MANY: {
					MSLUtil.execute(domain, new MSLAddCommand(domain, notifier,
						structuralFeature, (Collection) newValue, position));

					break;
				}

				case Notification.REMOVE: {
					MSLUtil.execute(domain, new MSLRemoveCommand(domain,
						notifier, structuralFeature, oldValue,
						(position == Notification.NO_INDEX) ? 0
							: position));

					break;
				}

				case Notification.REMOVE_MANY: {
					MSLUtil.execute(domain, new MSLRemoveCommand(domain,
						notifier, structuralFeature, (Collection) oldValue,
						(int[]) newValue));

					break;
				}

				case Notification.MOVE: {
					MSLUtil.execute(domain, new MSLMoveCommand(domain,
						notifier, structuralFeature, newValue, position,
						((Integer) oldValue).intValue()));

					break;
				}
			}
		}
	}

	/**
	 * Adds a command to command stack given an EMF notification from a
	 * resource.
	 */
	private void generateResourceCommand(Notification notification) {

		int eventType = notification.getEventType();

		if (eventType == EventTypes.CREATE) {
			return;
		}
		
		int feature = notification.getFeatureID(Resource.class);

		if (feature < 0) {
			return;
		}
		
		if (!domain.isWriteInProgress()) {
			// TODO: Should tighten up the protocol to require compliance on resource changes.
			//       However, too many clients are currently violating this rule (incl. MSL).
			//       Until then, at least any changes made in an undo interval will be
			//       undoable.
			return;
		}

		Resource notifier = (Resource) notification.getNotifier();

		Object newValue = notification.getNewValue();
		Object oldValue = notification.getOldValue();

		int position = notification.getPosition();

//		boolean wasSet = notification.wasSet();

		switch (eventType) {

// TODO: Support undoable SET commands on resources
//			case Notification.SET: {
//				MSLUtil.execute(domain,
//					new MSLResourceSetCommand(domain, notifier, feature,
//						newValue, oldValue, position, wasSet));
//
//				break;
//			}

// TODO: Support undoable UNSET commands on resources
//			case Notification.UNSET: {
//				MSLUtil.execute(domain,
//					new MSLResourceUnsetCommand(domain, notifier,
//						feature, newValue, oldValue, wasSet));
//
//				break;
//			}

			case Notification.ADD: {
				MSLUtil.execute(domain, new MSLResourceAddCommand(domain, notifier,
					feature, newValue, position));

				break;
			}

			case Notification.ADD_MANY: {
				MSLUtil.execute(domain, new MSLResourceAddCommand(domain, notifier,
					feature, (Collection) newValue, position));

				break;
			}

			case Notification.REMOVE: {
				MSLUtil.execute(domain, new MSLResourceRemoveCommand(domain,
					notifier, feature, oldValue,
					(position == Notification.NO_INDEX) ? 0
						: position));

				break;
			}

			case Notification.REMOVE_MANY: {
				MSLUtil.execute(domain, new MSLResourceRemoveCommand(domain,
					notifier, feature, (Collection) oldValue,
					(int[]) newValue));

				break;
			}

			case Notification.MOVE: {
				MSLUtil.execute(domain, new MSLResourceMoveCommand(domain,
					notifier, feature, newValue, position,
					((Integer) oldValue).intValue()));

				break;
			}
		}
	}
}