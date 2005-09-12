/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

 package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecorationBase;


/**
 * This represents a decoration object returned by a decorator target. The
 * client holds onto this so that it can be deleted later.
 * 
 * @author cmahoney
 * @canBeSeenBy %level1
 */
public interface IDecoration extends IDecorationBase {
	// empty interface
}
