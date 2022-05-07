/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.testproviders;

import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractMatchingObject;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A matching object used by the tests.
 * 
 * @author Anthony Hunter
 */
public class TestMatchingObject
    extends AbstractMatchingObject {

    private String component;
    
    public TestMatchingObject(String name, String component,
            String displayName, Image image, IElementSelectionProvider provider) {
        super(name, displayName, image, provider);
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

}
