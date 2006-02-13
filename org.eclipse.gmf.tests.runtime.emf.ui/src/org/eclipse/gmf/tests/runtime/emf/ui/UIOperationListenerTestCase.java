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

package org.eclipse.gmf.tests.runtime.emf.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.UML2Package;

import org.eclipse.gmf.runtime.emf.core.EditingDomain;
import org.eclipse.gmf.runtime.emf.core.IOperationEvent;
import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
import org.eclipse.gmf.runtime.emf.ui.UIOperationListener;

/**
 * Tests for Operation Listener
 * 
 * @author mgoyal
 * @deprecated Tests obsolete UIOperationListener.
 */
public class UIOperationListenerTestCase
	extends BaseTestCase {

	/**
	 * Base Constructor.
	 * 
	 * @param name
	 */
	public UIOperationListenerTestCase(String name) {
		super(name);
	}

	private static final String CLASS_NAME = "MyTestClass"; //$NON-NLS-1$

	private Resource res;

	private org.eclipse.uml2.Model model;
	private EditingDomain domain = MSLEditingDomain.INSTANCE;
	private TestOperationListener listener;

	protected void setUp()
		throws Exception {

		super.setUp();

		res = ResourceUtil.create("\\tmp\\operationUtilTest.emx", //$NON-NLS-1$
			UML2Package.eINSTANCE.getModel(), 0);

		model = (Model) ResourceUtil.getFirstRoot(res);
		listener = new TestOperationListener();
		domain.addOperationListener(listener);
	}

	protected void tearDown()
		throws Exception {
		model = null;

		domain.removeOperationListener(listener);
		listener = null;

		ResourceSet rset = res.getResourceSet();
		ResourceUtil.unload(res);
		rset.getResources().remove(res);

		super.tearDown();
	}

	/**
	 * @author mgoyal
	 *
	 */
	private class ModifyOperation extends ResourceSetModifyOperation implements Runnable {

		private boolean bSuccessful = false;
		/**
		 * @param label
		 */
		public ModifyOperation() {
			super("Test Modify Operation"); //$NON-NLS-1$
		}
		
		/**
		 * @see org.eclipse.gmf.runtime.emf.core.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
		 */
		protected void execute(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
			// change the model
			((NamedElement) model.createOwnedMember(
				UML2Package.eINSTANCE.getClass_())).setName(CLASS_NAME);
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				domain.run(this, new NullProgressMonitor());
				bSuccessful = true;
			} catch (InvocationTargetException e) {
				bSuccessful = false;
			} catch (InterruptedException e) {
				bSuccessful = false;
			}
		}
		
		public boolean isSuccessful() {
			return bSuccessful;
		}
	}
	
	private class TestOperationListener extends UIOperationListener {
		public Thread doneThread;
		public Thread undoneThread;
		public Thread redoneThread;
		/**
		 * @see org.eclipse.gmf.runtime.emf.core.OperationListener#done(org.eclipse.gmf.runtime.emf.core.IOperationEvent)
		 */
		public void doneOnUI(IOperationEvent event) {
			doneThread = Thread.currentThread();
			super.doneOnUI(event);
		}
		
		
		/**
		 * @see org.eclipse.gmf.runtime.emf.core.OperationListener#undone(org.eclipse.gmf.runtime.emf.core.IOperationEvent)
		 */
		public void undoneOnUI(IOperationEvent event) {
			undoneThread = Thread.currentThread();
			super.undoneOnUI(event);
		}
		
		
		/**
		 * @see org.eclipse.gmf.runtime.emf.core.OperationListener#redone(org.eclipse.gmf.runtime.emf.core.IOperationEvent)
		 */
		public void redoneOnUI(IOperationEvent event) {
			redoneThread = Thread.currentThread();
			super.redoneOnUI(event);
		}
		
		public void reset() {
			undoneThread = null;
			doneThread = null;
			redoneThread = null;
		}
	}
	
	/**
	 * Tests if the operation listener was notified on the right thread.
	 */
	public void test_OperationListenerNotified() {
		ModifyOperation operation = new ModifyOperation();
		Thread thread = new Thread(operation);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			assertTrue(false);
		}

		final boolean[] timedOut = new boolean[1];
		timedOut[0] = false;
		Display.getCurrent().timerExec(1000, new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				timedOut[0] = true;
			}
		});
		
		while(Display.getCurrent().readAndDispatch()) {
			if(timedOut[0])
				break;
		}
		
		assertTrue(listener.doneThread == Display.getDefault().getThread());
		assertTrue(listener.undoneThread == null);
		assertTrue(listener.redoneThread == null);
		
		listener.reset();
	}
}
