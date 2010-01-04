/*
 * +------------------------------------------------------------------------+
 * | Licensed Materials - Property of IBM                                   |
 * | Copyright IBM Corp. 2009.  All Rights Reserved.                        |
 * |                                                                        |
 * | US Government Users Restricted Rights - Use, duplication or disclosure |
 * | restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 * +------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core;

import java.util.Map;

import org.eclipse.gmf.runtime.emf.clipboard.core.internal.PasteIntoParentOperation;
import org.eclipse.gmf.runtime.emf.clipboard.core.internal.PasteOperation;

/**
 * An additional interface that can be implemented by clients to inject
 * additional specific behavior into copy/paste operations. See
 * {@link IClipboardSupport} for an explanation of its use.
 * 
 * @author crevells
 */
public interface IClipboardSupport2 {

	/**
	 * Queries whether it is necessary to completely override the default paste
	 * into parent behaviour.
	 * 
	 * @param parentElement
	 *            the target element into which a child is to be pasted
	 * @param childEObject
	 *            the child element to be pasted
	 * 
	 * @return boolean <code>true</code> if we want to completely override the
	 *         paste operation for this child; <code>false</code>, otherwise
	 */
	/**
	 * @param pasteTarget
	 * @param hintsMap
	 * @return boolean <code>true</code> if we want to completely override the
	 *         paste into parent operation for this child; <code>false</code>,
	 *         otherwise
	 */
	boolean shouldOverridePasteIntoParentOperation(PasteTarget pasteTarget,
			Map hintsMap);

	/**
	 * Obtains an override for the specified paste into parent operation. This
	 * method is called immediately after (@link
	 * #shouldOverridePasteIntoParentOperation}, and then only if the latter
	 * returned <code>true</code>.
	 * 
	 * @param pasteOperation
	 *            the paste operation in progress
	 * @param pasteTarget
	 *            the target object into which the copied elements are to be
	 *            pasted
	 * @param hintsMap
	 *            the map of copy hints, possibly empty. Hints are defined by
	 *            the
	 *            {@link org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil}
	 *            class
	 * @return the overriding paste into parent operation
	 * @throws Exception
	 *             if anything goes wrong in initializing the paste operation
	 */
	PasteIntoParentOperation getPasteIntoParentOperation(
			PasteOperation pasteOperation, PasteTarget pasteTarget, Map hintsMap)
			throws Exception;
}
