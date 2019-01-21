import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
	ArrayList<Card> hand = new ArrayList<Card>();
	String name;
	
	public ArrayList<Card> clearHand() {
		ArrayList<Card> old_hand = new ArrayList<Card>();
		old_hand.addAll(this.hand);
		this.hand.clear();
				
		return old_hand;
	}
	
	// other possible functions draw card, discard card
	
	public void addCard( Card c ) {		
		this.hand.add(c);	
	}
	
	// removes card from hand according to position counting up from 1;
	public Card discardCard(int card_num) {
		Card c;
		
		c = this.hand.remove(card_num - 1);
		
		return c;
	}

 	public static void main( String[] args ) {
 		Player p1 = new Player();
 		Deck d = new Deck(1);
 		Card c;
 		Scanner kb = new Scanner(System.in);
 		int discard;
 		
 		d.shuffle();
 	
 		// Testing drawCard()
 		for (int i = 0; i < 5; i++ ) {
 			System.out.println( "Start of loop Player hand: " + p1.hand );
 			c = d.dealCard();
 			p1.addCard(c);		
 			System.out.println(c);
 			System.out.println( "End of loop Player hand: " + p1.hand );
 		}
 		// Testing discardCard()
 		System.out.println ( "Discard from your hand \n" + p1.hand + "\n[1   2   3   4   5 ]" ); 
 		discard = kb.nextInt();
 		
 		c = p1.discardCard(discard);
 		System.out.println(c);
 		System.out.println( "Player hand after discard: " + p1.hand );
 		d.returnCard(c);
 		
 		
 // Testing clearHand
 		ArrayList<Card> return_to_deck = new ArrayList<Card>();
 		return_to_deck = p1.clearHand();
 		System.out.println ( "Calling .clearHand()" );
 		System.out.println ( "Player hand: " + p1.hand );
 		System.out.println ( "Cards to return: " + return_to_deck );
 		
 // Testing returning card to deck
 		System.out.println( "Deck\n" + d.cards + "\nwith " + ((ArrayList<Card>) d.cards).size() );
 		while ( return_to_deck.size() > 0 ) {
 			c = return_to_deck.remove(0);
 			((ArrayList<Card>) d.cards).add(c);
 		}		
 		System.out.println( "Remaining cards from hand: " + return_to_deck );
 		System.out.println( "Deck\n" + d.cards + "\nwith " + ((ArrayList<Card>) d.cards).size() );				
 	}
 	
}