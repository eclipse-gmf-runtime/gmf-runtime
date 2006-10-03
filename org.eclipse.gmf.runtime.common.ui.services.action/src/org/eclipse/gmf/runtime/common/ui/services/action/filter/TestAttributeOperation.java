/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.filter;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.util.HashUtil;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.filter.IActionFilterProvider;

/**
 * An operation that performs attribute tests by determining whether a specific
 * attribute matches the state of a target object.
 * 
 * @author khussey
 *
 */
public class TestAttributeOperation implements IOperation {

    /**
     * The target of the attribute test.
     * 
     */
    private final Object target;

    /**
     * The name of the attribute to test.
     * 
     */
    private final String name;

    /**
     * The value of the attribute to test.
     * 
     */
    private final String value;

    /**
     * Constructs a new test attribute operation with the specified target
     * object, attribute name, and attribute value.
     * 
     * @param target The target of the attribute test.
     * @param name The name of the attribute to test.
     * @param value The value of the attribute to test.
     * 
     */
    public TestAttributeOperation(Object target, String name, String value) {
        super();

        assert null != target : "target cannot be null"; //$NON-NLS-1$
        assert null != name : "name cannot be null"; //$NON-NLS-1$
        assert null != value : "value cannot be null"; //$NON-NLS-1$

        this.target = target;
        this.name = name;
        this.value = value;
    }

    /**
     * Retrieves the value of the <code>target</code> instance variable.
     * 
     * @return The value of the <code>target</code> instance variable.
     * 
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Retrieves the value of the <code>name</code> instance variable.
     * 
     * @return The value of the <code>name</code> instance variable.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the value of the <code>value</code> instance variable.
     * 
     * @return The value of the <code>value</code> instance variable.
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieves a hash code value for this test attribute operation. This
     * method is supported for the benefit of hashtables such as those provided
     * by <code>java.util.HashMap</code>.
     * 
     * @return A hash code value for this test attribute operation.
     * 
     * @see Object#hashCode()
     * 
     */
    public int hashCode() {
        return HashUtil.hash(HashUtil.hash(getName()), getValue());
    }

    /**
     * Indicates whether some other test attribute operation is "equal to" this
     * test attribute operation.
     * 
     * @return <code>true</code> if this test attribute operation is the same
     *          as the test attribute operation argument; <code>false</code>
     *          otherwise.
     * @param operation The reference test attribute operation with which to
     *                   compare.
     * 
     */
    private boolean equals(TestAttributeOperation operation) {
        return getName().equals(operation.getName())
            && getValue().equals(operation.getValue());
    }

    /**
     * Indicates whether some other object is "equal to" this test attribute
     * operation.
     * 
     * @return <code>true</code> if this test attribute operation is the same
     *          as the object argument; <code>false</code> otherwise.
     * @param object The reference object with which to compare.
     * 
     * @see Object#equals(Object)
     * 
     */
    public boolean equals(Object object) {
        return object instanceof TestAttributeOperation
            && equals((TestAttributeOperation) object);
    }

    /**
     * Executes this test attribute operation on the specified provider.
     * 
     * @param provider The provider on which to execute this operation.
     * 
     * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(IProvider)
     * 
     */
    public Object execute(IProvider provider) {
        return Boolean.valueOf(
            ((IActionFilterProvider) provider).testAttribute(
                getTarget(),
                getName(),
                getValue()));
    }

}
