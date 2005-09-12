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

package org.eclipse.gmf.tests.runtime.common.core.internal.l10n;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;

public class AbstractResourceManagerTest extends TestCase {
	private static String ERROR_MESSAGE = "#getString(java.lang.String) failed"; //$NON-NLS-1$

	private String key1 = "Hello_World_1"; //$NON-NLS-1$
	private String key2 = "One_2"; //$NON-NLS-1$
	private String key3 = "Two_3"; //$NON-NLS-1$
	private String key4 = "Three_4"; //$NON-NLS-1$

	private String value1 = "Hello World"; //$NON-NLS-1$
	private String value2 = "One"; //$NON-NLS-1$
	private String value3 = "Two"; //$NON-NLS-1$
	private String value4 = "Three"; //$NON-NLS-1$
	
	private String message1key = "message1";  //$NON-NLS-1$
	private String message1arg1 = "Hello"; //$NON-NLS-1$
	private String message1arg2 = "world"; //$NON-NLS-1$
	private String message1 = "Hello, world!"; //$NON-NLS-1$

	private String list0 = "";  //$NON-NLS-1$
	private String list1 = "One";  //$NON-NLS-1$
	private String list2 = "One and two";  //$NON-NLS-1$
	private String list3 = "One, two, and three";  //$NON-NLS-1$
	private String one = "One";  //$NON-NLS-1$
	private String two = "two";  //$NON-NLS-1$
	private String three = "three";  //$NON-NLS-1$
	
	private AbstractResourceManager resourceManager;

	public AbstractResourceManagerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		resourceManager = new ResourceManager();

	}
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(AbstractResourceManagerTest.class);
	}

	public void testGetString() {

		// all values should be present, when a value is not present the key
		// is returned instead - if that is the case - signal test failed
		assertEquals(
			AbstractResourceManager.class +ERROR_MESSAGE,
			value1,
			resourceManager.getString(key1));
		assertEquals(
			AbstractResourceManager.class +ERROR_MESSAGE,
			value2,
			resourceManager.getString(key2));
		assertEquals(
			AbstractResourceManager.class +ERROR_MESSAGE,
			value3,
			resourceManager.getString(key3));
		assertEquals(
			AbstractResourceManager.class +ERROR_MESSAGE,
			value4,
			resourceManager.getString(key4));
	}

	public void testFormatMessage() {
		assertEquals(
				message1,
				resourceManager.formatMessage(
						message1key,
						new Object[] {message1arg1, message1arg2}));
	}
	
	public void testFormatListArray() {
		assertEquals(
				list0,
				resourceManager.formatList(new String[0]));
		assertEquals(
				list1,
				resourceManager.formatList(new String[] {one}));
		assertEquals(
				list2,
				resourceManager.formatList(new String[] {one, two}));
		assertEquals(
				list3,
				resourceManager.formatList(new String[] {one, two, three}));
	}
	
	public void testFormatListCollection() {
		java.util.List args = new java.util.LinkedList();
		
		assertEquals(list0, resourceManager.formatList(args));
		
		args.add(one);
		assertEquals(list1, resourceManager.formatList(args));
		
		args.add(two);
		assertEquals(list2, resourceManager.formatList(args));
		
		args.add(three);
		assertEquals(list3, resourceManager.formatList(args));
	}
}
