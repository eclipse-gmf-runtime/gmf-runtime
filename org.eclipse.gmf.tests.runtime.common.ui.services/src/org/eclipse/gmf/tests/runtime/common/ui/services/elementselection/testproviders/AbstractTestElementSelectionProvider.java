/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.testproviders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionProvider;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractMatchingObject;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IMatchingObject;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IMatchingObjectsOperation;
import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestElementSelectionProviderContext;
import org.eclipse.swt.graphics.Image;

/**
 * An abstract class for the element selection providers used by the tests.
 * 
 * @author Anthony Hunter
 */
public abstract class AbstractTestElementSelectionProvider
    extends AbstractElementSelectionProvider {

    private List matchingObjects = new ArrayList();

    public AbstractTestElementSelectionProvider() {
        super();
        initializeMatchingObjects();
    }

    private void initializeMatchingObjects() {
        String[] names = new String[] {"One", "Two", "Three", "Four", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "Five", "Six", "Seven", "Eight", "Nine"}; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
        String component = getTestElementComponent();
        Image image = getTestElementImage();
        for (int i = 0; i < names.length; i++) {
            TestMatchingObject testMatchingObject = new TestMatchingObject(
                names[i], component, names[i] + TestMatchingObject.DASHES
                    + component, image, this);
            matchingObjects.add(testMatchingObject);
        }
    }

    protected abstract String getTestElementComponent();

    protected abstract Image getTestElementImage();

    public void run(IProgressMonitor monitor) {
        /**
         * filter the matching objects using the user input and filter.
         */
        String filter = validatePattern(getElementSelectionInput().getInput());
        Pattern pattern = Pattern.compile(filter);
        for (Iterator iter = matchingObjects.iterator(); iter.hasNext();) {
            AbstractMatchingObject element = (AbstractMatchingObject) iter
                .next();
            Matcher matcher = pattern.matcher(element.getName().toLowerCase());
            /**
             * If element matches user input.
             */
            if (matcher.matches()) {
                /**
                 * If element matches input filter.
                 */
                if (getElementSelectionInput().getFilter().select(element)) {
                    fireMatchingObjectEvent(element);
                }
            }
            if (monitor.isCanceled()) {
                break;
            }
        }

        fireEndOfMatchesEvent();
    }

    public boolean provides(IOperation operation) {
        assert operation instanceof IMatchingObjectsOperation;
        Object context = ((IMatchingObjectsOperation) operation)
            .getElementSelectionInput().getContext();
        if (context instanceof TestElementSelectionProviderContext) {
            return true;
        }
        return false;
    }

    public Object resolve(IMatchingObject object) {
        /**
         * Just return a String for the tests
         */
        return object.getDisplayName();
    }

    /**
     * Convert the UNIX style pattern entered by the user to a Java regex
     * pattern (? = any character, * = any string).
     * 
     * @param string
     *            the UNIX style pattern.
     * @return a Java regex pattern.
     */
    private String validatePattern(String string) {
        if (string.equals(StringStatics.BLANK)) {
            return string;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = Character.toLowerCase(string.charAt(i));
            if (c == '?') {
                result.append('.');
            } else if (c == '*') {
                result.append(".*"); //$NON-NLS-1$
            } else if (c == '?') {
                result.append("\\."); //$NON-NLS-1$
            } else {
                result.append(c);
            }
        }
        result.append(".*"); //$NON-NLS-1$
        return result.toString();
    }
}
