import java.util.ArrayList;
import java.util.List;

public class CardPlayer1 {
	
	private List<Card> deck = new ArrayList<Card>();
	private String name;
	private boolean hasLost;
	
	public CardPlayer1(String name) {
		this.name = name;
		this.hasLost = false;
	}

	public void addCard(Card card) {
		deck.add(card);
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	public Card getFirstCard() {
		return deck.get(0);
	}
	
	public void removeFirstCard() {
		deck.remove(0);
	}
	
	public boolean hasLost() {
		return hasLost;
	}
	
	public void hasLost(boolean hasLost) {
		this.hasLost = hasLost;
	}
	
	public String getName() {
		return name;
	}
	
}