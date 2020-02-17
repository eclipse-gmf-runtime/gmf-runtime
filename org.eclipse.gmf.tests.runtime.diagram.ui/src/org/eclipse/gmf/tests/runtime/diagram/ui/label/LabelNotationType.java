/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;

/**
 * Element types for notation elements defined in the Diagram UI plugin.
 * 
 * @author crevells
 */
public class LabelNotationType
    extends AbstractElementTypeEnumerator {

    public static final INotationType GEFLABEL_NOTE = (INotationType) getElementType(LabelConstants.ID_GEFLABEL);

    public static final INotationType WRAPPINGLABEL_NOTE = (INotationType) getElementType(LabelConstants.ID_WRAPPINGLABEL);

    public static final INotationType OLDWRAPLABEL_NOTE = (INotationType) getElementType(LabelConstants.ID_OLDWRAPLABEL);

    public static final INotationType WRAPLABEL_NOTE = (INotationType) getElementType(LabelConstants.ID_WRAPLABEL);
}
