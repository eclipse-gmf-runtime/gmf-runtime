/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core.internal.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.util.Proxy;
import org.eclipse.gmf.runtime.common.core.util.StringUtil;

/**
 * Tests org.eclipse.gmf.runtime.common.core.internal.util.StringUtil
 * @author Wayne Diu, wdiu
 */
public class StringUtilTest extends TestCase {

    private final String src = "I am writing a test case with the word a.\nThe word a is a very important word because I want to replace all instances of it.  It is a word.  And I must test case sensitive replaces too, okay?"; //$NON-NLS-1$ 

    protected static class Fixture extends Proxy {

        protected Fixture(Object realObject) {
            super(realObject);
        }

    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(StringUtilTest.class);
    }

    public StringUtilTest(String name) {
        super(name);
    }
    
    /*
     * The test cases replace a with a a because that could result in
     * infinite recursion if I did not write the replace methods correctly.
     */

    public void test_Replace() {
    	assertTrue(StringUtil.replace(src, "a", "a a", false).equals("I a am writing a test case with the word a.\nThe word a is a very important word because I want to replace all instances of it.  It is a word.  And I must test case sensitive replaces too, okay?")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    	assertTrue(StringUtil.replaceAll(src, "a", "a a", true).equals("I a am writing a a test ca ase with the word a a.\nThe word a a is a a very importa ant word beca ause I wa ant to repla ace a all insta ances of it.  It is a a word.  And I must test ca ase sensitive repla aces too, oka ay?")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void test_ReplaceWholeWords() {
    	assertTrue(StringUtil.replaceWholeWords(src, "a", "a a", true).equals("I am writing a a test case with the word a.\nThe word a is a very important word because I want to replace all instances of it.  It is a word.  And I must test case sensitive replaces too, okay?")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    	assertTrue(StringUtil.replaceAllWholeWords(src, "a", "a a", false).equals("I am writing a a test case with the word a a.\nThe word a a is a a very important word because I want to replace all instances of it.  It is a a word.  And I must test case sensitive replaces too, okay?")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    public void test_dgdEncodeURL() {
    	//These should not change after encoding
    	assertTrue(StringUtil.encodeURL("http://www.ibm.ca/").equals("http://www.ibm.ca/")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("http://www.ibm.ca").equals("http://www.ibm.ca")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("C:\\dir\\sub").equals("C:\\dir\\sub")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("Fil\u00E9name with spaces.doc").equals("Fil\u00E9name with spaces.doc")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=rational&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=rational&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$

    	//These should change after encoding
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=\u00E9\u00E7\u00E5\u00E2\u00E8&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=%C3%A9%C3%A7%C3%A5%C3%A2%C3%A8&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=\u00E9\u00E7\u00E5\u00E2\u00E8%3d&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=%C3%A9%C3%A7%C3%A5%C3%A2%C3%A8%3d&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=\u00E9\u00E7\u00E5\u00E2\u00E8%a&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=%C3%A9%C3%A7%C3%A5%C3%A2%C3%A8%25a&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=\u00E9\u00E7\u00E5\u00E2\u00E8%fg%a&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=%C3%A9%C3%A7%C3%A5%C3%A2%C3%A8%25fg%25a&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=\u00E9\u00E7\u00E5\u00E2\u00E8%%a&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=%C3%A9%C3%A7%C3%A5%C3%A2%C3%A8%25%25a&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$ 
    	assertTrue(StringUtil.encodeURL("http://www.ibm.com/Search/?q=\u00E9\u00E7\u00E5\u00E2\u00E8%&v=14&lang=en&cc=ca").equals("http://www.ibm.com/Search/?q=%C3%A9%C3%A7%C3%A5%C3%A2%C3%A8%25&v=14&lang=en&cc=ca")); //$NON-NLS-1$ //$NON-NLS-2$
    }    
}
