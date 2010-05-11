/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesStatusCodes;
import org.eclipse.gmf.examples.runtime.ui.pde.util.Log;

/**
 * The EmptyResourceBundle represents a resource bundle object that is always empty.
 * This object is used as a deafult return value whenever the loading of a resource bundle
 * has failed. The object keeps error logging every attempt to access to.
 * 
 * @author Natalia Balaba
 * @canBeSeenBy %partners
 */
public final class EmptyResourceBundle extends ResourceBundle {

	// --------------------------------------------------------------------//
	// ------------  STATIC VARIABLES BEGIN -------------------------------//
	// --------------------------------------------------------------------//

	/**
	 * error message
	 */
	private static String MISSING_BUNDLE_MESSAGE = "Resource bundle ({0}) is missing."; //$NON-NLS-1$

	/**
	 * error message
	 */
	private static String INVALID_ACCESS_MESSAGE = "Attempt to access resource in missing bundle ({0})."; //$NON-NLS-1$

	// --------------------------------------------------------------------//
	// ------------  STATIC VARIABLES END ---------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  INSTANCE VARIABLES BEGIN -----------------------------//
	// --------------------------------------------------------------------//

	/**
	 * name of the bundle that failed to load - will be used for exception 
	 * messages
	 */
	private String bundleName = null;

	/**
	 * collection of  bundle keys - always empty
	 */
	private Vector keys = new Vector();

	// --------------------------------------------------------------------//
	// ------------  INSTANCE VARIABLES END -------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  CONSTRUCTORS BEGIN -----------------------------------//
	// --------------------------------------------------------------------//

	/**
	 * Create an instance of EmptyResourceBundle and log the error to the log 
	 * file.
	 * 
	 * @param bundleName name of the bundle
	 */
	public EmptyResourceBundle(String bundleName) {
		super();
		this.bundleName = bundleName;
		Log.warning(
			GmfExamplesPlugin.getDefault(),
			GmfExamplesStatusCodes.L10N_FAILURE,
			MessageFormat.format(
				MISSING_BUNDLE_MESSAGE,
				new Object[] { getBundleName()}));
	}

	// --------------------------------------------------------------------//
	// ------------  CONSTRUCTORS END -------------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  INSTANCE METHODS BEGIN -------------------------------//
	// --------------------------------------------------------------------//

	/* This method always returns null  - since there is not associated value
	 * @see java.util.ResourceBundle#handleGetObject(String)
	 */
	protected Object handleGetObject(String key) {
		return null;
	}

	/* 
	 * Return empty keys enumeration
	 * @see java.util.ResourceBundle#getKeys()
	 */
	public Enumeration getKeys() {
		Log.warning(
			GmfExamplesPlugin.getDefault(),
			GmfExamplesStatusCodes.L10N_FAILURE,
			MessageFormat.format(
				INVALID_ACCESS_MESSAGE,
				new Object[] { getBundleName()}));

		return keys.elements();
	}

	/**
	 * Returns the bundleName.
	 * @return java.lang.String - name of the missing resource bundle
	 */
	private String getBundleName() {
		return bundleName;
	}

	// --------------------------------------------------------------------//
	// ------------  INSTANCE METHODS END ---------------------------------//
	// --------------------------------------------------------------------//

}
