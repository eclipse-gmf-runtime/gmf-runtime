/******************************************************************************
 * Copyright (c) 2006, 2014 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.elementselection;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestElementSelectionProviderContext;
import org.eclipse.jface.viewers.IFilter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

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

    public void ignoreBlankUserInput() {
        input.setInput(StringStatics.BLANK);
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 0);
    }

    public void ignoreFullNameUserInput() {
        input.setInput("one");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }

    public void ignoreNamePrefixUserInput() {
        input.setInput("t");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 6);
    }

    public void ignoreAnotherNamePrefixUserInput() {
        input.setInput("th");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }

    public void ignoreAnyStringUserInput() {
        input.setInput("t*ee");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }

    public void ignoreOnCharacterUserInput() {
        input.setInput("t?ree");//$NON-NLS-1$
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 3);
    }
    
	public void test_testNothing() {
		// There is an issue when running the tests, run no tests for now.
	}
}