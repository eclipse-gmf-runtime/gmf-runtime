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

package org.eclipse.gmf.tests.runtime.diagram.ui.services;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.IProviderPolicy;
import org.eclipse.gmf.runtime.common.core.service.ProviderPriority;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultPaletteProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteService;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteDrawer;
import org.eclipse.gmf.runtime.gef.ui.internal.palette.PaletteStack;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.TestingConfigurationElement;
import org.eclipse.ui.IEditorPart;

/**
 * Tests for the Palette Service.
 * 
 * @author cmahoney
 * 
 */
public class PaletteServiceTests
	extends TestCase {

	static class MyPaletteService
		extends PaletteService {

		/**
		 * Override to allow passing in of the provider, instead of initializing
		 * via the <code>ConfigurationElement</code>.
		 */
		static class ProviderDescriptor
			extends PaletteService.ProviderDescriptor {

			public boolean areActivitiesEnabled = true;

			protected ProviderDescriptor(IProvider provider) {
				super(new TestingConfigurationElement());
				this.provider = provider;
				provider.addProviderChangeListener(this);
			}

			public IProvider getProvider() {
				return provider;
			}

			protected IProviderPolicy getPolicy() {
				return null;
			}

			public boolean provides(IOperation operation) {
				return areActivitiesEnabled;
			}

			public void setActivitiesEnabled(boolean b) {
				areActivitiesEnabled = b;
			}
		}

		protected MyPaletteService() {
			super();
		}

		public void addPaletteProvider(ProviderPriority priority,
				ProviderDescriptor provider) {

			super.addProvider(priority, provider);
		}

		public void removePaletteProvider(Service.ProviderDescriptor provider) {

			super.removeProvider(provider);
		}
	}

	/**
	 * A test palette provider.
	 */
	public static class ProviderA
		extends DefaultPaletteProvider {

		public static String DRAWER_A = "DRAWER_A"; //$NON-NLS-1$

		public static String TOOL_A = "TOOL_A"; //$NON-NLS-1$

		public static String STACK_A = "STACK_A"; //$NON-NLS-1$

		public void contributeToPalette(IEditorPart editor, Object content,
				PaletteRoot root) {

			PaletteDrawer drawerA = new PaletteDrawer(DRAWER_A, DRAWER_A);
			drawerA.add(new PaletteToolEntry(TOOL_A, TOOL_A, null));
			PaletteStack stackA = new PaletteStack(STACK_A, STACK_A, STACK_A,
				null);
			stackA.add(new PaletteToolEntry(TOOL_A, TOOL_A, null));
			drawerA.add(stackA);
			root.add(drawerA);
		}
	}

	/**
	 * A test palette provider.
	 */
	public static class ProviderB
		extends DefaultPaletteProvider {

		public static String DRAWER_B = "DRAWER_B"; //$NON-NLS-1$

		public static String TOOL_B = "TOOL_B"; //$NON-NLS-1$

		public void contributeToPalette(IEditorPart editor, Object content,
				PaletteRoot root) {

			PaletteDrawer drawerB = new PaletteDrawer(DRAWER_B, DRAWER_B);
			drawerB.add(new PaletteToolEntry(TOOL_B, TOOL_B, null));
			root.add(drawerB);

			PaletteDrawer drawerA = (PaletteDrawer) findChildPaletteEntry(root,
				ProviderA.DRAWER_A);
			drawerA.add(new PaletteToolEntry(TOOL_B, TOOL_B, null));
			PaletteStack stackA = (PaletteStack) findChildPaletteEntry(drawerA,
				ProviderA.STACK_A);
			stackA.add(new PaletteToolEntry(TOOL_B, TOOL_B, null));
		}
	}

	/**
	 * A test palette provider.
	 */
	public static class ProviderC
		extends DefaultPaletteProvider {

		public static String DRAWER_C = "DRAWER_C"; //$NON-NLS-1$

		public static String TOOL_C = "TOOL_C"; //$NON-NLS-1$

		public void contributeToPalette(IEditorPart editor, Object content,
				PaletteRoot root) {

			PaletteDrawer drawerC = new PaletteDrawer(DRAWER_C, DRAWER_C);
			drawerC.add(new PaletteToolEntry(TOOL_C, TOOL_C, null));
			root.add(drawerC);

			PaletteDrawer drawerA = (PaletteDrawer) findChildPaletteEntry(root,
				ProviderA.DRAWER_A);
			drawerA.add(new PaletteToolEntry(TOOL_C, TOOL_C, null));
			PaletteStack stackA = (PaletteStack) findChildPaletteEntry(drawerA,
				ProviderA.STACK_A);
			stackA.add(new PaletteToolEntry(TOOL_C, TOOL_C, null));
		}
	}

	/**
	 * A test palette provider.
	 */
	public static class ProviderD
		extends DefaultPaletteProvider {

		public static String DRAWER_D = "DRAWER_D"; //$NON-NLS-1$

		public static String TOOL_D = "TOOL_D"; //$NON-NLS-1$

		public void contributeToPalette(IEditorPart editor, Object content,
				PaletteRoot root) {

			PaletteDrawer drawerD = new PaletteDrawer(DRAWER_D, DRAWER_D);
			drawerD.add(new PaletteToolEntry(TOOL_D, TOOL_D, null));
			root.add(drawerD);

			PaletteDrawer drawerA = (PaletteDrawer) findChildPaletteEntry(root,
				ProviderA.DRAWER_A);
			drawerA.add(new PaletteToolEntry(TOOL_D, TOOL_D, null));
			PaletteDrawer drawerB = (PaletteDrawer) findChildPaletteEntry(root,
				ProviderB.DRAWER_B);
			if (drawerB != null) {
				drawerB.add(new PaletteToolEntry(TOOL_D, TOOL_D, null));
			}
			PaletteStack stackA = (PaletteStack) findChildPaletteEntry(drawerA,
				ProviderA.STACK_A);
			stackA.add(new PaletteToolEntry(TOOL_D, TOOL_D, null));
		}
	}

	public PaletteServiceTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(PaletteServiceTests.class);
	}

	/**
	 * Finds a palette entry starting from the given container and using the
	 * given path
	 * 
	 * @param paletteRoot
	 * @param path
	 * @return the entry or <code>null</code> if not found
	 */
	private static PaletteEntry findChildPaletteEntry(
			PaletteContainer container, String childId) {

		Iterator entries = container.getChildren().iterator();
		while (entries.hasNext()) {
			PaletteEntry entry = (PaletteEntry) entries.next();
			if (entry.getId().equals(childId)) {
				return entry;
			}
		}
		return null;
	}

	MyPaletteService paletteService;

	protected void setUp()
		throws Exception {
		super.setUp();
		setPaletteService(new MyPaletteService());
	}

	public MyPaletteService getPaletteService() {
		return paletteService;
	}

	public void setPaletteService(MyPaletteService service) {
		paletteService = service;
	}

	/**
	 * Tests that the UI contributions made by a provider affected by a
	 * capability will be shown/hidden as the capability is enabled/disabled.
	 * 
	 * @throws Exception
	 */
	public void testCapabilityFiltering()
		throws Exception {

		// set up providerA
		MyPaletteService.ProviderDescriptor descriptorA = new MyPaletteService.ProviderDescriptor(
			new ProviderA());
		getPaletteService().addPaletteProvider(ProviderPriority.LOW,
			descriptorA);

		// set up providerB
		MyPaletteService.ProviderDescriptor descriptorB = new MyPaletteService.ProviderDescriptor(
			new ProviderB());
		getPaletteService().addPaletteProvider(ProviderPriority.MEDIUM,
			descriptorB);

		// set up providerC
		MyPaletteService.ProviderDescriptor descriptorC = new MyPaletteService.ProviderDescriptor(
			new ProviderC());
		getPaletteService().addPaletteProvider(ProviderPriority.HIGH,
			descriptorC);

		// set up providerD
		MyPaletteService.ProviderDescriptor descriptorD = new MyPaletteService.ProviderDescriptor(
			new ProviderD());
		getPaletteService().addPaletteProvider(ProviderPriority.HIGHEST,
			descriptorD);

		// test service
		DiagramDocumentEditor editor = new DiagramDocumentEditor(true);

		PaletteRoot paletteRoot = getPaletteService().createPalette(editor,
			"DUMMY CONTENT"); //$NON-NLS-1$

		PaletteDrawer drawerA = (PaletteDrawer) paletteRoot.getChildren()
			.get(1);
		PaletteEntry toolA = (PaletteEntry) drawerA.getChildren().get(0);
		PaletteStack stackA = (PaletteStack) drawerA.getChildren().get(1);
		PaletteEntry stackAToolA = (PaletteEntry) stackA.getChildren().get(0);
		PaletteEntry stackAToolC = (PaletteEntry) stackA.getChildren().get(2);

		validatePaletteEntries(paletteRoot, true, true);

		stackA.setActiveEntry(stackAToolC);

		descriptorB.setActivitiesEnabled(false);
		getPaletteService().updatePalette(paletteRoot, editor, "DUMMY CONTENT"); //$NON-NLS-1$
		validatePaletteEntries(paletteRoot, false, true);

		descriptorB.setActivitiesEnabled(true);
		descriptorD.setActivitiesEnabled(false);
		getPaletteService().updatePalette(paletteRoot, editor, "DUMMY CONTENT"); //$NON-NLS-1$
		validatePaletteEntries(paletteRoot, true, false);

		descriptorB.setActivitiesEnabled(false);
		descriptorD.setActivitiesEnabled(true);
		getPaletteService().updatePalette(paletteRoot, editor, "DUMMY CONTENT"); //$NON-NLS-1$
		validatePaletteEntries(paletteRoot, false, true);

		descriptorD.setActivitiesEnabled(false);
		getPaletteService().updatePalette(paletteRoot, editor, "DUMMY CONTENT"); //$NON-NLS-1$
		validatePaletteEntries(paletteRoot, false, false);

		// Verify that the instances of the entries did not change.
		assertEquals(drawerA, paletteRoot.getChildren().get(1));
		assertEquals(toolA, drawerA.getChildren().get(0));
		assertEquals(stackA, drawerA.getChildren().get(1));
		assertEquals(stackAToolA, stackA.getChildren().get(0));
		assertEquals(stackAToolC, stackA.getActiveEntry());

	}

	/**
	 * Validates many of the palette entries.
	 * 
	 * @param providerBEnabled
	 *            true if provider B is enabled
	 * @param providerDEnabled
	 *            true if provider D is enabled
	 */
	private void validatePaletteEntries(PaletteRoot paletteRoot,
			boolean providerBEnabled, boolean providerDEnabled) {

		PaletteDrawer drawerA = null;
		PaletteDrawer drawerB = null;
		PaletteStack stackA;

		// verify drawers
		int index = 1; // skip default drawer
		drawerA = (PaletteDrawer) paletteRoot.getChildren().get(index++);
		assertEquals(ProviderA.DRAWER_A, drawerA.getId());
		if (providerBEnabled) {
			drawerB = (PaletteDrawer) paletteRoot.getChildren().get(index++);
			assertEquals(ProviderB.DRAWER_B, drawerB.getId());
		}
		assertEquals(ProviderC.DRAWER_C, (((PaletteDrawer) paletteRoot
			.getChildren().get(index++)).getId()));
		if (providerDEnabled) {
			assertEquals(ProviderD.DRAWER_D, (((PaletteDrawer) paletteRoot
				.getChildren().get(index++)).getId()));
		}

		// verify drawer A tools
		index = 0;
		assertEquals(ProviderA.TOOL_A, (((PaletteEntry) drawerA.getChildren()
			.get(index++)).getId()));
		stackA = (PaletteStack) drawerA.getChildren().get(index++);
		assertEquals(ProviderA.STACK_A, stackA.getId());
		if (providerBEnabled) {
			assertEquals(ProviderB.TOOL_B, (((PaletteEntry) drawerA
				.getChildren().get(index++)).getId()));
		}
		assertEquals(ProviderC.TOOL_C, (((PaletteEntry) drawerA.getChildren()
			.get(index++)).getId()));
		if (providerDEnabled) {
			assertEquals(ProviderD.TOOL_D, (((PaletteEntry) drawerA
				.getChildren().get(index++)).getId()));
		}

		// verify stack A tools
		index = 0;
		assertEquals(ProviderA.TOOL_A, (((PaletteEntry) stackA.getChildren()
			.get(index++)).getId()));
		if (providerBEnabled) {
			assertEquals(ProviderB.TOOL_B, (((PaletteEntry) stackA
				.getChildren().get(index++)).getId()));
		}
		assertEquals(ProviderC.TOOL_C, (((PaletteEntry) stackA.getChildren()
			.get(index++)).getId()));
		if (providerDEnabled) {
			assertEquals(ProviderD.TOOL_D, (((PaletteEntry) stackA
				.getChildren().get(index++)).getId()));
		}

		// verify drawer B tools
		if (providerBEnabled) {
			index = 0;
			assertEquals(ProviderB.TOOL_B, (((PaletteEntry) drawerB
				.getChildren().get(index++)).getId()));
			if (providerDEnabled) {
				assertEquals(ProviderD.TOOL_D, (((PaletteEntry) drawerB
					.getChildren().get(index++)).getId()));
			}

		}
	}

	// /**
	// * Prints out the palette entries to the console. Used for debugging.
	// *
	// * @param paletteContainer
	// */
	// private void printPalette(PaletteContainer paletteContainer, String
	// prefix) {
	// for (Iterator iter = paletteContainer.getChildren().iterator(); iter
	// .hasNext();) {
	// PaletteEntry entry = (PaletteEntry) iter.next();
	// System.out.println(prefix + entry.getLabel());
	// if (entry instanceof PaletteContainer) {
	// printPalette((PaletteContainer) entry, prefix + " "); //$NON-NLS-1$
	// }
	// }
	// }

}
