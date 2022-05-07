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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.emf.workspace.ResourceUndoContext;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.NotationFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Tests the GMF editing domain factory.
 * 
 * @author ldamus
 */
public class GMFEditingDomainFactoryTests extends BaseTests {

	private DefaultOperationHistory history;

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(GMFEditingDomainFactoryTests.class,
				"GMFEditingDomainFactoryTests Test Suite"); //$NON-NLS-1$
	}

	@Override
	protected void setUp() throws Exception {
		history = new DefaultOperationHistory();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		history = null;
	}

	@Override
	protected TransactionalEditingDomain createEditingDomain() {
		return GMFEditingDomainFactory.getInstance().createEditingDomain(
				history);
	}

	/**
	 * Tests the resource undo policy for the
	 * <code>GMFEditingDomainFactory</code>. <code>ResourceUndoContext</code>s
	 * should not be added to operations because of notifications on transient
	 * features. These are considered as not modifying the resource.
	 */
	public void test_transientNotifications() {
		IUndoContext context = new ResourceUndoContext(domain,
				testNotationResource);
		IUndoableOperation op = new AbstractEMFOperation(domain,
				"transientNotifications") { //$NON-NLS-1$
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {

				notationRoot.getTransientChildren().add(
						NotationFactory.eINSTANCE.createNode());

				return Status.OK_STATUS;
			}
		};

		try {
			history.execute(op, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}

		assertFalse("Resource should not have context", op.hasContext(context));
	}
}
