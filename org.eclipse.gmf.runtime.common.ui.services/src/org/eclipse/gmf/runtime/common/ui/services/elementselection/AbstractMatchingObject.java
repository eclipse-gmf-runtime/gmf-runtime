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
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import org.eclipse.swt.graphics.Image;

/**
 * Abstract implementation of an IElementSelectionProvider.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public class AbstractMatchingObject
	implements IMatchingObject {

	/**
	 * The dashes used to construct the display name.
	 */
	public static String DASHES = " - "; //$NON-NLS-1$

	/**
	 * The name of the matching object.
	 */
	private String name;

	/**
	 * The display name of the matching object.
	 * <p>
	 * The display name is the matching object name followed by dashes followed
	 * by a fully qualified name to distingish between objects with the same
	 * name.
	 */
	private String displayName;

	/**
	 * The image of the matching object.
	 */
	private Image image;

	/**
	 * The element selection provider that that provided this matching object.
	 */
	private IElementSelectionProvider provider;

	/**
	 * Constructor for an AbstractMatchingObject.
	 * 
	 * @param name
	 *            the name of the matching object.
	 * @param displayName
	 *            The display name of the matching object.
	 * @param image
	 *            The image of the matching object.
	 * @param provider
	 *            The element selection provider that that provided this
	 *            matching object.
	 */
	public AbstractMatchingObject(String name, String displayName, Image image,
			IElementSelectionProvider provider) {
		this.name = name;
		this.displayName = displayName;
		this.image = image;
		this.provider = provider;
	}

    /**
     * {@inheritDoc}
     */
	public String getName() {
		return name;
	}

    /**
     * {@inheritDoc}
     */
	public String getDisplayName() {
		return displayName;
	}

    /**
     * {@inheritDoc}
     */
	public Image getImage() {
		return image;
	}

    /**
     * {@inheritDoc}
     */
	public String toString() {
		return displayName != null ? displayName
			: super.toString();
	}

    /**
     * {@inheritDoc}
     */
	public IElementSelectionProvider getProvider() {
		return provider;
	}
}
