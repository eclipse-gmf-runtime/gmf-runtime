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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.NoteViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.RGB;

/**
 * @author crevells
 */
public class LabelViewProvider
    extends AbstractViewProvider {

    static public class GEFLabelNoteViewFactory
        extends NoteViewFactory {

        protected void decorateView(View containerView, View view,
                IAdaptable semanticAdapter, String semanticHint, int index,
                boolean persisted) {
            initializeFromPreferences(view);

            FillStyle fillStyle = (FillStyle) view
                .getStyle(NotationPackage.Literals.FILL_STYLE);
            if (fillStyle != null) {
                // fill color
                RGB fillRGB = ColorConstants.lightGray.getRGB();
                fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB)
                    .intValue());
            }

            getViewService().createNode(semanticAdapter, view,
                LabelConstants.VIEWTYPE_GEFLABEL, ViewUtil.APPEND, persisted,
                getPreferencesHint());
        }
    }

    static public class WrappingLabelNoteViewFactory
        extends NoteViewFactory {

        protected void decorateView(View containerView, View view,
                IAdaptable semanticAdapter, String semanticHint, int index,
                boolean persisted) {
            initializeFromPreferences(view);

            FillStyle fillStyle = (FillStyle) view
                .getStyle(NotationPackage.Literals.FILL_STYLE);
            if (fillStyle != null) {
                // fill color
                RGB fillRGB = ColorConstants.lightBlue.getRGB();
                fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB)
                    .intValue());
            }

            getViewService().createNode(semanticAdapter, view,
                LabelConstants.VIEWTYPE_WRAPPINGLABEL, ViewUtil.APPEND,
                persisted, getPreferencesHint());
        }
    }

    static public class OldWrapLabelNoteViewFactory
        extends NoteViewFactory {

        protected void decorateView(View containerView, View view,
                IAdaptable semanticAdapter, String semanticHint, int index,
                boolean persisted) {
            initializeFromPreferences(view);

            FillStyle fillStyle = (FillStyle) view
                .getStyle(NotationPackage.Literals.FILL_STYLE);
            if (fillStyle != null) {
                // fill color
                RGB fillRGB = ColorConstants.lightGreen.getRGB();
                fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB)
                    .intValue());
            }

            getViewService().createNode(semanticAdapter, view,
                LabelConstants.VIEWTYPE_OLDWRAPLABEL, ViewUtil.APPEND,
                persisted, getPreferencesHint());
        }
    }

    static public class WrapLabelNoteViewFactory
        extends NoteViewFactory {

        protected void decorateView(View containerView, View view,
                IAdaptable semanticAdapter, String semanticHint, int index,
                boolean persisted) {
            initializeFromPreferences(view);

            FillStyle fillStyle = (FillStyle) view
                .getStyle(NotationPackage.Literals.FILL_STYLE);
            if (fillStyle != null) {
                // fill color
                RGB fillRGB = ColorConstants.yellow.getRGB();
                fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB)
                    .intValue());
            }

            getViewService().createNode(semanticAdapter, view,
                LabelConstants.VIEWTYPE_WRAPLABEL, ViewUtil.APPEND,
                persisted, getPreferencesHint());
        }
    }
    /** list of supported shape views. */
    static private final Map nodeMap = new HashMap();
    static {
        nodeMap.put(LabelConstants.VIEWTYPE_GEFLABEL_NOTE,
            GEFLabelNoteViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_WRAPPINGLABEL_NOTE,
            WrappingLabelNoteViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_OLDWRAPLABEL_NOTE,
            OldWrapLabelNoteViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_WRAPLABEL_NOTE,
            WrapLabelNoteViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_GEFLABEL,
            BasicNodeViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_WRAPPINGLABEL,
            BasicNodeViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_OLDWRAPLABEL,
            BasicNodeViewFactory.class);
        nodeMap.put(LabelConstants.VIEWTYPE_WRAPLABEL,
            BasicNodeViewFactory.class);
    }

    /**
     * Returns the shape view class to instantiate based on the passed params
     * 
     * @param semanticAdapter
     * @param containerView
     * @param semanticHint
     * @return Class
     */
    protected Class getNodeViewClass(IAdaptable semanticAdapter,
            View containerView, String semanticHint) {
        if (semanticHint != null && semanticHint.length() > 0)
            return (Class) nodeMap.get(semanticHint);
        return (Class) nodeMap.get(getSemanticEClass(semanticAdapter));
    }

}
