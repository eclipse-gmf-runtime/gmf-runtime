/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the scope for the element selection service.
 *
 * @author Anthony Hunter
 */
public class ElementSelectionScopeTest {
	/**
	 * Test that you can set one ElementSelectionScope.
	 */
	@Test
	public void test_oneScopeSet() {
		ElementSelectionScope scope = new ElementSelectionScope();
		scope.set(ElementSelectionScope.GLOBAL);
		assertFalse(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.VISIBLE));
		assertTrue(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.GLOBAL));
		assertFalse(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.BINARIES));
		assertFalse(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.SOURCES));
	}

	/**
	 * Test that you can set two kinds of ElementSelectionScope.
	 */
	@Test
	public void test_twoScopeSet() {
		ElementSelectionScope scope = new ElementSelectionScope();
		scope.set(ElementSelectionScope.GLOBAL);
		scope.set(ElementSelectionScope.VISIBLE);
		assertTrue(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.VISIBLE));
		assertTrue(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.GLOBAL));
		assertFalse(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.BINARIES));
		assertFalse(ElementSelectionScope.isSet(scope.intValue(), ElementSelectionScope.SOURCES));
	}
}
