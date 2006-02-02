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
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A standard structured content provider for the element selection composite.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionCompositeContentProvider
    implements IStructuredContentProvider {

    /**
     * The list of matching objects for the element selection input.
     */
    private List matchingObjects = new ArrayList();

    /**
     * Constructor for the ElementSelectionCompositeContentProvider.
     * 
     * @param input
     *            element selection input.
     */
    public ElementSelectionCompositeContentProvider() {
        super();
    }

    /**
     * @inheritDoc
     */
    public Object[] getElements(Object inputElement) {
        assert inputElement instanceof AbstractElementSelectionInput;
        AbstractElementSelectionInput input = (AbstractElementSelectionInput) inputElement;

        /*
         * Clean the previous list
         */
        matchingObjects.clear();

        /*
         * Initialize all possible matching objects from the select element
         * service.
         */
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        for (Iterator iter = matches.iterator(); iter.hasNext();) {
            List element = (List) iter.next();
            matchingObjects.addAll(element);
        }
        return matchingObjects.toArray();
    }

    /**
     * @inheritDoc
     */
    public void dispose() {
        // not implemented
    }

    /**
     * @inheritDoc
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // not implemented
    }

}
