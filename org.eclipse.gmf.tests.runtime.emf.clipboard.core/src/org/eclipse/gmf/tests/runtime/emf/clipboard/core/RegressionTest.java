package org.eclipse.gmf.tests.runtime.emf.clipboard.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;


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
	public void test_copyRoot_bugzilla107880() {
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
	public void test_copyPasteRoot_bugzilla107880() {
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
	public void test_copyPasteRootToItself_bugzilla107880() {
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
	public void test_copyPasteMultipleRoots_bugzilla107880() {
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
	public void test_pasteRootToInvalidTarget_bugzilla107880() {
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
	public void test_pasteToResource_bugzilla107880() {
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
	public void test_pasteIntoNonContainmentFeature() {
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
}
