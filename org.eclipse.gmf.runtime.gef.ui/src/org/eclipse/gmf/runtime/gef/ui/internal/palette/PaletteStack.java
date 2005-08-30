/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
 package org.eclipse.gmf.runtime.gef.ui.internal.palette;

import org.eclipse.jface.resource.ImageDescriptor;


/**
 * An implementation of a palette stack with identity
 * 
 * @author cmahoney
 */
public class PaletteStack
	extends org.eclipse.gef.palette.PaletteStack
	implements PaletteIdentity {

	/** the drawer's id */
	private String id;

	/**
	 * Creates a new PaletteStack.
	 * @param id the id
	 * @param name the name, shown only in the customize menu
	 * @param desc the description, shown only in the customize menu
	 * @param icon the name, shown only in the customize menu
	 */
	public PaletteStack(String id, String name, String desc,
			ImageDescriptor icon) {
		super(name, desc, icon);
		this.id = id;
	}

	/**
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteIdentity#getId()
	 */
	public String getId() {
		return id;
	}

}

