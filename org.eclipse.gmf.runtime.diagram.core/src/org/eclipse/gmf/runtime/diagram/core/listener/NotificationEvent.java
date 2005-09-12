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

package org.eclipse.gmf.runtime.diagram.core.listener;

import java.beans.PropertyChangeEvent;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;

/**
 * Wraps an EMF notification into a property change event to
 * allow it to be broadcast throughout the Diagram framework.
 * This class also provides some convenience methods to extract various
 * data from the notification.
 * 
 * @author mhanner
 */
public class NotificationEvent extends PropertyChangeEvent {
	
	private static final long serialVersionUID = 1L;
	
	/** the actual notitification. */
	private Notification _notification;
	
	/**
	 * Constructor
	 * Same as calling <code>NoficationEvent(notification.getFeature().getClass().getName(), notification );</code>
	 * @param notification the <code>Notification<code> to warp 
	 */
	public NotificationEvent(Notification notification ) {
		this( getPropertyName((ENamedElement)notification.getFeature()), notification );
	}
	
	/**
	 * Constructor
	 * @param propertyName Name of the property
	 * @param notification the <code>Notification<code> to warp
	 */
	public NotificationEvent( String propertyName,  Notification notification ) {
		this( notification.getNotifier(), propertyName, notification.getOldValue(), notification.getNewValue(), notification );
	}
	
	/**
	 * Constructor
	 * @param source the notification source
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 * @param notification	the <code>Notification<code> to warp 
	 */
	public NotificationEvent(Object source, Object oldValue, Object newValue, Notification notification ) {
		this (source, getPropertyName((ENamedElement)notification.getFeature()), oldValue, newValue, notification);
	}
	
	/**
	 * Constructor
	 * @param source the notification source
	 * @param propertyName the property that had changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 * @param notification	the <code>Notification<code> to warp 
	 */
	public NotificationEvent(Object source, String propertyName, Object oldValue, Object newValue, Notification notification ) {
		super(source, propertyName, oldValue, newValue);
		_notification = notification;
	}
	
	/**
	 * gets the property name, which is the fully qualified name of the element going up
	 * to the root EPackage. The same as calling <code>MetaModelUtil.getID( element )</code>
	 * @param element the element to get the name
	 * @return	the property name
	 */
	public static String getPropertyName( ENamedElement element ) {
		if (  element != null ) {
			String id = MetaModelUtil.getID( element );
			return id == null ? "" : id;//$NON-NLS-1$
		}
		return "";//$NON-NLS-1$
	}
	
	/**
	 * gets the actual EMF notification.
	 * @return the wraped <code>Notification<code> object
	 */
	public final Notification getNotification() {
		return _notification;
	}
	
	/**
	 * gets the Notifier,<code>EObject</code>, of the change.
	 * Same as calling <code>(EObject)getSource()</code>.
	 * @return the notifier
	 */
	public final EObject getElement() {
		return (EObject)getNotification().getNotifier();
	}
	
	/**
	 * gets the event source's name
	 * @return the name of the Notifier's <code>EObject</code>
	 */
	public final String getElementName() {
		return EObjectUtil.getName( getElement() );
	}
	
	/**
	 * gets the event source's id
	 * @return the id of the Notifier's <code>EObject</code>
	 */
	public final String getElementId() {
		return EObjectUtil.getID( getElement() );
	}
	
	/**
	 * Return the event type.  The return value can be 
	 * tested against the <code>org.eclipse.gmf.runtime.emf.core.internal.EventTypes</code>
	 * interface.
	 * @return the event type
	 */
	public final int getEventType() {
		return _notification.getEventType();
	}

	/**
	 * used to decide if this is an UNDO event or not
	 * @return <tt>true</tt> if this is an UNDO event; <tt>false</tt> otherwise.
	 */
	public final boolean isUndoNotification() {
		return MEditingDomainGetter.getMEditingDomain(this).isUndoNotification( getNotification() );
	}

	/**
	 * used to decide if this is a REDO event
	 * @return <tt>true</tt> if this is an REDO event; <tt>false</tt> otherwise.
	 */
	public final boolean isRedoNotification() {
		return MEditingDomainGetter.getMEditingDomain(this).isRedoNotification( getNotification() );
	}
	
	/**
	 * the object representing the feature of the notifier that has changed
	 * @return the feature
	 */
	public final Object getFeature() {
		return getNotification().getFeature();
	}
	
	/**
	 * checks if the event resulted in a slot modification (set/unset event)
	 * @return <tt>true</tt> if the event type is
	 * one of {@link EventTypes#SET} or {@link EventTypes#UNSET};
	 * otherwise <tt>false</tt>.
	 */
	public final boolean isSlotModified() {
		return getEventType() == EventTypes.SET || getEventType() == EventTypes.UNSET;
	}
	
	/**
	 * checks if the event resulted in adding an element
	 * @return <tt>true</tt> if the event type is
	 * one of {@link EventTypes#ADD} or {@link EventTypes#ADD_MANY};
	 * otherwise <tt>false</tt>.
	 */
	public final boolean isElementAddedToSlot() {
		return getEventType() == EventTypes.ADD || getEventType() == EventTypes.ADD_MANY;
	}
	

	/**
	 * checks if the event resulted in removing an element
	 * @return <tt>true</tt> if the event type is
	 * one of {@link EventTypes#REMOVE} or {@link EventTypes#REMOVE_MANY};
	 * otherwise <tt>false</tt>.
	 */
	public final boolean isElementRemovedFromSlot() {
		return getEventType() == EventTypes.REMOVE || getEventType() == EventTypes.REMOVE_MANY;
	}

	/**
	 * checks if the event resulted in destroying an element
	 * @return <tt>true</tt> if the event type equals
	 * {@link EventTypes#DESTROY}; otherwise <tt>false</tt>.
	 */
	public final boolean isDestroy() {
		return getEventType() == EventTypes.DESTROY;
	}
	
	/**
	 * checks if the event is an {@link EventTypes.MOVE} event.
	 * @return <tt>true</tt> if the event type equals
	 * {@link EventTypes.MOVE}; otherwise <tt>false</tt>.
	 */
	public final boolean isMove() {
		return getEventType() == EventTypes.MOVE;
	}
}
