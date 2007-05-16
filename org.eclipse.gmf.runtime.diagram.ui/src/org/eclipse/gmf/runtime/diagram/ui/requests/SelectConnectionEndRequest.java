package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;

public class SelectConnectionEndRequest extends Request{
	
	ConnectionEditPart connectionEditPart = null;
	boolean selectSource = false;
	
	
	public SelectConnectionEndRequest(boolean selectSource){
		this.selectSource = selectSource;
	}
	
	public boolean isSelectSource(){
		return selectSource;
	}
	
	public boolean isSelectTarget(){
		return !selectSource;
	}
	
	public void setConnectionEdtiPart(ConnectionEditPart connectionEditPart){
		this.connectionEditPart = connectionEditPart;
	}
	
	public ConnectionEditPart getConnectionEdtiPart(){
		return connectionEditPart;
	}

}
