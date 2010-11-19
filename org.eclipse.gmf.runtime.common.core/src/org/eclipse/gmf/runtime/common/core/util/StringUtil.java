/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;

import com.ibm.icu.util.StringTokenizer;

/**
 * In JDK 1.3 we don't have the luxury of replaceAll as in JDK 1.4.
 * The replace methods from this string class are intended to be used
 * until JDK 1.4 is used.
 * 
 * For completely different implementations of the replace methods, please see
 * http://forum.java.sun.com/thread.jsp?forum=31&thread=284142&message=1109785
 * 
 * Replacing a whole word is completely different from replacing all instances.
 * To avoid confusion, the method names are different too.
 * 
 * @author wdiu, Wayne Diu
 */
public class StringUtil {

	/**
	 * Delimiters to mark the beginning or end of a string, used for
	 * replaceWholeWords
	 */
	private static String delims = " !:;{}(),.?'\"\\\t\n\r"; //$NON-NLS-1$
	
	/**
	 * Internet protocol delimiter ://
	 */
	private static final String PROTOCOL_DELIMITER = "://"; //$NON-NLS-1$
	
	/**
	 * Length of the internet protocol delimiter ://
	 */
	private static final int PROTOCOL_DELIMITER_LENGTH = PROTOCOL_DELIMITER.length();
	
	/**
	 * Default URL Encoding method.  UTF-8.
	 */
	private static final String URL_ENCODING = "UTF-8"; //$NON-NLS-1$
	
	/**
	 * UTF-8 encoded %.  %25
	 */
	private static final String ENCODED_PERCENT = "%25"; //$NON-NLS-1$
	

	/**
	 * private constructor for the static class.
	 */
	private StringUtil() {
		super();
	}
	
	/**
	 * Replace the first instance of part of a string with another string
	 * starting from a point in the string.
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 * @param from in, out.  from[0] contains where to start initially
	 * and is changed to where it left off.
	 * @return the string containing the first instance of source found
	 * starting from a point in the string replaced with dest and in the
	 * from parameter, the end index of where the last replacement was made
	 */
	private static String replaceFrom(
		String string,
		String source,
		String dest,
		boolean caseSensitive,
		int[] from) {
		if (source.compareTo(StringStatics.BLANK) == 0)
			return string;
		int stringLength = string.length(),
			sourceLength = source.length(),
			destLength = dest.length();
		while (from[0] + sourceLength <= stringLength) {
			int compareResult;
			if (caseSensitive)
				compareResult =
					string.substring(
						from[0],
						from[0] + sourceLength).compareTo(
						source);
			else
				compareResult =
					string.substring(
						from[0],
						from[0] + sourceLength).compareToIgnoreCase(
						source);
			//not case sensitive
			if (compareResult == 0) {
				//System.out.println("matched " + string.substring(i, i + sourceLength) + " with " + source);
				int fromIndex = from[0];
				from[0] += destLength;
				return string.substring(0, fromIndex)
					+ dest
					+ string.substring(fromIndex + sourceLength, stringLength);
			}
			//System.out.println("did not match " + string.substring(i, i + sourceLength) + " with " + source);
			from[0]++;
		}
		return string;
	}

	/**
	 * Replace the first instance of part of a string with another string
	 * starting from a point in the string.
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 * @param from where to start
	 * @return the string containing the first instance of source found
	 * starting from a point in the string replaced with dest
	 */
	private static String replaceFrom(
		String string,
		String source,
		String dest,
		boolean caseSensitive,
		int from) {
		int fromArray[] = new int[1];
		fromArray[0] = from;
		return replaceFrom(string, source, dest, caseSensitive, fromArray);
	}

	/**
	 * Replace the first instance of part of a string with another string.
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 * @return the string containing the first instance of source replaced
	 * with dest
	 */
	public static String replace(
		String string,
		String source,
		String dest,
		boolean caseSensitive) {
		return replaceFrom(string, source, dest, caseSensitive, 0);
	}

	/**
	 * Replace all instances of part of a string with another string
	 * starting from a point in the string.
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 * @param from where to start
	 * @return the string containing all instances of source replaced with dest
	 */
	private static String replaceAll(
		String string,
		String source,
		String dest,
		boolean caseSensitive,
		int from) {
		int fromArray[] = new int[1];
		fromArray[0] = from;
		String newString =
			replaceFrom(string, source, dest, caseSensitive, fromArray);
		from = fromArray[0];
		//I don't see the need for compareTo or compareToIgnoreCase depending on caseSensitive,
		//but maybe I should like in the replace method
		if (newString.compareTo(string) == 0) { //it's the same, so return
			return newString;
		} else { //still something left to change
			return replaceAll(newString, source, dest, caseSensitive, from);
		}
	}

	/**
	 * Replace all instances of part of a string with another string
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 * @return the string containing all instances of source replaced with dest
	 */
	public static String replaceAll(
		String string,
		String source,
		String dest,
		boolean caseSensitive) {
		return replaceAll(string, source, dest, caseSensitive, 0);
	}

	/**
	 * Replaces whole words found in one string with another string.
	 * The whole words are delimted by delimiters defined in the DELIMTERES
	 * constant.
	 * 
	 * @param string is the string that contains the substring to be replaced
	 * @param pattern is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 * @param fromArray 0th element contains index of where to start searching
	 * @return the string containing one instances of source replaced with dest
	 */
	private static String replaceWholeWordsFrom(
		String string,
		String pattern,
		String dest,
		boolean caseSensitive,
		int[] fromArray) {

		boolean frontOK = false, backOK = false;
		// find index of the first occurence of the pattern string
		int index =
			(caseSensitive)
				? string.indexOf(pattern, fromArray[0])
				: string.toUpperCase().indexOf(
					pattern.toUpperCase(),
					fromArray[0]);

		//by default, if (index < 0) frontOK = false;
		// make sure that the front of the found pattern is prefixed with either
		// a delimeter character or nothing, since we are replacing WHOLE words
		// mark the front of the string to be OK if that is the case. Do similar
		// check for the back of the string
		if (index == 0)
			frontOK = true;
		else if (index > 0) {
			if (delims.indexOf(string.charAt(index - 1)) >= 0) {
				frontOK = true;
			}
		}

		//front is ok, check back
		if (frontOK) {
			if (index + pattern.length() >= string.length())
				backOK = true;
			else if (
				delims.indexOf(string.charAt((index + pattern.length()))) >= 0)
				backOK = true;

			if (backOK) {
				fromArray[0] = (index - 1 < 0) ? 0 : index + dest.length();
				return string.substring(0, (index - 1 < 0) ? 0 : index)
					+ dest
					+ ((index + pattern.length() > string.length())
						? StringStatics.BLANK
						: string.substring(
							index + pattern.length(),
							string.length()));
			}
		}

		if (index >= 0 /*&& (!frontOK || !backOK)*/
			&& index + 1 < string.length()) {
			fromArray[0] = index + 1;
			return replaceWholeWordsFrom(
				string,
				pattern,
				dest,
				caseSensitive,
				fromArray);
		}

		return string;

	}

	/**
	 * Replaces whole words found in one string with another string.
	 * The whole words are delimted by delimiters defined in the DELIMTERES
	 * constant.
	 * 
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 *
	 * @return the string containing the first instance of source replaced with
	 * dest.
	 */
	public static String replaceWholeWords(
		String string,
		String source,
		String dest,
		boolean caseSensitive) {

		int fromArray[] = new int[] {0};
		
		return replaceWholeWordsFrom(
			string,
			source,
			dest,
			caseSensitive,
			fromArray);
	}

	/**
	 * Replaces whole words found in one string with another string.
	 * The whole words are delimted by delimiters defined in the DELIMTERES
	 * constant.
	 * 
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that will be replaced if it is found
	 * in the string
	 * @param dest what the substring will be replaced with if it is found
	 * in the string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 *
	 * @return the string containing all instances of source replaced with
	 * dest.
	 */
	public static String replaceAllWholeWords(
		String string,
		String source,
		String dest,
		boolean caseSensitive) {

		int fromArray[] = new int[] {0};
		
		String oldResult = null,
			result =
				replaceWholeWordsFrom(
					string,
					source,
					dest,
					caseSensitive,
					fromArray);
		while (oldResult == null || !result.equals(oldResult)) {
			oldResult = result;
			result =
				replaceWholeWordsFrom(
					oldResult,
					source,
					dest,
					caseSensitive,
					fromArray);
		}
		return result;
	}

	/**
	 * Returns if a substring was found as a whole word in a string.
	 * The whole words are delimted by delimiters defined in the DELIMTERES
	 * constant.
	 * 
	 * @param string is the string that contains the substring to be replaced
	 * @param source is the substring that we are checking for in string
	 * @param caseSensitive true to do a case sensitive search, false to do
	 * a case insensitive search
	 *
	 * @return boolean true if the string was found as a word, false if the
	 * string was not found as a word.
	 */
	public static boolean doesWordExist(
		String string,
		String source,
		boolean caseSensitive) {
		StringTokenizer st = new StringTokenizer(string);

		while (st.hasMoreTokens()) {
			String token = st.nextToken();

			//== 0 if equal.
			if (((caseSensitive) && (token.compareTo(source) == 0))
				|| ((!caseSensitive)
					&& (token.toUpperCase().compareTo(source.toUpperCase())
						== 0))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Do not use Integer.parseInt().  This is doing something different.
	 * Based on code from Gleb's SetMultiplicityDialog.
	 * 
	 * @param string to check if it is a valid positive integer
	 * @return true if it's a valid positive integer, false if it isn't
	 */
	public static boolean isValidPositiveInteger(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) < '0' || string.charAt(i) > '9')
				return false;
		}
		return true;
	}

	/**
	 * Encode the url string
	 * 
	 * @param url String to encode
	 * @return String encoded URL
	 */
	public static String encodeURL(String url) {
		//find the ://
		int protocolIndex = url.indexOf(PROTOCOL_DELIMITER);
		
		if (protocolIndex > 0) {
			String start = url.substring(0, protocolIndex + PROTOCOL_DELIMITER_LENGTH);

			//find the first / after ;//
			int slashIndex = url.indexOf('/', protocolIndex + PROTOCOL_DELIMITER_LENGTH);
			if (slashIndex == -1) {
				slashIndex = url.length(); 
			}
			
			String domain = url.substring(protocolIndex + PROTOCOL_DELIMITER_LENGTH, slashIndex);

//			This code has been commented until we migrate to Java 2
//			IDN is only available in Java 2
//			private static final String DOMAIN_ENCODING = "ISO-8859-1";
//			try {
//				domain = java.net.IDN.toASCII(domain, DOMAIN_ENCODING);
//			} catch (IllegalArgumentException e) {
//				//this should not be logged, just do not convert the domain string
//			}

			//may be empty
			String end = url.substring(slashIndex, url.length());
			end = encodePercentage(end);
			end = encode(end);
			
			return start + domain + end;
		}
		return url;
	}
	
	/**
	 * Encodes %s in a string to %25 when not followed by a valid hex number.
	 *  
	 * @param string to encode
	 * @return String with %25 encoded
	 */
	private static String encodePercentage(String string) {
		for (int index = string.indexOf('%'); index != -1; ) {
			int length = string.length();
			
			if (length > index + 2) {
				//check the next 2 digits
				if (!isOKForHex(string.charAt(index + 1)) || !isOKForHex(string.charAt(index + 2))) {
					//encode % at index
					string = replace(string, index, ENCODED_PERCENT);
				}
			}
			else {
				//the string is too short for the numbers after % to be a valid
				//hex number
				string = replace(string, index, ENCODED_PERCENT);
			}
			
			index = string.indexOf('%', index + 1);
		}
		
		return string;
	}

	/**
	 * Verifies that a given character could be part of a valid hex number.
	 * 
	 * @param aChar checks that this char could be part of a valid hex number.
	 * @return true if the character could be part of a valid hex number.
	 */
	private static boolean isOKForHex(char aChar) {
		if ((aChar >= '0') && (aChar <= '9')) {
			return true;
		}
		if ((aChar >= 'a') && (aChar <= 'f')) {
			return true;
		}
		if ((aChar >= 'A') && (aChar <= 'F')) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a replaced string made up of the original string replaced with
	 * newPart at position index.
	 * 
	 * @param original String to replace
	 * @param index position to replace at
	 * @param newPart the new String to be replaced at position index
	 * @return replaced String made up of the original string replaced with
	 * newPart at position index.
	 */
	private static String replace(String original, int index, String newPart) {
		return original.substring(0, index) + newPart + original.substring(index + 1);
	}
	
	/**
	 * Finds a position of a special character that shouldn't be url encoded
	 * starting from the given start index.
	 * 
	 * Special characters are % / & = + ? #
	 * 
	 * @param string look for the special character in this string
	 * @param start index to start looking at the string from
	 * 
	 * @return position of a special character that shouldn't be url encoded
	 * starting from the given start index.  -1 if it could not find a special
	 * character that shouldn't be url encoded.
	 */
	private static int findUnEncodeableCharacter(String string, int start) {
		if (start >= string.length())
			return -1;
		int indices[] = new int[] {string.indexOf('%', start),
			string.indexOf('/', start), string.indexOf('&', start),
			string.indexOf('=', start), string.indexOf('+', start),
			string.indexOf('?', start), string.indexOf('#', start)
			};
		int index = -1;
		for (int i = 0; i < indices.length; i++) {
			if (indices[i] != -1 && indices[i] < index || index == -1) {
				index = indices[i];
			}
		}
		return index;
	}

	/**
	 * Runs URLEncoder.encode on a string, but excludes the special characters
	 * that should not be encoded.
	 * 
	 * It assumes that percentages that need to be encoded have already been
	 * encoded.  This may be done using the encodePercentage method.  Therefore,
	 * percentage characters are treated as special characters.
	 * 
	 * Special characters are determined by the findUnEncodeableCharacter
	 * method.
	 * 
	 * @param string
	 * @return
	 */
	private static String encode(String string) {
		int beginIndex = findUnEncodeableCharacter(string, 0);
		
		if (beginIndex == -1) beginIndex = 0;
		
		while (beginIndex < string.length()) {
			int endIndex = findUnEncodeableCharacter(string, beginIndex + 1);
			if (endIndex == -1) endIndex = string.length();
			
			String begin = string.substring(0, beginIndex + 1);
			String middle = string.substring(beginIndex + 1, endIndex);
			String end = string.substring(endIndex);
			
			//encode the middle
			try {
				middle = URLEncoder.encode(middle, URL_ENCODING);
			} catch (UnsupportedEncodingException e) {
				//UTF-8 should never be unavailable
				Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.ENCODING_FAILURE, URL_ENCODING + " unsupported."); //$NON-NLS-1$
				Trace.catching(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.EXCEPTIONS_CATCHING, StringUtil.class, "encode", e); //$NON-NLS-1$
			}
			
			beginIndex = begin.length() + middle.length();
			string = begin + middle + end;
		}
		return string;
		
	}
}
