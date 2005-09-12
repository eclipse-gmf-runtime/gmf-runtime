/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.commands;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * ModelJobRule used by ModelSystemJob. This Rule conflicts with all rules that
 * are instanceof ModelJobRule and aren't equal.
 * 
 * @author mgoyal
 */
class ModelJobRule
	implements ISchedulingRule {

	/**
	 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.runtime.jobs.ISchedulingRule)
	 */
	public boolean contains(ISchedulingRule rule) {
		if(this == rule) {
			// containment must be reflexive
			return true;
		}
		return false;
	}

	/**
	 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.runtime.jobs.ISchedulingRule)
	 */
	public boolean isConflicting(ISchedulingRule rule) {
		if (rule == this) {
			// conflict must be reflexive
			return true;
		} else if (rule instanceof ModelJobRule) {
			// I also conflict with any other model job rule
			return true;
		}
		return false;
	}
}
