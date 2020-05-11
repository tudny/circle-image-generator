package com.tudny.bitmapgenerator.core;

import java.awt.*;
import java.util.ArrayList;

public class Circle {

	private static final Double DEFAULT_RADIUS = 1.0;
	private static final Double DEFAULT_RADIUS_VELOCITY = 1.0;

	private Integer x;
	private Integer y;
	private Double radius = DEFAULT_RADIUS;
	private Double radiusVelocity = DEFAULT_RADIUS_VELOCITY;
	private Color color;

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Double getRadiusVelocity() {
		return radiusVelocity;
	}

	public void setRadiusVelocity(Double radiusVelocity) {
		this.radiusVelocity = radiusVelocity;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Circle(Integer x, Integer y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	private void update(ArrayList<Circle> circleArrayList) {

		for(Circle circle : circleArrayList){
			if(this != circle && this.intersects(circle)){
				radiusVelocity = 0.0;
			}
		}

		radius += radiusVelocity;

		/*
		Widzę, że ciekawość zżera. Czekam na wiadomość na messengerze z pytaniem, dlaczego owa klasa nie jest nigdzie użyta.
		* */
	}

	public boolean intersects(Circle circle){
		double dx = this.getX() - circle.getX();
		double dy = this.getY() - circle.getY();

		double dis = Math.sqrt(dx * dx + dy * dy);

		return ( dis <= this.getRadius() + circle.getRadius() );
	}
}
