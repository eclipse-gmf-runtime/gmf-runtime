/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import java.util.Dictionary;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * A class that encapsulates data for appearance properties change request.
 * Each request contains a map which will help the policy to navigate through
 * the properties.
 * 
 * Each entry in the map is the factory hint of the edit part as key and a 
 * dictionary of appearance properties  as a value. 
 * 
 * Each map describes appearance properties of a top-level shape together with it's
 * children.
 * 
 * For example, the connectable shape edit part with name, attribute, operation and
 *  shape compartments will return a map where:
 *  1 entry: 
 * 		connectable shape factory hint ->  dictionary:
 * 										   ID_FONT  -> font data
 * 										   ID_FONTCOLOR -> font color
 * 										   ID_LINECOLOR -> line color
 * 										   ID_FILLCOLOR -> fill color
 *  2d entry: attribute compartment hint -> dictionary(empty)
 *  3d entry: operation compartment hint -> dictionary(empty)
 *  4d entry: shape compartment hint -> dictionary(empty)
 * 
 * @author Natalia Balaba
 * @canBeSeenBy %level1
 */
public class ApplyAppearancePropertiesRequest extends Request {

	/**
	 * A map which describes appearance properties
	 */
	private Map properties = null;

	/**
	 * Create ApplyAppearancePropertiesRequest given the properties map.
	 * @param - map hich describes appearance properties
	 */
	public ApplyAppearancePropertiesRequest() {
		super(RequestConstants.REQ_APPLY_APPEARANCE_PROPERTIES);
	}
	
	/**
	 * @param map
	 */
	public void setProperties(Map map) {
		properties = map;
	}

	/**
	 * Return all the factpry hints present in the request
	 * @return - set of hints (strings)
	 */
	public Set getSemanticHints(){
		return properties.keySet();
	}
	
	/**
	 * Return properties dictionary for the given factory hint. The dictionary
	 * contains property ids as keys and property values as values (java objects,
	 * not just strings like in java.util.Properties)
	 * 
	 * @param semanticHint - facytory hint
	 * @return - properties dictionary for the given factory hint
	 */
	public Dictionary getPropertiesFor(String semanticHint){
		return (Dictionary) properties.get(semanticHint);
	}

}
