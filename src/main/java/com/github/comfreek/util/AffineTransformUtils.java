package com.github.comfreek.util;

import java.awt.geom.AffineTransform;

/**
 * Utility class.<br>
 * Its primary intent is to provide {@link #affineTransformToString(AffineTransform) affineTransformToString()}.
 *
 * @author ComFreek <comfreek@outlook.com> if not otherwise noted.
 * @license MIT (for all parts whose author is ComFreek)
 */
public class AffineTransformUtils {

    private AffineTransformUtils() {
    }

    /**
     * Converts an AffineTransform to a "matrix(...)" string representation as
     * used in SVG files for the "transform" attribute.
     *
     * @param at The AffineTransform object
     * @return Returns a "matrix(...)" string representation. for more
     * information
     * @author Jonathan Wood in a mail to the xmlgraphics-batik-users mailing list: <a href="http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/201208.mbox/%3cCAKiJDQTpJE-4hEGG-QMn=cDXK2tAn5H52TWBCZJxZ30pf15DYQ@mail.gmail.com%3e">click here</a>
     * @see <a
     * href="http://www.w3.org/TR/SVG11/coords.html#TransformAttribute">W3C SVG
     * 1.1 Specification: The 'transform' attribute</a>
     * @see <a
     * href="https://developer.mozilla.org/en/docs/Web/SVG/Attribute/transform">Mozilla
     * Developer Network: SVG transforma attribute</a>
     */
    public static String affineTransformToString(final AffineTransform at) {
        double[] matrix = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        at.getMatrix(matrix);
        return matrixArrayToString(matrix);
    }

    /**
     * Converts a double array in the form of {a, b, c, d, e, f} to a string
     * representation in the form of "matrix(a b c d e f)".
     * <p>
     * The returned string can be used in transform attributes of elements in
     * SVG files. files.
     *
     * @param vals A double array a
     * @return Returns the string representation in the form of "matrix(a b c d e f)".
     * @author Jonathan Wood in a mail to the xmlgraphics-batik-users mailing list: <a href="http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/201208.mbox/%3cCAKiJDQTpJE-4hEGG-QMn=cDXK2tAn5H52TWBCZJxZ30pf15DYQ@mail.gmail.com%3e">click here</a>
     * @see AffineTransformUtils#affineTransformToString(AffineTransform)
     * @see <a
     * href="http://www.w3.org/TR/SVG11/coords.html#TransformAttribute">W3C SVG
     * 1.1 Specification: The 'transform' attribute</a>
     * @see <a
     * href="https://developer.mozilla.org/en/docs/Web/SVG/Attribute/transform">Mozilla
     * Developer Network: SVG transforma attribute</a>
     */
    public static String matrixArrayToString(double[] vals) {
        return new StringBuilder("matrix(").append(vals[0]).append(" ").append(vals[1]).append(" ").append(vals[2]).append(" ").append(vals[3]).append(" ").append(vals[4]).append(" ").append(vals[5]).append(") ").toString();
    }
}
