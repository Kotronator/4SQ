package dsassigment;

import java.io.Serializable;

public class Point implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5497841316127816764L;
	
	public double x,y;
	
	public Point(double x, double y)
	{
		this.x=x;
		this.y=y;
	}

	public Point() {
		this(0,0);
	}

}
