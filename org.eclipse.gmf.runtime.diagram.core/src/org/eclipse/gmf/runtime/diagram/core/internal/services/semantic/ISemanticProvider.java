/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.gmf.runtime.common.core.command.ICommand;

/**
 * The interface for a semantic provider
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 * 
 * @deprecated Use the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}
 *             to get semantic commands.
 */
public interface ISemanticProvider {

	/**
	 * Returns a command that satisfies the semantic request in the given model context
	 * @param semanticRequest the specific semantic request
	 * @return ICommand
	 */
	ICommand getCommand(
		SemanticRequest semanticRequest);


}
