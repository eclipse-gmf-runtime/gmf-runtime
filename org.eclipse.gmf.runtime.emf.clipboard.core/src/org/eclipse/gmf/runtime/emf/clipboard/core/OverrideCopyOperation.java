/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004. All Rights Reserved.                     |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core;

/**
 * Abstract definition of an
 * {@link org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport}-defined
 * operation that completely overrides the default copy behaviour.
 * <p>
 * This class is intended to be extended by clients, to provide an alternative
 * copy operation in the clipboard support.
 * </p>
 * 
 * @see IClipboardSupport#getOverrideCopyOperation(CopyOperation)
 * 
 * @author Yasser Lulu
 */
public abstract class OverrideCopyOperation extends CopyOperation {
    private CopyOperation overriddenCopyOperation;

    /**
     * Initializes me with the default copy operation that I am overriding.
     * I may want to access this default operation later in performing the
     * copy.
     * 
     * @param overriddenCopyOperation the default copy operation that I override
     */
     public OverrideCopyOperation(CopyOperation overriddenCopyOperation) {
        super(overriddenCopyOperation.getProgressMonitor(),
                overriddenCopyOperation.getClipboardOperationHelper(),
                overriddenCopyOperation.getEObjects(), overriddenCopyOperation
                        .getEObjectsHintMap());
        this.overriddenCopyOperation = overriddenCopyOperation;
    }

    /**
     * Obtains the default copy operation that I am overriding.
     * 
     * @return the overridden copy operation
     */
    protected CopyOperation getOverriddenCopyOperation() {
        return overriddenCopyOperation;
    }
}