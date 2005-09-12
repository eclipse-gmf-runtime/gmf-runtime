/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.internal.views.properties.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import org.eclipse.gmf.runtime.common.ui.services.properties.internal.extended.ExtendedPropertyDescriptor;
//import org.eclipse.gmf.runtime.common.ui.services.properties.internal.extended.ExtendedTextPropertyDescriptor;

/**
 * @author Tauseef A. Israr
 * Created on: Sep 15, 2002
 * 
 */
public class ExtendedTextPropertyDescriptorTest extends TestCase {

//    ExtendedTextPropertyDescriptor propertyDescriptor;

    /**
     * Constructor for ExtendedTextPropertyDescriptorTest.
     * @param name
     */
    public ExtendedTextPropertyDescriptorTest(String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
//        propertyDescriptor = new ExtendedTextPropertyDescriptor("id1", "name"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testXDETextPropertyDescriptor() {
//        assertNotNull(propertyDescriptor.getBlank());

 //       ExtendedPropertyDescriptor propertyDescriptor1 =
 //           new ExtendedTextPropertyDescriptor("id1", "name"); //$NON-NLS-1$ //$NON-NLS-2$

 //       assertTrue(!propertyDescriptor.isCompatibleWith(propertyDescriptor1));
    }

    public static Test suite() {
        return new TestSuite(ExtendedTextPropertyDescriptorTest.class);
    }

}
