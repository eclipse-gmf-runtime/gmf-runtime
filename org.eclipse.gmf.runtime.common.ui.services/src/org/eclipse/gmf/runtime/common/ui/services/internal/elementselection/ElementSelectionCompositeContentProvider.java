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
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractMatchingObject;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A standard structured content provider for the element selection composite.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public class ElementSelectionCompositeContentProvider
	implements IStructuredContentProvider {

	/**
	 * The input to the element selection composite.
	 */
	private IElementSelectionInput input;

	/**
	 * The list of matching objects for the element selection input.
	 */
	private List matchingObjects = null;

	/**
	 * The filter entered by the user in the element selection composite.
	 */
	private String filter;

	/**
	 * Constructor for the ElementSelectionCompositeContentProvider.
	 * 
	 * @param input
	 *            element selection input.
	 */
	public ElementSelectionCompositeContentProvider(IElementSelectionInput input) {
		super();
		this.input = input;
		this.filter = StringStatics.BLANK;
	}

	/**
	 * @inheritDoc
	 */
	public Object[] getElements(Object inputElement) {
		/*
		 * If the filter is blank, return no elements.
		 */
		if (filter.equals(StringStatics.BLANK)) {
			return new Object[0];
		}

		/*
		 * Initialize all possible matching objects from the select element
		 * service the first time getElements is called.
		 */
		if (matchingObjects == null) {
			List matches = ElementSelectionService.getInstance()
				.getMatchingObjects(input);
			matchingObjects = new ArrayList();
			for (Iterator iter = matches.iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				matchingObjects.addAll(element);
			}
		}

		/*
		 * Now filter the matching elements using the filter.
		 */
		List result = new ArrayList();
		Pattern pattern = Pattern.compile(filter);
		for (Iterator iter = matchingObjects.iterator(); iter.hasNext();) {
			AbstractMatchingObject element = (AbstractMatchingObject) iter
				.next();
			Matcher matcher = pattern.matcher(element.getName().toLowerCase());
			if (matcher.matches()) {
				result.add(element);
			}
		}

		return result.toArray();
	}

	/**
	 * @inheritDoc
	 */
	public void dispose() {
		// not implemented
	}

	/**
	 * @inheritDoc
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			assert newInput instanceof String;
			filter = validatePattern((String) newInput);
		}
	}

	/**
	 * Convert the UNIX style pattern entered by the user to a Java regex
	 * pattern (? = any character, * = any string).
	 * 
	 * @param string
	 *            the UNIX style pattern.
	 * @return a Java regex pattern.
	 */
	private String validatePattern(String string) {
		if (string.equals(StringStatics.BLANK)) {
			return string;
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			char c = Character.toLowerCase(string.charAt(i));
			if (c == '?') {
				result.append('.');
			} else if (c == '*') {
				result.append(".*"); //$NON-NLS-1$
			} else if (c == '?') {
				result.append("\\."); //$NON-NLS-1$
			} else {
				result.append(c);
			}
		}
		result.append(".*"); //$NON-NLS-1$
		return result.toString();
	}
}
