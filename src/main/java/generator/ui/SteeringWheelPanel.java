package generator.ui;


import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.batik.parser.TransformListParser;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;

public class SteeringWheelPanel extends AbstractPanel {

    protected double oldX = 0.0;

    public SteeringWheelPanel() {
        JSVGCanvas c = new JSVGCanvas();
        c.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
        String uri = new File("steering-wheel.svg").toURI().toString();
        c.setURI(uri);
        c.loadSVGDocument(uri);
        c.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                SVGDocument svgDoc = e.getSVGDocument();
                Element elt = svgDoc.getElementById("layer1");
                elt.setAttributeNS(null, "cursor", "pointer");
                EventTarget t = (EventTarget) elt;
                t.addEventListener("mousemove", new OnMoveAction(), false);
            }
        });
        setLayout(new BorderLayout());
        add(c, BorderLayout.CENTER);

    }

    public class OnMoveAction implements EventListener {
        public void handleEvent(Event evt) {
            //Cast the event onto a Batik Mouse event,
            //so we can get ccordinates
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();
            //convert it to a point for use with the Matrix
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            Element elt = (Element) evt.getCurrentTarget();
            double degree = 0.5;
            if (oldX > pt.getX()) {
                degree = -0.5;
            }

            rotate(elt, degree);
            oldX = pt.getX();
        }
    }

    private void rotate(Element elt, double degree) {
        String strTransform = elt.getAttributeNS(null,
                SVGConstants.SVG_TRANSFORM_ATTRIBUTE);
        TransformListParser p = new TransformListParser();
        AWTTransformProducer tp = new AWTTransformProducer();
        p.setTransformListHandler(tp);
        p.parse(strTransform);
        AffineTransform at = tp.getAffineTransform();
        at.rotate(Math.toRadians(degree), 170.0, 170.0);
        elt.setAttributeNS(null, SVGConstants.SVG_TRANSFORM_ATTRIBUTE,
                affineTransformToString(at));
    }


    private String affineTransformToString(final AffineTransform at) {
        double[] matrix = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        at.getMatrix(matrix);
        return matrixArrayToString(matrix);
    }

    private String matrixArrayToString(double[] vals) {
        return new StringBuilder("matrix(").append(vals[0]).append(" ").append(vals[1]).append(" ").append(vals[2]).append(" ").append(vals[3]).append(" ").append(vals[4]).append(" ").append(vals[5]).append(") ").toString();
    }

}
