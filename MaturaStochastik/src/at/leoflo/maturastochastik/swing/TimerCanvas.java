package at.leoflo.maturastochastik.swing;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class TimerCanvas extends Canvas {
	private Color circleColor;
	private final Graphics g;
	private Polygon circle;
	
	public TimerCanvas(Color circleColor, Color backgroundColor) {
		super();
		
		this.circleColor = circleColor;
		g = this.;
		
		this.setBackground(backgroundColor);
		
		circle = new Polygon();
	}
	
	public void setAmount(double percentage) {
		double angle = (percentage / 50) * Math.PI;
		double minimumAngle = Math.PI / 1000;
		
		circle.addPoint(getHeight() / 2, getHeight() / 2);
		
		for (double currentAngle = 0; currentAngle < angle; currentAngle += minimumAngle) {
			int pointX = (int) (Math.cos(currentAngle) * getHeight());
			int pointY = (int) (Math.sin(currentAngle) * getHeight());
			
			circle.addPoint(pointX, pointY);
		}
		
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(circleColor);
		g.drawPolygon(circle);
	}
}
