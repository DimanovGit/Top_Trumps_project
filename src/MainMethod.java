import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMethod {
    public static void main(String[] args) {
        // list of cards per user
        List<Card> cards = new ArrayList<>();
        try (BufferedReader br =
                //directory is hard coded; change your directory accordingly; we'll fix that once we implement the GUI.
        		new BufferedReader(new FileReader("StarCitizenDeck.txt"))) {
        	String line=br.readLine();
        	while ((line = br.readLine()) != null) {
        		String[] splitLine = line.split(" ");
        		cards.add(new Card(splitLine[0],
        				Integer.parseInt(splitLine[1]),
        				Integer.parseInt(splitLine[2]),
        				Integer.parseInt(splitLine[3]),
        				Integer.parseInt(splitLine[4]),
        				Integer.parseInt(splitLine[5]))
        		);
        	}
        	br.readLine();
        } catch (IOException e) {
			
			e.printStackTrace();
		}
        
        // some game mechanisms below
     
        int numPlayers = 2;
        Collections.shuffle(cards);
        List<CardPlayer1> players = new ArrayList<CardPlayer1>();
        
        giveCardsToPlayers(players, cards, numPlayers);
        
        CardPlayer1 playerOne  = players.get(0);
        CardPlayer1 playerTwo = players.get(1);
        System.out.println("First to play: " + playerOne.getName());
        System.out.println(playerOne.getName() + " summons: " + playerOne.getDeck().get(0));
        System.out.println("Second to play: " + playerTwo.getName());
        System.out.println(playerTwo.getName() + " summons: " + playerTwo.getDeck().get(0));
        Attribute att = Attribute.valueOf(Attribute.class, "CARGO");
        System.out.println("CARGO is the type of battle");
        Card card = doBattle(playerOne.getDeck().get(0), playerTwo.getDeck().get(0), att);
        System.out.println(card);
        // Profi
        
    }
    
    public static Card doBattle(Card card1, Card card2, Attribute attribute) {
    	System.out.println(card1);
    	System.out.println(card2);
    	System.out.println(attribute);
    	
    	if (card1.getAttributeValue(attribute) > card2.getAttributeValue(attribute)) {
    		System.out.println("Card 1 Wins");
    		return card1;
    	}
    	if (card2.getAttributeValue(attribute) > card1.getAttributeValue(attribute)) {
    		System.out.println("Card 2 Wins");
    		return card2;
    	}
    	System.out.println("DRAW");
    	return null;
    }
    
    
    public static void giveCardsToPlayers(List<CardPlayer1> players, List<Card> cards, int numPlayers) {
    	for (int i = 0; i < numPlayers; i++) {
        	players.add(new CardPlayer1("Jorkata" + i));
        	for (int j = i * (cards.size()/numPlayers); j < (i+1) * (cards.size()/numPlayers); j++) {
        		players.get(i).addCard(cards.get(j));
        	}
        }
    	
        int nextCard = (cards.size() / numPlayers) * numPlayers;
        
        int nextPlayer = 0;
        while (nextCard < cards.size()) {
        	players.get(nextPlayer).addCard(cards.get(nextCard));
        	nextPlayer++;
        	nextCard++;
        }
    	
    }

} 
