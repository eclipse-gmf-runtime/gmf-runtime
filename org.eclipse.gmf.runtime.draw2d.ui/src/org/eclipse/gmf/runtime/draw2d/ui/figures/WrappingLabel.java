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

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.draw2d.text.TextLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.IMapModeHolder;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.text.TextFlowEx;
import org.eclipse.gmf.runtime.draw2d.ui.text.TextUtilitiesEx;
import org.eclipse.gmf.runtime.draw2d.ui.text.TruncatedSingleLineTextLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * An extended label that has the following extra features: <br>
 * 1. Allows selection, focus feedback, underlined and striked-through text.<br>
 * 2. Enhanced layout functionality for placing icon and text.<br>
 * 3. Text will be word-wrapped to fit the text in the width available.<br>
 * 4. Text will be truncated with an ellipsis if the entire text does not fit in
 * the space available.<br>
 * <p>
 * <b>EXPLANATION OF LAYOUTS</b><br>
 * 
 * This WrappingLabel contains functionality to display an icon alongside text.
 * The following will describe how the layout of the icon and text are done.
 * <p>
 * <br>
 * 
 * 
 * <u>Using {@link #setTextPlacement(int)}:</u>
 * <p>
 * 
 * The position of the text <i>relative</i> to the icon depends on
 * {@link #setTextPlacement(int)}. If the text placement is set to
 * {@link PositionConstants#EAST}, then the text would be placed on the right
 * of the icon. Similarly, if text placement is set to
 * {@link PositionConstants#WEST}, the text will be placed on the left of the
 * icon; {@link PositionConstants#NORTH} would put the text above the icon; and
 * {@link PositionConstants#SOUTH} would place the text below the icon.
 * <p>
 * <br>
 * 
 * <u>Using {@link #setTextAlignment(int)} and {@link #setIconAlignment(int)}:</u>
 * <p>
 * 
 * Use {@link #setTextAlignment(int)} and {@link #setIconAlignment(int)} to
 * align the text and icons <i>relative</i> to each other for more dynamic
 * control. If the text placement is on the east or west of the icon(s) (i.e.
 * the text on the right or left of the icon respectively), then only
 * {@link PositionConstants#TOP}, {@link PositionConstants#CENTER}, and
 * {@link PositionConstants#BOTTOM} can be used when calling
 * {@link #setTextAlignment(int)} and {@link #setIconAlignment(int)}. In this
 * case, setting the text alignment to {@link PositionConstants#TOP} will make
 * sure that the top of the text is aligned horizontally with the top of the
 * icon <i>if</i> the height of the icon is greater than the height of the
 * text. Similarly, setting the text alignment to
 * {@link PositionConstants#CENTER} will make sure that the top of the text is
 * aligned horizontally with the vertical center of the size of icon <i>if</i>
 * the height of the icon is greater than the height of the text. Also, setting
 * the text alignment to {@link PositionConstants#BOTTOM} will make sure that
 * the bottom of the text is aligned horizontally with the bottom of the icon
 * <i>if</i> the height of the icon is greater than the height of the text.
 * <p>
 * 
 * The other scenario is when the text placement is on the south or north of the
 * icon (i.e. the text is below or above the icon respectively). If this is
 * true, only {@link PositionConstants#LEFT}, {@link PositionConstants#CENTER},
 * and {@link PositionConstants#RIGHT} can be used when calling
 * {@link #setTextAlignment(int)} and {@link #setIconAlignment(int)}. In this
 * case, setting the text alignment to {@link PositionConstants#LEFT} will make
 * sure that the left of the text is aligned vertically with the left of the
 * icon <i>if</i> the width of the icon is greater than the width of the text.
 * Similarly, setting the text alignment to {@link PositionConstants#CENTER}
 * will make sure that the left of the text is aligned vertically with the
 * horizontal center of the icon <i>if</i> the width of the icon is greater
 * than the width of the text. Also, setting the text alignment to
 * {@link PositionConstants#RIGHT} will make sure that the right of the text is
 * aligned vertically with the right of the icon <i>if</i> the width of the
 * icon is greater than the width of the text.
 * <p>
 * 
 * {@link #setIconAlignment(int)} works identically as
 * {@link #setTextAlignment(int)}, except the roles of text and icon are
 * switched in the above descriptions.
 * <p>
 * <br>
 * 
 * 
 * <u>Using {@link #setAlignment(int)}:</u>
 * <p>
 * 
 * The entire label, text and icon, can be moved into different positions with
 * the label figure, if the figure is bigger than the icon and text. The
 * alignment of the label places the text and icon (no matter how they are
 * arranged relatively to each other) in the top-left, top, top-right, left,
 * center, right, bottom-left, bottom, or bottom-right of the bounds of this
 * <code>WrappingLabel</code> figure.
 * <p>
 * <br>
 * 
 * 
 * <u>Using {@link #setTextHorizontalAlignment(int)}:</u>
 * <p>
 * 
 * Use {@link #setTextJustification(int)} with {@link PositionConstants#LEFT},
 * {@link PositionConstants#CENTER}, or {@link PositionConstants#RIGHT} to
 * justify the text when wordwrap is turned on. The effect will be noticed in
 * multi-lined text only.
 * <p>
 * <br>
 * 
 * WARNING: User-nested figures are not expected within this figure.
 * <p>
 * 
 * Some code taken from the original <code>WrapLabel</code> in GMF by
 * melaasar.
 * <p>
 * 
 * @since 2.1
 * @author satif, crevells
 */
public class WrappingLabel
    extends Figure
    implements PositionConstants {

    static final String ELLIPSIS = "..."; //$NON-NLS-1$

    private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);

    private static final Map<IMapMode, MapModeConstants> mapModeConstantsMap = new WeakHashMap<IMapMode, MapModeConstants>();

    static class MapModeConstants {

        private static final int MAX_IMAGE_INFO = 12;

        public final WeakReference<IMapMode> mapModeRef;

        public final int nDPtoLP_3;

        public final int nDPtoLP_2;

        public final int nDPtoLP_0;

        public final Dimension dimension_nDPtoLP_0;

        public final WeakHashMap<Font, Dimension> fontToEllipseTextSize = new WeakHashMap<Font, Dimension>();

        public final SingleIconInfo[] singleIconInfos = new SingleIconInfo[MAX_IMAGE_INFO];

        public MapModeConstants(IMapMode mapMode) {
            this.mapModeRef = new WeakReference<IMapMode>(mapMode);
            nDPtoLP_2 = mapMode.DPtoLP(2);
            nDPtoLP_3 = mapMode.DPtoLP(3);
            nDPtoLP_0 = mapMode.DPtoLP(0);
            dimension_nDPtoLP_0 = new Dimension(nDPtoLP_0, nDPtoLP_0);
        }

        public Dimension getEllipseTextSize(Font f) {
            Dimension d = fontToEllipseTextSize.get(f);
            if (d == null) {
                IMapMode mapMode = mapModeRef.get();
                d = FigureUtilities.getTextExtents(ELLIPSIS, f);
                d.height = FigureUtilities.getFontMetrics(f).getHeight();
                d = new Dimension(mapMode.DPtoLP(d.width), mapMode
                    .DPtoLP(d.height));
                fontToEllipseTextSize.put(f, d);
            }
            return d;
        }

        public SingleIconInfo getSingleIconInfo(Image image) {
            if (image == null) {
                return SingleIconInfo.NULL_INFO;
            }
            SingleIconInfo info;
            for (int i = 0; i < MAX_IMAGE_INFO; ++i) {
                info = singleIconInfos[i];
                if (info == null) {
                    info = new SingleIconInfo(image);
                    singleIconInfos[i] = info;
                    return info;
                }
                if (info.icon == image) {
                    return info;
                }
            }
            int index = SingleIconInfo.count % MAX_IMAGE_INFO;
            info = new SingleIconInfo(image);
            singleIconInfos[index] = info;
            return info;
        }
    }

    // reserve 1 bit for these boolean flags
    private static int FLAG_SELECTED = Figure.MAX_FLAG << 1;

    private static int FLAG_HASFOCUS = Figure.MAX_FLAG << 2;

    // reserve 4 bits for these alignment variables
    /**
     * @see #setTextAlignment(int)
     */
    private static int FLAG_TEXT_ALIGN = Figure.MAX_FLAG << 3;

    /**
     * @see #setIconAlignment(int)
     */
    private static int FLAG_ICON_ALIGN = Figure.MAX_FLAG << 7;

    /**
     * @see #setAlignment(int)
     */
    private static int FLAG_LABEL_ALIGN = Figure.MAX_FLAG << 11;

    /**
     * @see #setTextPlacement(int)
     */
    private static int FLAG_TEXT_PLACEMENT = Figure.MAX_FLAG << 15;

    /**
     * The largest flag defined in this class. If subclasses define flags, they
     * should declare them as larger than this value and redefine MAX_FLAG to be
     * their largest flag value.
     * 
     * @see Figure#MAX_FLAG
     */
    @SuppressWarnings("hiding")
    protected static final int MAX_FLAG = FLAG_TEXT_PLACEMENT;

    private static abstract class IconInfo {

        /**
         * Gets the icon at the index location.
         * 
         * @param i
         *            the index to retrieve the icon of
         * @return <code>Image</code> that corresponds to the given index.
         */
        public abstract Image getIcon(int i);

        /**
         * Gets the icon size of the icon at the given index.
         * 
         * @param i
         * @return the <code>Dimension</code> that is the size of the icon at
         *         the given index.
         */
        public abstract Dimension getIconSize(IMapMode mapMode, int i);

        /**
         * @return the number of icons
         */
        public abstract int getNumberofIcons();

        /**
         * @return the <code>Dimension</code> that is the total size of all
         *         the icons.
         */
        public abstract Dimension getTotalIconSize(IMapMode mapMode);

        public abstract void invalidate();

        /**
         * Sets the icon at the index location.
         * 
         * @param icon
         * @param i
         */
        public abstract void setIcon(Image icon, int i);

        /**
         * 
         */
        public abstract int getMaxIcons();

    }

    private static class SingleIconInfo
        extends IconInfo {

        static int count;

        public static final SingleIconInfo NULL_INFO = new SingleIconInfo() {

            public int getNumberofIcons() {
                return 0;
            }
        };

        final Image icon;

        /** total icon size */
        private Dimension totalIconSize;

        private SingleIconInfo() {
            icon = null;// don't increment count, used only for NULL_INFO
        }

        public SingleIconInfo(Image icon) {
            this.icon = icon;
            ++count;
        }

        public final int getMaxIcons() {
            return 1;
        }

        public Image getIcon(int i) {
            if (i == 0) {
                return icon;
            } else if (i > 0) {
                return null;
            }
            throw new IndexOutOfBoundsException();
        }

        public void setIcon(Image img, int i) {
            throw new UnsupportedOperationException();
        }

        public Dimension getIconSize(IMapMode mapMode, int i) {
            if (i == 0) {
                return getTotalIconSize(mapMode);
            }

            throw new IndexOutOfBoundsException();
        }

        public int getNumberofIcons() {
            return 1;
        }

        public Dimension getTotalIconSize(IMapMode mapMode) {
            if (totalIconSize != null)
                return totalIconSize;

            if (icon != null && !icon.isDisposed()) {
                org.eclipse.swt.graphics.Rectangle imgBounds = icon.getBounds();
                totalIconSize = new Dimension(mapMode.DPtoLP(imgBounds.width),
                    mapMode.DPtoLP(imgBounds.height));
            } else {
                totalIconSize = EMPTY_DIMENSION;
            }

            return totalIconSize;
        }

        public void invalidate() {
            totalIconSize = null;
        }

    }

    private static class MultiIconInfo
        extends IconInfo {

        /** the label icons */
        private ArrayList<Image> icons = new ArrayList<Image>(2);

        /** total icon size */
        private Dimension totalIconSize;

        public MultiIconInfo() {
            super();
        }

        public int getMaxIcons() {
            return -1;
        }

        /**
         * Gets the icon at the index location.
         * 
         * @param i
         *            the index to retrieve the icon of
         * @return <code>Image</code> that corresponds to the given index.
         */
        public Image getIcon(int i) {
            if (i >= icons.size())
                return null;

            return icons.get(i);
        }

        /**
         * Sets the icon at the index location.
         * 
         * @param icon
         * @param i
         */
        public void setIcon(Image icon, int i) {
            int size = icons.size();
            if (i >= size) {
                for (int j = size; j < i; j++)
                    icons.add(null);
                icons.add(icon);
                icons.trimToSize();
            } else
                icons.set(i, icon);
        }

        /**
         * Gets the icon size of the icon at the given index.
         * 
         * @param i
         * @return the <code>Dimension</code> that is the size of the icon at
         *         the given index.
         */
        public Dimension getIconSize(IMapMode mapMode, int i) {
            Image img = getIcon(i);
            if (img != null && !img.isDisposed()) {
                org.eclipse.swt.graphics.Rectangle imgBounds = img.getBounds();
                return new Dimension(mapMode.DPtoLP(imgBounds.width), mapMode
                    .DPtoLP(imgBounds.height));
            }
            return EMPTY_DIMENSION;
        }

        /**
         * @return the number of icons
         */
        public int getNumberofIcons() {
            return icons.size();
        }

        /**
         * @return the <code>Dimension</code> that is the total size of all
         *         the icons.
         */
        public Dimension getTotalIconSize(IMapMode mapMode) {
            if (totalIconSize != null)
                return totalIconSize;
            int iconNum = getNumberofIcons();
            if (iconNum == 0) {
                return totalIconSize = EMPTY_DIMENSION;
            }

            totalIconSize = new Dimension();
            for (int i = 0; i < iconNum; i++) {
                Dimension iconSize = getIconSize(mapMode, i);
                totalIconSize.width += iconSize.width;
                if (iconSize.height > totalIconSize.height)
                    totalIconSize.height = iconSize.height;
            }

            return totalIconSize;
        }

        /**
         * 
         */
        public void invalidate() {
            totalIconSize = null;
        }
    }

    private MapModeConstants mapModeConstants;

    /**
     * the top-level flow figure
     */
    private FlowPage flowPage;

    /**
     * The cached preferred text size that can be used after
     * {@link #getPreferredSize(int, int)} is called.
     */
    protected Dimension preferredTextSize;

    /**
     * The cached truncation string size.
     */
    private Dimension truncationStringSize;

    private IconInfo iconInfo;

    /** the cached hint used to calculate text size */
    private int cachedPrefSizeHint_width;

    private int cachedPrefSizeHint_height;

    /** the icon location */
    private Point iconLocation;

    /**
     * Construct an empty wrapping label.
     */
    public WrappingLabel() {
        createTextFigures();

        setText("");//$NON-NLS-1$

        // default flags...
        setAlignmentFlags(CENTER, FLAG_TEXT_ALIGN);
        setAlignmentFlags(CENTER, FLAG_ICON_ALIGN);
        setAlignmentFlags(TOP | LEFT, FLAG_LABEL_ALIGN);
        setPlacementFlags(EAST, FLAG_TEXT_PLACEMENT);
        setTextJustification(LEFT);

        revalidate();
    }

    /**
     * Construct a wrapping label with the passed String as its text.
     * 
     * @param text
     *            the label text
     */
    public WrappingLabel(String text) {
        this();
        if (text != null) {
            setText(text);
        } else {
            setText("");//$NON-NLS-1$
        }
    }

    /**
     * Construct a wrapping label with thepassed Image as its icon.
     * 
     * @param image
     *            the label image
     */
    public WrappingLabel(Image image) {
        this();
        iconInfo = new SingleIconInfo(image);
    }

    /**
     * Construct a wrapping label with passed the String as its text and the
     * passed Image as its icon.
     * 
     * @param text
     *            the label text
     * @param image
     *            the label image
     */
    public WrappingLabel(String text, Image image) {
        this(text);
        iconInfo = new SingleIconInfo(image);
    }

    /**
     * Creates the top-level flow figure that will contain and draw the text.
     * <p>
     * 
     * @return the new top-level flow figure
     */
    private void createTextFigures() {
        TextFlowEx textFlow = new TextFlowEx();
        flowPage = new FlowPage();
        flowPage.add(textFlow);
        setLayoutManager(textFlow, false);
        add(flowPage);
    }

    /**
     * Returns the top-level text figure. This is public to accommodate a bug in
     * the TextDirectEditManager (see WrappingLabelDirectEditManager). It may
     * not always remain public.
     * 
     * @return the top-level text figure.
     */
    public IFigure getTextFigure() {
        return flowPage;
    }

    /**
     * Casts the text figure to a flowpage.
     * 
     * @return
     */
    private FlowPage getFlowPage() {
        return (FlowPage) getTextFigure();
    }

    /**
     * Returns the text flow.
     * 
     * @return the text flow
     */
    TextFlow getTextFlow() {
        return (TextFlow) flowPage.getChildren().get(0);
    }

    /**
     * @return <code>IMapMode</code> used by this figure.
     *         <code>IMapMode</code> that allows for the coordinate mapping
     *         from device to logical units.
     */
    IMapMode getFigureMapMode() {
        return getMapModeConstants().mapModeRef.get();
    }

    MapModeConstants getMapModeConstants() {
        if (mapModeConstants == null) {
            IMapMode mapMode = MapModeUtil.getMapMode(this);
            while (mapMode instanceof IMapModeHolder) {
                mapMode = ((IMapModeHolder) mapMode).getMapMode();
            }
            mapModeConstants = mapModeConstantsMap.get(mapMode);
            if (mapModeConstants == null) {
                mapModeConstants = new MapModeConstants(mapMode);
                mapModeConstantsMap.put(mapMode, mapModeConstants);
            }
        }
        return mapModeConstants;
    }

    private void alignOnHeight(Rectangle area, Rectangle childBounds,
            int alignment) {

        switch (alignment) {
            case TOP:
                childBounds.y = area.y;
                childBounds.y = area.y;
                break;
            case BOTTOM:
                childBounds.y = area.getBottom().y - childBounds.height;
                break;
            default:
                childBounds.y = area.y + (area.height - childBounds.height) / 2;
        }
    }

    private void alignOnWidth(Rectangle area, Rectangle childBounds,
            int alignment) {

        switch (alignment) {
            case LEFT:
                childBounds.x = area.x;
                break;
            case RIGHT:
                childBounds.x = area.getRight().x - childBounds.width;
                break;
            default:
                childBounds.x = area.x + (area.width - childBounds.width) / 2;
        }
    }

    private void calculateAlignment(Rectangle textBounds, Rectangle iconBounds) {

        Rectangle areaUsed = textBounds.getUnion(iconBounds);
        areaUsed.x = getInsets().left;
        areaUsed.y = getInsets().top;

        switch (getTextPlacement()) {
            case EAST:
            case WEST:
                alignOnHeight(areaUsed, textBounds, getTextAlignment());
                alignOnHeight(areaUsed, iconBounds, getIconAlignment());
                break;
            case NORTH:
            case SOUTH:
                alignOnWidth(areaUsed, textBounds, getTextAlignment());
                alignOnWidth(areaUsed, iconBounds, getIconAlignment());
                break;
        }
    }

    /**
     * Calculates the size of the Label using the passed Dimension as the size
     * of the Label's text.
     * 
     * @param txtSize
     *            the precalculated size of the label's text
     * @return the label's size
     * @since 2.0
     */
    protected Dimension calculateLabelSize(Dimension txtSize) {
        Dimension iconSize = getTotalIconSize();
        boolean isEmpty = (iconSize.width == 0 && iconSize.height == 0);
        int len = getText().length();
        if (len == 0 && isEmpty) {
            return new Dimension(txtSize.width, txtSize.height);
        }
        int gap = (len == 0 || isEmpty) ? 0
            : getIconTextGap();
        int placement = getTextPlacement();
        if (placement == WEST || placement == EAST) {
            return new Dimension(iconSize.width + gap + txtSize.width, Math
                .max(iconSize.height, txtSize.height));
        } else {
            return new Dimension(Math.max(iconSize.width, txtSize.width),
                iconSize.height + gap + txtSize.height);
        }
    }

    public void layout() {
        Rectangle textBounds = new Rectangle();
        Rectangle iconBounds = new Rectangle();

        calculateSizes(textBounds, iconBounds);
        calculatePlacement(textBounds, iconBounds);
        calculateAlignment(textBounds, iconBounds);
        calculateLabelAlignment(textBounds, iconBounds);

        if (hasIcons()) {
            setIconLocation(iconBounds.getLocation());
        }

        getTextFigure().setBounds(
            textBounds.getTranslated(getBounds().getLocation()));
    }

    /**
     * @param container
     * @param textBounds
     * @param iconBounds
     */
    private void calculateSizes(Rectangle textBounds, Rectangle iconBounds) {
        Rectangle area = getClientArea();

        Dimension preferredSize = getPreferredSize(area.width, area.height);

        Dimension minimumSize = getMinimumSize(area.width, area.height);

        Dimension shrinkAmount = preferredSize.getDifference(area.getSize()
            .getUnioned(minimumSize));

        Dimension textSize = preferredTextSize.getCopy();
        if (shrinkAmount.width > 0) {
            textSize.shrink(shrinkAmount.width, 0);
        }
        if (shrinkAmount.height > 0) {
            textSize.shrink(0, shrinkAmount.height);
        }

        if (getTextFlow().isTextTruncated()) {
            textBounds.setSize(textSize);
        } else {
            // This is needed for label alignment to work. The preferred text
            // size will extend the entire width, so use the actual text size
            // instead.
            textBounds.setSize(getTextFlow().getSize().intersect(textSize));
        }

        iconBounds.setSize(getTotalIconSize());
    }

    private void calculateLabelAlignment(Rectangle textBounds,
            Rectangle iconBounds) {

        Dimension offset = getClientArea().getSize().getDifference(
            textBounds.getUnion(iconBounds).getSize());
        switch (getAlignment()) {
            case TOP | LEFT:
                offset.height = 0;
                offset.width = 0;
                break;
            case TOP:
                offset.height = 0;
                offset.scale(0.5f);
                break;
            case TOP | RIGHT:
                offset.height = 0;
            case RIGHT:
                offset.width = offset.width * 2;
                offset.scale(0.5f);
                break;
            case BOTTOM | RIGHT:
                break;
            case BOTTOM:
                offset.height = offset.height * 2;
                offset.scale(0.5f);
                break;
            case BOTTOM | LEFT:
                offset.width = 0;
                break;
            case LEFT:
                offset.width = 0;
                offset.scale(0.5f);
                break;
            case CENTER:
                offset.scale(0.5f);
                break;
            default:
                offset.scale(0.5f);
                break;
        }
        textBounds.translate(offset.width, offset.height);
        iconBounds.translate(offset.width, offset.height);
    }

    private void calculatePlacement(Rectangle textBounds, Rectangle iconBounds) {
        int gap = (textBounds.isEmpty() || iconBounds.isEmpty()) ? 0
            : getIconTextGap();
        Insets insets = getInsets();
        switch (getTextPlacement()) {
            case EAST:
                iconBounds.x = insets.left;
                textBounds.x = iconBounds.width + gap + insets.left;
                break;
            case WEST:
                textBounds.x = insets.left;
                iconBounds.x = textBounds.width + gap + insets.left;
                break;
            case NORTH:
                textBounds.y = insets.top;
                iconBounds.y = textBounds.height + gap + insets.top;
                break;
            case SOUTH:
                textBounds.y = iconBounds.height + gap + insets.top;
                iconBounds.y = insets.top;
        }
    }

    /**
     * Returns the label's icon.
     * 
     * @return the label's icon.
     */
    public Image getIcon() {
        return getIcon(0);
    }

    /**
     * Gets the label's icon at the given index
     * 
     * @param index
     *            The icon index
     * @return the <code>Image</code> that is the icon for the given index.
     */
    public Image getIcon(int index) {
        if (iconInfo == null)
            return null;
        return iconInfo.getIcon(index);
    }

    /**
     * Determines if there is any icons by checking if icon size is zeros.
     * 
     * @return true if icons are present, false otherwise
     */
    protected boolean hasIcons() {
        return (getNumberofIcons() > 0);
    }

    /**
     * Returns the alignment of the label's icon relative to the label's text
     * bounds. This is only relevant if the icon's width or height (depending on
     * the location of the icon relative to the text) is smaller than the text's
     * width or height. The default is {@link PositionConstants#CENTER}.
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @return the icon alignment relative to the text bounds
     */
    public int getIconAlignment() {
        return getAlignment(FLAG_ICON_ALIGN);
    }

    /**
     * Returns the location of the Label's icon relative to the Label.
     * 
     * @return the icon's location
     * @since 2.0
     */
    protected Point getIconLocation() {
        return iconLocation;
    }

    protected void setIconLocation(Point location) {
        iconLocation = location;
    }

    /**
     * Returns the gap in pixels between the Label's icon and its text.
     * 
     * @return the gap
     * @since 2.0
     */
    public int getIconTextGap() {
        return getMapModeConstants().nDPtoLP_3;
    }

    public Dimension getMinimumSize(int w, int h) {
        if (minSize != null)
            return minSize;
        minSize = new Dimension();

        Font currentFont = getFont();
        Dimension ellipsisSize = getTruncationStringSize();
        Dimension textSize = new TextUtilitiesEx(getFigureMapMode())
            .getTextExtents(getText(), currentFont);
        textSize.intersect(ellipsisSize);

        Dimension labelSize = calculateLabelSize(textSize);
        Insets insets = getInsets();
        labelSize.expand(insets.getWidth(), insets.getHeight());
        minSize.union(labelSize);
        return minSize;
    }

    public Dimension getPreferredSize(int wHint, int hHint) {
        if (prefSize == null || wHint != cachedPrefSizeHint_width
            || hHint != cachedPrefSizeHint_height) {

            cachedPrefSizeHint_width = wHint;
            cachedPrefSizeHint_height = hHint;

            int minWHint = 0, minHHint = 0;
            if (wHint < 0)
                minWHint = -1;

            if (hHint < 0)
                minHHint = -1;

            Insets insets = getInsets();
            wHint = Math.max(minWHint, wHint - insets.getWidth());
            hHint = Math.max(minHHint, hHint - insets.getHeight());

            if (hasIcons()) {
                // start with the icon size and then add the text size
                prefSize = getTotalIconSize().getCopy();

                switch (getTextPlacement()) {
                    case EAST:
                    case WEST:
                        wHint = Math.max(minWHint, wHint
                            - (prefSize.width + getIconTextGap()));
                        preferredTextSize = getTextFigure().getPreferredSize(
                            wHint, hHint).getCopy();
                        prefSize.width += preferredTextSize.width
                            + getIconTextGap();
                        prefSize.height = Math.max(prefSize.height,
                            preferredTextSize.height);
                        break;
                    case NORTH:
                    case SOUTH:
                        hHint = Math.max(minHHint, hHint
                            - (prefSize.height + getIconTextGap()));
                        preferredTextSize = getTextFigure().getPreferredSize(
                            wHint, hHint).getCopy();
                        prefSize.width = Math.max(prefSize.width,
                            preferredTextSize.width);
                        prefSize.height += preferredTextSize.height
                            + getIconTextGap();
                        break;
                }

            } else {
                preferredTextSize = getTextFigure().getPreferredSize(wHint,
                    hHint).getCopy();
                prefSize = preferredTextSize.getCopy();
            }

            prefSize.expand(insets.getWidth(), insets.getHeight());

        }
        return prefSize;
    }

    /**
     * Returns the size of the truncation string based on the currently used Map
     * mode size.
     * 
     * @return the size of the truncation string
     * 
     */
    Dimension getTruncationStringSize() {
        if (truncationStringSize == null) {
            if (getTruncationString().equals(ELLIPSIS)) {
                truncationStringSize = getMapModeConstants()
                    .getEllipseTextSize(getFont());
            } else {
                Font f = getFont();
                IMapMode mapMode = getFigureMapMode();
                truncationStringSize = FigureUtilities.getTextExtents(
                    getTruncationString(), f);
                truncationStringSize.height = FigureUtilities.getFontMetrics(f)
                    .getHeight();
                truncationStringSize = new Dimension(mapMode
                    .DPtoLP(truncationStringSize.width), mapMode
                    .DPtoLP(truncationStringSize.height));
            }
        }
        return truncationStringSize;
    }

    /**
     * Returns the text of the label. Note that this is the complete text of the
     * label, regardless of whether it is currently being truncated.
     * 
     * @return the complete text of this label
     */
    public String getText() {
        return getTextFlow().getText();
    }

    /**
     * Gets the alignment of the label's text relative to the label's icon
     * bounds. This is only relevant if the text's width or height (depending on
     * the location of the text relative to the icon) is smaller than the icon's
     * width or height. The default text alignment is
     * {@link PositionConstants#CENTER}.
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @return the text alignment relative to the icon bounds
     */
    public int getTextAlignment() {
        return getAlignment(FLAG_TEXT_ALIGN);
    }

    /**
     * Returns the alignment of the label (icon and text) within the figure.
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @return the label alignment
     */
    public int getAlignment() {
        return getAlignment(FLAG_LABEL_ALIGN);
    }

    /**
     * Returns the bounds of the label's text. Note that the bounds are
     * calculated using the label's complete text regardless of whether the
     * label's text is currently truncated.
     * 
     * @return the bounds of this label's complete text relative to this
     *         figure's bounds
     */
    public Rectangle getTextBounds() {
        return new Rectangle(getFlowPage().getBounds().getLocation(),
            getTextFlow().getBounds().getSize());
    }

    /**
     * Returns the current placement of the label's text relative to its icon.
     * The default text placement is {@link PositionConstants#EAST}.
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @return the text placement
     */
    public int getTextPlacement() {
        return getPlacement(FLAG_TEXT_PLACEMENT);
    }

    public void invalidate() {
        prefSize = null;
        minSize = null;
        iconLocation = null;
        truncationStringSize = null;

        if (iconInfo != null) {
            iconInfo.invalidate();
        }
        super.invalidate();
    }

    public void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);

        if (hasIcons()) {
            paintIcons(graphics);
        }
    }

    protected void paintClientArea(Graphics graphics) {
        paintSelectionRectangle(graphics);
        paintFocusRectangle(graphics);
        super.paintClientArea(graphics);
    }

    private void paintSelectionRectangle(Graphics g) {
        if (isSelected()) {
            g.pushState();
            g.setBackgroundColor(ColorConstants.menuBackgroundSelected);
            g.fillRectangle(getVisibleTextBounds());
            g.popState();
            g.setForegroundColor(ColorConstants.white);
        }
    }

    private void paintFocusRectangle(Graphics g) {
        if (hasFocus()) {
            g.pushState();
            g.setXORMode(true);
            g.setForegroundColor(ColorConstants.menuBackgroundSelected);
            g.setBackgroundColor(ColorConstants.white);
            g.drawFocus(getVisibleTextBounds());
            g.popState();
        }
    }

    private Rectangle getVisibleTextBounds() {
        return getTextBounds().getIntersection(getClientArea());
    }

    /**
     * Paints the icon(s)
     * 
     * @param graphics
     *            The graphics context
     */
    private void paintIcons(Graphics graphics) {
        Point p = Point.SINGLETON;

        if (getIconLocation() != null) {
            p.setLocation(getIconLocation());

            Rectangle figBounds = getBounds();
            graphics.translate(figBounds.x, figBounds.y);

            int num = getNumberofIcons();
            for (int i = 0; i < num; i++) {
                Image icon = getIcon(i);
                if (icon != null) {
                    graphics.drawImage(icon, p);
                    p.x += getIconSize(i).width;
                }
            }
            graphics.translate(-figBounds.x, -figBounds.y);
        }
    }

    /**
     * Sets the label's icon to the passed image.
     * 
     * @param image
     *            the new label image
     * @since 2.0
     */
    public void setIcon(Image image) {
        setIcon(image, 0);
    }

    /**
     * Sets the label's icon at given index
     * 
     * @param image
     *            The icon image or null to remove the icon
     * @param index
     *            The icon index
     */
    public void setIcon(Image image, int index) {
        if (iconInfo == null) {
            if (index == 0) {
                iconInfo = getMapModeConstants().getSingleIconInfo(image);
            } else {
                iconInfo = new MultiIconInfo();
                iconInfo.setIcon(image, index);
            }
            revalidate();
            repaint();// Call repaint, in case the image dimensions are not
            // the same.
        } else if (iconInfo.getIcon(index) != image) {
            if (iconInfo.getMaxIcons() == 1) {
                if (index == 0) {
                    iconInfo = getMapModeConstants().getSingleIconInfo(image);
                    revalidate();
                    repaint();// Call repaint, in case the image dimensions
                    // are not the same.
                    return;
                }
                IconInfo oldIconInfo = iconInfo;
                iconInfo = new MultiIconInfo();
                iconInfo.setIcon(oldIconInfo.getIcon(0), 0);
            }
            iconInfo.setIcon(image, index);
            revalidate();
            repaint();// Call repaint, in case the image dimensions are not
            // the same.
        }
    }

    /**
     * Sets the alignment of the label's icon relative to the label's text
     * bounds. This is only relevant if the icon's width or height (depending on
     * the location of the icon relative to the text) is smaller than the text's
     * width or height. The default value is {@link PositionConstants#CENTER}.
     * <p>
     * If the text placement is NORTH/SOUTH, valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * <p>
     * If the text placement is EAST/WEST, valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * </UL>
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @param alignment
     *            the icon alignment relative to the text bounds
     */
    public void setIconAlignment(int alignment) {
        if (getIconAlignment() == alignment)
            return;
        setAlignmentFlags(alignment, FLAG_ICON_ALIGN);
        revalidate();
        repaint();
    }

    /**
     * getIconSize
     * 
     * @param index
     *            of icon to retrieve size of.
     * @return Dimension representing the icon size.
     */
    protected Dimension getIconSize(int index) {
        if (iconInfo == null)
            return EMPTY_DIMENSION;
        return iconInfo.getIconSize(getFigureMapMode(), index);
    }

    /**
     * getIconNumber
     * 
     * @return int number of icons in the wrap label
     */
    protected int getNumberofIcons() {
        if (iconInfo == null)
            return 0;
        return iconInfo.getNumberofIcons();
    }

    /**
     * getTotalIconSize Calculates the total union of icon sizes
     * 
     * @return Dimension that is the union of icon sizes
     */
    protected Dimension getTotalIconSize() {
        if (iconInfo == null)
            return EMPTY_DIMENSION;
        return iconInfo.getTotalIconSize(getFigureMapMode());
    }

    /**
     * Sets the alignment of the label (icon and text) within the figure. If
     * this figure's bounds are larger than the size needed to display the
     * label, the label will be aligned accordingly. The default is
     * {@link PositionConstants#LEFT}. Valid values are:
     * <UL>
     * <LI>{@link PositionConstants#TOP} | {@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#TOP} | {@link PositionConstants#RIGHT}
     * <LI><EM>{@link PositionConstants#LEFT}</EM>
     * <LI>{@link PositionConstants#CENTER}
     * <LI>{@link PositionConstants#RIGHT}
     * <LI>{@link PositionConstants#BOTTOM} | {@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#BOTTOM} | {@link PositionConstants#RIGHT}
     * </UL>
     * 
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @param alignment
     *            label alignment
     */
    public void setAlignment(int alignment) {
        if (getAlignment() == alignment)
            return;
        setAlignmentFlags(alignment, FLAG_LABEL_ALIGN);
        revalidate();
        repaint();
    }

    /**
     * Gets the truncation string. The default is an ellipsis "...". Clients may
     * override, but if the truncation string changes throughout the lifecycle
     * of this figure, then revalidate() should be called to ensure the cached
     * <code>truncationStringSize</code> is cleared.
     * 
     * @return the truncation string
     */
    protected String getTruncationString() {
        return ELLIPSIS;
    }

    /**
     * Sets the text in this label.
     * 
     * @param text
     *            the new text to be set
     */
    public void setText(String text) {
        getTextFlow().setText(text);
    }

    /**
     * Sets the alignment of the label's text relative to the label's icon
     * bounds. This is only relevant if the text's width or height (depending on
     * the location of the text relative to the icon) is smaller than the icon's
     * width or height. The default value is {@link PositionConstants#CENTER}.
     * <p>
     * If the text placement is EAST/WEST, valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * </UL>
     * <p>
     * If the text placement is NORTH/SOUTH, and the icon is bigger than the
     * text, then the location of the text can be controlled by how the text is
     * justified {@link #setTextJustification(int)}.
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @param alignment
     *            the text alignment relative to the icon bounds
     */
    public void setTextAlignment(int alignment) {
        if (getTextAlignment() == alignment)
            return;
        setAlignmentFlags(alignment, FLAG_TEXT_ALIGN);

        revalidate();
        repaint();
    }

    /**
     * Sets the text placement of the label relative to its icon. The default is
     * {@link PositionConstants#EAST}. Valid values are
     * <UL>
     * <LI><EM>{@link PositionConstants#EAST}</EM>
     * <LI>{@link PositionConstants#NORTH}
     * <LI>{@link PositionConstants#SOUTH}
     * <LI>{@link PositionConstants#WEST}
     * </UL>
     * 
     * e.g. PositionConstants#EAST indicates the text is on the east of the
     * icon.
     * <p>
     * <p>
     * See the documentation describing the layout of the label in the class
     * header {@link WrappingLabel} for more detailed information.
     * </p>
     * 
     * @param where
     *            the text placement
     */
    public void setTextPlacement(int where) {
        if (getTextPlacement() == where)
            return;
        setPlacementFlags(where, FLAG_TEXT_PLACEMENT);

        revalidate();
        repaint();
    }

    /**
     * Sets whether the label text should be underlined
     * 
     * @param underline
     *            Whether the label text should be underlined
     */
    public void setTextUnderline(boolean underline) {
        ((TextFlowEx) getTextFlow()).setTextUnderline(underline);
    }

    /**
     * @return whether the label text is underlined
     */
    public boolean isTextUnderlined() {
        return ((TextFlowEx) getTextFlow()).isTextUnderlined();
    }

    /**
     * Sets whether the label text should be striked-through
     * 
     * @param strikeThrough
     *            whether the label text should be striked-through
     */
    public void setTextStrikeThrough(boolean strikeThrough) {
        ((TextFlowEx) getTextFlow()).setTextStrikeThrough(strikeThrough);
    }

    /**
     * @return whether the label text is striked-through
     */
    public boolean isTextStrikedThrough() {
        return ((TextFlowEx) getTextFlow()).isTextStrikedThrough();
    }

    /**
     * Sets whether the label text should wrap
     * 
     * @param b
     *            whether the label text should wrap
     */
    public void setTextWrap(boolean textWrapOn) {
        if (textWrapOn != isTextWrapOn()) {
            setLayoutManager(getTextFlow(), textWrapOn);
            revalidate();
            repaint();
        }
    }

    /**
     * @return whether the label text wrap is on
     */
    public boolean isTextWrapOn() {
        return getTextFlow().getLayoutManager() instanceof ParagraphTextLayout;
    }

    /**
     * Sets the text justification of the label text. The default is
     * {@link PositionConstants#LEFT}. Valid values are
     * <UL>
     * <LI><EM>{@link PositionConstants#LEFT}</EM>
     * <LI>{@link PositionConstants#CENTER}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * 
     * @param alignment
     *            the text justification.
     */
    public void setTextJustification(int justification) {
        getFlowPage().setHorizontalAligment(justification);
    }

    /**
     * Gets the text justification of the label text.
     * 
     * @return the text justification -- {@link PositionConstants#LEFT},
     *         {@link PositionConstants#CENTER}, or
     *         {@link PositionConstants#RIGHT}
     */
    public int getTextJustification() {
        return getFlowPage().getHorizontalAligment();
    }

    /**
     * setPlacementFlags
     * 
     * @param align
     * @param flagOffset
     */
    private void setPlacementFlags(int align, int flagOffset) {
        flags &= ~(0x7 * flagOffset);
        switch (align) {
            case EAST:
                flags |= 0x1 * flagOffset;
                break;
            case WEST:
                flags |= 0x2 * flagOffset;
                break;
            case NORTH:
                flags |= 0x3 * flagOffset;
                break;
            case SOUTH:
                flags |= 0x4 * flagOffset;
                break;
        }
    }

    /**
     * getPlacement
     * 
     * @param flagOffset
     * @return PositionConstant representing the placement
     */
    private int getPlacement(int flagOffset) {
        int wrapValue = flags & (0x7 * flagOffset);
        if (wrapValue == 0x1 * flagOffset)
            return EAST;
        else if (wrapValue == 0x2 * flagOffset)
            return WEST;
        else if (wrapValue == 0x3 * flagOffset)
            return NORTH;
        else if (wrapValue == 0x4 * flagOffset)
            return SOUTH;

        return EAST;
    }

    /**
     * setAlignmentFlags
     * 
     * @param alignment
     * @param flagOffset
     */
    private void setAlignmentFlags(int alignment, int flagOffset) {
        flags &= ~(0xF * flagOffset);
        switch (alignment) {
            case CENTER:
                flags |= 0x1 * flagOffset;
                break;
            case TOP:
                flags |= 0x2 * flagOffset;
                break;
            case LEFT:
                flags |= 0x3 * flagOffset;
                break;
            case RIGHT:
                flags |= 0x4 * flagOffset;
                break;
            case BOTTOM:
                flags |= 0x5 * flagOffset;
                break;
            case TOP | LEFT:
                flags |= 0x6 * flagOffset;
                break;
            case TOP | RIGHT:
                flags |= 0x7 * flagOffset;
                break;
            case BOTTOM | LEFT:
                flags |= 0x8 * flagOffset;
                break;
            case BOTTOM | RIGHT:
                flags |= 0x9 * flagOffset;
                break;
        }
    }

    /**
     * Retrieves the alignment value from the flags member.
     * 
     * @param flagOffset
     *            that is the bitwise value representing the offset.
     * @return PositionConstant representing the alignment
     */
    private int getAlignment(int flagOffset) {
        int wrapValue = flags & (0xF * flagOffset);
        if (wrapValue == 0x1 * flagOffset)
            return CENTER;
        else if (wrapValue == 0x2 * flagOffset)
            return TOP;
        else if (wrapValue == 0x3 * flagOffset)
            return LEFT;
        else if (wrapValue == 0x4 * flagOffset)
            return RIGHT;
        else if (wrapValue == 0x5 * flagOffset)
            return BOTTOM;
        else if (wrapValue == 0x6 * flagOffset)
            return TOP | LEFT;
        else if (wrapValue == 0x7 * flagOffset)
            return TOP | RIGHT;
        else if (wrapValue == 0x8 * flagOffset)
            return BOTTOM | LEFT;
        else if (wrapValue == 0x9 * flagOffset)
            return BOTTOM | RIGHT;

        return CENTER;
    }

    /**
     * @return the focus state of this label
     */
    public boolean hasFocus() {
        return (flags & FLAG_HASFOCUS) != 0;
    }

    /**
     * Sets the focus state of this label
     * 
     * @param focus
     *            true will cause a focus rectangle to be drawn around the text
     *            of the Label
     */
    public void setFocus(boolean focus) {
        if (hasFocus() == focus)
            return;
        setFlag(FLAG_HASFOCUS, focus);
        repaint();
    }

    /**
     * @return the selection state of this label
     */
    public boolean isSelected() {
        return (flags & FLAG_SELECTED) != 0;
    }

    /**
     * Sets the selection state of this label
     * 
     * @param selected
     *            true will cause the label to appear selected
     */
    public void setSelected(boolean selected) {
        if (isSelected() == selected)
            return;
        setFlag(FLAG_SELECTED, selected);
        repaint();
    }

    public void setFont(Font f) {
        super.setFont(f);

        // need to trigger a repaint of the textflow
        getTextFlow().setFont(f);
    }

    private void setLayoutManager(TextFlow textFlow, boolean wrappingOn) {
        TextLayout layout;
        if (wrappingOn) {
            layout = new ParagraphTextLayout(textFlow,
                ParagraphTextLayout.WORD_WRAP_SOFT);
        } else {
            layout = new TruncatedSingleLineTextLayout((TextFlowEx) textFlow,
                getTruncationString());
        }
        layout.setFlowContext((FlowContext) ((FlowPage) textFlow.getParent())
            .getLayoutManager());
        textFlow.setLayoutManager(layout);
    }

    public String toString() {
        // for debugging purposes
        return getText();
    }

}
