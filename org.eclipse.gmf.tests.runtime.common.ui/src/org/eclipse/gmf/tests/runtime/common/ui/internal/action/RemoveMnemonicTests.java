/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.internal.action;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;

/**
 * Tests the action classes that remove the mnemonics and specifically removes
 * the Korean (&X). See RATLC00530818.
 * 
 * @author cmahoney
 */
public class RemoveMnemonicTests
	extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(RemoveMnemonicTests.class);
    }

    public RemoveMnemonicTests(String name) {
        super(name);
    }
    
	public void test_RATLC00530818() {
		String cmd = "My Command"; //$NON-NLS-1$
		String cmdEnglish = "My &Command"; //$NON-NLS-1$
		String cmdKorean = "My Command(&C)"; //$NON-NLS-1$

		assertEquals(cmd, GlobalAction.removeMnemonics(cmdEnglish));
		assertEquals(cmd, GlobalAction.removeMnemonics(cmdKorean));

		assertEquals(cmd, AbstractActionHandler.removeMnemonics(cmdEnglish));
		assertEquals(cmd, AbstractActionHandler.removeMnemonics(cmdKorean));
	}
}