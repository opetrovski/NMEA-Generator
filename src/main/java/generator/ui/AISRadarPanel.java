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
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;


public class AISRadarPanel extends AbstractPanel {

    protected Element selectedItem;
    protected SVGOMPoint initialDragPoint;
    protected final int DRAG_UP = 1;
    protected final int DRAG_DOWN = 0;
    protected int drag = DRAG_UP;
    SVGDocument svgDoc;

    public AISRadarPanel() {
        JSVGCanvas c = new JSVGCanvas();
        c.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
        String uri = new File("radar.svg").toURI().toString();
        c.setURI(uri);
        c.loadSVGDocument(uri);
        c.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                svgDoc = e.getSVGDocument();
                Element elt = svgDoc.getElementById("ship1");
                EventTarget t = (EventTarget) elt;

                // Adds  'mouseevent' listeners
                t.addEventListener("mousedown", new OnDownAction(), false);
                t.addEventListener("mousemove", new OnMoveAction(), false);
                t.addEventListener("mouseup", new OnUpAction(), false);
                t.addEventListener("mouseout", new OnLeaveAction(), false);
                //t.addEventListener("mouseover", new OnOverAction(), false);
                //t.addEventListener("click", new OnClickAction(), false);
            }
        });

//        c.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
//                System.out.println("x: "+e.getX() +" y:" + e.getY());
//            }
//        });

        JPanel configuration = new JPanel();
        configuration.setLayout(new GridBagLayout());
        addInputField(configuration, "Number of vessels", 10);
        addInputField(configuration, "Radius [nm]", 10);
        configuration.add(new JLabel(""));
        configuration.add(new JButton("Generate"));
        configuration
                .setBorder(BorderFactory.createTitledBorder("Configuration"));

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("AIS Radar"));
        add(configuration, BorderLayout.NORTH);
        add(c, BorderLayout.CENTER);
    }

    public class OnEnterAction implements EventListener {
        public void handleEvent(org.w3c.dom.events.Event evt) {
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            // Convert Screen coordinates to Document Coordinates.
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint) pt.matrixTransform(mat);
            System.out.println("OnEnterAction x: " + pt.getX() + " y:" + pt.getY());
        }
    }

    public class OnLeaveAction implements EventListener {
        public void handleEvent(org.w3c.dom.events.Event evt) {
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();
            drag = DRAG_UP;
            // Convert Screen coordinates to Document Coordinates.
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint) pt.matrixTransform(mat);
            System.out.println("OnLeaveAction x: " + pt.getX() + " y:" + pt.getY());
        }
    }

    public class OnOverAction implements EventListener {
        public void handleEvent(org.w3c.dom.events.Event evt) {
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            // Convert Screen coordinates to Document Coordinates.
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint) pt.matrixTransform(mat);
            System.out.println("OnOverAction x: " + pt.getX() + " y:" + pt.getY());
        }
    }

    public class OnClickAction implements EventListener {
        public void handleEvent(org.w3c.dom.events.Event evt) {
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            drag = DRAG_DOWN;
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            // Convert Screen coordinates to Document Coordinates.
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint) pt.matrixTransform(mat);
            System.out.println("OnClickAction x: " + pt.getX() + " y:" + pt.getY());
        }
    }

    public class OnDownAction implements EventListener {
        public void handleEvent(org.w3c.dom.events.Event evt) {
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            drag = DRAG_DOWN;
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            // Convert Screen coordinates to Document Coordinates.
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint) pt.matrixTransform(mat);
            System.out.println("OnDownAction x: " + pt.getX() + " y:" + pt.getY());
        }
    }

    public class OnUpAction implements EventListener {
        public void handleEvent(Event evt) {
            drag = DRAG_UP;
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            // Convert Screen coordinates to Document Coordinates.
            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint) pt.matrixTransform(mat);
            System.out.println("OnUpAction x: " + pt.getX() + " y:" + pt.getY());
        }
    }

    public class OnMoveAction implements EventListener {
        public void handleEvent(Event evt) {
            if (drag == DRAG_DOWN) {
                //Cast the event onto a Batik Mouse event,
                //so we can get ccordinates
                DOMMouseEvent elEvt = (DOMMouseEvent) evt;
                int nowToX = elEvt.getClientX();
                int nowToY = elEvt.getClientY();
                //convert it to a point for use with the Matrix
                SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
                //Get the items screen coordinates, and apply the transformation
                // elem -> screen
                SVGMatrix mat = ((SVGLocatable) evt.getTarget()).getScreenCTM();

                mat = mat.inverse();                  // screen -> elem
                SVGOMPoint dragpt = (SVGOMPoint) pt.matrixTransform(mat);
                System.out.println("OnMoveAction x: " + pt.getX() + " y:" + pt.getY());

                Element elt2 = ((SVGLocatable) evt.getTarget()).getNearestViewportElement();
                Element elt = svgDoc.getElementById("ship1");
                moveTo(elt, dragpt.getX(), dragpt.getY());

//                AWTTransformProducer tp = new AWTTransformProducer();
//                AffineTransform at = tp.getAffineTransform();
//                double sdf = dragpt.getX();
//                double asd = dragpt.getY();
//                at.setToTranslation(sdf,asd);
//                elt.setAttributeNS(null, SVGConstants.SVG_TRANSFORM_ATTRIBUTE,
//                        affineTransformToString(at));


//                elt.setAttribute("x", ""+ dragpt.getX());
//                elt.setAttribute("y", ""+ dragpt.getY());

//                AffineTransform translateBy5Units =
//                        AffineTransform.getTranslateInstance(5, 0);
//                SVGUtils.transformElement(elt, translateBy5Units);

//                String strTransform = elt.getAttributeNS(null,
//                SVGConstants.SVG_TRANSFORM_ATTRIBUTE);
//                TransformListParser p = new TransformListParser();
//                AWTTransformProducer tp = new AWTTransformProducer();
//                p.setTransformListHandler(tp);
//                p.parse(strTransform);
//                AffineTransform at = tp.getAffineTransform();
//                at.rotate(Math.toRadians(Double.valueOf("3.0")));
//
//                elt.setAttributeNS(null, SVGConstants.SVG_TRANSFORM_ATTRIBUTE,
//                        affineTransformToString(at));


//                Element elem = svgDoc.getElementById("your-id");
//                AffineTransform translateBy5Units =
//                        AffineTransform.getTranslateInstance(5, 0);
//                transformElement(elem, translateBy5Units);
//
//                AffineTransform scaleByFactor10 =
//                        AffineTransform.getScaleInstance(10, 10);
//                transformElement(elem, scaleByFactor10);

/* Now to actually move the objects, there are several approaches:
  92
  93            a.) If they are individual elements (i.e. no <g>´s), you can simply modify
  94                the element attributes X, Y. by:
  95                "element.setAttribute("x", ""+ dragpt.getX());"
  96
  97            b.) You can iterate though all the elements in the selection (in case of
  98                multiple selections or <g> elements) and individually change the x, y
  99                attributes of each item, as in a.)
 100
 101            c.) You can apply a transform on your selected element. To do so you need
 102                to perform a matrix transform from previous transforms this object
 103                has had. For example, the first time you drag an object, you will add a
 104                transform attribute such as: transform="translate(dragpt.x, dragpt.y)",
 105                but for subsequent drag operations you need to do, for each axis:
 106                oldtransformCoordinate + newCordinate
 107                For a fully functional example of this approach, you can see the Adobe
 108                SVGDraw application, found at:
 109                http://www.adobe.com/svg/demos/svgDraw/svgDraw/index.html
 110                Although this demo is purely Javascript-Adobe SVG Viewer application,
 111                you may learn a lot at how to handle the DOM and perform multiple
 112                operations. Pay specific attention to the Matrix transform methods.
 113
 114               To analyze easily the Javascript code, i recommend the Eclipse IDE v3.0.2
 115               with the jseditor plugin (sourceforge.net/projects/jseditor)
 116                         */
            }
        }
    }

    private void moveTo(Element elt, double x, double y) {
        String strTransform = elt.getAttributeNS(null,
                SVGConstants.SVG_TRANSFORM_ATTRIBUTE);
        TransformListParser p = new TransformListParser();
        AWTTransformProducer tp = new AWTTransformProducer();
        p.setTransformListHandler(tp);
        p.parse(strTransform);
        AffineTransform at = tp.getAffineTransform();
        at.setToTranslation(x, y);
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
