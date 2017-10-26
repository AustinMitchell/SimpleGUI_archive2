package testers;

import java.awt.Color;
import java.util.Arrays;

import simple.run.*;
import simple.gui.*;
import simple.gui.textarea.Label;
import simple.gui.textarea.TextArea;
import simple.misc.*;
import simple.misc.hex.*;

public class HexTest extends SimpleGUIApp {
    public static void main(String[]args) { start(new HexTest(), "Hex Tester"); }
    public HexTest() {super(1200, 800, 60);}

    // Sets hex orientation, pointy-top or flat-top
    final static HexWidget.HexType hexType = HexWidget.HexType.POINT_TOP;
    // This will be used in the hex array to switch between 2D and 3D hex coordinates
    final static HexData.CoordinateConverter coord = (hexType == HexWidget.HexType.FLAT_TOP) ?
                                                               HexData.FLAT_TOP_COORD :
                                                               HexData.POINT_TOP_COORD;
    // determines offset of the grid for 2D coordinates.
    final static int even = 1;
    // Width of grid if using a 2D array system
    final static int width = 8;
    // Height of grid if using a 2D array system
    final static int height = 8;
    // Radius of the drawn hexagons. Used for the hex widgets themselves
    final static int hex_draw_radius = 60;
    // If using a radial hex array, determines the radius in terms of number of hexes from the center
    final static int hex_grid_radius = 3;
    // used for color display for each hex widget
    final static int grid_color_offset = 3;
    // Used to draw the background line grid
    final static int gridline_radius = hex_draw_radius;
    
    // This point will be the center of the hex at (0, 0), or (0, 0, 0) in 3D
    final static int offsetx = 400;
    final static int offsety = 400;
    
    // When you click on a hex the 2D and 3D coordinates will display on the label
    Label hexLabel;
    // Hex array collection object
    HexArray<HexButton> hexArray;
    // Will be used to determine where to place each hexagon. Uses the 3D coordinates to determine position
    Vector[] scaledAxes = {
            null,
            null,
            null
    };
    
    Tuple[] edgeSet, cornerSet;
    Label[] hexEdges, hexCorners;
    Label edgeLabel, cornerLabel;
       
    @Override
    public void setup() {
        HexData.Generator<HexButton> dgen = new HexData.Generator<HexButton>() {

            @Override
            public HexButton generate(int x, int y, int hx, int hy, int hz) {
                return generate(null, new Tuple(hx, hy, hz));
            }

            @Override
            public HexButton generate(Tuple base, Tuple cube) {
                // This vector will determine the visual offset for each hex when drawn
                Vector shift = new Vector(0, 0);
                for (int axis=0; axis<3; axis++) {
                    // Takes each component of the cube coordinates and multiplies it by the same base vector component
                    // Adds it to the shift
                    shift = shift.add(scaledAxes[axis].mult(cube.entry(axis)));
                }
                
                // Create a new hexbutton with this data
                HexButton newHex;
                newHex = new HexButton(offsetx + (int)shift.x(), offsety + (int)shift.y(), hex_draw_radius, hexType);
                
                // Colors each hex. High component 0 is red, 1 is green and 2 is blue. Makes a kind of color wheel.
                float dist_ratio = (255f/(hex_grid_radius+grid_color_offset));
                int r = (int)(Math.max(cube.entry(0)+grid_color_offset, 0) * dist_ratio);
                int g = (int)(Math.max(cube.entry(1)+grid_color_offset, 0) * dist_ratio);
                int b = (int)(Math.max(cube.entry(2)+grid_color_offset, 0) * dist_ratio);
                newHex.setFillColor(new Color(r, g, b));
                return newHex;
            }
        };
        
        hexLabel = new Label(930, 10, 240, 50);
        // The containing box for the label is normally not drawn unless specifically requested
        hexLabel.setBoxVisible(true);
        
        hexCorners = new Label[6];
        hexEdges = new Label[6];
        cornerLabel = new Label(950,  70, 200, 30, "Corners:");
        cornerLabel.setTextColor(Color.white);
        cornerLabel.setAlignment(TextArea.Alignment.WEST);
        edgeLabel =   new Label(950, 420, 200, 30, "Edges:");
        edgeLabel.setTextColor(Color.white);
        edgeLabel.setAlignment(TextArea.Alignment.WEST);
        
        for (int i=0; i<6; i++) {
            hexCorners[i] = new Label(900, 100+i*50, 300, 50);
            hexCorners[i].setTextColor(Color.WHITE);
            hexEdges[i] = new Label(900, 450+i*50, 300, 50);
            hexEdges[i].setTextColor(Color.WHITE);
        }
        
        // Sets the 3 coordinate directions depending whether its a pointy-top or flat-top hexagon.
        // Scales each vector by the size of the hexagon radius
        scaledAxes[0] = new Vector(coord.getAxisVectors()[0]).mult(hex_draw_radius);
        scaledAxes[1] = new Vector(coord.getAxisVectors()[1]).mult(hex_draw_radius);
        scaledAxes[2] = new Vector(coord.getAxisVectors()[2]).mult(hex_draw_radius);
        // Using the radial hex constructor. When you iterate through this array, it will also iterate in radial form
        hexArray = new HexArray<HexButton>(even, coord, Tuple.createRadialHexGenerator(hex_grid_radius), dgen);
    
    }

    @Override
    public void loop() {
        // Draw the gridlines
        for (int i=0; i*gridline_radius < Math.max(getWidth(), getHeight()); i++) {
            Draw.setStroke(Color.BLACK);
            Draw.line(0, i*gridline_radius, getWidth(), i*gridline_radius);
            Draw.line(i*gridline_radius, 0, i*gridline_radius, getHeight());
        }
        
        // update and draw the hexes
        for (HexData<HexButton> hb: hexArray) {
            hb.data().update();
            hb.data().draw();
            
            // If a hex is clicked, update the label with the coordinates of that hexagon
            if (hb.data().clicked()) {
                hexLabel.setText("2D: " + hb.baseIndex() + " | 3D: " + hb.cubeIndex());
                cornerSet = hb.adjecentCorners();
                edgeSet = hb.adjecentEdges();
                for (int i=0; i<6; i++) {
                    hexCorners[i].setText(""+cornerSet[i]);
                    float[] properEdge = {edgeSet[i].entry(0)/2f, edgeSet[i].entry(1)/2f, edgeSet[i].entry(2)/2f};
                    hexEdges[i].setText(""+edgeSet[i] + " -> " + Arrays.toString(properEdge));
                }
            }
        }
        
        Draw.setColors(new Color(50, 50, 50), null);
        Draw.rect(900, 0, 300, 800);
        
        hexLabel.draw();
        cornerLabel.draw();
        edgeLabel.draw();
        for (int i=0; i<6; i++) {
            hexCorners[i].update();
            hexEdges[i].update();
            hexCorners[i].draw();
            hexEdges[i].draw();
            
            // When hovering over a corner dispaly label, this will indicate all the adjacent data with draw commands
            if (hexCorners[i].hovering() && cornerSet != null) {
                CornerData<Void> currentCornerData = new CornerData<Void>(cornerSet[i]);
                Vector currentCornerShift = new Vector(0, 0);
                
                Vector[] adjCorners = {new Vector(0, 0), new Vector(0, 0), new Vector(0, 0)};
                Tuple[] adjCornerData = currentCornerData.adjecentCorners();  
                Vector[] adjEdges   = {new Vector(0, 0), new Vector(0, 0), new Vector(0, 0)};
                Tuple[] adjEdgeData = currentCornerData.adjecentEdges();  
                Vector[] adjHexes   = {new Vector(0, 0), new Vector(0, 0), new Vector(0, 0)};
                Tuple[] adjHexData = currentCornerData.adjecentHexes();  
                
                for (int axis=0; axis<3; axis++) {
                    // Takes each component of the cube coordinates and multiplies it by the same base vector component
                    // Adds it to the shift
                    currentCornerShift = currentCornerShift.add(scaledAxes[axis].mult(currentCornerData.index().entry(axis)));
                    for(int j=0; j<3; j++) {
                        adjCorners[j] = adjCorners[j].add(scaledAxes[axis].mult(adjCornerData[j].entry(axis)));
                        // Edges coordinate systems are multiplied by 2
                        adjEdges[j] = adjEdges[j].add(scaledAxes[axis].mult(0.5f).mult(adjEdgeData[j].entry(axis)));
                        adjHexes[j] = adjHexes[j].add(scaledAxes[axis].mult(adjHexData[j].entry(axis)));
                    }
                }
                
                Draw.setColors(null, Color.DARK_GRAY, 5);
                for (int j=0; j<3; j++) {
                    Draw.line(offsetx+currentCornerShift.x(), offsety+currentCornerShift.y(), offsetx+adjCorners[j].x(), offsety+adjCorners[j].y());
                }
                
                Draw.setColors(Color.BLACK, Color.BLACK, 2);
                for (int j=0; j<3; j++) {
                    Draw.ovalCentered(offsetx+adjEdges[j].x(), offsety+adjEdges[j].y(), 7, 7);
                }
                
                Draw.setColors(Color.WHITE, Color.BLACK, 2);
                for (int j=0; j<3; j++) {
                    Draw.ovalCentered(offsetx+adjCorners[j].x(), offsety+adjCorners[j].y(), 7, 7);
                }
                
                Draw.setColors(new Color(255, 125, 125), Color.BLACK, 2);
                for (int j=0; j<3; j++) {
                    Draw.ovalCentered(offsetx+adjHexes[j].x(), offsety+adjHexes[j].y(), 7, 7);
                }
                
                Draw.setColors(Color.YELLOW, Color.BLACK, 1);
                Draw.ovalCentered(offsetx+currentCornerShift.x(), offsety+currentCornerShift.y(), 10, 10);
            }
            if (hexEdges[i].hovering() && edgeSet != null) {
                EdgeData<Void> currentEdgeData = new EdgeData<Void>(edgeSet[i]);
                Vector currentEdgeShift = new Vector(0, 0);
                
                Vector[] adjCorners = {new Vector(0, 0), new Vector(0, 0)};
                Tuple[] adjCornerData = currentEdgeData.adjecentCorners();  
                Vector[] adjEdges   = {new Vector(0, 0), new Vector(0, 0), new Vector(0, 0), new Vector(0, 0)};
                Tuple[] adjEdgeData = currentEdgeData.adjecentEdges();  
                Vector[] adjHexes   = {new Vector(0, 0), new Vector(0, 0)};
                Tuple[] adjHexData = currentEdgeData.adjecentHexes();  
                
                for (int axis=0; axis<3; axis++) {
                    // Takes each component of the cube coordinates and multiplies it by the same base vector component
                    // Adds it to the shift
                    currentEdgeShift = currentEdgeShift.add(scaledAxes[axis].mult(currentEdgeData.visualIndex()[axis]));
                    for(int j=0; j<2; j++) {
                        adjCorners[j] = adjCorners[j].add(scaledAxes[axis].mult(adjCornerData[j].entry(axis)));
                        // Edges coordinate systems are multiplied by 2
                        adjEdges[j*2] = adjEdges[j*2].add(scaledAxes[axis].mult(0.5f).mult(adjEdgeData[j*2].entry(axis)));
                        adjEdges[j*2+1] = adjEdges[j*2+1].add(scaledAxes[axis].mult(0.5f).mult(adjEdgeData[j*2+1].entry(axis)));
                        adjHexes[j] = adjHexes[j].add(scaledAxes[axis].mult(adjHexData[j].entry(axis)));
                    }
                }
                
                Draw.setColors(null, Color.DARK_GRAY, 5);
                Draw.line(offsetx+adjCorners[0].x(), offsety+adjCorners[0].y(), offsetx+adjCorners[1].x(), offsety+adjCorners[1].y());
                
                for (int j=0; j<4; j++) {
                    Draw.setColors(null, Color.DARK_GRAY, 5);
                    Draw.line(offsetx+adjEdges[j].x(), offsety+adjEdges[j].y(), offsetx+adjCorners[j/2].x(), offsety+adjCorners[j/2].y());
                    
                    Draw.setColors(Color.BLACK, Color.BLACK, 2);
                    Draw.ovalCentered(offsetx+adjEdges[j].x(), offsety+adjEdges[j].y(), 7, 7);
                }
                
                Draw.setColors(Color.WHITE, Color.BLACK, 2);
                for (int j=0; j<2; j++) {
                    Draw.ovalCentered(offsetx+adjCorners[j].x(), offsety+adjCorners[j].y(), 7, 7);
                }
                
                Draw.setColors(new Color(255, 125, 125), Color.BLACK, 2);
                for (int j=0; j<2; j++) {
                    Draw.ovalCentered(offsetx+adjHexes[j].x(), offsety+adjHexes[j].y(), 7, 7);
                }
                
                Draw.setColors(Color.YELLOW, Color.BLACK, 1);
                Draw.ovalCentered(offsetx+currentEdgeShift.x(), offsety+currentEdgeShift.y(), 10, 10);
            }
        }
        
        
        
        updateView();
    }

}
