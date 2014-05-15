package com.crossoutxtrem;

public class Combination
{
	private Circle circle1;
	private Circle circle2;
	
	public Combination(Circle circle1, Circle circle2)
	{
		this.circle1 = circle1;
		this.circle2 = circle2;
	}
	
	public Circle getCircle1()
	{
		return circle1;
	}
	
	public Circle getCircle2()
	{
		return circle2;
	}
	
	public boolean equals(Combination c)
	{
		return (circle1.equals(c.circle1)&&circle2.equals(c.circle2))||(circle2.equals(c.circle1)&&circle1.equals(c.circle2));
	}
}
