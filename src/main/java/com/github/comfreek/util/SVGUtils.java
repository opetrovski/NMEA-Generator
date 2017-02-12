package com.github.comfreek.util;

import org.apache.batik.parser.AWTTransformProducer;
import org.apache.batik.parser.TransformListParser;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;

import java.awt.geom.AffineTransform;

/**
 * This class provides static utility methods for the use with Batik and SVG
 * elements.
 *
 * @author ComFreek <comfreek@outlook.com> if not otherwise noted.
 * @license MIT (for all parts whose author is ComFreek)
 */
public class SVGUtils {

    private SVGUtils() {
    }

    /**
     * Applies an AffineTransform to a (SVG) element.
     * <br>
     * While this function works with all classes equal to or subclassing
     * {@link org.w3c.dom.Element Element}, adding and modifying the "transform"
     * attribute is probably of no effect.
     * <br>
     * Example:
     * <pre>
     * <code>  // Document svgDoc = ...;
     *   Element elem = svgDoc.getElementById("your-id");
     *   AffineTransform translateBy5Units =
     *       AffineTransform.getTranslateInstance(5, 0);
     *   transformElement(elem, translateBy5Units);
     *
     *   AffineTransform scaleByFactor10 =
     *       AffineTransform.getScaleInstance(10, 10);
     *   transformElement(elem, scaleByFactor10);
     * </code>
     * </pre>
     *
     * @param element   The element.
     * @param transform The AffineTransform you want to apply. The resulting
     *                  transformation of the object is
     *                  <code>transform.{@link AffineTransform#concatenate(AffineTransform) concatenated}(oldTransform)</code>,
     *                  where <code>oldTransform</code> represents the current local (i.e.
     *                  possible parents' transforms will not be taken into acconut) transform of
     *                  the element.
     * @see #getElementTransform(Element)
     */
    public static void transformElement(final Element element, final AffineTransform transform) {
        final AffineTransform oldTransform = getElementTransform(element);
        transform.concatenate(oldTransform);
        element.setAttributeNS(null, SVGConstants.SVG_TRANSFORM_ATTRIBUTE,
                AffineTransformUtils.affineTransformToString(transform));
    }

    /**
     * Retrieves and parses the transformation attribute of a Element into an
     * AffineTransform object.
     * <br>
     * <p>
     * This function can cope with all transform values supported by
     * {@link org.apache.batik.parser.TransformListParser TransformListParser}.
     * These include <code>translate()</code>, <code>rotate()</code>,
     * <code>scale()</code>, <code>skew()</code> and <code>matrix()</code>. It
     * will also correctly handle empty or non-existing attributes (i.e.
     * returning an Identity matrix).
     *
     * @param element The SVG element.
     * @return An AffineTransform which equals the (local) transformations
     * specified in the SVG element.
     * @author fireball in a mail to the xmlgraphics-batik-users mailing list:
     * <a href="http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/201208.mbox/%3c1344872547972-4655199.post@n4.nabble.com%3e">click here</a>
     * @see #transformElement(Element, AffineTransform)
     */
    public static AffineTransform getElementTransform(final Element element) {
        final String oldTransformStr = element.getAttributeNS(null, SVGConstants.SVG_TRANSFORM_ATTRIBUTE);
        final TransformListParser tListParser = new TransformListParser();
        final AWTTransformProducer tProducer = new AWTTransformProducer();

        tListParser.setTransformListHandler(tProducer);
        tListParser.parse(oldTransformStr);
        return tProducer.getAffineTransform();
    }
}
