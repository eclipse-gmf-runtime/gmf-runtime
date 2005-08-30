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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import org.eclipse.gmf.runtime.common.ui.services.properties.internal.extended.ExtendedPropertyDescriptor;
//import org.eclipse.gmf.runtime.common.ui.services.properties.internal.extended.PropertySource;


/**
 * @author Tauseef A. Israr
 * Created on: Sep 15, 2002
 * 
 */
public class PropertySourceTest extends TestCase {

//    private PropertySource propertySource;
    
    /**
     * Constructor for PropertySourceTest.
     * @param name
     */
    public PropertySourceTest(String name) {
        super(name);
    }
    
    public void testEditableValue(){
    	
    	//Object editableValue = "editable Value"; //$NON-NLS-1$
 //   	propertySource.setEditableValue(editableValue);
 //   	assertTrue(propertySource.getEditableValue().equals(editableValue));
		
		//String id1 = "id1"; //$NON-NLS-1$
		//String value1 = "value1"; //$NON-NLS-1$
		
//		propertySource.addProperty(id1, value1);
//		ExtendedPropertyDescriptor propertyDescriptor = new ExtendedPropertyDescriptor(id1, "Property1"); //$NON-NLS-1$
//		propertySource.addPropertyDescriptor(propertyDescriptor);
				
//		assertTrue(propertySource.getPropertyValue(id1).equals(value1));
//		assertTrue(propertySource.getPropertyDescriptors().length==1);
//		assertTrue(propertySource.isPropertySet(id1));
		
//		propertyDescriptor.setDirtyFlag(true);
//		assertTrue(!propertySource.getPropertyValue(id1).equals(value1));
//		assertTrue(!propertyDescriptor.isDirty());
		
//		propertySource.removeProperty(id1);
//		assertNull(propertySource.getPropertyValue(id1));
//		assertTrue(propertySource.getPropertyDescriptors().length==0);
    }
		
	
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
//        propertySource = new PropertySource();
    }
    
    public static Test suite() {
        return new TestSuite(PropertySourceTest.class);
    }

}
