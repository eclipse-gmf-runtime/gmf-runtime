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

package org.eclipse.gmf.runtime.emf.type.core;

import java.net.URL;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Interface used to define application-layer types that describe the kinds of
 * elements that can be displayed, created, modified and destroyed. These types
 * extend the types defined for any given metamodel, so that
 * <code>IElementType</code> s can be used to distinguish types of elements
 * that share the same metaclass.
 * <P>
 * Each element type defines how model objects that match its type are to be
 * displayed (by icon URL and display name), as well as how they are to be
 * created, modified and deleted (by edit helper and an accessor for edit
 * commands).
 * <P>
 * There are two known extensions of <code>IElementType</code>. These are
 * <code>IMetamodelType</code> and <code>ISpecializationType</code>. Each
 * metamodel type maps directly to an EClass. Each specialization type matches a
 * metamodel type with further restrictions expressed in a
 * <code>IElementMatcher</code> class.
 * <P>
 * Clients should not implement this interface directly, but should extend the
 * abstract implementation
 * {@link org.eclipse.gmf.runtime.emf.type.core.ElementType}.
 * 
 * @author ldamus
 */
public interface IElementType
	extends IAdaptable {

	/**
	 * Gets the unique identifier for this element type.
	 * 
	 * @return the unique identifier
	 */
	public abstract String getId();

	/**
	 * Gets the icon URL.
	 * 
	 * @return the icon URL
	 */
	public abstract URL getIconURL();

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */
	public abstract String getDisplayName();

	/**
	 * Gets the metaclass for this element type.
	 * 
	 * @return the metaclass
	 */
	public abstract EClass getEClass();

	/**
	 * Gets a command to edit an element of this type.
	 * 
	 * @param request
	 *            the edit request
	 * @return the edit command, or <code>null</code> if none is found. The
	 *         command returned may not be executable, and this should be tested
	 *         before it is executed.
	 */
	public abstract ICommand getEditCommand(IEditCommandRequest request);
    
    /**
     * Answers whether or not the requested edit can be performed.
     * 
     * @param req
     *            the edit request
     * @return <code>true</code> if the requested edit can be performed,
     *         <code>false</code> otherwise.
     */
    public boolean canEdit(IEditCommandRequest req);

	/**
	 * Gets the edit helper for this element type.
	 * 
	 * @return the edit helper
	 */
	public abstract IEditHelper getEditHelper();
	
	/**
	 * Gets the element supertypes for this type.
	 * Ordered from furthest supertype to nearest supertype.
	 * 
	 * @return the element supertypes
	 */
	public IElementType[] getAllSuperTypes();

}