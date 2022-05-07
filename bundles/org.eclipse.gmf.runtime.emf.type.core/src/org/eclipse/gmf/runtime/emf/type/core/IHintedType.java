/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;

/**
 * A type that requires a semantic hint for view creation.
 * 
 * <p>
 * This interface may be implemented by clients if the class extends the
 * abstract implementation
 * {@link org.eclipse.gmf.runtime.emf.type.core.ElementType} as
 * {@link IElementType} is not meant to be implemented by clients.
 * </p>
 * 
 * @author cmahoney
 */
public interface IHintedType
    extends IElementType {

    /**
     * Gets the semantic hint required for view creation.
     * 
     * @return the semantic hint.
     */
    String getSemanticHint();
}
