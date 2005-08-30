/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.clipboard.core;

import org.eclipse.emf.ecore.EPackage;

/**
 * Interface for objects that can create {@link IClipboardSupport}s for
 * supporting clipboard copy/paste operations on EMF models.
 * <p>
 * This interface is intended to be implemented by plug-ins
 * on the <tt>clipboardSupport</tt> extension point.
 * </p>
 * 
 * @see IClipboardSupport
 *
 * @author Christian W. Damus (cdamus)
 */
public interface IClipboardSupportFactory {
	/**
	 * Creates a new clipboard support utility instance.  This method may create
	 * a new instance or may return always the same reusable instance.
	 * Clipboard support utilities are not expected to retain any state that
	 * needs to be disposed.
	 * <p>
	 * A single factory class can support any number of EMF metamodels.  Hence
	 * the parameterization of the factory method by an <code>ePackage</code>.
	 * </p>
	 * 
	 * @param ePackage the <code>EPackage</code> (representing an EMF metamodel)
	 *     for which a clipboard support utility is required.
	 * @return a new clipboard support utility
	 */
	IClipboardSupport newClipboardSupport(EPackage ePackage);
}
