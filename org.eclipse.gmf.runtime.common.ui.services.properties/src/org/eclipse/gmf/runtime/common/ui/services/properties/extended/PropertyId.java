/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

/**
 * @author Tauseef A. Israr
 * Created on: Oct 7, 2002
 * 
 * The purpose of this class is to provide an id and a hook for a "hint" that
 * can be used in setting the property.
 * 
 * 
 */
public class PropertyId {

	/** ID of slot property. */
	public static final String SLOT_ID = "slot_id"; //$NON-NLS-1$

	/** ID of stereotype property. */
	public static final String STEREOTYPE_VALUE_ID = "stereotype_value_id"; //$NON-NLS-1$

	/** Default ID. */
	public static final String DEFAULT_ID = "default_id"; //$NON-NLS-1$

	/**
	 * String representation of the id.
	 * This variable is used by eclipse to compare properties in multiple 
	 * select.  Therefore, it is not unique, as there can exists properties
	 * with same ids.
	 * 
	 * Usually the id is CategoryName concatenated by the attribute name
	 * 
	 */
	private String id;

	/** 
	 * Hint variable is neccessary to provide a handle to this property.
	 * It is used in retrieving the object, the property.is associated with 
	 */
	private Object hint;

	/**
	 * It is the type of property.  For instance, type can be slot,taggedValue
	 * etc etc.
	 */
	private String type;

	/**
	 * Constructor for PropertyId.
	 * @param id String
	 * @param hint Object
	 * @param type String
	 */
	public PropertyId(String id, Object hint, String type) {
		super();
		this.id = id;
		this.hint = hint;	
	}

	/**
	 * Returns the hint.
	 * 
	 * @return Object
	 */
	public Object getHint() {
		return hint;
	}

	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the type.
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object arg0) {

		if (arg0 == this)
			return true;

		if (arg0 instanceof PropertyId) {

			PropertyId propertyId = (PropertyId) arg0;
			if ((propertyId.getId() == getId())
				&& (propertyId.getHint() == getHint())
				&& (propertyId.getType() == getType())) {
				return true;
			}
			boolean equals = false;
			if ((propertyId.getId() != null) && (getId() != null)) {
				equals = getId().equals(propertyId.getId());
				if (!equals)
					return false;
			}
			if ((getHint() != null) && (propertyId.getHint() != null)) {
				equals = getHint().equals(propertyId.getHint());
				if (!equals)
					return false;
			}
			if ((getType() != null) && (propertyId.getType() != null)) {
				equals = getType().equals(propertyId.getType());
				if (!equals)
					return false;
			}
            return equals;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int code = 0;
        if(getId() != null)
            code = code + getId().hashCode();
        if (getType() != null)
            code = code + getType().hashCode();
        if (getHint() != null)
            code = code + getHint().hashCode();              
		return code;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getId();
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param object
	 */
	public void setHint(Object object) {
		hint = object;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

}
