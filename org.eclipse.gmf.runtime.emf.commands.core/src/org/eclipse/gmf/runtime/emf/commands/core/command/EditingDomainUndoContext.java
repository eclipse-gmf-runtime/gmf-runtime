/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    /**
     * Initializes me with the editing domain that I represent.
     * 
     * @param domain
     *            the editing domain
     */
    public EditingDomainUndoContext(EditingDomain domain) {
        this.editingDomain = domain;
    }

    // Documentation copied from the interface
    public String getLabel() {
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

    // Redefines the inherited method
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
}
