/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.net.URL;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * An emumeration of element types and their associated element kind
 * 
 * @deprecated Replaced with the element type registry in
 *             <code>org.eclipse.gmf.runtime.emf.type.core</code>. Element Types are
 *             registered using the
 *             <code>org.eclipse.gmf.runtime.emf.type.core.elementTypes</code> extension
 *             point and can be accessed by ID using
 *             {@link org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry#getType(String)}.
 */
public abstract class ElementType extends EnumeratedType implements IElementType {
 
    /**
     * An internal unique identifier for this enumerated type.
     */
    private static int nextOrdinal = 0;

    /** the element kind */
    final private EClass languageElementKind;

	/** the element display name */
	final private String displayName;
	
	private IEditHelper editHelper;

    /**
     * Constructor for ElementTypeInfo.
     * @param displayName 		the localized name of the element type
     * @param name 				the name of the element type
     * @param languageElementKind 	the element kind of the element type
     * @param ordinal 				the ordinal of the enumerated type
     */
    protected ElementType(String displayName, String name, EClass languageElementKind, int ordinal) {
        super(name, ordinal);
       
        this.languageElementKind = languageElementKind;
        this.displayName = displayName;
    }

    /**
     * @see org.eclipse.gmf.runtime.common.core.util.EnumeratedType#getValues()
     */
    protected List getValues() {
        return null;
    }
    
    /**
     * Returns the nextOrdinal.
     * @return int
     */
    protected static int getNextOrdinal() {
        return nextOrdinal;
    }

    /**
     * Sets the nextOrdinal.
     * @param nextOrdinal The nextOrdinal to set
     */
    protected static void setNextOrdinal(int nextOrdinal) {
        ElementType.nextOrdinal = nextOrdinal;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.emf.core.internal.util.IElementType#getEClass()
     */
    public EClass getEClass() {
        return languageElementKind;
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.util.IElementType#getEClassName()
	 */
	public String getEClassName() {
		return MetaModelUtil.getID(getEClass());
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.util.IElementType#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(this.getClass())) {
			return this;
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest)
	 */
	public ICommand getEditCommand(IEditCommandRequest request) {
		
		if (getEditHelper() != null) {
			return getEditHelper().getEditCommand(request);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getIconURL()
	 */
	public URL getIconURL() {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getId()
	 */
	public String getId() {
		return getName();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditHelper()
	 */
	public IEditHelper getEditHelper() {
		
		if (editHelper == null) {
			// This should find the default semantic service edit helper
			org.eclipse.gmf.runtime.emf.type.core.IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(EcorePackage.eINSTANCE.getEModelElement());
			
			if (elementType != null) {
				editHelper = elementType.getEditHelper();
			}
		}
		return editHelper;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getAllSuperTypes()
	 */
	public org.eclipse.gmf.runtime.emf.type.core.IElementType[] getAllSuperTypes() {
		return new IElementType[] {};
	}
	
	
}
