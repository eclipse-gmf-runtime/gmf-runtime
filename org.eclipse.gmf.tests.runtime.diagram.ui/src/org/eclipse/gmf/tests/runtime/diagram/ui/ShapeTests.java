/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.diagram.ui;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author sshaw
 *
 * Shape Tests
 */
public class ShapeTests extends AbstractShapeTests {

	/**
	 * @param arg0
	 */
	public ShapeTests(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	protected void setTestFixture() {
		testFixture = new DiagramTestFixture();
	}
	
	public static Test suite() {
		return new TestSuite(ShapeTests.class);
	}
}
