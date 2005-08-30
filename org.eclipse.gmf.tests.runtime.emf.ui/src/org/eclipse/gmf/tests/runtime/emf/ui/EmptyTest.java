/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.emf.ui;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * This test class does not test anything. It is simply an empty TestCase
 * so that the framework in com.ibm.xtools.bml.ui.tests works.
 * This class should be deleted when someone adds a test class to this
 * test plug-in. 
 * 
 * @author Anthony Hunter 
 * <a href="mailto:anthonyh@ca.ibm.com">mailto:anthonyh@ca.ibm.com</a>
 */
public class EmptyTest extends TestCase {

	protected static class Fixture {
		// Protected constructor.
	}

	private Fixture fixture = null;

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(EmptyTest.class);
	}

	public EmptyTest(String name) {
		super(name);
	}

	protected Fixture getFixture() {
		return fixture;
	}

	private void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	protected void setUp() {
		setFixture(new Fixture());
	}

	public void test_empty() {
		// Do nothing.
	}
}

