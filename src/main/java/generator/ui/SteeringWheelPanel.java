package generator.ui;


import generator.nmea.Yacht;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SteeringWheelPanel extends AbstractPanel {

    protected double oldX = 0.0;
    Yacht yacht;

    public SteeringWheelPanel(Yacht yacht) {
        this.yacht = yacht;
        JSVGCanvas c = new JSVGCanvas();
        c.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);

        try {
            String uri = getFileURI("steering-wheel.svg");
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
        }catch(Exception e){
            System.err.println("Failed to load steering-wheel.svg. " + e.getMessage());
        }

        setLayout(new BorderLayout());
        add(c, BorderLayout.CENTER);

    }

    private String getFileURI(String filename) throws Exception {
        Path path = Files.createTempFile("","");
        path.toFile().deleteOnExit();
        InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
        Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        String absolutePath = path.toUri().getPath();
        if( absolutePath.startsWith("/C:") ){
            absolutePath = absolutePath.substring(3);
        }
        return absolutePath;
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
                yacht.decRudderPosition();
            }
            else{
                yacht.incRudderPosition();
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
