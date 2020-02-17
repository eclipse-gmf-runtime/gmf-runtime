/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

/**
 * An enumeration of constants based on the typesafe enum pattern.
 * 
 * @author khussey
 */
public abstract class EnumeratedType
	implements Serializable {

	/**
	 * The name of this enumerated value.
	 */
	private final transient String name;

	/**
	 * The ordinal for this enumerated value.
	 */
	private final int ordinal;

	/**
	 * Concrete subclasses should define a private static field to generate
	 * ordinals and a private constructor as follows:
	 * <p>
	 * <code>
	 * private static int nextOrdinal = 0;
	 *
	 * private Subclass(String name) {
	 *     super(name, nextOrdinal++);
	 * }
	 * </code>
	 * 
	 * @param name
	 *            The name of the new enumerated type.
	 * @param ordinal
	 *            The ordinal for the new enumerated type.
	 */
	protected EnumeratedType(String name, int ordinal) {
		super();

		this.name = name;
		this.ordinal = ordinal;
	}

	/**
	 * Retrieves the value of the <code>name</code> instance variable.
	 * 
	 * @return The value of the <code>name</code> instance variable.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the value of the <code>ordinal</code> instance variable.
	 * 
	 * @return The value of the <code>ordinal</code> instance variable.
	 */
	public final int getOrdinal() {
		return ordinal;
	}

	/**
	 * Retrieves the list of constants for this enumerated type.
	 * <p>
	 * Concrete subclasses should define a private static array of values and
	 * a(n) (final) implementation of this method as follows:
	 * <p>
	 * <code>
	 * private static final Subclass[] VALUES = { ... };
	 * 
	 * protected final List getValues() {
	 *     return Collections.unmodifiableList(Arrays.asList(VALUES));
	 * }
	 * </code>
	 * 
	 * @return The list of constants for this enumerated type.
	 */
	protected abstract List getValues();

	/**
	 * Indicates whether some other object is "equal to" this enumerated type.
	 * 
	 * @return <code>true</code> if this enumerated type is the same as the
	 *         object argument; <code>false</code> otherwise.
	 * @param object
	 *            The reference object with which to compare.
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	public final boolean equals(Object object) {
		return super.equals(object);
	}

	/**
	 * Retrieves a hash code value for this enumerated type. This method is
	 * supported for the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return A hash code value for this enumerated type.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode() {
		return super.hashCode();
	}

	/**
	 * Retrieves a textual representation of this enumerated type.
	 * 
	 * @return A textual representation of this enumerated type.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

	/**
	 * Designates an alternative object to be used when reading an enumerated
	 * type from a stream.
	 * 
	 * @return The alternative enumerated type object.
	 * @throws ObjectStreamException
	 * @see java.io.Serializable
	 */
	protected final Object readResolve()
		throws ObjectStreamException {
		return getValues().get(getOrdinal());
	}
}
