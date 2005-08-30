/*
 * Created on Apr 28, 2003
 *
 */
package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.tools.ConnectionEndpointTracker;

/**
 * @author mmuszyns
 *
 * This class overrides its superclass in order to fix 
 * a problem with proper tracking feedback for zooming
 * and scrolling.
 */
public class ConnectorEndpointTracker extends ConnectionEndpointTracker {
	
	/**
	 * constructor
	 * @param cep the connection edit part to use with this tracker
	 */
	public ConnectorEndpointTracker(ConnectionEditPart cep) {
		super(cep);
	}

	/*
	 * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
	 * 
	 * superclass implementation is overriden to fix a problem
	 * with proper tracking feedback for zooming and scrolling
	 */
	protected void updateTargetRequest() {
		ReconnectRequest request = (ReconnectRequest)getTargetRequest();
		Point p = getLocation();
		getConnection().translateToRelative(p);
		request.setLocation(p);
	}

}
