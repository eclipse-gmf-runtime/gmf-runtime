/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;

/**
 * @author mmostafa
 * 
 * request to select one of the connection's ends
 *
 */
public class SelectConnectionEndRequest extends Request{
	
	ConnectionEditPart connectionEditPart = null;
	boolean selectSource = false;
	
	
	/**
     * constructor
	 * @param selectSource, true means select source otherwise it will select target
	 */
	public SelectConnectionEndRequest(boolean selectSource){
		this.selectSource = selectSource;
	}
	
	/**
     * checks if this is a select source request
	 * @return true is select source request, otherwise false
	 */
	public boolean isSelectSource(){
		return selectSource;
	}
	
    /**
     * checks if this is a select target request
     * @return true is select target request, otherwise false
     */
	public boolean isSelectTarget(){
		return !selectSource;
	}
	
	/**
     * sets the connection edit part on the request
	 * @param connectionEditPart
	 */
	public void setConnectionEdtiPart(ConnectionEditPart connectionEditPart){
		this.connectionEditPart = connectionEditPart;
	}
	
	/**
     * access the connection edit part of this request
	 * @return the request's connection edit part
	 */
	public ConnectionEditPart getConnectionEdtiPart(){
		return connectionEditPart;
	}

}
