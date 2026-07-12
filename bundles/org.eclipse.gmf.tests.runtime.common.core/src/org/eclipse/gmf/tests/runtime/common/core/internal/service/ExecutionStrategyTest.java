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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.ProviderPriority;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author khussey
 */
public class ExecutionStrategyTest {

	protected static class Provider extends AbstractProvider {

		private final String name;

		protected Provider(String name) {
			super();

			this.name = name;
		}

		protected String getName() {
			return name;
		}

		@Override
		public boolean provides(IOperation operation) {
			return true;
		}

	}

	protected static class Operation implements IOperation {

		protected Operation() {
			super();
		}

		@Override
		public Object execute(IProvider provider) {
			return ((Provider) provider).getName();
		}

	}

	protected static class Fixture extends ExecutionStrategy {

		private static final long serialVersionUID = 1L;

		protected Fixture() {
			super("Fixture", 0); //$NON-NLS-1$
		}

		@Override
		protected List getValues() {
			return super.getValues();
		}

		@Override
		public List execute(Service service, IOperation operation) {
			return Collections.EMPTY_LIST;
		}

	}

	private Fixture fixture = null;

	private ServiceTest.Fixture service = null;

	protected Fixture getFixture() {
		return fixture;
	}

	private void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	protected ServiceTest.Fixture getService() {
		return service;
	}

	private void setService(ServiceTest.Fixture service) {
		this.service = service;
	}

	@BeforeEach
	public void setUp() {
		setFixture(new Fixture());

		setService(new ServiceTest.Fixture());

		getService().addFixtureProvider(ProviderPriority.HIGHEST,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("Highest"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.HIGHEST,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("tsehgiH"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.HIGH,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("High"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.HIGH,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("hgiH"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.MEDIUM,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("Medium"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.MEDIUM,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("muideM"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.LOW,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("Low"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.LOW,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("woL"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.LOWEST,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("Lowest"))); //$NON-NLS-1$
		getService().addFixtureProvider(ProviderPriority.LOWEST,
				new ServiceTest.Fixture.ProviderDescriptor(new Provider("tsewoL"))); //$NON-NLS-1$
	}

	@Test
	public void test_readResolve() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		ObjectOutput output = null;
		ObjectInput input = null;
		try {
			output = new ObjectOutputStream(stream);
			for (Iterator i = getFixture().getValues().iterator(); i.hasNext();) {
				output.writeObject(i.next());
			}
			output.flush();

			input = new ObjectInputStream(new ByteArrayInputStream(stream.toByteArray()));
			for (Iterator i = getFixture().getValues().iterator(); i.hasNext();) {
				assertSame(i.next(), input.readObject());
			}
		} catch (Exception e) {
			fail();
		} finally {
			try {
				output.close();
				input.close();
			} catch (Exception e) {
				// Nothing to do
			}
		}
	}

	@Test
	public void test_execute_FIRST() {
		List result = ExecutionStrategy.FIRST.execute(getService(), new Operation());
		assertEquals(1, result.size());
		assertEquals("Highest", result.get(0)); //$NON-NLS-1$
	}

	@Test
	public void test_execute_LAST() {
		List result = ExecutionStrategy.LAST.execute(getService(), new Operation());
		assertEquals(1, result.size());
		assertEquals("tsewoL", result.get(0)); //$NON-NLS-1$
	}

	@Test
	public void test_execute_FORWARD() {
		List result = ExecutionStrategy.FORWARD.execute(getService(), new Operation());
		assertEquals(10, result.size());
		assertEquals("Highest", result.get(0)); //$NON-NLS-1$
		assertEquals("tsehgiH", result.get(1)); //$NON-NLS-1$
		assertEquals("High", result.get(2)); //$NON-NLS-1$
		assertEquals("hgiH", result.get(3)); //$NON-NLS-1$
		assertEquals("Medium", result.get(4)); //$NON-NLS-1$
		assertEquals("muideM", result.get(5)); //$NON-NLS-1$
		assertEquals("Low", result.get(6)); //$NON-NLS-1$
		assertEquals("woL", result.get(7)); //$NON-NLS-1$
		assertEquals("Lowest", result.get(8)); //$NON-NLS-1$
		assertEquals("tsewoL", result.get(9)); //$NON-NLS-1$
	}

	@Test
	public void test_execute_REVERSE() {
		List result = ExecutionStrategy.REVERSE.execute(getService(), new Operation());
		assertEquals(10, result.size());
		assertEquals("tsewoL", result.get(0)); //$NON-NLS-1$
		assertEquals("Lowest", result.get(1)); //$NON-NLS-1$
		assertEquals("woL", result.get(2)); //$NON-NLS-1$
		assertEquals("Low", result.get(3)); //$NON-NLS-1$
		assertEquals("muideM", result.get(4)); //$NON-NLS-1$
		assertEquals("Medium", result.get(5)); //$NON-NLS-1$
		assertEquals("hgiH", result.get(6)); //$NON-NLS-1$
		assertEquals("High", result.get(7)); //$NON-NLS-1$
		assertEquals("tsehgiH", result.get(8)); //$NON-NLS-1$
		assertEquals("Highest", result.get(9)); //$NON-NLS-1$
	}

}
