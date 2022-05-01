/******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.DiagramEditingDomainFactory;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class DiagramEditingDomainTestCase
	extends TestCase {

	EClass eCls;
	
	public void testDiagramEventBrokerAsSpecialListener() {
		final TransactionalEditingDomain domain = DiagramEditingDomainFactory.getInstance().createEditingDomain();
		final Resource r = domain.getResourceSet().createResource(URI.createURI("file:///foo.logic2")); //$NON-NLS-1$
		eCls = EcoreFactory.eINSTANCE.createEClass();
		eCls.setName(""); //$NON-NLS-1$
		
		// Set up the resource contents.
		try {
			new AbstractTransactionalCommand(domain, "Setup", null) { //$NON-NLS-1$
				protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
					
					r.getContents().add(eCls);
					
					return CommandResult.newOKCommandResult();
				}
			}.execute(new NullProgressMonitor(),null);
		} catch (ExecutionException e) {
			fail();
		}
		
		DiagramEventBroker.startListening(domain);
		DiagramEventBroker.getInstance(domain).addNotificationListener(eCls, new NotificationListener() {
			public void notifyChanged(Notification notification) {
				if (notification.getNotifier() == eCls && notification.getFeature() == EcorePackage.eINSTANCE.getENamedElement_Name()) {
					try {
						new AbstractTransactionalCommand(domain, "Add Attribute", null) { //$NON-NLS-1$
							protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
								throws ExecutionException {
								
								eCls.getEStructuralFeatures().add(EcoreFactory.eINSTANCE.createEAttribute());
								
								return CommandResult.newOKCommandResult();
							}
							
						}.execute(new NullProgressMonitor(),null);
					} catch (ExecutionException e) {
						fail();
					}
				}
			}
		});
		
		AbstractTransactionalCommand cmd = new AbstractTransactionalCommand(domain, "Set Name", null) { //$NON-NLS-1$
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
				eCls.setName("foo"); //$NON-NLS-1$
				
				return CommandResult.newOKCommandResult();
			}
		};
		
		final boolean[] regularListenerWasCalled = new boolean[1];
		final boolean[] notificationsWereEmpty = new boolean[1];
		regularListenerWasCalled[0] = false;
		notificationsWereEmpty[0] = true;
		
		domain.addResourceSetListener(new ResourceSetListenerImpl() {
			public boolean isPostcommitOnly() {
				return true;
			}
			
			public void resourceSetChanged(ResourceSetChangeEvent event) {
				regularListenerWasCalled[0] = true;
				notificationsWereEmpty[0] = event.getNotifications().isEmpty();
			}
		});
		
		try {
			cmd.execute(new NullProgressMonitor(),null);
			cmd.undo(new NullProgressMonitor(),null);
			cmd.redo(new NullProgressMonitor(), null);
			cmd.undo(new NullProgressMonitor(),null);
			cmd.redo(new NullProgressMonitor(),null);
		} catch (ExecutionException e) {
			fail();
		}

		assertSame(eCls.eResource(),r);
		assertEquals(3,eCls.getEStructuralFeatures().size());
		assertTrue(regularListenerWasCalled[0]);
		assertFalse(notificationsWereEmpty[0]);
	}

	public static Test suite() {
		return new TestSuite(DiagramEditingDomainTestCase.class);
	}
}
