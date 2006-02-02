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

import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Test cases for the scope for the element selection service.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionScopeTest
    extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ElementSelectionScopeTest.class);
    }

    /**
     * Test that you can set one ElementSelectionScope.
     */
    public void test_oneScopeSet() {
        ElementSelectionScope scope = new ElementSelectionScope();
        scope.set(ElementSelectionScope.GLOBAL);
        assertFalse(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.VISIBLE));
        assertTrue(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.GLOBAL));
        assertFalse(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.BINARIES));
        assertFalse(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.SOURCES));
    }

    /**
     * Test that you can set two kinds of ElementSelectionScope.
     */
    public void test_twoScopeSet() {
        ElementSelectionScope scope = new ElementSelectionScope();
        scope.set(ElementSelectionScope.GLOBAL);
        scope.set(ElementSelectionScope.VISIBLE);
        assertTrue(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.VISIBLE));
        assertTrue(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.GLOBAL));
        assertFalse(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.BINARIES));
        assertFalse(ElementSelectionScope.isSet(scope.intValue(),
            ElementSelectionScope.SOURCES));
    }
}
