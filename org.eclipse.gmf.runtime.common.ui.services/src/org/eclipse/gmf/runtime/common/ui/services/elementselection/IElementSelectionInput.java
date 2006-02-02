/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Interface describing the input for the element selection service.
 * 
 * @author Anthony Hunter
 */
public interface IElementSelectionInput {

    /**
     * Retrieves the scope of the search for input types.
     * 
     * @return ElementSelectionScope the scope of the search for input types.
     */
    public ElementSelectionScope getScope();

    /**
     * Retrieves the list of the input types. Each input type should implement
     * the <code>IElementType</code> interface.
     * 
     * @return List the list of the input types.
     */
    public List getTypes();

    /**
     * Retrieves the context for the input. Most frequently the context is an
     * EMF EObject.
     * 
     * @return IAdaptable the context for the input.
     */
    public IAdaptable getContext();

    /**
     * Retrieves a filter for the input. The filter is used to match objects
     * based on a string (? = any character, * = any string).
     * 
     * @return IAdaptable the filter for the input.
     */
    public String getFilter();
}
