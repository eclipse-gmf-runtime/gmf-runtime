/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.services.action.internal.filter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.IProviderPolicy;
import org.eclipse.gmf.runtime.common.core.service.ProviderPriority;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.AbstractActionFilterProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.TestAttributeOperation;

/**
 * @author khussey
 *
 */
public class ActionFilterServiceTest extends TestCase {

	public class ActionFilterProvider extends AbstractActionFilterProvider {

		private final String name;

		private final String value;

		protected ActionFilterProvider(String name, String value) {
			super();

			this.name = name;
			this.value = value;
		}

		protected String getName() {
			return name;
		}

		protected String getValue() {
			return value;
		}

		public boolean provides(IOperation operation) {
			TestAttributeOperation tao = (TestAttributeOperation) operation;

			return tao.getName().equals(getName())
				&& tao.getValue().equals(getValue());
		}

		public boolean testAttribute(
			Object target,
			String nam,
			String val) {

			return String.valueOf(target).equals(val);
		}

	}

	protected static class Fixture extends ActionFilterService {

		protected static class ProviderDescriptor
			extends ActionFilterService.ProviderDescriptor {

			protected ProviderDescriptor(IProvider provider) {
				super(null);

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
				return getProvider().provides(operation);
			}

		}

		protected Fixture() {
			super();
		}

		protected void addFixtureProvider(
			ProviderPriority priority,
			Service.ProviderDescriptor provider) {

			super.addProvider(priority, provider);
		}

		protected void removeFixtureProvider(
			Service.ProviderDescriptor provider) {

			super.removeProvider(provider);
		}

	}

	private Fixture fixture = null;

	public ActionFilterServiceTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ActionFilterServiceTest.class);
	}

	protected Fixture getFixture() {
		return fixture;
	}

	private void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	protected void setUp() throws Exception {
		setFixture(new Fixture());
	}

	public void test_testAttribute() {
        
		String prefix = "@"; //$NON-NLS-1$

		String zero = "zero"; //$NON-NLS-1$
		String one = "one"; //$NON-NLS-1$

		assertTrue(!getFixture().testAttribute(zero, prefix + getName(), zero));


        IOperationHistory history = ActionManager.getDefault().getOperationHistory();
        IUndoableOperation operation = new AbstractOperation(
            "ActionFilterServiceTest") { //$NON-NLS-1$

            public IStatus execute(IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                return Status.OK_STATUS;
            }

            public IStatus redo(IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                return Status.OK_STATUS;
            }

            public IStatus undo(IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                return Status.OK_STATUS;
            }
        };
        
		ActionFilterProvider provider0 =
			new ActionFilterProvider(getName(), zero);
		Fixture.ProviderDescriptor providerDescriptor0 =
			new Fixture.ProviderDescriptor(provider0);
		getFixture().addFixtureProvider(
			ProviderPriority.HIGHEST,
			providerDescriptor0);

		assertTrue(!getFixture().testAttribute(zero, prefix + getName(), zero));

        try {
            history.execute(operation, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        history.dispose(
            IOperationHistory.GLOBAL_UNDO_CONTEXT, true, true, false);

		assertTrue(getFixture().testAttribute(zero, prefix + getName(), zero));
		assertTrue(getFixture().testAttribute(one, prefix + getName(), zero));

        try {
            history.execute(operation, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        
        history.dispose(
            IOperationHistory.GLOBAL_UNDO_CONTEXT, true, true, false);

		assertTrue(!getFixture().testAttribute(one, prefix + getName(), zero));
		assertTrue(!getFixture().testAttribute(one, prefix + getName(), one));

		ActionFilterProvider provider1 =
			new ActionFilterProvider(getName(), one);
		Fixture.ProviderDescriptor providerDescriptor1 =
			new Fixture.ProviderDescriptor(provider1);
		getFixture().addFixtureProvider(
			ProviderPriority.LOWEST,
			providerDescriptor1);

		assertTrue(!getFixture().testAttribute(one, prefix + getName(), one));

        try {
            history.execute(operation, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        history.dispose(
            IOperationHistory.GLOBAL_UNDO_CONTEXT, true, true, false);

		assertTrue(getFixture().testAttribute(one, prefix + getName(), one));
		assertTrue(getFixture().testAttribute(zero, prefix + getName(), one));

        try {
            history.execute(operation, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        history.dispose(
            IOperationHistory.GLOBAL_UNDO_CONTEXT, true, true, false);

		assertTrue(!getFixture().testAttribute(zero, prefix + getName(), one));
		assertTrue(getFixture().testAttribute(zero, prefix + getName(), zero));

		getFixture().removeFixtureProvider(providerDescriptor0);

		assertTrue(getFixture().testAttribute(zero, prefix + getName(), zero));

        try {
            history.execute(operation, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        history.dispose(
            IOperationHistory.GLOBAL_UNDO_CONTEXT, true, true, false);

		assertTrue(!getFixture().testAttribute(zero, prefix + getName(), zero));

	}

}
