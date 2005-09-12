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

package org.eclipse.gmf.runtime.emf.core.edit;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * <p>
 * MSL filters are used to control what events get sent to listeners. To define
 * a new filter, one would subclass <code>MFilter</code> and implement the
 * abstract method <code>matches</code>. An MSL listener must have a filter
 * to start receiving events. An <code>MListener</code> with a
 * <code>null</code> filter will not receive events.
 * </p>
 * <p>
 * The class also defines some pre-defined filters that can be composed to build
 * more complex filters.  Some of these are filter instances, some are
 * classes.
 * </p>
 * 
 * @author rafikj
 */
public abstract class MFilter {

	/** A wildcard filter, matching all notifications. */
	public final static MFilter WILDCARD_FILTER = new MFilter() {

		public boolean matches(Notification event) {
			return true;
		}};
		
	/**
	 * Filter for resource loaded events.
	 * 
	 * @see #RESOURCE_UNLOADED_FILTER
	 */
	public final static MFilter RESOURCE_LOADED_FILTER = new MFilter.And(
		new MFilter.And(new MFilter.EventType(Notification.SET),
			new MFilter.NotifierType(Resource.class, false)), new MFilter() {

			public boolean matches(Notification event) {
				if (event.getFeatureID(Resource.class) == Resource.RESOURCE__IS_LOADED) {
					return event.getNewBooleanValue();
				}
				return false;
			}
		});

	/**
	 * Filter for resource unloaded events.
	 * @see #RESOURCE_LOADED_FILTER
	 * 
	 */
	public final static MFilter RESOURCE_UNLOADED_FILTER = new MFilter.And(
		new MFilter.And(new MFilter.EventType(Notification.SET),
			new MFilter.NotifierType(Resource.class, false)), new MFilter() {

			public boolean matches(Notification event) {
				if (event.getFeatureID(Resource.class) == Resource.RESOURCE__IS_LOADED) {
					return !event.getNewBooleanValue();
				}
				return false;
			}
		});
	
	/**
	 * Filter for root added events.
	 * 
	 * @see #RESOURCE_ROOT_REMOVED_FILTER
	 */
	public final static MFilter RESOURCE_ROOT_ADDED_FILTER = new MFilter.And(
		new MFilter.NotifierType(Resource.class, false), new MFilter.Or(
			new MFilter.EventType(Notification.ADD), new MFilter.EventType(
				Notification.ADD_MANY)));

	/**
	 * Filter for root removed events.
	 * 
	 * @see #RESOURCE_ROOT_ADDED_FILTER
	 */
	public final static MFilter RESOURCE_ROOT_REMOVED_FILTER = new MFilter.And(
		new MFilter.NotifierType(Resource.class, false), new MFilter.Or(
			new MFilter.EventType(Notification.REMOVE), new MFilter.EventType(
				Notification.REMOVE_MANY)));

	
	/**
	 * Filter for resource saved events.  This filter is the same as the dirtied
	 * filter, so don't add both.
	 * 
	 * @see #RESOURCE_DIRTIED_FILTER
	 */
	public final static MFilter RESOURCE_SAVED_FILTER = new MFilter.And(
		new MFilter.EventType(Notification.SET), new MFilter.NotifierType(
			Resource.class, false));

	/**
	 * Filter for resource dirtied events.  This filter is the same as the saved
	 * filter, so don't add both.
	 * 
	 * @see #RESOURCE_SAVED_FILTER
	 */
	public final static MFilter RESOURCE_DIRTIED_FILTER = new MFilter.And(
		new MFilter.EventType(Notification.SET), new MFilter.NotifierType(
			Resource.class, false));

	/**
	 * Filter for element separated or absorbed events.
	 */
	public final static MFilter SEPARATED_ABSORBED_FILTER = new MFilter.Or(
			new MFilter.EventType(EventTypes.SEPARATE), new MFilter.EventType(
				EventTypes.ABSORB));

	/**
	 * Filter for element created events.
	 * 
	 * @see #ELEMENT_DELETED_FILTER
	 * @see #ELEMENT_MODIFIED_FILTER
	 */
	public final static MFilter ELEMENT_CREATED_FILTER = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.Or(
			new MFilter.EventType(Notification.ADD), new MFilter.EventType(
				Notification.ADD_MANY)));

	/**
	 * Filter for element deleted events.
	 * 
	 * @see #ELEMENT_CREATED_FILTER
	 * @see #ELEMENT_MODIFIED_FILTER
	 */
	public final static MFilter ELEMENT_DELETED_FILTER = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.Or(
			new MFilter.EventType(Notification.REMOVE), new MFilter.EventType(
				Notification.REMOVE_MANY)));

	/**
	 * Filter for element modified events.
	 * 
	 * @see #ELEMENT_CREATED_FILTER
	 * @see #ELEMENT_DELETED_FILTER
	 */
	public final static MFilter ELEMENT_MODIFIED_FILTER = new MFilter.And(
		new MFilter.EventType(Notification.SET), new MFilter.NotifierType(
			EObject.class, false));

	/**
	 * Filter for element loaded events.
	 */
	public final static MFilter ELEMENT_LOADED_FILTER = 
			new MFilter.EventType(EventTypes.LOAD);

	/**
	 * Filter for undo interval closed events.
	 */
	public final static MFilter UNDO_INTERVAL_CLOSED_FILTER = new MFilter.And(
		new MFilter.EventType(EventTypes.CREATE), new MFilter.NotifierType(
			MUndoInterval.class, false));
	
	/**
	 * Checks if the event matches the filter.
	 * 
	 * @param event
	 *            The event to match.
	 * @return True if event matches the filter, false otherwise.
	 */
	public abstract boolean matches(Notification event);

	/**
	 * Negate a filter.
	 */
	public final static class Not
		extends MFilter {

		private MFilter filter = null;

		public Not(MFilter filter) {

			super();

			this.filter = filter;
		}

		public boolean matches(Notification event) {
			return !filter.matches(event);
		}
	}

	/**
	 * And two filters.
	 */
	public final static class And
		extends MFilter {

		private MFilter filter1 = null;

		private MFilter filter2 = null;

		public And(MFilter filter1, MFilter filter2) {

			super();

			this.filter1 = filter1;
			this.filter2 = filter2;
		}

		public boolean matches(Notification event) {
			return filter1.matches(event) && filter2.matches(event);
		}
	}

	/**
	 * Or two filters.
	 */
	public final static class Or
		extends MFilter {

		private MFilter filter1 = null;

		private MFilter filter2 = null;

		public Or(MFilter filter1, MFilter filter2) {

			super();

			this.filter1 = filter1;
			this.filter2 = filter2;
		}

		public boolean matches(Notification event) {
			return filter1.matches(event) || filter2.matches(event);
		}
	}

	/**
	 * Filter on event type.
	 */
	public final static class EventType
		extends MFilter {

		private int eventType = -1;

		public EventType(int eventType) {

			super();

			this.eventType = eventType;
		}

		public boolean matches(Notification event) {
			return event.getEventType() == eventType;
		}
	}

	/**
	 * Filter on notifier.
	 */
	public final static class Notifier
		extends MFilter {

		private WeakReference reference = null;

		public Notifier(Object notifier) {

			super();

			reference = new WeakReference(notifier);
		}

		public boolean matches(Notification event) {

			Object notifier = (reference == null) ? null
				: reference.get();

			return (event.getNotifier() == notifier);
		}
	}

	/**
	 * Filter on a collection of notifiers.
	 */
	public final static class Notifiers
		extends MFilter {

		private Map references = new WeakHashMap();

		public Notifiers() {
			super();
		}

		public void add(Object notifier) {
			references.put(notifier, null);
		}

		public void remove(Object notifier) {
			references.remove(notifier);
		}

		public boolean matches(Notification event) {
			return references.containsKey(event.getNotifier());
		}
	}

	/**
	 * Filter on notifier type.
	 */
	public final static class NotifierType
		extends MFilter {

		private Class type = null;

		private boolean strict = false;

		public NotifierType(Class type, boolean strict) {

			super();

			this.type = type;
		}

		public boolean matches(Notification event) {

			Object notifier = event.getNotifier();

			Class notifierType = notifier.getClass();

			if (type == notifierType)
				return true;

			else if (strict)
				return false;

			else
				return type.isInstance(notifier);
		}
	}

	/**
	 * Filter on notifier <code>EClass</code>.
	 */
	public final static class NotifierEClass
		extends MFilter {

		private EClass eClass = null;

		private boolean strict = false;

		public NotifierEClass(EClass eClass, boolean strict) {

			super();

			this.eClass = eClass;
		}

		public boolean matches(Notification event) {

			Object notifier = event.getNotifier();

			if (notifier instanceof EObject) {

				EClass notifierEClass = ((EObject) notifier).eClass();

				if (eClass == notifierEClass)
					return true;

				else if (strict)
					return false;

				else
					return notifierEClass.getEAllSuperTypes().contains(eClass);

			} else
				return false;
		}
	}
	
	/**
	 * Filter resource events that do not pertain to a given collection of content
	 *  type identifiers (Strings). Filters any non-resource level events.
	 */
	public final static class ResourceContentType
		extends MFilter {
		
		private Collection contentTypes;
		
		/**
		 * Constructs me.
		 * 
		 * @param contentTypes A collection of string objects for the
		 *  unique identifiers of distinct content types.
		 */
		public ResourceContentType(Collection contentTypes) {
			super();
			assert contentTypes != null;
			
			this.contentTypes = contentTypes;
		}

		public boolean matches(Notification event) {
			Object notifier = event.getNotifier();
			
			if (notifier instanceof Resource) {
				return resourceMatchesContentTypes((Resource)notifier);
			}
			
			return false;
		}
		
		/**
		 * Determines if a provided resource matches one of the provided
		 *  content types.
		 *  
		 * @param r A valid MSL resource that belongs to an MSL editing domain.
		 * 
		 * @return true if the resource's content type matches, false otherwise
		 */
		private boolean resourceMatchesContentTypes(Resource r) {
			IContentTypeManager manager = Platform.getContentTypeManager();
			IContentType[] types = manager.findContentTypesFor(MSLUtil.getFilePath(r));
			
			for (int i=0; i<types.length; i++) {
				if (contentTypes.contains(types[i].getId())) {
					return true;
				}
			}
			
			return false;
		}
	}
}