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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IFilter;

/**
 * Interface describing the input for the element selection service.
 * 
 * @author Anthony Hunter
 */
public interface IElementSelectionInput {

    /**
     * Retrieves the scope of the search.
     * 
     * @return ElementSelectionScope the scope of the search.
     */
    public ElementSelectionScope getScope();

    /**
     * Retrieves the filter provided for the input. The filter is used to give
     * the user of the element selection service control over what types are
     * selected by the providers.
     * <p>
     * Most frequently, the filter will select an object if it matches a
     * specific list of types (<code>IElementType</code>).
     * </p>
     * 
     * @return List the list of the input types.
     */
    public IFilter getFilter();

    /**
     * Retrieves the context for the input.
     * 
     * @return IAdaptable the context for the input.
     */
    public IAdaptable getContext();

    /**
     * Retrieves a string input filter for the input. The filter is used to
     * match objects based on a string (? = any character, * = any string).
     * 
     * @return String the string input filter.
     */
    public String getInput();
}
