/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.editparts;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.tests.runtime.diagram.ui.util.TestsPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.AbstractImageEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.URLImageEditPart;
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
	//private String TEST1 = "test1.WMF";//$NON-NLS-1$
	private String TEST2 = "test2.emf";//$NON-NLS-1$
	private String TEST3 = "test3.jpg";//$NON-NLS-1$
	private String TEST4 = "test4.gif";//$NON-NLS-1$
	private String TEST5 = "test5.bmp";//$NON-NLS-1$
	private String TEST6 = "test6.svg";//$NON-NLS-1$
	
	// non-image tests 
	private String TEST7 = "test7.txt";//$NON-NLS-1$
	private String TEST8 = "test8.doc";//$NON-NLS-1$
	private String TEST9 = "test9.xls";//$NON-NLS-1$
	private String TEST10 = "nofile.xxx";//$NON-NLS-1$
	//private String TEST11 = "test11.xsd";//$NON-NLS-1$
	
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
		return FileLocator.find(TestsPlugin.getDefault().getBundle(), path, null);
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
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.editparts.AbstractImageEditPartTests#getFixture()
	 */
	public List getFixtures() {
		List fixtures = new ArrayList(6);
		
		//fixtures.add(new URLImageEditPartFixture1(
		//	getNode(), getURL(TEST1)));
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
		//nonImageFixtures.add(new URLImageEditPartFixture1(
			//getNode(), getURL(TEST11)));
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
