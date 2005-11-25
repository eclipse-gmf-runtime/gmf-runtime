/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.l10n.SharedImages;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;


/**
 * Provides Geoshape Icons
 * 
 *  @author jschofie
 *  @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class DiagramIconProvider extends AbstractProvider
implements IIconProvider {
		
	/** map for storing icon images based on type */
	private static HashMap typeIconMap = new HashMap();
	static {
  		typeIconMap.put(DiagramNotationType.NOTE, SharedImages.IMG_NOTE);
    	typeIconMap.put(DiagramNotationType.TEXT, SharedImages.IMG_TEXT);		
	}



	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider#getIcon(org.eclipse.core.runtime.IAdaptable, int)
	 */
	public Image getIcon(IAdaptable hint, int flags) {
		
		if( hint instanceof View && DiagramViewProvider.isTextView((View)hint)) {
			return SharedImages.get(SharedImages.IMG_TEXT);
		}
		
		else if( hint instanceof View && DiagramViewProvider.isNoteView((View)hint)) {
			
			return SharedImages.get(SharedImages.IMG_NOTE);
		}
		
		else if (hint.getAdapter(IElementType.class) != null){
			String fileName = (String) typeIconMap.get(hint);
			return SharedImages.get(fileName);
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
		   (DiagramViewProvider.isNoteView((View)adapter)
		   	|| DiagramViewProvider.isTextView((View)adapter))) {
			return true;
		}
		
		if (oper.getHint().getAdapter(IElementType.class) != null) {
			String fileName = (String) typeIconMap.get(oper.getHint());
				return (fileName != null);				
		}
				
		return false;
	}

}
