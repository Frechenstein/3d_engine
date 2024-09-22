package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class Main {
	
	public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        frame.setTitle("3D Renderer");
        
        int[] x = new int[] {0};
        int[] y = new int[] {0};
        
        // slider to control horizontal rotation
        JSlider headingSlider = new JSlider(-180, 180, 0);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);
        
        // panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(Color.blue);
                //g2.drawOval(getWidth() / 2 - 10, getHeight() / 2 - 10, 20, 20);
                
                List<Triangle> tris = new ArrayList<>();
                
                // Tetraeder
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(-100, 100, -100),
                        Color.WHITE));
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.RED));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        new Vertex(100, 100, 100),
                        Color.GREEN));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        new Vertex(-100, -100, 100),
                        Color.BLUE));
                
                // Würfel
//                tris.add(new Triangle(new Vertex(100, 100, 100),
//                		new Vertex(100, 100, -100),
//                		new Vertex(100, -100, -100),
//                		Color.WHITE));
//                tris.add(new Triangle(new Vertex(100, -100, 100),
//                		new Vertex(100, 100, 100),
//                		new Vertex(100, -100, -100),
//                		Color.WHITE));
//                
//                tris.add(new Triangle(new Vertex(-100, 100, 100),
//                		new Vertex(-100, -100, 100),
//                		new Vertex(100, -100, 100),
//                		Color.WHITE));
//                tris.add(new Triangle(new Vertex(100, -100, 100),
//                		new Vertex(-100, 100, 100),
//                		new Vertex(100, 100, 100),
//                		Color.WHITE));
//                
//                tris.add(new Triangle(new Vertex(100, 100, -100),
//                		new Vertex(-100, 100, -100),
//                		new Vertex(-100, -100, -100),
//                		Color.WHITE));
//                tris.add(new Triangle(new Vertex(100, 100, -100),
//                		new Vertex(-100, -100, -100),
//                		new Vertex(100, -100, -100),
//                		Color.WHITE));
//                
//                tris.add(new Triangle(new Vertex(-100, 100, -100),
//                		new Vertex(-100, 100, 100),
//                		new Vertex(-100, -100, 100),
//                		Color.WHITE));
//                tris.add(new Triangle(new Vertex(-100, -100, -100),
//                		new Vertex(-100, 100, -100),
//                		new Vertex(-100, -100, 100),
//                		Color.WHITE));
//                
//                tris.add(new Triangle(new Vertex(-100, 100, -100),
//                		new Vertex(100, 100, -100),
//                		new Vertex(100, 100, 100),
//                		Color.WHITE));
//                tris.add(new Triangle(new Vertex(-100, 100, 100),
//                		new Vertex(-100, 100, -100),
//                		new Vertex(100, 100, 100),
//                		Color.WHITE));
//                
//                tris.add(new Triangle(new Vertex(-100, -100, 100),
//                		new Vertex(100, -100, 100),
//                		new Vertex(100, -100, -100),
//                		Color.WHITE));
//                tris.add(new Triangle(new Vertex(-100, -100, 100),
//                		new Vertex(-100, -100, -100),
//                		new Vertex(100, -100, -100),
//                		Color.WHITE));
  
                double heading = Math.toRadians(x[0]);
                Matrix3 headingTransform = new Matrix3(new double[]{
                		Math.cos(heading), 0, -Math.sin(heading),
                		0, 1, 0,
                		Math.sin(heading), 0, Math.cos(heading)
                });
                double pitch = Math.toRadians(y[0]);
                Matrix3 pitchTransform = new Matrix3(new double[]{
                		1, 0, 0,
                		0, Math.cos(pitch), Math.sin(pitch),
                		0, -Math.sin(pitch), Math.cos(pitch)
                });
                //Merge matrices in advance
                Matrix3 transform = headingTransform.multiply(pitchTransform);
                
                // The generated shape is centered on the origin (0, 0, 0), and we will do rotation around the origin later.
                
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);
                for (Triangle t : tris) {
                    Vertex v1 = transform.transform(t.v1);
                    Vertex v2 = transform.transform(t.v2);
                    Vertex v3 = transform.transform(t.v3);
                    Path2D path = new Path2D.Double();
                    path.moveTo(v1.x, v1.y);
                    path.lineTo(v2.x, v2.y);
                    path.lineTo(v3.x, v3.y);
                    path.closePath();
                    g2.draw(path);
                }
            }
        };
        
        pane.add(renderPanel, BorderLayout.CENTER);
        
        headingSlider.addChangeListener(e -> {
        	x[0] = headingSlider.getValue();
        	renderPanel.repaint();
        });
        pitchSlider.addChangeListener(e -> {
        	y[0] = pitchSlider.getValue();
        	renderPanel.repaint();
        });
        
        renderPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double yi = 180.0 / renderPanel.getHeight();
                double xi = 180.0 / renderPanel.getWidth();
                x[0] = (int) (e.getX() * xi);
                y[0] = -(int) (e.getY() * yi);
                //System.out.println(x[0] + " " + y[0]);
                renderPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        
        frame.setSize(600, 600);
        frame.setVisible(true);
	}

}


//x indicates the movement in the left and right directions
//y indicates the up-and-down movement on the screen
//z indicates depth (so the z axis is perpendicular to your screen). Positive z means "towards the observer".
class Vertex {
	double x;
	double y;
	double z;
	Optional<Color> color;
	
	Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = Optional.empty();
 	}
	
	Vertex(double x, double y, double z, Color color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = Optional.of(color);
 	}
}

class Triangle {
	Vertex v1;
	Vertex v2;
	Vertex v3;
	Color color;
	
	Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
	}
}

class Matrix3 {
    double[] values;
    Matrix3(double[] values) {
        this.values = values;
    }
    Matrix3 multiply(Matrix3 other) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] +=
                        this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new Matrix3(result);
    }
    Vertex transform(Vertex in) {
        return new Vertex(
            in.x * values[0] + in.y * values[3] + in.z * values[6],
            in.x * values[1] + in.y * values[4] + in.z * values[7],
            in.x * values[2] + in.y * values[5] + in.z * values[8]
        );
    }
}