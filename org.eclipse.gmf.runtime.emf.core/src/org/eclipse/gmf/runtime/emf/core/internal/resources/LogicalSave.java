/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;

import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;

/**
 * Customization of the XML serialization (saving) for logical resources.
 * Special requirements for logical resources include:
 * <ul>
 *   <li>the determination of what is a cross-doc and what is a same-doc
 *       reference is based on the physical URI rather than the logical URI</li>
 *   <li>elements in the content tree that are stored in other resources are
 *       skipped (not saved in this resource)</li>
 * </ul>
 *
 * @author Christian W. Damus (cdamus)
 */
public class LogicalSave
	extends XMISaveImpl {

	private LogicalHelper logicalHelper;
	private LogicalResource logicalResource;
	private boolean savingResourceMap = false;
	
	public LogicalSave(XMLHelper helper) {
		super(helper);
		
		if (helper instanceof LogicalHelper) {
			logicalHelper = (LogicalHelper) helper;
			logicalResource = logicalHelper.getLogicalResource();
		}
	}

	protected void saveElementID(EObject o) {
		if (o instanceof ResourceMap && logicalHelper != null) {
			// writing the sub-unit map.  It needs to encode child references
			//    using physical URIs.  The rest of the resource
			//    needs to encode intra-logical-model references as relative
			//    to the root resource URI
			savingResourceMap = true;
			logicalHelper.setUrisRelativeToRoot(false);
			super.saveElementID(o);
			logicalHelper.setUrisRelativeToRoot(true);
			savingResourceMap = false;
		} else {
			super.saveElementID(o);
		}
	}

	protected void saveElement(EObject o, EStructuralFeature f) {
		// if we're saving an AnyType, then it is coming from the extended
		//    data (compatibility mode), so we cannot check for separateness
		//    (which it wouldn't be, anyway)
		if (savingResourceMap || logicalResource == null ||
				(o instanceof AnyType) || !logicalResource.isSeparate(o)) {
			super.saveElement(o, f);
		}
	}
	
	protected int sameDocSingle(EObject o, EStructuralFeature f) {
	    EObject value = (EObject) helper.getValue(o, f);
		if (value == null) {
			return SKIP;
		} else if (value.eIsProxy()) {
			return CROSS_DOC;
		} else {
			return isSameDocument(o, value) ? SAME_DOC : CROSS_DOC;
		}
	}
	
	private boolean isSameDocument(EObject source, EObject target) {
		boolean result = false;
		
	      Resource targetRes = target.eResource();
	      if (targetRes instanceof LogicalResource && logicalHelper != null) {
	    	  // can only have same-doc ref if both resources are logical
	    	  LogicalResourceUnit sourceRes = logicalHelper.getUnit();
		      
		      result = sourceRes == ((LogicalResource) targetRes).getUnit(target);
	      } else {
	    	  result = targetRes == helper.getResource();
	      }
		
		return result;
	}
	
	protected int sameDocMany(EObject o, EStructuralFeature f) {
		InternalEList values = (InternalEList) helper.getValue(o, f);
		if (values.isEmpty()) {
			return SKIP;
		}

		for (Iterator i = values.basicIterator(); i.hasNext();) {
			EObject value = (EObject) i.next();
			if (value.eIsProxy()) {
				return CROSS_DOC;
			} else if (!isSameDocument(o, value)) {
				return CROSS_DOC;
			}
		}

		return SAME_DOC;
	}
}
