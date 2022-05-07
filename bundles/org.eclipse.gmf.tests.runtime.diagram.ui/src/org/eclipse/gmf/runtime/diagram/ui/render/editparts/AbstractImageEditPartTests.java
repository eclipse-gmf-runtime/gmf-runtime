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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.diagram.core.DiagramEditingDomainFactory;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.DiagramCreator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import junit.framework.TestCase;


/**
 * @author sshaw
 *
 * Test class for the AbstractEditPartImage class.
 */
abstract public class AbstractImageEditPartTests 
extends TestCase {
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	private Node node;

	public Node getNode() {
		return node;
	}

	private TransactionalEditingDomain editingDomain;
	
	/**
	 * Sets up the fixture, for example, open a network connection.
	 * This method is called before a test is executed.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		editingDomain = DiagramEditingDomainFactory.getInstance().createEditingDomain();
		final Diagram dgrm = DiagramCreator.createEmptyDiagram(getPreferenceHint(),
			editingDomain);
		
        AbstractEMFOperation operation = new AbstractEMFOperation(
            editingDomain, "") { //$NON-NLS-1$

            protected IStatus doExecute(IProgressMonitor monitor,
                    IAdaptable info)
                throws ExecutionException {
                
		Resource resource = editingDomain
			.createResource("null:/org.eclipse.gmf.tests.runtime.diagram.ui"); //$NON-NLS-1$
		resource.getContents().add(dgrm);
                
                return Status.OK_STATUS;
            };
        };
        try {
            OperationHistoryFactory.getOperationHistory().execute(operation,
                    new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            assertFalse(false);
        }
        
		node = createNode(dgrm);
	}
	
	/**
	 * @return
	 */
	protected PreferencesHint getPreferenceHint() {
		return PreferencesHint.USE_DEFAULTS;
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected Node createNode(final Diagram diagram) {

		final List list = new ArrayList(1);
		
		AbstractEMFOperation operation = new AbstractEMFOperation(
			editingDomain, "") { //$NON-NLS-1$

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				
				Node note1 = ViewService.createNode(diagram,
					ViewType.NOTE, getPreferenceHint());
				assertNotNull("Note1 creation failed", note1); //$NON-NLS-1$
				list.add(note1);
				
				return Status.OK_STATUS;
			};
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(operation,
					new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
			assertFalse(false);
		}

		return (Node)list.get(0);
	}
	
	abstract public List getFixtures();
	
	public void test_regenerateImageFromSource() {
		List fixtures = getFixtures();
		ListIterator li = fixtures.listIterator();
		int testno = 1;
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof AbstractImageEditPart) {
				AbstractImageEditPart fixture = (AbstractImageEditPart)obj;
				
				RenderedImage renderedImage = fixture.regenerateImageFromSource();
				verifyRenderedImage(renderedImage, testno++);
			}
		}
	}
	
	/**
	 * @param renderedImage
	 */
	protected void verifyRenderedImage(RenderedImage renderedImage, int testno) {
		assertTrue("renderedImage is null in testno " + (Integer.valueOf(testno)).toString(),//$NON-NLS-1$
			renderedImage != null);
		
		Image swtImage = renderedImage.getSWTImage();
		assertTrue("swtImage is null in testno " + (Integer.valueOf(testno)).toString(), //$NON-NLS-1$
			swtImage != null);
		
		// ensure the protection image isn't being returned.
		Rectangle rect = swtImage.getBounds();
		assertTrue("swtImage is not correct size in testno " + (Integer.valueOf(testno)).toString(),//$NON-NLS-1$
			rect.width > 10 && rect.height > 10);
	}

	public void test_getRenderedImage() {
		List fixtures = getFixtures();
		ListIterator li = fixtures.listIterator();
		int testno = 1;
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof AbstractImageEditPart) {
				AbstractImageEditPart fixture = (AbstractImageEditPart)obj;
				
				RenderedImage renderedImage = fixture.getRenderedImage();
				verifyRenderedImage(renderedImage, testno++);
			}
		}
	}
}
