/******************************************************************************
 * Copyright (c) 2005, 2021 IBM Corporation, Christian W. Damus, and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Christian W. Damus - bug 457888
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core;

import org.eclipse.gmf.tests.runtime.emf.type.core.commands.CreateElementCommandTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.commands.DeferredSetValueCommandTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.commands.DestroyElementCommandTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.commands.SetValueCommandTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.requests.SetRequestTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AbstractEditHelperAdviceTest.class, AbstractEditHelperTest.class, ElementTypeRegistryTest.class,
		ClientContextManagerTest.class, CreateElementCommandTest.class, CreateElementRequestTest.class,
		DeferredSetValueCommandTest.class, DestroyElementCommandTest.class, MetamodelTypeDescriptorTest.class,
		MetamodelTypeTest.class, MoveRequestTest.class, MultiClientContextTest.class, SetRequestTest.class,
		SetValueCommandTest.class, SpecializationTypeDescriptorTest.class, SpecializationTypeTest.class,
		ElementTypeUtilTest.class, })
public class AllTests {
}
