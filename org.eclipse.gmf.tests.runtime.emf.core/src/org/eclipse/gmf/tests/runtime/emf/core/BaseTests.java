/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.osgi.framework.Bundle;


/**
 * Base TestCase for emf core tests.
 */
public class BaseTests
	extends TestCase {
	public static final Bundle BUNDLE =
		Platform.getBundle("org.eclipse.gmf.tests.runtime.emf.core"); //$NON-NLS-1$
	
	protected IProject project;
	protected TransactionalEditingDomain domain;
	protected Resource testEcoreResource;
	protected EPackage ecoreRoot;
	protected Resource testNotationResource;
	protected Diagram notationRoot;
	
	protected static final String PROJECT_NAME = "emfcoretests"; //$NON-NLS-1$
	protected static final String ECORE_RESOURCE_NAME = "/" + PROJECT_NAME + "/emfcoretests.ecore";  //$NON-NLS-1$//$NON-NLS-2$
	protected static final String NOTATION_RESOURCE_NAME = "/" + PROJECT_NAME + "/emfcorenotationtests.xmi";  //$NON-NLS-1$//$NON-NLS-2$

	public BaseTests() {
		super();
	}
	
	public BaseTests(String name) {
		super(name);
	}
	
	//
	// Test configuration methods
	//
	
	protected void setUp()
		throws Exception {
		
		boolean assertsEnabled = false;
		assert (assertsEnabled = true) == true;
		if (!assertsEnabled) {
		 fail("Asserts must be enabled for this test case. Add the '-ea' VM argument to proceed.");//$NON-NLS-1$
		}
		
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		if (!project.exists()) {
			project.create(null);
		}
		
		project.open(null);
	
		domain = createEditingDomain();
		
		AbstractEMFOperation op = new AbstractEMFOperation(domain, "") { //$NON-NLS-1$
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {
				try {
					Resource original = domain.loadResource(URI.createURI(
							BUNDLE.getEntry("/test_models/test_model.ecore") //$NON-NLS-1$
									.toString()).toString());

					original.setURI(URI.createPlatformResourceURI(
							NOTATION_RESOURCE_NAME, true));
					original.save(Collections.EMPTY_MAP);
					testEcoreResource = original;
					ecoreRoot = (EPackage) find(testEcoreResource, "root"); //$NON-NLS-1$
					
					original = domain.loadResource(URI.createURI(
							BUNDLE.getEntry("/test_models/test_notation_model.xmi") //$NON-NLS-1$
									.toString()).toString());

					original.setURI(URI.createPlatformResourceURI(
							NOTATION_RESOURCE_NAME, true));
					original.save(Collections.EMPTY_MAP);
					testNotationResource = original;
					notationRoot = (Diagram) find(testNotationResource, "root"); //$NON-NLS-1$
				} catch (IOException e) {
					fail("Failed to load test model: " + e.getLocalizedMessage()); //$NON-NLS-1$
				}

				return Status.OK_STATUS;
			}
		};
		
		try {
			op.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			fail("Failed to load test model: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
	}
	
	/** May be overridden by subclasses to create non-default editing domains. */
	protected TransactionalEditingDomain createEditingDomain() {
		return GMFEditingDomainFactory.getInstance().createEditingDomain();
	}

	protected void tearDown()
		throws Exception {
		
		ecoreRoot = null;
		if (testEcoreResource != null) {
			if (testEcoreResource.isLoaded()) {
				testEcoreResource.unload();
			}
			
			if (testEcoreResource.getResourceSet() != null) {
				testEcoreResource.getResourceSet().getResources().remove(testEcoreResource);
			}
			testEcoreResource = null;
		}
		
		if ((project != null) && project.exists()) {
			project.delete(true, true, null);
		}
		
		project = null;
		domain = null;
	}

	//
	// Other framework methods
	//
	
	protected Resource createTestResource(String name) {
		Resource result = null;
		
		try {
			InputStream input =
				BUNDLE.getEntry("/test_models/" + name).openStream(); //$NON-NLS-1$
			
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path(PROJECT_NAME + '/' + name));
			file.create(input, true, null);
			
			result = domain.createResource(
				URI.createPlatformResourceURI(file.getFullPath().toString(), true).toString());
		} catch (Exception e) {
			fail("Unexpected exception creating test resource: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		
		return result;
	}
	
	/**
	 * Records a failure due to an exception that should not have been thrown.
	 * 
	 * @param e the exception
	 */
	protected void fail(Exception e) {
		e.printStackTrace();
		fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
	}
	
	/**
	 * Asserts that we can find an object having the specified name.
	 * 
	 * @param name the name to seek
	 * 
	 * @see #find(String)
	 */
	protected void assertFound(String name) {
		assertNotNull("Did not find " + name, find(testEcoreResource, name)); //$NON-NLS-1$
	}
	
	/**
	 * Asserts that we can find an object having the specified name, relative
	 * to the specified starting object.
	 * 
	 * @param start the object from which to start looking (to which the
	 *     <code>name</code> is relative).  This can be a resource or an
	 *     element
	 * @param name the name to seek
	 * 
	 * @see #find(Object, String)
	 */
	protected void assertFound(Object start, String name) {
		assertNotNull("Did not find " + name, find(testEcoreResource, name)); //$NON-NLS-1$
	}
	
	/**
	 * Asserts that we cannot find an object having the specified name.
	 * 
	 * @param name the name to (not) seek
	 * 
	 * @see #find(String)
	 */
	protected void assertNotFound(String name) {
		assertNull("Found " + name, find(testEcoreResource, name)); //$NON-NLS-1$
	}
	
	/**
	 * Asserts that we cannot find an object having the specified name, relative
	 * to the specified starting object.
	 * 
	 * @param start the object from which to start looking (to which the
	 *     <code>name</code> is relative).  This can be a resource or an
	 *     element
	 * @param name the name to (not) seek
	 * 
	 * @see #find(Object, String)
	 */
	protected void assertNotFound(Object start, String name) {
		assertNull("Found " + name, find(testEcoreResource, name)); //$NON-NLS-1$
	}
	
	/**
	 * Finds the object in the test model having the specified qualified name,
	 * starting from some object.
	 * 
	 * @param object the starting object (resource or element)
	 * @param qname a slash-delimited qualified name, relative to the
	 *     provided <code>object</code>
	 * @return the matching object, or <code>null</code> if not found
	 */
	protected EObject find(Object start, String qname) {
		EObject result = null;
		Object current = start;
		
		String[] names = tokenize(qname);
		
		for (int i = 0; (current != null) && (i < names.length); i++) {
			String name = names[i];
			result = null;
			
			for (EObject child : getContents(current)) {
				
				if (name.equals(getName(child))) {
					result = child;
					break;
				}
			}
			
			current = result;
		}
		
		return result;
	}

	/**
	 * Gets the name of a library object.
	 * 
	 * @param object the object
	 * @return its name
	 */
	private String getName(EObject object) {
		
		if (object instanceof ENamedElement) {
			return ((ENamedElement) object).getName();
		} else if (object instanceof Diagram) {
			return ((Diagram) object).getName();
		}
		return null;
	}
	
	/**
	 * Gets the contents of an object.
	 * 
	 * @param object an object, which may be a resource or an element
	 * @return its immediate contents (children)
	 */
	private List<EObject> getContents(Object object) {
		if (object instanceof EObject) {
			return ((EObject) object).eContents();
		} else if (object instanceof Resource) {
			return ((Resource) object).getContents();
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * Tokenizes a qualified name on the slashes.
	 * 
	 * @param qname a qualified name
	 * @return the parts between the slashes
	 */
	private String[] tokenize(String qname) {
		return qname.split("/"); //$NON-NLS-1$
	}
}
