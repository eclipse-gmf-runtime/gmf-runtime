/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.internal.action;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.common.ui.action.ActionManagerChangeEvent;
import org.eclipse.gmf.runtime.common.ui.action.IActionManagerChangeListener;
import org.eclipse.gmf.runtime.common.ui.action.IActionWithProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author khussey
 */
public class ActionManagerTest {

	protected static class RepeatableAction implements IActionWithProgress {

		private final String label;

		private final boolean runnable;

		public RepeatableAction(String label, boolean runnable) {
			super();

			this.label = label;
			this.runnable = runnable;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public boolean isRunnable() {
			return runnable;
		}

		@Override
		public void refresh() {
			/* Empty block */}

		@Override
		public void run(IProgressMonitor progressMonitor) {
			/* Empty block */}

		@Override
		public WorkIndicatorType getWorkIndicatorType() {
			return WorkIndicatorType.NONE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#setup()
		 */
		@Override
		public boolean setup() {
			return true;
		}
	}

	protected static class Fixture extends ActionManager {

		public Fixture() {
			super(OperationHistoryFactory.getOperationHistory());
		}

		protected IActionWithProgress getFixtureAction() {
			return super.getAction();
		}

		protected void setFixtureAction(IActionWithProgress action) {
			super.setAction(action);
		}

		@Override
		protected void fireActionManagerChange(ActionManagerChangeEvent event) {
			super.fireActionManagerChange(event);
		}

	}

	private ActionManagerChangeEvent actionManagerChangeEvent = null;

	private Exception exception = null;

	private Fixture fixture = null;

	protected ActionManagerChangeEvent getActionManagerChangeEvent() {
		return actionManagerChangeEvent;
	}

	protected void setActionManagerChangeEvent(ActionManagerChangeEvent actionManagerChangeEvent) {
		this.actionManagerChangeEvent = actionManagerChangeEvent;
	}

	protected Exception getException() {
		return exception;
	}

	protected void setException(Exception exception) {
		this.exception = exception;
	}

	protected Fixture getFixture() {
		return fixture;
	}

	protected void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	@BeforeEach
	public void setUp() {
		setFixture(new Fixture());
	}

	@Test
	public void test_add_remove_ActionManagerChangeListener() {
		IActionManagerChangeListener listener = new IActionManagerChangeListener() {
			@Override
			public void actionManagerChanged(ActionManagerChangeEvent event) {
				setActionManagerChangeEvent(event);
			}
		};

		assertNull(getActionManagerChangeEvent());

		getFixture().addActionManagerChangeListener(listener);
		getFixture().fireActionManagerChange(new ActionManagerChangeEvent(getFixture()));

		assertNotNull(getActionManagerChangeEvent());
		assertSame(getFixture(), getActionManagerChangeEvent().getSource());

		setActionManagerChangeEvent(null);
		getFixture().removeActionManagerChangeListener(listener);
		getFixture().fireActionManagerChange(new ActionManagerChangeEvent(getFixture()));

		assertNull(getActionManagerChangeEvent());
	}

	@Test
	public void test_fireActionManagerChange() {
		final int count = 99;

		final IActionManagerChangeListener[] listeners = new IActionManagerChangeListener[count];

		for (int i = 0; i < count; i++) {
			listeners[i] = new IActionManagerChangeListener() {
				@Override
				public void actionManagerChanged(ActionManagerChangeEvent event) {
					/* Empty block */}
			};
		}

		Thread addThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < count; i++) {
					getFixture().addActionManagerChangeListener(listeners[i]);

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
				ActionManagerChangeEvent event = new ActionManagerChangeEvent(getFixture());

				try {
					for (int i = 0; i < count; i++) {
						getFixture().fireActionManagerChange(event);

						try {
							Thread.sleep(1);
						} catch (InterruptedException ie) {
							/* Empty block */}

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
					getFixture().removeActionManagerChangeListener(listeners[i]);

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

	@Test
	public void test_clear() {
		assertNull(getFixture().getFixtureAction());

		getFixture().setFixtureAction(new RepeatableAction(getName(), true));
		assertNotNull(getFixture().getFixtureAction());

		getFixture().clear();
		assertNull(getFixture().getFixtureAction());
	}

	@Test
	public void test_run() {
		assertNull(getFixture().getFixtureAction());

		try {
			getFixture().run(new RepeatableAction(getName(), false));
			fail();
		} catch (UnsupportedOperationException uoe) {
			assertNull(getFixture().getFixtureAction());
		}

		IActionWithProgress action = new RepeatableAction(getName(), true);
		try {
			getFixture().run(action);
			assertSame(action, getFixture().getFixtureAction());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		try {
			getFixture().run(new RepeatableAction(getName(), false));
			fail();
		} catch (UnsupportedOperationException uoe) {
			assertSame(action, getFixture().getFixtureAction());
		}

		action = new RepeatableAction(getName(), true);
		try {
			getFixture().run(action);
			assertSame(action, getFixture().getFixtureAction());
		} catch (Exception e) {
			fail();
		}
	}

	protected String getName() {
		return this.getClass().getSimpleName();
	}
}
