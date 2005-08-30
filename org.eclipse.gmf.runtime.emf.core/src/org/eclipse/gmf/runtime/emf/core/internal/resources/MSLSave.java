/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl;

import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;

/**
 * This class changes the behavior of the default XMISaver so that a ClearCase
 * token is written at the top of the file.
 * 
 * @author rafikj
 */
public class MSLSave
	extends LogicalSave {

	/**
	 * Discards control characters 0x00 - 0x1F except for TAB, CR, and LF, which
	 * are escaped by EMF.
	 * 
	 * @author khussey
	 * 
	 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=50403"
	 * @see "http://greenbytes.de/tech/webdav/rfc3470.html#binary"
	 */
	protected static class Escape
		extends XMLSaveImpl.Escape {

		private static final char[] NOTHING = {};

		public String convert(String input) {
			boolean changed = false;
			int inputLength = input.length();
			grow(inputLength);
			input.getChars(0, inputLength, value, 0);
			int pos = 0;

			while (inputLength-- > 0) {

				switch (value[pos]) {
					case '&':
						pos = replace(pos, AMP, inputLength);
						changed = true;
						break;
					case '<':
						pos = replace(pos, LESS, inputLength);
						changed = true;
						break;
					case '"':
						pos = replace(pos, QUOTE, inputLength);
						changed = true;
						break;
					case '\n':
						pos = replace(pos, LF, inputLength);
						changed = true;
						break;
					case '\r':
						pos = replace(pos, CR, inputLength);
						changed = true;
						break;
					case '\t':
						pos = replace(pos, TAB, inputLength);
						changed = true;
						break;
					default:
						if (value[pos] < 0x20) {
							// these characters are illegal in XML
							pos = replace(pos, NOTHING, inputLength);
							changed = true;
						} else {
							pos++;
						}
						break;
				}
			}

			return changed ? new String(value, 0, pos)
				: input;
		}
	}

	/**
	 * Constructor.
	 */
	public MSLSave(XMLHelper helper) {
		super(helper);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl#init(org.eclipse.emf.ecore.xmi.XMLResource,
	 *      java.util.Map)
	 */
	protected void init(XMLResource resource, Map options) {
		super.init(resource, options);

		if (null != escape) {
			escape = new Escape();
		}
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl#writeTopObjects(java.util.List)
	 */
	public Object writeTopObjects(List contents) {

		writeCCToken();
		return super.writeTopObjects(contents);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl#writeTopObject(org.eclipse.emf.ecore.EObject)
	 */
	protected Object writeTopObject(EObject top) {

		writeCCToken();
		writeArtifactVersionToken();
		return super.writeTopObject(top);
	}

	private void writeArtifactVersionToken() {
		// TODO The following token will be replaced by a future infrastructure that will
		//  allow client of MSL to specify their application ID and version number for
		//  backward/forward compatibility.
		doc.add("<?RSA version=\"7.0\"?>"); //$NON-NLS-1$
		doc.addLine();
	}

	/**
	 * Write ClearCase token.
	 */
	private void writeCCToken() {
		doc.add("<!--" + MSLConstants.CC_TOKEN + "-->"); //$NON-NLS-1$ //$NON-NLS-2$
		
		doc.addLine();
	}
}