/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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