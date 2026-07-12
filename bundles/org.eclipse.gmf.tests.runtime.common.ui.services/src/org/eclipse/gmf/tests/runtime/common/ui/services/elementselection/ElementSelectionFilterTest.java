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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestElementSelectionProviderContext;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.testproviders.TestMatchingObject;
import org.eclipse.jface.viewers.IFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the filter for the element selection service, the filter being
 * the programatic filter at the application level to filter specific element
 * types.
 *
 * @author Anthony Hunter
 */
public class ElementSelectionFilterTest {

	private AbstractElementSelectionInput input;

	@BeforeEach
	public void setUp() throws Exception {
		if (input == null) {
			// There is an issue when running the tests, run no tests for now.
			return;
		}
		ElementSelectionScope scope = ElementSelectionScope.VISIBLE;
		IAdaptable context = new TestElementSelectionProviderContext();
		IFilter filter = new IFilter() {

			@Override
			public boolean select(Object toTest) {
				return true;
			}

		};
		input = new AbstractElementSelectionInput(filter, context, scope, "t");//$NON-NLS-1$
	}

	@AfterEach
	public void tearDown() {
		input = null;
	}

	public void ignoreAllElementsFilter() {
		List matches = ElementSelectionService.getInstance().getMatchingObjects(input);
		assertTrue(matches.size() == 6);
	}

	public void ignoreBlueElementsFilter() {
		input.setFilter(new IFilter() {

			@Override
			public boolean select(Object element) {
				if (element instanceof TestMatchingObject) {
					if (((TestMatchingObject) element).getComponent().equals("Blue")) {//$NON-NLS-1$
						return true;
					}
				}
				return false;
			}
		});
		List matches = ElementSelectionService.getInstance().getMatchingObjects(input);
		assertTrue(matches.size() == 2);
	}

	@Test
	public void test_testNothing() {
		// There is an issue when running the tests, run no tests for now.
	}
}
