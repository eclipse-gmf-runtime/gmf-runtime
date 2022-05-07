/******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.command;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gmf.runtime.emf.commands.core.internal.l10n.EMFCommandsCoreMessages;

/**
 * An {@link IUndoContext} that tags an EMF operation with the editing domain
 * that it affects. Two editing domain contexts match if and only if they
 * reference the same {@link EditingDomain} instance.
 * 
 * @author ldamus
 */
public final class EditingDomainUndoContext
    implements IUndoContext {

    private final EditingDomain editingDomain;

    private String label;
    
    /**
     * Initializes me with the editing domain that I represent.
     * 
     * @param domain
     *            the editing domain
     */
    public EditingDomainUndoContext(EditingDomain domain) {
    	this(domain, null);
    }

    /**
     * Initializes me with the editing domain that I represent.
     * 
     * @param domain
     *            the editing domain
     * @param label
     *            the label for the context
     * @since 1.4
     */
    public EditingDomainUndoContext(EditingDomain domain, String label) {
        this.editingDomain = domain;
        this.label = label;
    }
    
    /*
     * (non-Javadoc)
     * @see org.eclipse.core.commands.operations.IUndoContext#getLabel()
     */
    public String getLabel() {
    	if (label != null) {
			return label;
    	}
        return EMFCommandsCoreMessages.editingDomainContext;
    }

    /**
     * I match another <code>context</code> if it is a
     * <code>EditingDomainUndoContext</code> representing the same editing
     * domain as I.
     */
    public boolean matches(IUndoContext context) {
        return this.equals(context);
    }

    /**
     * I am equal to other <code>EditingDomainUndoContext</code> on the same
     * editing domain as mine.
     */
    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof EditingDomainUndoContext) {
            result = getEditingDomain() == ((EditingDomainUndoContext) o)
                .getEditingDomain();
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return editingDomain == null ? 0
            : editingDomain.hashCode();
    }

    /**
     * Obtains the editing domain.
     * 
     * @return my editing domain
     */
    public final EditingDomain getEditingDomain() {
        return editingDomain;
    }
    
    /**
     * The string representation of this operation.  Used for debugging purposes only.
     * This string should not be shown to an end user.
     * 
     * @return The string representation.
     */
    public String toString() {
    	return getLabel();
    }
}
