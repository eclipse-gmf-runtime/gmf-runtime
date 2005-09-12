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


package org.eclipse.gmf.tests.runtime.common.ui.internal.dialogs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.gmf.runtime.common.ui.dialogs.SelectableElement;
import org.eclipse.gmf.runtime.common.ui.dialogs.SelectedType;

/**
 * @author cmcgee
*/
public class SelectableElementTest extends TestCase {

    SelectableElement root,branch1,branch2,brancha,branchb,branchc,branchd;
    TestHint branchcHint, branchdHint;
    
    private static class TestHint extends SelectableElement {
    	String name, id;
    	public TestHint(String name, String id) {
    		super(name,id, null, null);
    		
    		this.name = name;
    		this.id = id;
    	}
    	
    	public String getId() {
    		return id;
    	}
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        branchcHint = new TestHint("branchc","org.eclipse.gmf.tests.runtime.common.ui.branchc");  //$NON-NLS-1$//$NON-NLS-2$
        branchdHint = new TestHint("branchd","org.eclipse.gmf.tests.runtime.common.ui.branchd"); //$NON-NLS-1$ //$NON-NLS-2$
        
        root = new SelectableElement("", "",null,new String("root"));  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        branch1 = new SelectableElement("branch1","branch1",null,new String("branch1"));  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        branch2 = new SelectableElement("branch2","branch2",null,new String("branch2")); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
        brancha = new SelectableElement("brancha","brancha",null,new String("brancha"));  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        branchb = new SelectableElement("branchb","branchb",null,new String("branchb"));  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        branchc = new SelectableElement("branchc","branchc",null,branchcHint);  //$NON-NLS-1$//$NON-NLS-2$
        branchd = new SelectableElement("branchd","branchd",null,branchdHint); //$NON-NLS-1$ //$NON-NLS-2$
        root.addChild(branch1);
        root.addChild(branch2);
        branch1.addChild(brancha);
        branch1.addChild(branchb);
        branch2.addChild(brancha);
        branch2.addChild(branchb);
        branch2.addChild(branchc);
        branch2.addChild(branchd);
        
        brancha.setSelectedType(SelectedType.SELECTED);
        branchc.setSelectedType(SelectedType.SELECTED);
    }

    public void test_getSelectedElementIds() {
    	List ids = root.getSelectedElementIds();
    	Set hints = new HashSet();
    	root.getHints(ids,hints);
    	Assert.assertTrue(hints.contains("brancha")); //$NON-NLS-1$
    	Assert.assertTrue(hints.contains(branchcHint));
    	Assert.assertFalse(hints.contains(branchdHint));
    }
    
    public static Test suite() {
        return new TestSuite(SelectableElementTest.class);
    }
}
