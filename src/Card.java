import java.util.HashMap;
import java.util.Map;

public class Card {
	
	private String description;
	
	private Map<Attribute, Integer> attributes;
	
	public Card(String description, int size, int speed, int range, int firepower, int cargo) {
		this.attributes = new HashMap<Attribute, Integer>();
		this.description = description;
		attributes.put(Attribute.SIZE, size);
		attributes.put(Attribute.SPEED, speed);
		attributes.put(Attribute.RANGE, range);
		attributes.put(Attribute.FIREPOWER, firepower);
		attributes.put(Attribute.CARGO, cargo);
	}
	
	public Integer getSize() {
		return attributes.get(Attribute.SIZE);
	}
	
	public Integer getSpeed() {
		return attributes.get(Attribute.SPEED);
	}
	
	public Integer getRange() {
		return attributes.get(Attribute.RANGE);
	}
	
	public Integer getFirepower() {
		return attributes.get(Attribute.FIREPOWER);
	}
	
	public Integer getCargo() {
		return attributes.get(Attribute.CARGO);
	}
	
	public Integer getAttributeValue(Attribute attribute) {
		return attributes.get(attribute);
	}
	
	public Attribute getHighestAttribute() {
		int highestAttributeValue = 0;
		Attribute highestAttribute = null;
		for (Attribute attribute : Attribute.values()) {
			if (this.getAttributeValue(attribute) > highestAttributeValue) {
				highestAttributeValue = this.getAttributeValue(attribute);
				highestAttribute = attribute;
			}
		}
		return highestAttribute;
	}
	
	public String toString() {
		return "Card name: " + description + " Size: " + getSize() 
		                     + " Speed: " + getSpeed() 
		                     + " Range: " + getRange()
		                     + " Firepower: " + getFirepower()
		                     + " Cargo: " + getCargo();
	}
	
}    
