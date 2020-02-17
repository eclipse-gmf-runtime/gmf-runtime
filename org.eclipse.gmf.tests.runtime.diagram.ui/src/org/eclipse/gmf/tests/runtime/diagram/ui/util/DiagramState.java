/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author gsturov
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class DiagramState {
	
	public ArrayList props = new ArrayList();
	
	public DiagramState(DiagramEditPart diagramEditPart) {

		//EditPartViewer viewer = diagramEditPart.getViewer();
		final Diagram d = (Diagram) diagramEditPart.getModel();
		
		ArrayList views = new ArrayList();
		views.addAll(d.getChildren());
		
		Collections.sort(views, new Comparator() {
			public int compare(Object o1, Object o2) {
				return (ViewUtil.getIdStr((View)o1).compareTo(ViewUtil.getIdStr(((View) o2))));
			}
		});
		
//		for (Iterator it = views.iterator(); it.hasNext();) {
			//TODO sc - add check back in
//			IView view = (IView) it.next();
//			props.add(view.getIdStr());
//			IGraphicalEditPart editpart = (IGraphicalEditPart) viewer.getEditPartRegistry().get(view);
//			IPropertySource propSource = (IPropertySource) editpart.getAdapter(IPropertySource.class);
//			IPropertyDescriptor [] descriptors = propSource.getPropertyDescriptors();
//            
//
//            ArrayList descriptorList= new ArrayList();
//            descriptorList.addAll(Arrays.asList(descriptors));
//            
//            // We are sorting the list of properties based on their id
//            // Before we had assumed that the order of the properties was
//            // always the same if the view had the same set of properties
//            // this assumption is now incorrect.  Don't know why .. just
//            // start changed after a build.
//            Collections.sort(descriptorList,new Comparator() {
//               public int compare(Object ob1,Object ob2){
//                String a1= ((IPropertyDescriptor)ob1).getId().toString();
//                String a2 =((IPropertyDescriptor)ob2).getId().toString();
//                return a1.compareTo(a2);
//               }
//            });
//            
//            for (Iterator it1 = descriptorList.iterator(); it1.hasNext();){
//                IPropertyDescriptor descriptor = (IPropertyDescriptor)it1.next();
//                ILabelProvider label = descriptor.getLabelProvider();
//                Object id = descriptor.getId();
//                String name = descriptor.getDisplayName();
//                String val = label.getText(propSource.getPropertyValue(id));
//                
//                props.add("  " + name + " = " + val); //$NON-NLS-1$ //$NON-NLS-2$
//            }           			
//		
		//}
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof DiagramState))
			return false;
			
		DiagramState state = (DiagramState) obj;
		
		int i1 = props.size();
		int i2 = state.props.size();
		
		if (i1 > i2) {
			reportDifference(i1 - 1, state);
			return false;
		}
		
		if (i2 > i1) {
			reportDifference(i2 - 1, state);
			return false;
		}
		
		int i = 0;
		for (Iterator it = props.iterator(); it.hasNext();) {
			String s1 = (String) it.next();
			String s2 = (String) state.props.get(i);
			if (!s1.equals(s2)) {
				reportDifference(i, state);
				return false;
			}
			i++;
		}
		
		return true;		
	}
	
	public void reportDifference(int propIndex, DiagramState state) {
		
		System.out.println("-----------------------------------------------------"); //$NON-NLS-1$
		System.out.println("Diagram Status difference report. See the last entry!"); //$NON-NLS-1$
		
		int i1 = props.size();
		int i2 = state.props.size();
		
		for (int i = 0; i <= propIndex; i++) {
			String s1 = i <= i1 - 1 ? (String) props.get(i) : "---------"; //$NON-NLS-1$
			String s2 = i <= i2 - 1 ? (String) state.props.get(i) : "---------"; //$NON-NLS-1$
			String s = s1 + "    " + s2; //$NON-NLS-1$
			System.out.println(s);
		}
		System.out.println("-----------------------------------------------------"); //$NON-NLS-1$
	}
}

