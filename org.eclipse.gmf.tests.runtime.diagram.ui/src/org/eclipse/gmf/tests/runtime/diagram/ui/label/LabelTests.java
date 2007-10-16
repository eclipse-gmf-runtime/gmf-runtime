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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.SharedImages;
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

    public void testWrapLabelDelegate()
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
            .setText("Let's put a bit of text in here that would cause the label to wrap.");
        oldWrapLabel.setIcon(SharedImages.get(SharedImages.IMG_NOTE));
        wrapLabel
            .setText("Let's put a bit of text in here that would cause the label to wrap.");
        wrapLabel.setIcon(SharedImages.get(SharedImages.IMG_NOTE));

        flushEventQueue();
        Thread.sleep(5000);

        assertEquals(oldWrapLabel.getIconAlignment(), wrapLabel
            .getIconAlignment());
//        assertEquals(oldWrapLabel.getIconBounds(), wrapLabel.getIconBounds());
        assertEquals(oldWrapLabel.getIconTextGap(), wrapLabel.getIconTextGap());
        assertTrue(Math.abs(oldWrapLabel.getSubStringText().length() - wrapLabel
            .getSubStringText().length()) <= 2);
        assertEquals(oldWrapLabel.getText(), wrapLabel.getText());
        assertEquals(oldWrapLabel.getTextAlignment(), wrapLabel
            .getTextAlignment());
//        assertEquals(oldWrapLabel.getTextBounds(), wrapLabel.getTextBounds());
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
}
