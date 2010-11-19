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

package org.eclipse.gmf.runtime.common.core.util;

/**
 * A utility for generating unique hash values.
 * 
 * @author khussey
 */
public final class HashUtil {

	/**
	 * An arbitrary constant non-zero value.
	 */
	protected static final int CONSTANT = 17;

	/**
	 * An odd prime.
	 */
	protected static final int PRIME = 37;

	/**
	 * Constructs a new hash util.
	 */
	private HashUtil() {
		super();
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, boolean field) {
		return PRIME * base + (field ? 0
			: 1);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(boolean field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, byte field) {
		return PRIME * base + field;
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(byte field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, char field) {
		return PRIME * base + field;
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(char field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, short field) {
		return PRIME * base + field;
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(short field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, int field) {
		return PRIME * base + field;
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, long field) {
		return PRIME * base + (int) (field ^ (field >>> 32));
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(long field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, float field) {
		return PRIME * base + Float.floatToIntBits(field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(float field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(int base, double field) {
		return hash(base, Double.doubleToLongBits(field));
	}

	/**
	 * Retrieves a unique hash value for the specified field, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified field.
	 * @param field
	 *            The field which to retrieve a hash value.
	 */
	public static int hash(double field) {
		return hash(CONSTANT, field);
	}

	/**
	 * Retrieves a unique hash value for the specified object, based on the
	 * specified base value.
	 * 
	 * @return A hash value for the specified object.
	 * @param base
	 *            The value on which to base the hash value.
	 * @param object
	 *            The object for which to retrieve a hash value.
	 */
	public static int hash(int base, Object object) {
		return PRIME * base + (null == object ? 0
			: object.hashCode());
	}

	/**
	 * Retrieves a unique hash value for the specified object, based on a
	 * constant base value.
	 * 
	 * @return A hash value for the specified object.
	 * @param object
	 *            The object for which to retrieve a hash value.
	 */
	public static int hash(Object object) {
		return hash(CONSTANT, object);
	}
}
