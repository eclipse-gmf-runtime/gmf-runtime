/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.elementselection;

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
import org.eclipse.jface.viewers.IFilter;

/**
 * Test cases for the user input for the element selection service.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionUserInputTest
    extends TestCase {

    private AbstractElementSelectionInput input;

    protected void setUp()
        throws Exception {
        super.setUp();
        ElementSelectionScope scope = ElementSelectionScope.VISIBLE;
        IAdaptable context = new TestElementSelectionProviderContext();
        IFilter filter = new IFilter() {

            public boolean select(Object toTest) {
                return true;
            }

        };
        input = new AbstractElementSelectionInput(filter, context, scope,
            StringStatics.BLANK);
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
        return new TestSuite(ElementSelectionUserInputTest.class);
    }

    public void testBlankUserInput() {
        input.setInput(StringStatics.BLANK);
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 0);
    }

    public void testFullNameUserInput() {
        input.setInput("one");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }

    public void testNamePrefixUserInput() {
        input.setInput("t");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 6);
    }

    public void testAnotherNamePrefixUserInput() {
        input.setInput("th");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }

    public void testAnyStringUserInput() {
        input.setInput("t*ee");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }

    public void testOnCharacterUserInput() {
        input.setInput("t?ree");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }
}