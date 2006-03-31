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
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestElementSelectionProviderContext;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.testproviders.TestMatchingObject;
import org.eclipse.jface.viewers.IFilter;

/**
 * Test cases for the filter for the element selection service, the filter being
 * the programatic filter at the application level to filter specific element
 * types.
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
        IFilter filter = new IFilter() {

            public boolean select(Object toTest) {
                return true;
            }

        };
        input = new AbstractElementSelectionInput(filter, context, scope, "t");//$NON-NLS-1$
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

    public void testAllElementsFilter() {
        List matches = ElementSelectionService.getInstance()
        .getMatchingObjects(input);
        assertTrue(matches.size() == 6);
    }

    public void testBlueElementsFilter() {
        input.setFilter(new IFilter() {

            public boolean select(Object element) {
                if (element instanceof TestMatchingObject) {
                    if (((TestMatchingObject) element).getComponent().equals(
                        "Blue")) {//$NON-NLS-1$
                        return true;
                    }
                }
                return false;
            }
        });
        List matches = ElementSelectionService.getInstance()
            .getMatchingObjects(input);
        assertTrue(matches.size() == 2);
    }
}
