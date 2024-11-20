/**
  * Particle
  **/
public class Particle {
  double x, y, velX, velY, mass, radius;
  String shape;

  public Particle(double x, double y, double velX, double velY, double mass, double radius, String shape) {
    this.x = x;
    this.y = y;
    this.velX = velX;
    this.velY = velY;
    this.mass = mass;
    this.radius = radius;
    this.shape = shape;
  }
}
