/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
 package org.eclipse.gmf.runtime.emf.core.internal.util;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;

 /**
  * A type that requires a semantic hint for view creation. 
  * 
  * @author cmahoney
  */
 public interface IHintedType
 	extends IElementType {

 	/**
 	 * Gets the semantic hint required for view creation.
 	 * 
 	 * @return the semantic hint.
 	 */
 	String getSemanticHint();
 }
