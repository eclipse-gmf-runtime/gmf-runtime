/******************************************************************************
 * Copyright (c) 2005, 2016 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.clipboard.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.examples.extlibrary.EXTLibraryFactory;
import org.eclipse.emf.examples.extlibrary.Library;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport;


/**
 * Tests for regressions in defects.
 * 
 * @author Christian Vogt (cvogt)
 */
public class RegressionTest	extends BaseClipboardTest {

	public RegressionTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(RegressionTest.class, "Clipboard Regression Tests"); //$NON-NLS-1$
	}

	/**
	 * Tests copying root elements to the clipboard.
	 */
	public void offtest_copyRoot_bugzilla107880() {
		if (writing()) {
			List objects = new ArrayList();
			
			// copy root1
			objects.add(root1);
			String result = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(result);
			assertFalse(result.length() == 0);
			
			int length = result.length();
			
			// copy root2
			objects.add(root2);
			result = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(result);
			assertTrue(result.length() > length);
		}
	}
	
	/**
	 * Tests copying a root element to the clipboard and pasting
	 * this element into another element.
	 */
	public void offtest_copyPasteRoot_bugzilla107880() {
		if (writing()) {
			List objects = new ArrayList();
	
			// level12 has no branches
			assertTrue(level12.getBranches().size() == 0);
	
			// copy root3
			objects.add(root3);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into level1
			Collection eObjects = paste(copyStr, level12, Collections.EMPTY_MAP);
			assertEquals(eObjects.size(), objects.size());
	
			// check that the library was copied into the branches containment list
			assertTrue(level12.getBranches().size() == 1);
		}
	}

	/**
	 * Tests copying a root element to the clipboard and pasting a copy
	 * to itself.
	 */
	public void offtest_copyPasteRootToItself_bugzilla107880() {
		if (writing()) {
			List objects = new ArrayList();
	
			// root3 has no branches
			assertEquals(root3.getBranches().size(), 0);
	
			// copy root3
			objects.add(root3);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into root3
			Collection eObjects = paste(copyStr, root3, Collections.EMPTY_MAP);
			assertTrue(eObjects.size() == objects.size());
	
			// check that the library was copied into the branches containment list
			assertTrue(root3.getBranches().size() == 1);
		}
	}
	
	/**
	 * Tests copying multiple roots and non-roots to the clipboard at the same time
	 * and pasting them into and element.
	 */
	public void offtest_copyPasteMultipleRoots_bugzilla107880() {
		if (writing()) {
			List objects = new ArrayList();
	
			// root3 has no branches
			assertTrue(root3.getBranches().size() == 0);
	
			// copy root1, level2writer, level2book, and root3
			objects.add(root1);
			objects.add(level2writer);
			objects.add(level2book);
			objects.add(root3);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into root3
			Collection eObjects = paste(copyStr, root3, Collections.EMPTY_MAP);
			assertTrue(eObjects.size() == objects.size());
	
			// check that the items were copied into acceptable containment features
			assertTrue(root3.getWriters().size() == 1);
			assertTrue(root3.getBooks().size() == 1);
			assertTrue(root3.getBranches().size() == 2);
		}
	}
	
	/**
	 * Tests copying a root element to the clipboard and pasting into
	 * an invalid target element (a target element which does not have
	 * a feature capable of containing the copied element).
	 */
	public void offtest_pasteRootToInvalidTarget_bugzilla107880() {
		if (writing()) {
			List objects = new ArrayList();
	
			// copy root3
			objects.add(root3);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into level1writer
			Collection eObjects = paste(copyStr, level1writer, Collections.EMPTY_MAP);
			assertTrue(eObjects.size() == 0);
		}
	}
	
	/**
	 * Tests pasting elements into the resource.
	 */
	public void offtest_pasteToResource_bugzilla107880() {
		if (writing()) {
			List objects = new ArrayList();
	
			// testResource has 3 contents
			assertTrue(testResource.getContents().size() == 3);
	
			// copy root3 and level1book
			objects.add(root3);
			objects.add(level1book);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into testResource
			Collection eObjects = paste(copyStr, testResource, Collections.EMPTY_MAP);
			assertEquals(eObjects.size(), objects.size());
	
			// check that the library and book were copied into the resource
			assertTrue(testResource.getContents().size() == 5);
		}
	}
	
	/**
	 * Tests copying an element to the clipboard and pasting into
	 * a target which has a non-containment feature which can reference
	 * the type of the copied element.
	 */
	public void offtest_pasteIntoNonContainmentFeature() {
		if (writing()) {
			List objects = new ArrayList();
	
			// level2book has no author
			assertNull(level2book.getAuthor());
	
			// copy level2writer
			objects.add(level2writer);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into level2book
			Collection eObjects = paste(copyStr, level2book, Collections.EMPTY_MAP);
			assertTrue(eObjects.size() == 0);
			
			// level2book has no author
			assertNull(level2book.getAuthor());
		}
	}
	
	/**
	 * Check that we ignore container features as well as containments when
	 * "resolving" features on paste.
	 */
	public void offtest_resolvingContainerFeature_129046() {
		if (writing()) {
			List objects = new ArrayList();

			// create a few branches in level1 (we already have one, level1-2)
			Library level1a = EXTLibraryFactory.eINSTANCE.createLibrary();
			level1a.setName("level1a"); //$NON-NLS-1$
			level1.getBranches().add(level1a);
			Library level1b = EXTLibraryFactory.eINSTANCE.createLibrary();
			level1b.setName("level1b"); //$NON-NLS-1$
			level1.getBranches().add(level1b);
			Library level1c = EXTLibraryFactory.eINSTANCE.createLibrary();
			level1c.setName("level1c"); //$NON-NLS-1$
			level1.getBranches().add(level1c);

			XMLResource xml = (XMLResource) testResource;
			if (xml.getID(level1a) == null) {
				// create some IDs because our resource impl didn't do it for us
				xml.setID(level1a, EcoreUtil.generateUUID());
				xml.setID(level1b, EcoreUtil.generateUUID());
				xml.setID(level1c, EcoreUtil.generateUUID());
			}
			
			// verify that they know their parent branch
			assertSame(level1, level1a.getParentBranch());
			assertSame(level1, level1b.getParentBranch());
			assertSame(level1, level1c.getParentBranch());
			
			// copy level1
			objects.add(level1);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste level1 copy into root3
			Collection eObjects = paste(copyStr, root3, Collections.EMPTY_MAP);
			assertEquals(1, eObjects.size());
			
			Object pasted = eObjects.iterator().next();
			assertTrue(pasted instanceof Library);
			
			// verify that the pasted library knows its container
			Library pastedLibrary = (Library) pasted;
			assertSame(root3, pastedLibrary.getParentBranch());
			
			// get its branches and verify them
			assertEquals(4, pastedLibrary.getBranches().size());
			assertSame(pastedLibrary, (pastedLibrary.getBranches().get(0)).getParentBranch());
			assertSame(pastedLibrary, (pastedLibrary.getBranches().get(1)).getParentBranch());
			assertSame(pastedLibrary, (pastedLibrary.getBranches().get(2)).getParentBranch());
			assertSame(pastedLibrary, (pastedLibrary.getBranches().get(3)).getParentBranch());
			
			// make sure that the original level1 branches are unchanged
			assertSame(level1, level1a.getParentBranch());
			assertSame(level1, level1b.getParentBranch());
			assertSame(level1, level1c.getParentBranch());
		}
	}
	
	/**
	 * Tests that characters like '&amp;' are escaped in the copy resource.
	 */
	public void offtest_escapeURI_218307() {
		if (writing()) {
			List objects = new ArrayList();
	
			String name = "/" + PROJECT_NAME + "/test&escapes.extlibrary"; //$NON-NLS-1$ //$NON-NLS-2$
			testResource.setURI(URI.createPlatformResourceURI(name, true));
			
			// level12 has no branches
			assertTrue(level12.getBranches().size() == 0);
	
			// copy root3
			objects.add(root3);
			String copyStr = copy(objects, Collections.EMPTY_MAP);
			assertNotNull(copyStr);
			assertFalse(copyStr.length() == 0);
			
			// paste into level1
			Collection eObjects = paste(copyStr, level12, Collections.EMPTY_MAP);
			assertEquals(eObjects.size(), objects.size());
	
			// check that the library was copied into the branches containment list
			assertTrue(level12.getBranches().size() == 1);
		}
	}
	
	/**
	 * Tests that the two <code>createClipboardSupport</code> methods will
	 * return the same clipboard helper. The correct way is to call the method
	 * that takes an <code>EObject</code>, but it is possible that clients are
	 * still calling the method that takes an <code>EClass</code>.
	 */
	public void test_createClipboardSupportMethods() {
		if (writing()) {
			IClipboardSupport clipboardSupport = ClipboardUtil
					.createClipboardSupport(level12book.eClass());
			IClipboardSupport clipboardSupport2 = ClipboardUtil
					.createClipboardSupport(level12book);

			assertEquals(clipboardSupport, clipboardSupport2);
		}
	}
}
