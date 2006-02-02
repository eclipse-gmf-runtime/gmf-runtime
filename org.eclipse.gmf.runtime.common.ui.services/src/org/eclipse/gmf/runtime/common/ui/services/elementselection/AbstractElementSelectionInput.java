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
 * Abstract implementation of an IElementSelectionInput.
 * 
 * @author Anthony Hunter
 */
public class AbstractElementSelectionInput
    implements IElementSelectionInput {

    /**
     * The scope used for the search for matching objects.
     */
    private ElementSelectionScope scope;

    /**
     * A list of the input types.
     */
    private List types;

    /**
     * A context for the input.
     */
    private IAdaptable context;

    /**
     * A filter for the input.
     */
    private String filter;

    /**
     * Constructor for an AbstractElementSelectionInput
     * 
     * @param types
     *            A list of the types.
     * @param context
     *            A context for the input.
     * @param scope
     *            The scope used for the search for matching objects.
     * @param filter
     *            The filter used for the search for matching objects.
     */
    public AbstractElementSelectionInput(List types, IAdaptable context,
            ElementSelectionScope scope, String filter) {
        super();
        this.types = types;
        this.context = context;
        this.scope = scope;
        this.filter = filter;
    }

    /**
     * @inheritDoc
     */
    public ElementSelectionScope getScope() {
        return scope;
    }

    /**
     * @inheritDoc
     */
    public List getTypes() {
        return types;
    }

    /**
     * @inheritDoc
     */
    public IAdaptable getContext() {
        return context;
    }

    /**
     * @inheritDoc
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Set the context for the element selection input.
     * 
     * @param context
     *            the context for the element selection input.
     */
    public void setContext(IAdaptable context) {
        this.context = context;
    }

    /**
     * Set the filter for the element selection input.
     * 
     * @param filter
     *            the filter for the element selection input.
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Set the scope for the element selection input.
     * 
     * @param scope
     *            the scope for the element selection input.
     */
    public void setScope(ElementSelectionScope scope) {
        this.scope = scope;
    }

    /**
     * Set the list of types for the element selection input.
     * 
     * @param types
     *            the list of types for the element selection input.
     */
    public void setTypes(List types) {
        this.types = types;
    }

}
