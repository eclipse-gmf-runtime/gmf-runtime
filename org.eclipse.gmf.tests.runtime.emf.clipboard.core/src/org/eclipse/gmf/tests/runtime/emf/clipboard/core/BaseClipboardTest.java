package org.eclipse.gmf.tests.runtime.emf.clipboard.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.examples.extlibrary.Book;
import org.eclipse.emf.examples.extlibrary.Library;
import org.eclipse.emf.examples.extlibrary.Writer;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.osgi.framework.Bundle;

/**
 * Sets up a test framework for clipboard tests.
 * 
 * @author Christian Vogt (cvogt)
 */
public abstract class BaseClipboardTest extends TestCase {

	static final Bundle CLIPBOARD_TESTS_BUNDLE =
		Platform.getBundle("org.eclipse.gmf.tests.runtime.emf.clipboard.core"); //$NON-NLS-1$

	protected static final String PROJECT_NAME = "clipboardTests"; //$NON-NLS-1$
	protected static final String RESOURCE_NAME = "/" + PROJECT_NAME + "/logres.extlibrary";  //$NON-NLS-1$//$NON-NLS-2$

	private Transaction tx;
	
	private ChangeDescription lastChange;
	
	protected TransactionalEditingDomain domain;

	protected IProject project;

	protected Resource testResource;

	//
	// Model structure created by setUp():
	//
	// Library root1                     (== root1)
	//  +- Writer level1 writer          (== level1writer)
	//  +- Book level1 book              (== level1book)
	//  +- Library level1                (== level1)
	//  |   +- Writer level1-2 writer    (== level12writer)
	//  |   +- Book level1-2 book        (== level12book)
	//  |   +- Library level1-2          (== level12)
	// Library root2                     (== root2)
	//  +- Writer level2 writer          (== level2writer)
	//  +- Book level2 book              (== level2book)
	// Library root3                     (== root3) 
	//
	protected Library root1;
	protected Writer level1writer;
	protected Book level1book;
	protected Library level1;
	protected Writer level12writer;
	protected Book level12book;
	protected Library level12;
	protected Library root2;
	protected Writer level2writer;
	protected Book level2book;
	protected Library root3;

	/**
	 * Constructor.
	 * 
	 * @param name
	 */
	public BaseClipboardTest(String name) {
		super(name);
	}


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {

		project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		if (!project.exists()) {
			project.create(null);
		}
		
		project.open(null);
	
		domain = GMFEditingDomainFactory.getInstance().createEditingDomain();
		
		tx = ((InternalTransactionalEditingDomain) domain).startTransaction(false, null);
		
		try {
			Resource originalRes = domain.getResourceSet().getResource(
					URI.createURI(
							CLIPBOARD_TESTS_BUNDLE.getEntry(
								"/test_models/clipboard_test.extlibrary") //$NON-NLS-1$
					.toString()), true);
			originalRes.setURI(URI.createPlatformResourceURI(RESOURCE_NAME));
			originalRes.save(Collections.EMPTY_MAP);
			testResource = originalRes;

			// see above for model info
			root1 = (Library)testResource.getContents().get(0);
			level1writer = (Writer)root1.getWriters().get(0);
			level1book = (Book)root1.getBooks().get(0);
			level1 = (Library)root1.getBranches().get(0);
			
			level12writer = (Writer)level1.getWriters().get(0);
			level12book = (Book)level1.getBooks().get(0);
			level12 = (Library)level1.getBranches().get(0);
			
			root2 = (Library)testResource.getContents().get(1);
			level2writer = (Writer)root2.getWriters().get(0);
			level2book = (Book)root2.getBooks().get(0);
			
			root3 = (Library)testResource.getContents().get(2);
		} catch (IOException e) {
			fail("Failed to load test model: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		root1 = null;
		level1writer = null;
		level1book = null;
		level1 = null;
		level12writer = null;
		level12book = null;
		level12 = null;
		root2 = null;
		level2writer = null;
		level2book = null;
		root3 = null;

		if (testResource != null) {
			if (testResource.isLoaded()) {
				testResource.unload();
			}
			
			if (testResource.getResourceSet() != null) {
				testResource.getResourceSet().getResources().remove(testResource);
			}
			testResource = null;
		}
		
		if ((project != null) && project.exists()) {
			project.delete(true, true, null);
		}
		
		project = null;
		domain = null;
	}

	/**
	 * Copy elements to a the clipboard.
	 * 
	 * @param eObjects a collection of {@link EObject}s to be serialized
	 * @param hints a mapping of hints (defined as constants on this class), or
	 *     <code>null</code> to provide no hints
	 * 
	 * @return the serial form of the <code>eObjects</code>
	 */
	protected String copy(Collection objects, Map hints) {
		try {
			return ClipboardUtil.copyElementsToString(
				objects, hints, new NullProgressMonitor());
		} catch (Exception ex) {
			fail("Failed to copy elements to string."); //$NON-NLS-1$
		}
		return null;
	}
	
	/**
	 * Deerializes elements from a string (obtained from the system clipboard)
	 * and pastes them into the specified target element.
	 *
	 * @param string the string containing the elements to be pasted
	 * @param target the element into which the new elements are to be
	 *     pasted (must be of type EObject or Resource)
	 * @param hints a mapping of hints (defined as constants on this class), or
	 *     <code>null</code> to provide no hints
	 * 
	 * @return the newly pasted {@link EObject}s
	 */
	protected Collection paste(final String str, final Object target, final Map hints) {
		assert (target instanceof Resource || target instanceof EObject);

		Collection result = null;
		Transaction pasteTx = null;
		
		try {
			pasteTx = ((InternalTransactionalEditingDomain) domain).startTransaction(false, null);
		} catch (Exception e) {
			fail("Failed to paste elements from string: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		
		try {
			if (target instanceof Resource) {
				result = ClipboardUtil.pasteElementsFromString(
					str, (Resource)target, hints, new NullProgressMonitor());
			} else {
				// else it must be an EObject
				result =  ClipboardUtil.pasteElementsFromString(
					str, (EObject)target, hints, new NullProgressMonitor());
			}
		} catch (Exception ex) {
			fail("Failed to paste elements from string."); //$NON-NLS-1$
		} finally {
			try {
				pasteTx.commit();
			} catch (RollbackException e) {
				fail("Failed to paste elements from string: " + e.getLocalizedMessage()); //$NON-NLS-1$
			}
		}

		return result;
	}
	
	/**
	 * Must be called first in every test method that needs to run in a write
	 * action.  If we are not in a write action, we return <code>false</code>.
	 * However, in this case, we do also re-execute the original test method
	 * inside of a new write action, in which nested execution this method will
	 * return <code>true</code>.  Therefore, the entire test method should be
	 * in an <code>if</code> block conditional on this result.  Following this
	 * <code>if</code> block, it is safe to access the undo interval created
	 * during the test via the {@link #getLastUndo()} method in an
	 * <code>else</code> block.
	 * <p>
	 * Example:
	 * </p>
	 * <pre>
	 *     if (writing()) {
	 *        // ... do stuff in a write action ...
	 *     }
	 * </pre>
	 * 
	 * @return whether we are in a write action or not
	 * 
	 * @see #getLastUndo()
	 */
	protected boolean writing() {
		boolean result = (tx != null);
		
		if (!result) {
			try {
				tx = ((InternalTransactionalEditingDomain) domain).startTransaction(false, null);
			} catch (Exception e) {
				fail("Could not start transaction: " + e.getLocalizedMessage()); //$NON-NLS-1$
			}
			
			try {
				runTest();
			} catch (AssertionFailedError e) {
				tx.rollback();
				throw e;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
			} catch (Throwable t) {
				tx.rollback();
				throw (Error) t;
			} finally {
				if (tx.isActive()) {
					try {
						tx.commit();
					} catch (RollbackException e) {
						fail("Transaction rolled back: " + e.getLocalizedMessage()); //$NON-NLS-1$
					}
				}
			}
				
			lastChange = tx.getChangeDescription();
		}
		
		return result;
	}
	
	/**
	 * Gets the change description created by the test within its
	 * <pre>
	 *     if (writing()) {
	 *        // ... do stuff in a write action ...
	 *     } else {
	 *         ChangeDescription change = getLastChange();
	 *         
	 *         // ... do stuff with the change description ...
	 *     }
	 * </pre>
	 * block.
	 * 
	 * @return the test's change description
	 * 
	 * @see #writing()
	 */
	protected ChangeDescription getLastChange() {
		return lastChange;
	}
}
