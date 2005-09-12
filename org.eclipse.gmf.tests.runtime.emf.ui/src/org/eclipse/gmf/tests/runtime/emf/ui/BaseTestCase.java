/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.ui;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MListener;

/**
 * @author mgoyal
 *
 * Base test case.
 */
public abstract class BaseTestCase
	extends TestCase {

	protected MListener listener = null;

	protected MFilter filter = null;

	public BaseTestCase(String name) {
		super(name);
	}

	protected void setUp()
		throws Exception {

		listener = new MListener() {

			public void onEvent(List events) {
				assertFalse(events.isEmpty());
				processEvents(events);
			}
		};

		filter = new MFilter() {

			public boolean matches(Notification event) {
				return true;
			}
		};

		listener.setFilter(filter);

		listener.startListening();
	}

	protected void processEvents(List events) {
		// implement in derived classes.
	}

	protected void tearDown()
		throws Exception {
		listener.stopListening();
	}
}