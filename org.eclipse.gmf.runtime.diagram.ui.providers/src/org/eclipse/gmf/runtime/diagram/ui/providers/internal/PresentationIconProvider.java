/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.PresentationNotationType;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramProvidersResourceManager;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import com.ibm.xtools.notation.View;


/**
 * Provides Geoshape Icons
 * 
 *  @author jschofie
 *  @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class PresentationIconProvider extends AbstractProvider
implements IIconProvider {
	
	private static final String ICON_NOTE = "note.gif";  //$NON-NLS-1$
	private static final String ICON_TEXT = "text.gif"; //$NON-NLS-1$
	
	/** map for storing icon images based on type */
	private static HashMap typeIconMap = new HashMap();
	static {
  		typeIconMap.put(PresentationNotationType.NOTE, ICON_NOTE);
    	typeIconMap.put(PresentationNotationType.TEXT, ICON_TEXT);		
	}



	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider#getIcon(org.eclipse.core.runtime.IAdaptable, int)
	 */
	public Image getIcon(IAdaptable hint, int flags) {
		
		if( hint instanceof View && PresentationViewProvider.isTextView((View)hint)) {
			return DiagramProvidersResourceManager.getInstance().getImage(ICON_TEXT);
		}
		
		else if( hint instanceof View && PresentationViewProvider.isNoteView((View)hint)) {
			
			return DiagramProvidersResourceManager.getInstance().getImage(ICON_NOTE);
		}
		
		else if (hint.getAdapter(IElementType.class) != null){
			String fileName = (String) typeIconMap.get(hint);
			return DiagramProvidersResourceManager.getInstance().getImage(fileName);
		}
		return null;	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {

		GetIconOperation oper = (GetIconOperation)operation;
		
		IAdaptable adapter = oper.getHint();
		
		if (adapter == null){
			return false;
		}
				
		if(adapter instanceof View && 
		   (PresentationViewProvider.isNoteView((View)adapter)
		   	|| PresentationViewProvider.isTextView((View)adapter))) {
			return true;
		}
		
		if (oper.getHint().getAdapter(IElementType.class) != null) {
			String fileName = (String) typeIconMap.get(oper.getHint());
				return (fileName != null);				
		}
				
		return false;
	}

}
