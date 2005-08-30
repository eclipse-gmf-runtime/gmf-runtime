/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.edithelper;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * A command factory for commands that will modify elements of a specific type.
 * <P>
 * Clients should not implement this interface directly, but should subclass
 * {@link org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper}instead.
 * 
 * @author ldamus
 */
public interface IEditHelper {

	/**
	 * Gets an edit command to perform the work requested in <code>req</code>.
	 * <P>
	 * The work should be considered unsupported by this edit helper if the
	 * command returned is <code>null</code> or its
	 * <code>isExecutable()</code> method returns <code>false</code>.
	 * 
	 * @param req
	 *            the edit request
	 * @return the edit command, or <code>null</code> if none could be found
	 */
	public ICommand getEditCommand(IEditCommandRequest req);

	/**
	 * Gets the values that can be contained in the <code>feature</code> of
	 * <code>eContainer</code>. Such values may be element types that could
	 * be owned by the <code>eContainer</code>, or actual model element
	 * instances that can be referenced by the feature.
	 * 
	 * @param eContainer
	 *            the container object
	 * @param feature
	 *            the feature of the container object
	 * @return the possible values that can be contained in the feature
	 */
	public List getContainedValues(EObject eContainer, EReference feature);
}