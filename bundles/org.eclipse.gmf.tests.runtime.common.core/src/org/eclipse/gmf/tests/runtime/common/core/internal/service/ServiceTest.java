/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core.internal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.common.core.service.IProviderPolicy;
import org.eclipse.gmf.runtime.common.core.service.ProviderChangeEvent;
import org.eclipse.gmf.runtime.common.core.service.ProviderPriority;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceTest {

	protected static class Fixture extends Service {

		protected static class ProviderDescriptor extends Service.ProviderDescriptor {

			protected ProviderDescriptor(IProvider provider) {
				super(null);

				this.provider = provider;
				provider.addProviderChangeListener(this);
			}

			@Override
			public IProvider getProvider() {
				return provider;
			}

			@Override
			protected IProviderPolicy getPolicy() {
				return null;
			}

		}

		protected Fixture() {
			super(true);
		}

		protected List getFixtureProviders(ExecutionStrategy strategy, ProviderPriority priority,
				IOperation operation) {
			return super.getProviders(strategy, priority, operation);
		}

		protected void addFixtureProvider(ProviderPriority priority, Service.ProviderDescriptor provider) {
			super.addProvider(priority, provider);
		}

		protected void removeFixtureProvider(Service.ProviderDescriptor provider) {
			super.removeProvider(provider);
		}

	}

	private Fixture fixture = null;

	protected Fixture getFixture() {
		return fixture;
	}

	private void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	@BeforeEach
	public void setUp() {
		setFixture(new Fixture());
	}

	@Test
	public void test_providerChanged() {
		getFixture().addProviderChangeListener(new IProviderChangeListener() {
			@Override
			public final void providerChanged(ProviderChangeEvent event) {
				assertEquals(getFixture(), event.getSource());
				throw new RuntimeException();
			}
		});

		try {
			getFixture().providerChanged(new ProviderChangeEvent(getFixture()));
			fail();
		} catch (Exception e) {
			// Nothing to do
		}
	}

	@Test
	public void test_provides() {
		ExecutionStrategy strategy = new ExecutionStrategy("Dummy") {//$NON-NLS-1$

			private static final long serialVersionUID = 1L;

			@Override
			public List execute(Service service, IOperation operation) {
				return Collections.EMPTY_LIST;
			}
		};

		IOperation operation = new IOperation() {
			@Override
			public Object execute(IProvider provider) {
				return null;
			}
		};
		assertTrue(!getFixture().provides(operation));

		IProvider trueProvider = new AbstractProvider() {
			@Override
			public boolean provides(IOperation op) {
				return true;
			}
		};
		Fixture.ProviderDescriptor trueProviderDescriptor = new Fixture.ProviderDescriptor(trueProvider);
		getFixture().addFixtureProvider(ProviderPriority.MEDIUM, trueProviderDescriptor);
		assertTrue(getFixture().provides(operation));

		List cachedProviders = getFixture().getFixtureProviders(strategy, ProviderPriority.MEDIUM, operation);
		assertTrue(trueProvider == cachedProviders.get(0));

		IProvider falseProvider = new AbstractProvider() {
			@Override
			public boolean provides(IOperation op) {
				return false;
			}
		};
		Fixture.ProviderDescriptor falseProviderDescriptor = new Fixture.ProviderDescriptor(falseProvider);
		getFixture().addFixtureProvider(ProviderPriority.MEDIUM, falseProviderDescriptor);
		assertTrue(getFixture().provides(operation));
		assertTrue(cachedProviders != getFixture().getFixtureProviders(strategy, ProviderPriority.MEDIUM, operation));
		assertTrue(
				trueProvider == getFixture().getFixtureProviders(strategy, ProviderPriority.MEDIUM, operation).get(0));

		getFixture().removeFixtureProvider(trueProviderDescriptor);
		assertTrue(!getFixture().provides(operation));

		getFixture().removeFixtureProvider(falseProviderDescriptor);
		assertTrue(!getFixture().provides(operation));
	}

}
