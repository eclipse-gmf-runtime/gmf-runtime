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

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DescriptionCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.diagram.ui.label.LabelExDelegate;
import org.eclipse.gmf.runtime.diagram.ui.label.WrappingLabelDelegate;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.draw2d.ui.figures.LabelEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author crevells
 */
public class LabelEditPartProvider
    extends AbstractEditPartProvider {

    static public class GEFLabelNoteEditPart
        extends NoteEditPart {

        public GEFLabelNoteEditPart(View view) {
            super(view);
        }

        public EditPart getPrimaryChildEditPart() {
            return getChildBySemanticHint(LabelConstants.VIEWTYPE_GEFLABEL);
        }
    }

    static public class WrappingLabelNoteEditPart
        extends NoteEditPart {

        public WrappingLabelNoteEditPart(View view) {
            super(view);
        }

        public EditPart getPrimaryChildEditPart() {
            return getChildBySemanticHint(LabelConstants.VIEWTYPE_WRAPPINGLABEL);
        }
    }

    static public class OldWrapLabelNoteEditPart
        extends NoteEditPart {

        public OldWrapLabelNoteEditPart(View view) {
            super(view);
        }

        public EditPart getPrimaryChildEditPart() {
            return getChildBySemanticHint(LabelConstants.VIEWTYPE_OLDWRAPLABEL);
        }
    }

    static public class WrapLabelNoteEditPart
        extends NoteEditPart {

        public WrapLabelNoteEditPart(View view) {
            super(view);
        }

        public EditPart getPrimaryChildEditPart() {
            return getChildBySemanticHint(LabelConstants.VIEWTYPE_WRAPLABEL);
        }
    }

    static public class GEFLabelTextCompartmentEditPart
        extends DescriptionCompartmentEditPart {

        public GEFLabelTextCompartmentEditPart(View view) {
            super(view);
        }

        protected IFigure createFigure() {
            LabelEx label = new LabelEx();
            return label;
        }

        protected ILabelDelegate createLabelDelegate() {
            Assert.isTrue(getFigure() instanceof LabelEx);
            return new LabelExDelegate((LabelEx) getFigure());
        }

        public IParser getParser() {
            if (parser == null) {
                EObject object = getPrimaryView();
                ParserHintAdapter hintAdapter = new ParserHintAdapter(object,
                    CommonParserHint.DESCRIPTION);
                parser = ParserService.getInstance().getParser(hintAdapter);

            }
            return parser;
        }
    }

    static public class WrappingLabelTextCompartmentEditPart
        extends DescriptionCompartmentEditPart {

        public WrappingLabelTextCompartmentEditPart(View view) {
            super(view);
        }

        protected IFigure createFigure() {
            WrappingLabel label = new WrappingLabel("New WrappingLabel"); //$NON-NLS-1$
             label.setTextWrap(true);
            return label;
        }

        protected ILabelDelegate createLabelDelegate() {
            Assert.isTrue(getFigure() instanceof WrappingLabel);
            return new WrappingLabelDelegate((WrappingLabel) getFigure());
        }

        public IParser getParser() {
            if (parser == null) {
                EObject object = getPrimaryView();
                ParserHintAdapter hintAdapter = new ParserHintAdapter(object,
                    CommonParserHint.DESCRIPTION);
                parser = ParserService.getInstance().getParser(hintAdapter);

            }
            return parser;
        }
    }

    static public class OldWrapLabelTextCompartmentEditPart
        extends DescriptionCompartmentEditPart {

        public OldWrapLabelTextCompartmentEditPart(View view) {
            super(view);
        }

        protected IFigure createFigure() {
            OriginalWrapLabel label = new OriginalWrapLabel("Old WrapLabel"); //$NON-NLS-1$
            label.setTextWrap(true);
            return label;
        }

        protected ILabelDelegate createLabelDelegate() {
            Assert.isTrue(getFigure() instanceof OriginalWrapLabel);
            return new OriginalWrapLabelDelegate(
                (OriginalWrapLabel) getFigure());
        }

        public IParser getParser() {
            if (parser == null) {
                EObject object = getPrimaryView();
                ParserHintAdapter hintAdapter = new ParserHintAdapter(object,
                    CommonParserHint.DESCRIPTION);
                parser = ParserService.getInstance().getParser(hintAdapter);

            }
            return parser;
        }
    }

    static public class WrapLabelTextCompartmentEditPart
        extends DescriptionCompartmentEditPart {

        public WrapLabelTextCompartmentEditPart(View view) {
            super(view);
        }

        protected IFigure createFigure() {
            WrapLabel label = new WrapLabel("WrapLabel Delegate"); //$NON-NLS-1$
             label.setTextWrap(true);
           return label;
        }

        protected ILabelDelegate createLabelDelegate() {
            Assert.isTrue(getFigure() instanceof WrapLabel);
            return new WrappingLabelDelegate(
                (WrapLabel) getFigure());
        }
        public IParser getParser() {
            if (parser == null) {
                EObject object = getPrimaryView();
                ParserHintAdapter hintAdapter = new ParserHintAdapter(object,
                    CommonParserHint.DESCRIPTION);
                parser = ParserService.getInstance().getParser(hintAdapter);

            }
            return parser;
        }
    }

    /** list of supported shape editparts. */
    private Map shapeMap = new HashMap();
    {
        shapeMap.put(LabelConstants.VIEWTYPE_GEFLABEL_NOTE,
            GEFLabelNoteEditPart.class);
        shapeMap.put(LabelConstants.VIEWTYPE_WRAPPINGLABEL_NOTE,
            WrappingLabelNoteEditPart.class);
        shapeMap.put(LabelConstants.VIEWTYPE_OLDWRAPLABEL_NOTE,
            OldWrapLabelNoteEditPart.class);
        shapeMap.put(LabelConstants.VIEWTYPE_WRAPLABEL_NOTE,
            WrapLabelNoteEditPart.class);
    }

    /** list of supported text editparts. */
    private Map textCompartmentMap = new HashMap();
    {
        textCompartmentMap.put(LabelConstants.VIEWTYPE_GEFLABEL,
            GEFLabelTextCompartmentEditPart.class);
        textCompartmentMap.put(LabelConstants.VIEWTYPE_WRAPPINGLABEL,
            WrappingLabelTextCompartmentEditPart.class);
        textCompartmentMap.put(LabelConstants.VIEWTYPE_OLDWRAPLABEL,
            OldWrapLabelTextCompartmentEditPart.class);
        textCompartmentMap.put(LabelConstants.VIEWTYPE_WRAPLABEL,
            WrapLabelTextCompartmentEditPart.class);
    }

    /**
     * Gets a Node's editpart class. This method should be overridden by a
     * provider if it wants to provide this service.
     * 
     * @param view
     *            the view to be <i>controlled</code> by the created editpart
     */
    protected Class getNodeEditPartClass(View view) {
        String type = view.getType();
        Class clazz = null;
        if (type != null && type.length() > 0) {
            clazz = (Class) textCompartmentMap.get(type);
            if (clazz == null)
                clazz = (Class) shapeMap.get(type);
        } else {
            if (getReferencedElementEClass(view) == NotationPackage.eINSTANCE
                .getDiagram()) {
                clazz = NoteEditPart.class;
            }
        }
        return clazz;
    }

}
