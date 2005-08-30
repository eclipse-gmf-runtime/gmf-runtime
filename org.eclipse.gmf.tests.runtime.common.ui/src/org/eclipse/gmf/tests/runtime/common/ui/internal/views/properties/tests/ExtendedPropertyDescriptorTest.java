/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
