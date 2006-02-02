/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.elementselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestElementSelectionProviderContext;

/**
 * Test cases for the filter for the element selection service.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionFilterTest
    extends TestCase {

    private AbstractElementSelectionInput input;

    protected void setUp()
        throws Exception {
        super.setUp();
        ElementSelectionScope scope = ElementSelectionScope.VISIBLE;
        IAdaptable context = new TestElementSelectionProviderContext();
        input = new AbstractElementSelectionInput(new ArrayList(), context,
            scope, StringStatics.BLANK);
    }

    protected void tearDown()
        throws Exception {
        super.tearDown();
        input = null;
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ElementSelectionFilterTest.class);
    }

    public void testBlankFilter() {
        input.setFilter(StringStatics.BLANK);
        List matches = getMatchingObjects();
        assertTrue(matches.size() == 0);
    }

    public void testFullNameFilter() {
        input.setFilter("one");//$NON-NLS-1$
        List matches = getMatchingObjects();
        assertTrue(matches.size() == 3);
    }

    public void testNamePrefixFilter() {
        input.setFilter("t");//$NON-NLS-1$
        List matches = getMatchingObjects();
        assertTrue(matches.size() == 6);
    }

    public void testAnotherNamePrefixFilter() {
        input.setFilter("th");//$NON-NLS-1$
        List matches = getMatchingObjects();
        assertTrue(matches.size() == 3);
    }

    public void testAnyStringFilter() {
        input.setFilter("t*ee");//$NON-NLS-1$
        List matches = getMatchingObjects();
        assertTrue(matches.size() == 3);
    }

    public void testOnCharacterFilter() {
        input.setFilter("t?ree");//$NON-NLS-1$
        List matches = getMatchingObjects();
        assertTrue(matches.size() == 3);
    }

    /**
     * Due to the fact that the ElementSelectionService uses the FORWARD
     * execution strategy, the service returns a list of list of objects, this
     * method compresses it down to a list of objects.
     * 
     * @param input
     *            the element selection input
     * @return List of matching objects from the element selection service.
     */
    private List getMatchingObjects() {
        List matchingObjects = new ArrayList();
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        for (Iterator iter = matches.iterator(); iter.hasNext();) {
            List element = (List) iter.next();
            matchingObjects.addAll(element);
        }
        return matchingObjects;
    }
}
