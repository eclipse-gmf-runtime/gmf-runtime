/******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg;

import java.awt.Color;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.svggen.SVGColor;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGPaintDescriptor;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/**
 * @author sshaw
 *
 * Utility class that will replace Black and white with corresponding outline and
 * fill colors respectively.
 */
public class SVGColorConverter {
	
	static private SVGColorConverter INSTANCE = new SVGColorConverter();
	
	/**
	 * getInstance
	 * Implementation of the singleton pattern
	 * 
	 * @return the Singleton Instance of SVGColorConverter
	 */
	static public SVGColorConverter getInstance() {
		return INSTANCE;
	}
	/**
	 * 
	 */
	private SVGColorConverter() {
		super();
	}
	
	/**
	 * replaceDocumentColors
	 * Given an SVGOMDocument, this method will replace all fill, stroke and gradient
	 * colors with values that are based in.  Black color will be replaced by the given
	 * outlineColor and White color will be replaced by the given fillColor.
	 * 
	 * @param svgDoc SVGOMDocument to perform the replace on.
	 * @param fillColor This is the fill color that will replace all occurrences of white
	 * @param outlineColor This is the outline color that will replace all occurrences of black.
	 */
	public void replaceDocumentColors(SVGOMDocument svgDoc, Color fillColor, Color outlineColor) {
		SVGSVGElement svgRoot = svgDoc.getRootElement();
		NodeIterator ni = svgDoc.createNodeIterator(svgRoot, NodeFilter.SHOW_ALL, null, true);
		Node node = ni.nextNode();
		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node; 
				
				if (fillColor != null) {
					setColorAttribute(svgDoc, element, SVGConstants.SVG_FILL_ATTRIBUTE, false, fillColor);
					setColorAttribute(svgDoc, element, SVGConstants.SVG_STOP_COLOR_ATTRIBUTE, false, fillColor);
				}
				
				if (outlineColor != null) {
					setColorAttribute(svgDoc, element, SVGConstants.SVG_STROKE_ATTRIBUTE, true, outlineColor);
					setColorAttribute(svgDoc, element, SVGConstants.SVG_STOP_COLOR_ATTRIBUTE, true, outlineColor);
				}
			}
			
			node = ni.nextNode();
		}
	}
	
	public static final String SEMICOLON = ";"; //$NON-NLS-1$
	public static final String COLON = ":"; 	//$NON-NLS-1$		

	private boolean isBlack(String color) {
		String cleanColorStr = cleanFromSpaces(color);
		return cleanColorStr.equals("#000000") || cleanColorStr.equalsIgnoreCase("BLACK") || cleanColorStr.equalsIgnoreCase("rgb(0,0,0)");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}
	
	private boolean isWhite(String color) {
		String cleanColorStr = cleanFromSpaces(color);
		return cleanColorStr.equalsIgnoreCase("#FFFFFF") || cleanColorStr.equalsIgnoreCase("WHITE") || cleanColorStr.equalsIgnoreCase("rgb(255,255,255)");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}	
	
	private String cleanFromSpaces(String s) {
		char [] chars = new char[s.length()];
		int index = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				chars[index++] = s.charAt(i); 
			}
		}
		return new String(chars, 0, index);
	}
	
	/**
	 * setColorAttribute
	 * Utility function that will set the color attribute to the specified color
	 * 
	 * @param element Element to set the attribute color of
	 * @param attributeName String attribute name, usually one of SVGConstants.SVG_FILL_ATTRIBUTE or
	 * SVGConstants.SVG_STROKE_ATTRIBUTE
	 * @param boolean black is true if replace with color, false to replace white.
	 * @param color Color to change the attribute to.
	 */
	protected void setColorAttribute(SVGOMDocument svgDoc, Element element, String attributeName, boolean black, Color color) {
		SVGPaintDescriptor svgPD = SVGColor.toSVG(color, SVGGeneratorContext.createDefault(svgDoc));

		if (element.hasAttribute(attributeName)) {
			String value = element.getAttribute(attributeName);

			// Only change the Stroke color if the current color is black
			if( attributeName.equals(SVGConstants.SVG_STROKE_ATTRIBUTE) ||
				attributeName.equals(SVGConstants.SVG_FILL_ATTRIBUTE)) {
				
				if( (black && isBlack(value)) || (!black && isWhite(value)) ) {  
					element.setAttribute(attributeName, svgPD.getPaintValue());
				}
			}
		}
		else if (element.hasAttribute(SVGConstants.SVG_STYLE_TAG)) {
			//TODO There must be a better way to set fill / outline color with the Batik libraries.
			String style = element.getAttribute(SVGConstants.SVG_STYLE_TAG);
						
			String preColor = ""; //$NON-NLS-1$
			int nStart = style.indexOf(attributeName + COLON);
			if (nStart != -1) {
				if (nStart > 0)
					preColor = style.substring(0, nStart);
								
				String postColor = ""; //$NON-NLS-1$
				int nEnd = style.indexOf(SEMICOLON, nStart);
				if (nEnd == -1)
					nEnd = style.length();
				if (nEnd < style.length() - 1)
					postColor = style.substring(nEnd + 1, style.length());
				
				String currentColor = style.substring(nStart+attributeName.length() + 1, nEnd);
				if( (black && isBlack(currentColor)) || (!black && isWhite(currentColor))) {
					if (!currentColor.equals(SVGConstants.SVG_NONE_VALUE)) {				
						StringBuffer styleBuffer = new StringBuffer(style.length() + 5);
						styleBuffer.append(preColor);
						styleBuffer.append(" "); //$NON-NLS-1$
						styleBuffer.append(attributeName);
						styleBuffer.append(COLON);
						styleBuffer.append(svgPD.getPaintValue());
						if (!postColor.equals("")) {	//$NON-NLS-1$
							styleBuffer.append(SEMICOLON);
							styleBuffer.append(postColor);
						}
						String styleNew = styleBuffer.toString();
										
						element.setAttribute(SVGConstants.SVG_STYLE_TAG, styleNew);
					}
			    }
			}
		}
	}
}
