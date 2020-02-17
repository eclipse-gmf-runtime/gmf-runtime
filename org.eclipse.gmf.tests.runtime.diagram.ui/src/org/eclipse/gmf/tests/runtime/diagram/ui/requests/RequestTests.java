/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeRequest;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestFixture;

/**
 * Tests things relating to Requests.
 * 
 * @author cmahoney
 */
public class RequestTests
	extends AbstractTestBase {

	public static Test suite() {
		TestSuite s = new TestSuite(RequestTests.class);
		return s;
	}

	public RequestTests() {
		super("RequestTests Test Suite");//$NON-NLS-1$
	}

	protected void setTestFixture() {
		testFixture = new PresentationTestFixture();
	}

	protected PresentationTestFixture getFixture() {
		return (PresentationTestFixture) testFixture;
	}

	/**
	 * Tests the API of the <code>CreateUnspecifiedTypeRequest</code>.
	 * 
	 * @throws Exception
	 */
	public void testCreateUnspecifiedTypeRequest()
		throws Exception {

		List elementTypes = new ArrayList();
		elementTypes.add(DiagramNotationType.NOTE);
		elementTypes.add(DiagramNotationType.TEXT);

		CreateUnspecifiedTypeRequest request = new CreateUnspecifiedTypeRequest(
			elementTypes, PreferencesHint.USE_DEFAULTS);

		Map extendedData = new HashMap();
		Point location = new Point(1, 2);
		Dimension size = new Dimension(10, 5);
		Object type = new Integer(5);

		request.setExtendedData(extendedData);
		request.setLocation(location);
		request.setSize(size);
		request.setType(type);

		assertEquals(extendedData, request.getExtendedData());
		assertEquals(extendedData, request.getRequestForType(
			DiagramNotationType.NOTE).getExtendedData());
		assertEquals(extendedData, request.getRequestForType(
			DiagramNotationType.TEXT).getExtendedData());

		assertEquals(location, request.getLocation());
		assertEquals(location, request.getRequestForType(
			DiagramNotationType.NOTE).getLocation());
		assertEquals(location, request.getRequestForType(
			DiagramNotationType.TEXT).getLocation());

		assertEquals(size, request.getSize());
		assertEquals(size, request.getRequestForType(DiagramNotationType.NOTE)
			.getSize());
		assertEquals(size, request.getRequestForType(DiagramNotationType.TEXT)
			.getSize());

		assertEquals(type, request.getType());
		assertEquals(type, request.getRequestForType(DiagramNotationType.NOTE)
			.getType());
		assertEquals(type, request.getRequestForType(DiagramNotationType.TEXT)
			.getType());

	}

}
