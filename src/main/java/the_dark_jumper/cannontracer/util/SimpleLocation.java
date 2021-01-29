package the_dark_jumper.cannontracer.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleLocation {
	@JsonProperty("x")
	public double x;

	@JsonProperty("y")
	public double y;

	@JsonProperty("z")
	public double z;

	public void init(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public SimpleLocation() {
		init(0, 0, 0);
	}
	public SimpleLocation(double x, double y, double z) {
		init(x, y, z);
	}
	
	public static SimpleLocation add(SimpleLocation a, SimpleLocation b) {
		return new SimpleLocation(a.x + b.x , a.y + b.y , a.z + b.z);
	}
	
	public void add(SimpleLocation a) {
		this.x += a.x;
		this.y += a.y;
		this.z += a.z;
	}
	
	public boolean equals(SimpleLocation a) {
		return (this.x == a.x && this.y == a.y && this.z == a.z);
	}
	
	public String getString() {
		return ""+x+","+y+","+z;
	}
	
	public SimpleLocation copy() {
		return new SimpleLocation(x, y, z);
	}
}
