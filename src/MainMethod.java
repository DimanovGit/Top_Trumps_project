import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainMethod {
	
	private static final String PLAYER = "Player";
	
    public static void main(String[] args) {
        int numPlayers = 5;
        List<Card> cards = getDeckFromFile();
        Collections.shuffle(cards);
        List<CardPlayer1> players = new ArrayList<CardPlayer1>();
        giveCardsToPlayers(players, cards, numPlayers);
        startGame(players);
    }
    
    
    
    
    
    private static List<Card> getDeckFromFile() {
    	List<Card> cards = new ArrayList<Card>();
    	try (BufferedReader br = new BufferedReader(new FileReader("StarCitizenDeck.txt"))) {
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
    	return cards;
    }
    
    private static CardPlayer1 doBattle(List<CardPlayer1> players, Attribute attribute) {
        CardPlayer1 winningPlayer = null;
    	int maxValue = 0;
    	
    	for(CardPlayer1 player : players) {
    		if (player.hasLost()) {
    			continue;
    		}
    		Card card = player.getFirstCard();
    		System.out.println("{" + player.getDeck().size() + "} " + player.getName() + " " + card);
    		int attributeValue = card.getAttributeValue(attribute);
    		if (attributeValue > maxValue) {
    			winningPlayer = player;
    			maxValue = attributeValue;
    		} else if (attributeValue == maxValue) {
    			winningPlayer = null;
    		}
    	}
    	if (winningPlayer != null) {
    		System.out.println("Winning player is: " + winningPlayer.getName());
    	} else {
    		System.out.println("DRAW!");
    	}
    	return winningPlayer;
    }
    
    
    private static void giveCardsToPlayers(List<CardPlayer1> players, List<Card> cards, int numPlayers) {
    	for (int i = 0; i < numPlayers; i++) {
    		if (i == 0) {
    			players.add(new CardPlayer1("Player"));
    		} else {
    			players.add(new CardPlayer1("AI-" + i));
    		}
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
    
    private static void startGame(List<CardPlayer1> players) {
    	CardPlayer1 activePlayer = players.get(new Random().nextInt(players.size()));
    	List<Card> cardsAfterDraw = new ArrayList<Card>();
    	CardPlayer1 winningPlayer = null;
    	while (true) {
    		System.out.println("Active player is: " + activePlayer.getName());
        	if (PLAYER.equals(activePlayer.getName())) {
        		System.out.println("Your card is: " + activePlayer.getFirstCard());
        		System.out.println("Choose attribute: ");
        		Attribute attribute = getAttributeFromPlayer();
        		winningPlayer = doBattle(players, attribute);
        	} else {
        		System.out.println("Their card is: " + activePlayer.getFirstCard());
        		System.out.println("Their attribute choice is: " + activePlayer.getFirstCard().getHighestAttribute());
        		winningPlayer = doBattle(players, activePlayer.getFirstCard().getHighestAttribute());
        	}
        	sortOutCardsAfterBattle(players, winningPlayer, cardsAfterDraw);
        	System.out.println("Number of cards in the pile: {" + cardsAfterDraw.size() + "}");
        	System.out.println("----------------------------------------------");
        	checkIfGameHasEnded(players);
        	if (winningPlayer != null) {
        		activePlayer = winningPlayer;
        	}
    	}
    }
    
    private static void checkIfGameHasEnded(List<CardPlayer1> players) {
    	int activePlayers = 0;
    	CardPlayer1 lastActivePlayer = null;
    	for (CardPlayer1 player : players) {
    		if (!player.hasLost()) {
    			activePlayers++;
    			lastActivePlayer = player;
    		}
    	}
    	
    	if (activePlayers == 1) {
    		System.out.println("THE GAME HAS ENDED!");
    		System.out.println("The winner is: " + lastActivePlayer.getName());
    		System.exit(0);
    	}
    }
    
    private static void sortOutCardsAfterBattle(List<CardPlayer1> players, CardPlayer1 winningPlayer, List<Card> cardsAfterDraw) {
    	if (winningPlayer == null) {
    		for (CardPlayer1 player : players) {
    			if (player.hasLost()) {
    				continue;
    			}
    			cardsAfterDraw.add(player.getFirstCard());
    			player.removeFirstCard();
    		}
    	} else {
    		winningPlayer.getDeck().addAll(cardsAfterDraw);
    		cardsAfterDraw.clear();
    		for (CardPlayer1 player : players) {
    			if (player.hasLost()) {
    				continue;
    			}
    			winningPlayer.addCard(player.getFirstCard());
    			player.removeFirstCard();
    		}
    	}
    	for (CardPlayer1 player : players) {
			if (player.getDeck().isEmpty()) {
				player.hasLost(true);
			}
		}
    }
    
    private static Attribute getAttributeFromPlayer() {
    	
    	while (true) {
    		Scanner reader = new Scanner(System.in);
        	String input = reader.nextLine();
    		try {
        		Attribute attribute = Attribute.valueOf(input);
        		return attribute;
        	} catch (Exception e) {
        		System.out.println("That's an invalid attribute, try again!");
        	}
    	}
    }

} 