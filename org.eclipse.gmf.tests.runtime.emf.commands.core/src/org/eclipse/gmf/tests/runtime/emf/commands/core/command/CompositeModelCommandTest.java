/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.commands.core.command;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.examples.extlibrary.EXTLibraryPackage;
import org.eclipse.emf.examples.extlibrary.Library;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.compatibility.AbstractCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;

/**
 * @author khussey
 *  
 */
public class CompositeModelCommandTest
	extends TestCase {

	private Resource resource;
	private Library library;
	private MyListener listener;
	
	/**
	 * Command that checks that a write action is already open when it is
	 * executed. Used to verify that the CompositeModelCommand opens a write
	 * action before executing its composed commands.
	 */
	protected class MyAbstractCommand
		extends AbstractCommand {

		private AbstractModelCommand myModelCommand = new AbstractModelCommand(
			getLabel(), null) {

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {
				library.setName(getLabel());
				return null;
			}
		};

		public MyAbstractCommand(String label) {
			super(label);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			assertTrue(
				"Expect write action to have been opened by the containing CompositeModelCommand.", OperationUtil.canWrite()); //$NON-NLS-1$
			myModelCommand.execute(progressMonitor);
			
			return newOKCommandResult();
		}

		public AbstractModelCommand getModelCommand() {
			return myModelCommand;
		}
	};
	
	protected class MyListener extends ResourceSetListenerImpl {
		public List notifications;
		
		public boolean isPostcommitOnly() {
			return true;
		}
		
		public NotificationFilter getFilter() {
			return NotificationFilter.createNotifierTypeFilter(
					EXTLibraryPackage.eINSTANCE.getLibrary());
		}
		
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			notifications = new java.util.ArrayList(event.getNotifications());
		}
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CompositeModelCommandTest.class);
	}

	public CompositeModelCommandTest(String name) {
		super(name);
	}

	protected void setUp() {
		resource = ResourceUtil.create(
				null, EXTLibraryPackage.eINSTANCE.getLibrary());
		library = (Library) ResourceUtil.getFirstRoot(resource);
		
		listener = new MyListener();
		MEditingDomain.INSTANCE.addResourceSetListener(listener);
	}
	
	protected void tearDown() {
		MEditingDomain.INSTANCE.removeResourceSetListener(listener);
		resource.unload();
		MEditingDomain.INSTANCE.getResourceSet().getResources().remove(resource);
		library = null;
	}

	public void test_execute() {
		
		MyAbstractCommand modelCommand1 = new MyAbstractCommand("1"); //$NON-NLS-1$
		MyAbstractCommand modelCommand2 = new MyAbstractCommand("2"); //$NON-NLS-1$

		CompositeModelCommand compositeCommand = new CompositeModelCommand("Composite Model Command Test"); //$NON-NLS-1$
		compositeCommand.compose(modelCommand1);
		compositeCommand.compose(modelCommand2);
		
		compositeCommand.execute(new NullProgressMonitor());
		
		assertNotNull(listener.notifications);
		assertEquals(2, listener.notifications.size());
		assertEquals("1", ((Notification) listener.notifications.get(0)).getNewValue()); //$NON-NLS-1$
		assertEquals("2", ((Notification) listener.notifications.get(1)).getNewValue()); //$NON-NLS-1$
	}

	public void test_redo() {
		
		MyAbstractCommand modelCommand1 = new MyAbstractCommand("1"); //$NON-NLS-1$
		MyAbstractCommand modelCommand2 = new MyAbstractCommand("2"); //$NON-NLS-1$

		CompositeModelCommand compositeCommand = new CompositeModelCommand("Composite Model Command Test"); //$NON-NLS-1$
		compositeCommand.compose(modelCommand1);
		compositeCommand.compose(modelCommand2);
		
		compositeCommand.execute(new NullProgressMonitor());
		compositeCommand.undo();
		
		try {
			compositeCommand.redo();
		} catch (Exception e) {
			fail();
		}
		
		assertNotNull(listener.notifications);
		assertEquals(2, listener.notifications.size());
		assertEquals("1", ((Notification) listener.notifications.get(0)).getNewValue()); //$NON-NLS-1$
		assertEquals("2", ((Notification) listener.notifications.get(1)).getNewValue()); //$NON-NLS-1$
	}

	public void test_undo() {
		
		MyAbstractCommand modelCommand1 = new MyAbstractCommand("1"); //$NON-NLS-1$
		MyAbstractCommand modelCommand2 = new MyAbstractCommand("2"); //$NON-NLS-1$

		CompositeModelCommand compositeCommand = new CompositeModelCommand("Composite Model Command Test"); //$NON-NLS-1$
		compositeCommand.compose(modelCommand1);
		compositeCommand.compose(modelCommand2);
		
		compositeCommand.execute(new NullProgressMonitor());
		
		try {
			compositeCommand.undo();
		} catch (Exception e) {
			fail();
		}
		
		assertNotNull(listener.notifications);
		assertEquals(2, listener.notifications.size());
		assertEquals("1", ((Notification) listener.notifications.get(0)).getNewValue()); //$NON-NLS-1$
		assertNull(((Notification) listener.notifications.get(1)).getNewValue());
	}

}