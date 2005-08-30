/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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