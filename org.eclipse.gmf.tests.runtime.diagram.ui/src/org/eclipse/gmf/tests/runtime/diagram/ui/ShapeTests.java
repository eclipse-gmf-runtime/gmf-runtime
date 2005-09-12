/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Mar 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
