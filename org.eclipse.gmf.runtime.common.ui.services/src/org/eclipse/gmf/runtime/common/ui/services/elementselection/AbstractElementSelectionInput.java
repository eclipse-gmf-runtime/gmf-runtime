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
    private IFilter filter;

    /**
     * A context for the input.
     */
    private IAdaptable context;

    /**
     * A input filter for the input.
     */
    private String input;

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
    public AbstractElementSelectionInput(IFilter filter, IAdaptable context,
            ElementSelectionScope scope, String input) {
        super();
        this.input = input;
        this.context = context;
        this.scope = scope;
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    public ElementSelectionScope getScope() {
        return scope;
    }

    /**
     * {@inheritDoc}
     */
    public IFilter getFilter() {
        return filter;
    }

    /**
     * {@inheritDoc}
     */
    public IAdaptable getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    public String getInput() {
        return input;
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
    public void setFilter(IFilter filter) {
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
     * Set the input filter for the element selection input.
     * 
     * @param input
     *            the input filter for the element selection input.
     */
    public void setInput(String input) {
        this.input = input;
    }

}
