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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AbstractEditHelperAdviceTest.suite());
        suite.addTest(AbstractEditHelperTest.suite());
        suite.addTest(ElementTypeRegistryTest.suite());
        suite.addTest(ClientContextManagerTest.suite());
        suite.addTest(CreateElementCommandTest.suite());
        suite.addTest(CreateElementRequestTest.suite());
        suite.addTest(DeferredSetValueCommandTest.suite());
        suite.addTest(DestroyElementCommandTest.suite());
        suite.addTest(MetamodelTypeDescriptorTest.suite());
        suite.addTest(MetamodelTypeTest.suite());
        suite.addTest(MoveRequestTest.suite());
        suite.addTest(MultiClientContextTest.suite());
        suite.addTest(SetRequestTest.suite());
        suite.addTest(SetValueCommandTest.suite());
        suite.addTest(SpecializationTypeDescriptorTest.suite());
        suite.addTest(SpecializationTypeTest.suite());
        suite.addTest(ElementTypeUtilTest.suite());
        return suite;
    }

    public AllTests() {
        super(""); //$NON-NLS-1$
    }

}
