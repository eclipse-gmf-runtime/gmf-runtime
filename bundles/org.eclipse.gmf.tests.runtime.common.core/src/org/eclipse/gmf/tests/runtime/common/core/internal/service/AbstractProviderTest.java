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

import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.common.core.service.ProviderChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractProviderTest {

	protected static class Fixture extends AbstractProvider {

		protected Fixture() {
			super();
		}

		@Override
		protected void fireProviderChange(ProviderChangeEvent event) {
			super.fireProviderChange(event);
		}

		@Override
		public boolean provides(IOperation operation) {
			return true;
		}

	}

	private Fixture fixture = null;

	private Exception exception = null;

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

	@BeforeEach
	public void setUp() {
		setFixture(new Fixture());
	}

	@Test
	public void test_add_remove_ProviderChangeListener() {
		IProviderChangeListener listener = new IProviderChangeListener() {
			@Override
			public final void providerChanged(ProviderChangeEvent event) {
				throw new RuntimeException();
			}
		};

		getFixture().addProviderChangeListener(listener);
		try {
			getFixture().fireProviderChange(new ProviderChangeEvent(getFixture()));
			fail();
		} catch (Exception e) {
			// Nothing to do
		}

		getFixture().removeProviderChangeListener(listener);
		try {
			getFixture().fireProviderChange(new ProviderChangeEvent(getFixture()));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void test_fireProviderChange() {
		final int count = 99;

		final IProviderChangeListener[] listeners = new IProviderChangeListener[count];

		for (int i = 0; i < count; i++) {
			listeners[i] = new IProviderChangeListener() {
				@Override
				public void providerChanged(ProviderChangeEvent event) {
					// Nothing to do
				}
			};
		}

		Thread addThread = new Thread(new Runnable() {
			@Override
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
			@Override
			public void run() {
				ProviderChangeEvent event = new ProviderChangeEvent(getFixture());

				try {
					for (int i = 0; i < count; i++) {
						getFixture().fireProviderChange(event);

						try {
							Thread.sleep(1);
						} catch (InterruptedException ie) {
							// Nothing to do
						}

					}
				} catch (Exception e) {
					setException(e);
				}
			}
		});
		fireThread.start();

		Thread removeThread = new Thread(new Runnable() {
			@Override
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
