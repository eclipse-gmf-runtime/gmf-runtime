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

package org.eclipse.gmf.tests.runtime.common.ui.internal.views.properties.tests;

//import junit.framework.Test;
import junit.framework.TestCase;
//import junit.framework.TestSuite;

//import org.eclipse.gmf.runtime.common.ui.services.properties.internal.extended.ExtendedPropertyDescriptor;
//import org.eclipse.gmf.runtime.common.ui.services.properties.internal.extended.PropertySource;

/**
 * @author Tauseef A. Israr
 * Created on: Sep 15, 2002
 * 
 */
public class ExtendedPropertyDescriptorTest extends TestCase {

/*    
    private String id1 = "id1"; //$NON-NLS-1$
    private String displayName = "Name"; //$NON-NLS-1$
    private ExtendedPropertyDescriptor propertyDescriptor;
*/    
    
    /**
     * Constructor for ExtendedPropertyDescriptorTest.
     * @param name
     */
    public ExtendedPropertyDescriptorTest(String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
//       propertyDescriptor = new ExtendedPropertyDescriptor(id1,displayName);
    }
    
    public void testXDEPropertyDescriptor(){
 /*   	
    	propertyDescriptor.setReadOnly(true);
    	propertyDescriptor.setDirtyFlag(true);
    	PropertySource propertySource = new PropertySource();
    	propertySource.addPropertyDescriptor(propertyDescriptor);
    	
    	assertTrue(propertyDescriptor.isReadOnly());
    	assertTrue(propertyDescriptor.isDirty());
    	assertEquals(propertySource,propertyDescriptor.getPropertySource());
    	assertNotNull(propertyDescriptor.getBlank());
*/    	
    }
/*    
    public static Test suite() {
    	
        return new TestSuite(ExtendedPropertyDescriptorTest.class);
        
    }    		
*/
}
