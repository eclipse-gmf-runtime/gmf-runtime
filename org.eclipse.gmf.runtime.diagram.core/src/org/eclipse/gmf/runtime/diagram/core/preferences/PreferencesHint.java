/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.core.preferences;

import java.util.HashMap;
import java.util.Map;

/**
 * This specifies the hint with which a preference store containing the diagram
 * preferences will be registered against in the preferences registry. This hint
 * will be passed around and when a view is created it will use this hint to
 * initialize its properties based on the values in the preference store
 * registered against this hint in the preferences registry.
 * 
 * @author cmahoney
 */
public class PreferencesHint {

	/**
	 * The hint that indicates there are no preferences to be used for the
	 * generic diagram preferences, instead use the default values.
	 */
	public static PreferencesHint USE_DEFAULTS = new PreferencesHint(
		"UseDefaults"); //$NON-NLS-1$

	/**
	 * The hint that indicates that the preferences under the "Modeling"
	 * category should be used for the diagramming preferences. 
	 * @deprecated If you were using this and need assistance ask Cherie (cmahoney@ca.ibm.com).
     * To be deleted after Dec 21, 2005.
	 */
	public static final PreferencesHint MODELING = new PreferencesHint(
		"MODELING"); //$NON-NLS-1$

	/**
	 * A map of preference hints to preference stores
	 * {@link org.eclipse.jface.preference.IPreferenceStore}.
	 */
	private static Map preferenceStores = new HashMap();

	/**
	 * The unique id string of this hint.
	 */
	private String id;

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the unique id string of this hint
	 */
	public PreferencesHint(String id) {
		this.id = id;
	}

	/**
	 * Gets the unique id string of this hint
	 * 
	 * @return the id
	 */
	private String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return arg0 instanceof PreferencesHint ? getId().equals(
			((PreferencesHint) arg0).getId())
			: false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * Registers a preference store containing some or all of the generic
	 * diagram preferences (those preferences defined in the diagram layer) to a
	 * preference hint. When a diagram preference is needed, it will be
	 * retrieved from the preference store using the hint specified in the root
	 * editpart. This allows each diagram editor to have its own diagram
	 * preferences. The preference store is only registered against the hint
	 * given if there is not already a preference store registered with that
	 * same preference hint.
	 * 
	 * @param preferencesHint
	 *            the preference hint for which the preferences store is to be
	 *            associated
	 * @param preferenceStore
	 *            the preference store initialized with the diagram preferences
	 *            (an instance of
	 *            {@link org.eclipse.jface.preference.IPreferenceStore})
	 * @return true if the preference store was successfully registered; false
	 *         otherwise
	 */
	public static boolean registerPreferenceStore(PreferencesHint preferencesHint,
			Object preferenceStore) {
		if (preferenceStores.containsKey(preferencesHint)) {
			return false;
		}
		preferenceStores.put(preferencesHint, preferenceStore);
		return true;
	}

	/**
	 * Gets the preference store registered with the preference hint given.
	 * 
	 * @param preferenceHint
	 *            the preference hint
	 * @return the preference store (an instance of
	 *         {@link org.eclipse.jface.preference.IPreferenceStore})
	 *         registered with the preferences hint given; if no preference
	 *         store has been registered with this hint the preference store
	 *         with the default values is returned.
	 */
	public Object getPreferenceStore() {
		Object store = preferenceStores.get(this);
		if (store == null) {
			store = preferenceStores.get(PreferencesHint.USE_DEFAULTS);
		}
		return store;
	}

}
