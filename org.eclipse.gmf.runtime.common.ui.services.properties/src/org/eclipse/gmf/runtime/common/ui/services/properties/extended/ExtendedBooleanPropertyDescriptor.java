/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.PSFResourceManager;


/**
 * @author Tauseef A. Israr
 * Created on: Sep 9, 2002
 */
public class ExtendedBooleanPropertyDescriptor
	extends ExtendedComboboxPropertyDescriptor {

	private static final String TRUE = PSFResourceManager.getI18NString("ExtendedBooleanPropertyDescriptor.True"); //$NON-NLS-1$

	private static final String FALSE = PSFResourceManager.getI18NString("ExtendedBooleanPropertyDescriptor.False"); //$NON-NLS-1$

	/**
	 * Constructor for ExtendedBooleanPropertyDescriptor.
	 * 
	 * @param id
	 * @param displayName
	 */
	public ExtendedBooleanPropertyDescriptor(Object id, String displayName) {
		super(id, displayName, new String[] { FALSE, TRUE });
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getLabelProvider()
	 */
	public ILabelProvider getLabelProvider() {
		return new LabelProvider() {
			public String getText(Object object) {
				if (object instanceof Integer) {
					if (((Integer) object).intValue() == 0)
						return FALSE;
					else if (((Integer) object).intValue() == 1)
						return TRUE;
				}
				return (String) getBlank();
			}
		};
	}

	/**
	 * Convert value to index.
	 * 
	 * @param b 
	 * @return an instance of <code>Integer</code>
	 */
	static public Integer valueToIndex(Boolean b) {
		return new Integer(b.booleanValue() ? 1 : 0);
	}

	/**
	 * Convert value to index.
	 * 
	 * @param i
	 * @return an instance of <code>Boolean</code>
	 */
	static public Boolean indexToValue(Integer i) {
		return new Boolean(i.intValue() == 0 ? false : true);
	}

}
