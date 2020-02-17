/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.examples.runtime.emf.clipboard.library;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.examples.extlibrary.EXTLibraryPackage;

import org.eclipse.gmf.runtime.emf.clipboard.core.AbstractClipboardSupport;
import org.eclipse.gmf.runtime.emf.clipboard.core.PasteAction;


/**
 * Implementation of copy/paste semantics peculiar to the <code>Library</code>
 * metamodel.  The special semantics that we implement are:
 * <ul>
 *   <li>whenever an Author is copied, all of his/her books are copied, also,
 *       though they are not contained by the author</li>
 *   <li>whenever an Author that is pasted would have the same name as another
 *       already in the destination library, then we don't create a duplicate
 *       but merge the new author with the existing one.  This effectively
 *       adds all of the new author's books that we have copied from another
 *       library to the existing author who, having the same name, is assumed
 *       to be the same person</li>
 *   <li>hints to determine the names of objects, which names are not always
 *       implemented by attributes named "name"</li>
 * </ul>
 *
 * @author Christian W. Damus (cdamus)
 */
class LibraryClipboardSupport
	extends AbstractClipboardSupport {

	/**
	 * Initializes me.
	 */
	public LibraryClipboardSupport() {
		super();
	}

	/**
	 * Provide a mapping of name attributes for the <code>EClass</code>es of
	 * the Library metamodel.
	 */
	protected EAttribute getNameAttribute(EClass eClass) {
		EAttribute result;
		
		switch (eClass.getClassifierID()) {
		case EXTLibraryPackage.BOOK:
			result = EXTLibraryPackage.eINSTANCE.getBook_Title();
			break;
		case EXTLibraryPackage.LIBRARY:
			result = EXTLibraryPackage.eINSTANCE.getLibrary_Name();
			break;
		case EXTLibraryPackage.WRITER:
			result = EXTLibraryPackage.eINSTANCE.getWriter_Name();
			break;
		default:
			result = null;
			break;
		}
		
		return result;
	}
	
	/**
	 * Merge an author into an existing author of the same name when pasting.
	 */
	public PasteAction getPasteCollisionAction(EClass eClass) {
		if (eClass == EXTLibraryPackage.eINSTANCE.getWriter()) {
			return PasteAction.MERGE;
		} else {
			return super.getPasteCollisionAction(eClass);
		}
	}
	
	/**
	 * We always copy an author's books.
	 */
	public boolean isCopyAlways(EObject context, EReference eReference,
			Object value) {
		
		if (eReference == EXTLibraryPackage.eINSTANCE.getWriter_Books()) {
			return true;
		} else {
			return super.isCopyAlways(context, eReference, value);
		}
	}
}
