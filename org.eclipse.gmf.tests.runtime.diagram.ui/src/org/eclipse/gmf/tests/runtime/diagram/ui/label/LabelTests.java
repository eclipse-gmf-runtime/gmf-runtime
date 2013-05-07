/******************************************************************************
 * Copyright (c) 2007, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.SharedImages;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicTestFixture;

/**
 * Tests functionality relating to notes, note attachments, and text shapes.
 * 
 * @author crevells
 */
public class LabelTests
    extends AbstractTestBase {

    public LabelTests(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(LabelTests.class);
    }

    protected void setTestFixture() {
        testFixture = new LogicTestFixture();
    }

    protected LogicTestFixture getFixture() {
        return (LogicTestFixture) testFixture;
    }

    public void ignore_testWrapLabelDelegate()
        throws Exception {

        getFixture().openDiagram();

        NoteEditPart oldWrapLabelEP = (NoteEditPart) getFixture()
            .createShapeUsingTool(LabelNotationType.OLDWRAPLABEL_NOTE,
                new Point(10, 10), new Dimension(200, 50), getDiagramEditPart());
        OriginalWrapLabel oldWrapLabel = (OriginalWrapLabel) oldWrapLabelEP
            .getChildBySemanticHint(LabelConstants.VIEWTYPE_OLDWRAPLABEL)
            .getFigure();

        NoteEditPart wrapLabelDelegateEP = (NoteEditPart) getFixture()
            .createShapeUsingTool(LabelNotationType.WRAPLABEL_NOTE,
                new Point(10, 10), new Dimension(200, 50), getDiagramEditPart());
        WrapLabel wrapLabel = (WrapLabel) wrapLabelDelegateEP
            .getChildBySemanticHint(LabelConstants.VIEWTYPE_WRAPLABEL)
            .getFigure();

        oldWrapLabel
            .setText("Let's put a bit of text in here that would cause the label to wrap."); //$NON-NLS-1$
        oldWrapLabel.setIcon(SharedImages.get(SharedImages.IMG_NOTE));
        wrapLabel
            .setText("Let's put a bit of text in here that would cause the label to wrap."); //$NON-NLS-1$
        wrapLabel.setIcon(SharedImages.get(SharedImages.IMG_NOTE));

        flushEventQueue();

        assertEquals(oldWrapLabel.getIconAlignment(), wrapLabel
            .getIconAlignment());
        assertEquals(oldWrapLabel.getIconTextGap(), wrapLabel.getIconTextGap());
        assertEquals(oldWrapLabel.getText(), wrapLabel.getText());
        assertEquals(oldWrapLabel.getTextAlignment(), wrapLabel
            .getTextAlignment());
        assertEquals(oldWrapLabel.getTextPlacement(), wrapLabel
            .getTextPlacement());
        assertEquals(oldWrapLabel.isTextTruncated(), wrapLabel
            .isTextTruncated());
        assertEquals(oldWrapLabel.isTextUnderlined(), wrapLabel
            .isTextUnderlined());
        assertEquals(oldWrapLabel.isTextStrikedThrough(), wrapLabel
            .isTextStrikedThrough());
        assertEquals(oldWrapLabel.isTextWrapped(), wrapLabel.isTextWrapped());
        assertEquals(oldWrapLabel.getTextWrapAlignment(), wrapLabel
            .getTextWrapAlignment());
        assertEquals(oldWrapLabel.isSelected(), wrapLabel.isSelected());
        assertEquals(oldWrapLabel.hasFocus(), wrapLabel.hasFocus());

    }

    public void testLabelAlignment()
        throws Exception {
        getFixture().openDiagram();

        NoteEditPart noteEP = (NoteEditPart) getFixture().createShapeUsingTool(
            DiagramNotationType.NOTE, new Point(10, 10),
            new Dimension(300, 300), getDiagramEditPart());
        IGraphicalEditPart labelEP = noteEP
            .getChildBySemanticHint(CommonParserHint.DESCRIPTION);
        ILabelDelegate label = (ILabelDelegate) labelEP
            .getAdapter(ILabelDelegate.class);

        label.setText("hi"); //$NON-NLS-1$

        Point loc[][] = new Point[3][3];
        label.setAlignment(PositionConstants.TOP | PositionConstants.LEFT);
        flushEventQueue();
        loc[0][0] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.TOP);
        flushEventQueue();
        loc[0][1] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.TOP | PositionConstants.RIGHT);
        flushEventQueue();
        loc[0][2] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.LEFT);
        flushEventQueue();
        loc[1][0] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.CENTER);
        flushEventQueue();
        loc[1][1] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.RIGHT);
        flushEventQueue();
        loc[1][2] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.BOTTOM | PositionConstants.LEFT);
        flushEventQueue();
        loc[2][0] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.BOTTOM);
        flushEventQueue();
        loc[2][1] = label.getTextBounds().getLocation();

        label.setAlignment(PositionConstants.BOTTOM | PositionConstants.RIGHT);
        flushEventQueue();
        loc[2][2] = label.getTextBounds().getLocation();

        for (int i = 0; i < 2; i++) {
            assertTrue(loc[i][0].x < loc[i][1].x && loc[i][1].x < loc[i][2].x);
            assertTrue(loc[i][0].y == loc[i][1].y && loc[i][1].y == loc[i][2].y);
        }

    }
}
