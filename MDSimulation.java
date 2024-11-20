/**
 * MDSimulation
 **/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MDSimulation extends JFrame {
  private ParticleSimulation simulation;
  private List<RectangularShape> particleShapes;
  // private List<Ellipse2D.Double> circleShapes;
  // private List<Rectangle2D.Double> rectangleShapes;
  private static final int SCREEN_WIDTH = 1300;
  private static final int SCREEN_HEIGHT = 842;

  public MDSimulation() {
    // INITIALIZE VARS
    simulation = new ParticleSimulation(SCREEN_WIDTH, SCREEN_HEIGHT);
    particleShapes = new ArrayList<>();
    // circleShapes = new ArrayList<>();
    // rectangleShapes = new ArrayList<>();

    // Initialize particle shapes based on the particle positions
    for (Particle p : simulation.getParticles()) {
      if (p.shape == "circle") {
        particleShapes.add(new Ellipse2D.Double(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2));
      }
      if (p.shape == "square") {
        particleShapes.add(new Rectangle2D.Double(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2));
      }
    }

    // Setup JFrame
    setTitle("Particle Simulation");
    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Add custom DrawingPanel to JFrame
    DrawingPanel drawingPanel = new DrawingPanel();
    add(drawingPanel);

    // Timer for animation loop (approx 60 FPS)
    Timer timer = new Timer(1, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulation.update(0.001); // Update simulation (dt ~ 1/60 second)
        simulation.collision();

        // Update particle shapes based on particle positions
        for (int i = 0; i < simulation.getParticles().size(); i++) {
          Particle p = simulation.getParticles().get(i);
          particleShapes.get(i).setFrame(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2);
        }

        drawingPanel.repaint(); // Redraw the panel
      }
    });
    timer.start();
  }

  // Custom JPanel subclass to handle painting particles
  private class DrawingPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(Color.BLUE); // Set a color for the particles

      List<Particle> particles = simulation.getParticles();

      g2d.setFont(new Font("SansSerif", Font.BOLD, 25)); // Change 16 to adjust the size as needed

      if (particleShapes != null) {
        for (int i = 0; i < particleShapes.size(); i++) {
          RectangularShape shape = particleShapes.get(i);
          Particle p = particles.get(i);

          // Draw the particle (circle)
          g2d.fill(shape);

          // Draw the mass text in the center of the particle
          g2d.setColor(Color.WHITE); // Set color for text
          String massText = String.format("%.1f", p.mass); // Format mass to 1 decimal place
          FontMetrics metrics = g.getFontMetrics(g.getFont());

          // Calculate the coordinates to center the text
          int textX = (int) (shape.getX() + shape.getWidth() / 2 - metrics.stringWidth(massText) / 2);
          int textY = (int) (shape.getY() + shape.getHeight() / 2 + metrics.getHeight() / 4);

          g2d.drawString(massText, textX, textY);
          g2d.setColor(Color.BLUE); // Reset color for next particle
        }
      }

      // if (circleShapes != null) {
      //   for (int i = 0; i < circleShapes.size(); i++) {
      //     Ellipse2D.Double shape = circleShapes.get(i);
      //     Particle p = particles.get(i);
      //
      //     // Draw the particle (circle)
      //     g2d.fill(shape);
      //
      //     // Draw the mass text in the center of the particle
      //     g2d.setColor(Color.WHITE); // Set color for text
      //     String massText = String.format("%.1f", p.mass); // Format mass to 1 decimal place
      //     FontMetrics metrics = g.getFontMetrics(g.getFont());
      //
      //     // Calculate the coordinates to center the text
      //     int textX = (int) (shape.getX() + shape.width / 2 - metrics.stringWidth(massText) / 2);
      //     int textY = (int) (shape.getY() + shape.height / 2 + metrics.getHeight() / 4);
      //
      //     g2d.drawString(massText, textX, textY);
      //     g2d.setColor(Color.BLUE); // Reset color for next particle
      //   }
      // }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MDSimulation app = new MDSimulation();
      app.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
      app.setVisible(true);
      app.pack();
    });
  }
}
