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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.common.core.service.ProviderChangeEvent;

public class AbstractProviderTest extends TestCase {

    protected static class Fixture extends AbstractProvider {

        protected Fixture() {
            super();
        }

        protected void fireProviderChange(ProviderChangeEvent event) {
            super.fireProviderChange(event);
        }

        public boolean provides(IOperation operation) {
            return true;
        }

    }

    private Fixture fixture = null;

    private Exception exception = null;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(AbstractProviderTest.class);
    }

    public AbstractProviderTest(String name) {
        super(name);
    }

    protected Fixture getFixture() {
        return fixture;
    }

    private void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    protected Exception getException() {
        return exception;
    }

    protected void setException(Exception exception) {
        this.exception = exception;
    }

    protected void setUp() {
        setFixture(new Fixture());
    }

    public void test_add_remove_ProviderChangeListener() {
        IProviderChangeListener listener = new IProviderChangeListener() {
            public final void providerChanged(ProviderChangeEvent event) {
                throw new RuntimeException();
            }
        };

        getFixture().addProviderChangeListener(listener);
        try {
            getFixture().fireProviderChange(
                new ProviderChangeEvent(getFixture()));
            fail();
        } catch (Exception e) {
        	// Nothing to do
        }

        getFixture().removeProviderChangeListener(listener);
        try {
            getFixture().fireProviderChange(
                new ProviderChangeEvent(getFixture()));
        } catch (Exception e) {
            fail();
        }
    }

    public void test_fireProviderChange() {
        final int count = 99;

        final IProviderChangeListener[] listeners =
            new IProviderChangeListener[count];

        for (int i = 0; i < count; i++) {
            listeners[i] = new IProviderChangeListener() {
                public void providerChanged(ProviderChangeEvent event) {
                	//Nothing to do 	
                }
            };
        }

        Thread addThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < count; i++) {
                    getFixture().addProviderChangeListener(listeners[i]);

                    if (null != getException()) {
                        break;
                    }
                }
            }
        });
        addThread.start();

        Thread fireThread = new Thread(new Runnable() {
            public void run() {
                ProviderChangeEvent event =
                    new ProviderChangeEvent(getFixture());

                try {
                    for (int i = 0; i < count; i++) {
                        getFixture().fireProviderChange(event);

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ie) {
                        	//Nothing to do	
                        }

                    }
                } catch (Exception e) {
                    setException(e);
                }
            }
        });
        fireThread.start();

        Thread removeThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < count; i++) {
                    getFixture().removeProviderChangeListener(listeners[i]);

                    if (null != getException()) {
                        break;
                    }
                }
            }
        });
        removeThread.start();

        try {
            fireThread.join();
        } catch (InterruptedException ie) {
            setException(ie);
        }

        if (null != getException()) {
            fail();
        }
    }

}
