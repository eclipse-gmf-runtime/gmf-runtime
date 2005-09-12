/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core.internal.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.util.Proxy;

public class ProxyTest extends TestCase {

    protected static class Fixture extends Proxy {

        protected Fixture(Object realObject) {
            super(realObject);
        }

    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ProxyTest.class);
    }

    public ProxyTest(String name) {
        super(name);
    }

    public void test_Proxy() {
        try {
            new Fixture(null);
            fail();
        } catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
        	// nothing to do
        }
    }

}
