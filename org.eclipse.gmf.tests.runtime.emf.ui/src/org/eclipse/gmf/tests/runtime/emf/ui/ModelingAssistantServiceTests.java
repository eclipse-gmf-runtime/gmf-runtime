/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.IProviderPolicy;
import org.eclipse.gmf.runtime.common.core.service.ProviderPriority;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.emf.type.core.ElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.TestingConfigurationElement;

/**
 * Tests for the Modeling Assistant Service.
 * 
 * @author cmahoney
 * 
 */
public class ModelingAssistantServiceTests
	extends TestCase {

	/**
	 * Override to make some methods and classes available.
	 */
	protected static class MyModelingAssistantService
		extends ModelingAssistantService {

		/**
		 * Override to allow passing in of the provider, instead of initializing
		 * via the <code>ConfigurationElement</code>.
		 */
		protected static class ProviderDescriptor
			extends ModelingAssistantService.ProviderDescriptor {

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

		protected MyModelingAssistantService() {
			super();
		}

		protected void addModelingAssistantProvider(ProviderPriority priority,
				ProviderDescriptor provider) {

			super.addProvider(priority, provider);
		}

		protected void removeModelingAssistantProvider(
				Service.ProviderDescriptor provider) {

			super.removeProvider(provider);
		}

	}

	/**
	 * A concrete element type class.
	 */
	public static class MyElementType
		extends ElementType {

		public MyElementType(String id) {
			super(id, null, id);
		}

		public IEditHelper getEditHelper() {
			return null;
		}

	}

	/**
	 * A dummy modeling assistant provider that takes the list of types to be
	 * returned.
	 */
	public class MyModelingAssistantProvider
		extends ModelingAssistantProvider {

		private List types;

		public MyModelingAssistantProvider(List types) {
			super();
			this.types = types;
		}

		public List getTypesForPopupBar(IAdaptable host) {
			return types;
		}
	}
	
	private static boolean shouldBeContributed = false;

	public static boolean myStaticMethod() {
        return shouldBeContributed;
    }

    public static class MyModelingAssistantProviderViaXML
        extends ModelingAssistantProvider {

        public List getTypesForPopupBar(IAdaptable host) {
            if (host.getAdapter(MyElementType.class) != null) {
                return Collections.singletonList(new MyElementType("TYPE1")); //$NON-NLS-1$
            } else {
                return Collections.EMPTY_LIST;
            }
        }
    }
		
	private MyModelingAssistantService modelingAssistantService = null;

	public ModelingAssistantServiceTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ModelingAssistantServiceTests.class);
	}

	protected void setUp()
		throws Exception {
		super.setUp();
		setModelingAssistantService(new MyModelingAssistantService());
	}

	public MyModelingAssistantService getModelingAssistantService() {
		return modelingAssistantService;
	}

	public void setModelingAssistantService(MyModelingAssistantService service) {
		modelingAssistantService = service;
	}

	/**
	 * Tests that the UI contributions made by a provider affected by a
	 * capability will be shown/hidden as the capability is enabled/disabled.
	 * 
	 * @throws Exception
	 */
	public void testCapabilityFiltering()
		throws Exception {

		// set up provider1
		MyElementType TYPE1 = new MyElementType("TYPE1"); //$NON-NLS-1$	
		MyModelingAssistantService.ProviderDescriptor provider1Descriptor = new MyModelingAssistantService.ProviderDescriptor(
			new MyModelingAssistantProvider(Collections.singletonList(TYPE1)));
		getModelingAssistantService().addModelingAssistantProvider(
			ProviderPriority.LOW, provider1Descriptor);

		// set up provider2
		MyElementType TYPE2 = new MyElementType("TYPE2"); //$NON-NLS-1$
		MyModelingAssistantService.ProviderDescriptor provider2Descriptor = new MyModelingAssistantService.ProviderDescriptor(
			new MyModelingAssistantProvider(Collections.singletonList(TYPE2)));
		getModelingAssistantService().addModelingAssistantProvider(
			ProviderPriority.HIGH, provider2Descriptor);

		// test service
		List allTypes = getModelingAssistantService()
			.getTypesForPopupBar(null);
		assertEquals(2, allTypes.size());

		provider1Descriptor.setActivitiesEnabled(false);
		assertEquals(1, getModelingAssistantService()
			.getTypesForPopupBar(null).size());

		provider1Descriptor.setActivitiesEnabled(true);
		List allTypesAgain = getModelingAssistantService()
			.getTypesForPopupBar(null);
		assertEquals(2, allTypesAgain.size());
		assertTrue(Arrays.equals(allTypes.toArray(), allTypesAgain.toArray()));
	}
	
	  
    /**
     * Tests the ability of a client to use a static method to assist in
     * identifying the editor in the extension point XML.This test uses the
     * modeling assistant extension point defined in the XML for this plugin.
     * 
     * @throws Exception
     */
    //public void testStaticMethodInExtension()
    //    throws Exception {

    //    shouldBeContributed = true;
    //    assertTrue(!ModelingAssistantService.getInstance().getTypesForPopupBar(
    //        new MyElementType("TYPE1")).isEmpty());

    //    shouldBeContributed = false;
    //    assertTrue(ModelingAssistantService.getInstance().getTypesForPopupBar(
    //        new MyElementType("TYPE1")).isEmpty());

    //}   

}
