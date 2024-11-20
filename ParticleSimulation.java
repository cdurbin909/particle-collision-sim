import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import javax.swing.*;

class ParticleSimulation extends JFrame {
  private java.util.List<Particle> particles;
  private int SCREEN_WIDTH, SCREEN_HEIGHT = 0;

  public ParticleSimulation(int SCREEN_WIDTH, int SCREEN_HEIGHT) {
    this.SCREEN_WIDTH = SCREEN_WIDTH;
    this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    particles = new ArrayList<>();
    // Add some particles with initial positions and small velocities
    int partCount = 5;
    for (int i = 0; i < partCount; i++) {
      Random rand = new Random();
      int x = rand.nextInt(60, 1240);
      int y = rand.nextInt(60, 782);
      int negativeX = rand.nextInt(0, 2);
      int velX = rand.nextInt(200, 1000);
      int negativeY = rand.nextInt(0, 2);
      int velY = rand.nextInt(200, 1000);
      double mass = rand.nextDouble() * 25;

      int minRadius = 25;
      int maxRadius = 85;

      int radius = (int) Math.round(minRadius + (mass / 25) * (maxRadius - minRadius));
      if (negativeX == 0) {
        velX *= -1;
      }
      if (negativeY == 0) {
        velY *= -1;
      }
      particles.add(new Particle(x, y, velX, velY, mass, radius, "circle"));
    }
    // particles.add(new Particle((SCREEN_WIDTH / 2) - 120, (SCREEN_HEIGHT / 2) - 30, 0, 0, 1, 60, "circle"));
    // particles.add(new Particle((SCREEN_WIDTH / 2) - 60, (SCREEN_HEIGHT / 2) - 30, 0, 0, 1, 60, "circle"));
    // particles.add(new Particle((SCREEN_WIDTH / 2), (SCREEN_HEIGHT / 2) - 30, 0, 0, 1, 60, "circle"));
    // particles.add(new Particle(900, (SCREEN_HEIGHT / 2) - 30, -800, 0, 1, 60, "circle"));
  }

  public void collision() {
    for (int i = 0; i < particles.size(); i++) {
      Particle p1 = particles.get(i);
      for (int j = i + 1; j < particles.size(); j++) { // Start j at i + 1 to avoid double checking
        Particle p2 = particles.get(j);

        // Calculate the distance between particles
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check if the particles are colliding
        double combinedRadius = p1.radius + p2.radius; // Replace particleRadius * 2
        if (distance <= combinedRadius) {
          // Normalized vector components between the particles
          double nx = dx / distance;
          double ny = dy / distance;

          // Relative velocity components along the collision normal
          double dvx = p1.velX - p2.velX;
          double dvy = p1.velY - p2.velY;
          double dotProduct = dvx * nx + dvy * ny;

          // Skip if the particles are moving apart
          if (dotProduct > 0)
            continue;

          // Impulse scalar based on masses and normal velocity
          double impulse = (2 * dotProduct) / (p1.mass + p2.mass);

          // Update velocities in the direction of the normal
          p1.velX -= impulse * p2.mass * nx;
          p1.velY -= impulse * p2.mass * ny;
          p2.velX += impulse * p1.mass * nx;
          p2.velY += impulse * p1.mass * ny;

          // Calculate overlap and apply separation to prevent sticking
          double overlap = combinedRadius - distance;
          p1.x += overlap * nx / 2;
          p1.y += overlap * ny / 2;
          p2.x -= overlap * nx / 2;
          p2.y -= overlap * ny / 2;
        }
      }
    }
  }

  public void update(double dt) {
    for (Particle p : particles) {
      p.x += p.velX * dt;
      p.y += p.velY * dt;

      // Boundary collision detection using particleRadius
      double diameter = p.radius * 2; // Replace particleRadius * 2
      if (p.x - p.radius <= 0 && p.velX < 0 || p.x >= SCREEN_WIDTH - p.radius && p.velX > 0) {
        p.velX = -p.velX;
      }
      if (p.y - p.radius <= 0 && p.velY < 0 || p.y >= (SCREEN_HEIGHT - 28) - p.radius && p.velY > 0) {
        p.velY = -p.velY;
      }
    }
  }

  public java.util.List<Particle> getParticles() {
    return particles;
  }
}
