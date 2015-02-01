/******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;

/**
 * Partial implementation of the {@link IAdviceBindingDescriptor} protocol for
 * advice bindings.
 * 
 * @since 1.9
 */
public abstract class AbstractAdviceBindingDescriptor implements IAdviceBindingDescriptor {

	/**
	 * The advice binding ID.
	 */
	protected final String id;
	/**
	 * The identifier of this element type.
	 */
	protected final String typeId;
	/**
	 * Indicates the related element types that should inherite this advice.
	 */
	protected final AdviceBindingInheritance inheritance;

	/**
	 * The edit helper advice.
	 */
	protected IEditHelperAdvice editHelperAdvice;

	public AbstractAdviceBindingDescriptor(String id, String typeID, AdviceBindingInheritance inheritance,
			IEditHelperAdvice editHelperAdvice) {
		
		super();

		this.id = id;
		this.typeId = typeID;
		this.inheritance = inheritance;
		this.editHelperAdvice = editHelperAdvice;
	}

	public AbstractAdviceBindingDescriptor(String id, String typeID, AdviceBindingInheritance inheritance) {
		this(id, typeID, inheritance, null);
	}

	public AbstractAdviceBindingDescriptor(String id, String typeID) {
		this(id, typeID, AdviceBindingInheritance.ALL, null);
	}

	public String getTypeId() {
		return typeId;
	}

	/**
	 * Returns the advice binding id.
	 * 
	 * @return the advice binding id
	 */
	public String getId() {
		return id;
	}

	public IEditHelperAdvice getEditHelperAdvice() {
		return editHelperAdvice;
	}

	public AdviceBindingInheritance getInheritance() {
		return inheritance;
	}

}