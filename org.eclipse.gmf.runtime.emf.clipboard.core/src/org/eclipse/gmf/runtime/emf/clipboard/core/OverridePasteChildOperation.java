/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core;

/**
 * Abstract definition of an
 * {@link org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport}-defined
 * operation that completely overrides the default paste-child behaviour.
 * <p>
 * This class is intended to be extended by clients, to provide an alternative
 * paste operation in the clipboard support.
 * </p>
 * 
 * @see IClipboardSupport#getOverrideChildPasteOperation(PasteChildOperation)
 * 
 * @author Yasser Lulu
 */
public abstract class OverridePasteChildOperation
	extends PasteChildOperation {

	private PasteChildOperation overriddenChildPasteOperation;

    /**
     * Initializes me with the default paste operation that I am overriding.
     * I may want to access this default operation later in performing the
     * paste.
     * 
     * @param overriddenChildPasteOperation the default paste operation that I
     *     override
     */
	public OverridePasteChildOperation(
			PasteChildOperation overriddenChildPasteOperation) {
		super(overriddenChildPasteOperation);
		this.overriddenChildPasteOperation = overriddenChildPasteOperation;
	}

	/**
     * Obtains the default child-paste operation that I am overriding.
     * 
     * @return the overridden paste operation
	 */
	protected PasteChildOperation getOverriddenPasteChildOperation() {
		return overriddenChildPasteOperation;
	}

}