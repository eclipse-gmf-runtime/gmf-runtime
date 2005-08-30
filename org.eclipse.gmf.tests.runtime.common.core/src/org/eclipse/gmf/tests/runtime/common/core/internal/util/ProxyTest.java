/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
