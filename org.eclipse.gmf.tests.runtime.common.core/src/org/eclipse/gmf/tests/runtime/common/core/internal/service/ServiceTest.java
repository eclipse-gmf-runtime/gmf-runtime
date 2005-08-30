/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core.internal.service;

import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.common.core.service.IProviderPolicy;
import org.eclipse.gmf.runtime.common.core.service.ProviderChangeEvent;
import org.eclipse.gmf.runtime.common.core.service.ProviderPriority;
import org.eclipse.gmf.runtime.common.core.service.Service;

public class ServiceTest extends TestCase {

    protected static class Fixture extends Service {

        protected static class ProviderDescriptor
            extends Service.ProviderDescriptor {

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

        }

        protected Fixture() {
            super(true);
        }

        protected List getFixtureProviders(
            ExecutionStrategy strategy,
            ProviderPriority priority,
            IOperation operation) {
            return super.getProviders(strategy, priority, operation);
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

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ServiceTest.class);
    }

    public ServiceTest(String name) {
        super(name);
    }

    protected Fixture getFixture() {
        return fixture;
    }

    private void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    protected void setUp() {
        setFixture(new Fixture());
    }

    public void test_providerChanged() {
        getFixture().addProviderChangeListener(new IProviderChangeListener() {
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

    public void test_provides() {
        ExecutionStrategy strategy = new ExecutionStrategy("Dummy") {//$NON-NLS-1$
    		public List execute(Service service, IOperation operation) {
                return Collections.EMPTY_LIST;
            }
        };

        IOperation operation = new IOperation() {
            public Object execute(IProvider provider) {
                return null;
            }
        };
        assertTrue(!getFixture().provides(operation));

        IProvider trueProvider = new AbstractProvider() {
            public boolean provides(IOperation op) {
                return true;
            }
        };
        Fixture.ProviderDescriptor trueProviderDescriptor =
            new Fixture.ProviderDescriptor(trueProvider);
        getFixture().addFixtureProvider(
            ProviderPriority.MEDIUM,
            trueProviderDescriptor);
        assertTrue(getFixture().provides(operation));

        List cachedProviders =
            getFixture().getFixtureProviders(
            	strategy,
                ProviderPriority.MEDIUM,
                operation);
        assertTrue(trueProvider == cachedProviders.get(0));

        IProvider falseProvider = new AbstractProvider() {
            public boolean provides(IOperation op) {
                return false;
            }
        };
        Fixture.ProviderDescriptor falseProviderDescriptor =
            new Fixture.ProviderDescriptor(falseProvider);
        getFixture().addFixtureProvider(
            ProviderPriority.MEDIUM,
            falseProviderDescriptor);
        assertTrue(getFixture().provides(operation));
        assertTrue(
            cachedProviders
                != getFixture().getFixtureProviders(
                	strategy,
                    ProviderPriority.MEDIUM,
                    operation));
        assertTrue(
            trueProvider
                == getFixture().getFixtureProviders(
                	strategy,
                    ProviderPriority.MEDIUM,
                    operation).get(
                    0));

        getFixture().removeFixtureProvider(trueProviderDescriptor);
        assertTrue(!getFixture().provides(operation));

        getFixture().removeFixtureProvider(falseProviderDescriptor);
        assertTrue(!getFixture().provides(operation));
    }

}
