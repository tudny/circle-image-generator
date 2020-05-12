package com.tudny.bitmapgenerator.core;

import java.awt.*;
import java.util.ArrayList;

public class Circle {

	private static final Double DEFAULT_RADIUS = 1.0;
	private static final Double DEFAULT_RADIUS_VELOCITY = 0.5;

	public static Circle factorNewCircle(int x, int y, int color, ArrayList<Circle> circles){
		Circle circle = new Circle(x, y, color);
		for(Circle cir : circles){
			if(cir != circle && cir.intersects(circle)){
				return null;
			}
		}

		return circle;
	}

	private Integer x;
	private Integer y;
	private Double radius = DEFAULT_RADIUS;
	private Double radiusVelocity = DEFAULT_RADIUS_VELOCITY;
	private int color;

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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Circle(Integer x, Integer y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public void update(ArrayList<Circle> circleArrayList) {

		for(Circle circle : circleArrayList){
			if(this != circle && this.intersects(circle)){
				radiusVelocity = 0.0;
			}
		}

		radius += radiusVelocity;

		/*
		Widzę, że ciekawość zżera. Czekam na wiadomość na messengerze z pytaniami o kod inne bzdety np czemu mam ukryte wszystkie repo.
		* */
	}

	public boolean intersects(Circle circle){
		double dx = this.getX() - circle.getX();
		double dy = this.getY() - circle.getY();

		double dis = Math.sqrt(dx * dx + dy * dy);

		return ( dis <= this.getRadius() + circle.getRadius() + 1);
	}

	@Override
	public String toString() {
		return "Circle{" +
				"x=" + x +
				", y=" + y +
				", radius=" + radius +
				", radiusVelocity=" + radiusVelocity +
				", color=" + color +
				'}';
	}
}
