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

package org.eclipse.gmf.runtime.emf.type.core.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;

/**
 * A Singleton which provides a single point of reference for all of the
 * plug-in's localization needs.
 * <P>
 * This class is not intended to be used by clients of the element type registry
 * framework.
 * 
 * @author ldamus
 */
public class ResourceManager
	extends AbstractResourceManager {

	/**
	 * Singleton instance of the resource manager.
	 */
	private static ResourceManager instance = new ResourceManager();

	/**
	 * Initializes me.
	 */
	private ResourceManager() {
		super();
	}

	/**
	 * Retrieves the singleton instance of this resource manager.
	 * 
	 * @return The singleton resource manager.
	 */
	public static ResourceManager getInstance() {
		return instance;
	}

	/**
	 * Retrieves a localized string for the specified key.
	 * 
	 * @param key
	 *            The resource bundle key.
	 * @return A localized string value, or the original <code>key</key> if the
	 *         bundle does not include this entry.
	 */
	public static String getLocalizedString(String key) {
		return getInstance().getString(key);
	}

	/**
	 * Creates a localized, parameterized message from the specified pattern and
	 * argument keys in the resource bundle.
	 * 
	 * @param patternKey
	 *            resource bundle key of the message pattern
	 * @param argKeys
	 *            resource bundle keys of the arguments to the pattern
	 * @return the formatted message
	 * 
	 * @see java.text.MessageFormat
	 */
	public static String getMessage(String patternKey, String[] argKeys) {
		ResourceManager mgr = getInstance();

		Object[] args = new Object[argKeys.length];

		for (int i = 0; i < argKeys.length; i++) {
			args[i] = mgr.getString(argKeys[i]);
		}

		return getMessage(patternKey, args);
	}

	/**
	 * Creates a localized, parameterized message from the specified pattern and
	 * argument keys in the resource bundle.
	 * 
	 * @param patternKey
	 *            resource bundle key of the message pattern
	 * @param args
	 *            literal values as arguments to the pattern
	 * @return the formatted message
	 * 
	 * @see java.text.MessageFormat
	 */
	public static String getMessage(String patternKey, Object[] args) {
		return getInstance().formatMessage(patternKey, args);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#initializeResources()
	 */
	protected void initializeResources() {
		initializeMessageResources();
	}

	/**
	 * Convenience method to obtain my plug-in instance.
	 * 
	 * @return my plug-in
	 */
	protected Plugin getPlugin() {
		return EMFTypePlugin.getPlugin();
	}
}