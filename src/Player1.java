import java.util.ArrayList;
import java.util.List;

public class Player1 {
	
	private List<Card> deck = new ArrayList<Card>();
	private String name;
	
	public Player1(String name) {
		this.name = name;
	}

	public void addCard(Card card) {
		deck.add(card);
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	public String getName() {
		return name;
	}
	
}