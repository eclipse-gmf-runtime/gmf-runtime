/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core.internal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Status;

/**
 * @author Yasser Lulu
 * 
 */
public class TestingConfigurationElement implements IConfigurationElement {
    private Map attributeMap;
    private Map childrenMap;
    public static final TestingConfigurationElement[] EMPTY_TESTING_CONFIG_ELEMENT =
        new TestingConfigurationElement[0];

    /**
     * Constructor for TestingConfigurationElement.
     */
    public TestingConfigurationElement(Map attributeMap, Map childrenMap) {
        setAttributeMap(attributeMap);
        setChildrenMap(childrenMap);
    }  
    

    /**
     * Constructor for TestingConfigurationElement.
     */
    public TestingConfigurationElement() {
        this(new HashMap(), new HashMap());
    }

    public void addAttribute(String name, String value) {
        getAttributeMap().put(name, value);
    }

    public void addChildren(String name, List childrenList) {
        getChildrenMap().put(name, childrenList);
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#createExecutableExtension(String)
     */
    public Object createExecutableExtension(String propertyName)
        throws CoreException {
        throw new CoreException(new Status(Status.ERROR, "", 0, "", null)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getAttribute(String)
     */
    public String getAttribute(String name) {
        return (String) getAttributeMap().get(name);
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getAttributeAsIs(String)
     */
    public String getAttributeAsIs(String name) {
        return getAttribute(name);
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getAttributeNames()
     */
    public String[] getAttributeNames() {
        return null;
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getChildren()
     */
    public IConfigurationElement[] getChildren() {
        return null;
    }
    
    public final List getChildrenList(String name){
        return (List)getChildrenMap().get(name);
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getChildren(String)
     */
    public IConfigurationElement[] getChildren(String name) {
        List childrenList = getChildrenList(name);
        return ((childrenList != null) && (childrenList.isEmpty() == false))
            ? (TestingConfigurationElement[]) childrenList.toArray(
                EMPTY_TESTING_CONFIG_ELEMENT)
            : EMPTY_TESTING_CONFIG_ELEMENT;
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getDeclaringExtension()
     */
    public IExtension getDeclaringExtension() {
        return null;
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getName()
     */
    public String getName() {
        return null;
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getValue()
     */
    public String getValue() {
        return null;
    }

    /**
     * @see org.eclipse.core.runtime.IConfigurationElement#getValueAsIs()
     */
    public String getValueAsIs() {
        return null;
    }

    /**
     * Returns the attributeMap.
     * @return Map
     */
    public Map getAttributeMap() {
        return attributeMap;
    }

    /**
     * Returns the childrenMap.
     * @return Map
     */
    public Map getChildrenMap() {
        return childrenMap;
    }

    /**
     * Sets the attributeMap.
     * @param attributeMap The attributeMap to set
     */
    private void setAttributeMap(Map attributeMap) {
        this.attributeMap = attributeMap;
    }

    /**
     * Sets the childrenMap.
     * @param childrenMap The childrenMap to set
     */
    private void setChildrenMap(Map childrenMap) {
        this.childrenMap = childrenMap;
    }


	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IConfigurationElement#getParent()
	 */
	public Object getParent() {
		return null;
	}


	public String getNamespace() throws InvalidRegistryObjectException {
		return null;
	}


	public boolean isValid() {
		return false;
	}

}
