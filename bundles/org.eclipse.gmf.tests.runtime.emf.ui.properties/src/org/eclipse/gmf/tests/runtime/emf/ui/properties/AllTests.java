/******************************************************************************
 * Copyright (c) 2005-2021 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.ui.properties;

import org.eclipse.gmf.tests.runtime.emf.ui.properties.sections.UndoableModelPropertySectionTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author gvaradar
 */
@Suite
@SelectClasses({ UndoableModelPropertySectionTest.class, })
public class AllTests {
}
