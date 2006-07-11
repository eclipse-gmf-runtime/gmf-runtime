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
package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs;

import java.text.ParseException;

import com.ibm.icu.text.NumberFormat;

/**
 * StringValidator verifies the integrity of the user input.  It is used by
 * page setup dialog to verify the integrity of margin and page size values.
 * 
 * @author etworkow
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
class StringValidator {

	/**
	 * Verify input is valid - not null and is a positive number.
	 *  
	 * @param input
	 * @return
	 */
	public static boolean isValid(String input) {

		return isNotEmpty(input) && 
			   isDouble(input) && 
			   isPositive(input);
	}
		
	/**
	 * Verify user input string is not empty.
	 * 
	 * @param input the value entered by the user
	 * @return boolean true is string it not empty, false otherwise
	 */
	private static boolean isNotEmpty(String input) {
		if (input.length() > 0) return true;
		return false;
	}
	
	/**
	 * Verify all characters in a user input string are numeric.
	 * 
	 * @param input the value entered by the user
	 * @return boolean true is string contains only numeric characters, false otherwise
	 */
	private static boolean isDouble(String input) {
		// Verify input can be parsed as double
		char[] tokens = input.toCharArray();
		if (!(Character.isDigit(tokens[0])))
			return false;
		
		try {
            NumberFormat.getNumberInstance().parse(input);
		} catch (ParseException e) {
            return false;
        }
		
		return true;
	}
	
	/**
	 * Verify the value entered by the user is positive.
	 * 
	 * @param input the value entered by the user
	 * @return boolean true if value > 0, false otherwise
	 */
	private static boolean isPositive(String input) {
		if (isDouble(input)) {
			try {
				// Verify input is a positive number
				Number n = NumberFormat.getNumberInstance().parse(input);
				if (n.doubleValue() >= 0) 
					return true;
			} catch (ParseException e) {
				return false;
			}			
		}
		// If input is not numeric, return false
		return false;
	}
}

