
public enum Attribute {
	
	SIZE(1),
    SPEED(2),
    RANGE(3),
    FIREPOWER(4),
    CARGO(5);
	
	private final int order;
	
	Attribute(int order) {
		this.order = order;
	}
	
	public int getOrder() {
		return order;
	}
	
	public static Attribute getValue(String attribute) {
		try {
			int order = Integer.parseInt(attribute);
			return valueOf(order);
		} catch (NumberFormatException nfe) {
			return valueOf(attribute);
		}
	}
	
	public static Attribute valueOf(int order) {
		if (order == 1) {
			return SIZE;
		} else if (order == 2) {
			return SPEED;
		} else if (order == 3) {
			return RANGE;
		} else if (order == 4) {
			return FIREPOWER;
		} else if (order == 5) {
			return CARGO;
		} else {
			throw new IllegalArgumentException("Invalid value: " + order);
		}
	}
	
}
