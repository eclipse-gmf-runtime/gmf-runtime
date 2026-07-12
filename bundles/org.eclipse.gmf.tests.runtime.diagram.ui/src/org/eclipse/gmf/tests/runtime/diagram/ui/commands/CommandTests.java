/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author jschofie
 */
@Suite
@SelectClasses({
		// $JUnit-BEGIN$
		SetBoundsCommandTest.class, SendToBackCommandTest.class, BringToFrontCommandTest.class,
		SendBackwardCommandTest.class, BringForwardCommandTest.class,
// $JUnit-END$
})
public class CommandTests {
}
