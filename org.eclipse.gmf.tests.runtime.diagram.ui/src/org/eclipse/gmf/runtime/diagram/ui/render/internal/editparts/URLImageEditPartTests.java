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

package org.eclipse.gmf.runtime.diagram.ui.render.internal.editparts;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.tests.runtime.diagram.ui.util.TestsPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author sshaw
 *
 * Test class for URL based images
 */
public class URLImageEditPartTests
	extends AbstractImageEditPartTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.gmf.tests.runtime.diagram.ui.internal.editparts"); //$NON-NLS-1$
		//$JUnit-BEGIN$
		suite.addTestSuite(URLImageEditPartTests.class);
		//$JUnit-END$
		return suite;
	}
	
	// image tests
	private String TEST1 = new String("test1.wmf");//$NON-NLS-1$
	private String TEST2 = new String("test2.emf");//$NON-NLS-1$
	private String TEST3 = new String("test3.jpg");//$NON-NLS-1$
	private String TEST4 = new String("test4.gif");//$NON-NLS-1$
	private String TEST5 = new String("test5.bmp");//$NON-NLS-1$
	private String TEST6 = new String("test6.svg");//$NON-NLS-1$
	
	// non-image tests 
	private String TEST7 = new String("test7.txt");//$NON-NLS-1$
	private String TEST8 = new String("test8.doc");//$NON-NLS-1$
	private String TEST9 = new String("test9.xls");//$NON-NLS-1$
	private String TEST10 = new String("nofile.xxx");//$NON-NLS-1$
	private String TEST11 = new String("test11.xsd");//$NON-NLS-1$
	
	private static final String TRANSLATE_PATH_ARGUMENT = "$nl$"; //$NON-NLS-1$
	private static final String IMAGES = "images"; //$NON-NLS-1$

	protected String getImagePathName() {
		return IMAGES + IPath.SEPARATOR;
	}
	
	private URL getURL( String fileName ) {
		/* 
		 * prefix path with "$nl$" and use Plugin.find() to search for the 
		 * locale specific file
		 */
		IPath path =
			new Path(TRANSLATE_PATH_ARGUMENT).append(
				getImagePathName() + fileName);
		return TestsPlugin.getDefault().find(path);
	}
	
	class URLImageEditPartFixture1 extends URLImageEditPart {
		
		private URL url;
		
		/**
		 * @param view
		 */
		public URLImageEditPartFixture1(View view, URL url) {
			super(view);
			this.url = url;
		}
		protected URL getURL() {
			if (url == null)
				return super.getURL();
			
			return url;
		}
		protected String getImagePath() {
			return null;
		}
		protected String getPathImagePathIsRelativeTo() {
			return null;
		}
		public RenderedImage regenerateImageFromSourceTest() {
			return super.regenerateImageFromSource();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.internal.editparts.AbstractImageEditPartTests#getFixture()
	 */
	public List getFixtures() {
		List fixtures = new ArrayList(6);
		
		fixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST1)));
		fixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST2)));
		fixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST3)));
		fixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST4)));
		fixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST5)));
		fixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST6)));
		
		return fixtures;
	}
	
	/**
	 * @return
	 */
	protected List getNonImageFixtures() {
		List nonImageFixtures = new ArrayList(3);
		
		nonImageFixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST7)));
		nonImageFixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST8)));
		nonImageFixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST9)));
		nonImageFixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST10)));
		nonImageFixtures.add(new URLImageEditPartFixture1(
			getNode(), getURL(TEST11)));
		nonImageFixtures.add(new URLImageEditPartFixture1(
			getNode(), null)); // test null URL
		return nonImageFixtures;
	}
	
	public void test_regenerateImageFromSource() {
		List fixtures = getFixtures();
		ListIterator li = fixtures.listIterator();
		int testno = 1;
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof AbstractImageEditPart) {
				
				URLImageEditPartFixture1 fixture = (URLImageEditPartFixture1)obj;
				RenderedImage renderedImage = fixture.regenerateImageFromSourceTest();
				verifyRenderedImage(renderedImage, testno++);
			}
		} 
	}
	
	public void test_regenerateImageFromSource_InvalidSource() {
		List fixtures = getNonImageFixtures();
		ListIterator li = fixtures.listIterator();
		int testno = 1;
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof AbstractImageEditPart) {
				URLImageEditPartFixture1 fixture = (URLImageEditPartFixture1)obj;
				
				RenderedImage renderedImage = fixture.regenerateImageFromSourceTest();
				
				assertNull("Non image file was rendered to an image? " + (new Integer(testno++)).toString(), //$NON-NLS-1$
						(renderedImage == null?(Object) renderedImage:(Object) renderedImage.getSWTImage()));
			}
		}
	}
}
