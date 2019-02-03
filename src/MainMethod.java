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
        List<CardPlayer> players = new ArrayList<CardPlayer>();
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
    
    private static CardPlayer doBattle(List<CardPlayer> players, Attribute attribute) {
    	List<CardPlayer> drawnPlayers = new ArrayList<>();
        CardPlayer winningPlayer = null;
    	int maxValue = 0;
    	
    	for(CardPlayer player : players) {
    		if (player.hasLost()) {
    			continue;
    		}
    		Card card = player.getFirstCard();
    		System.out.println("{" + player.getDeck().size() + "} " + player.getName() + " " + card);
    		int attributeValue = card.getAttributeValue(attribute);
    		if (attributeValue > maxValue) {
    			winningPlayer = player;
    			maxValue = attributeValue;
    			drawnPlayers.clear();
    		} else if (attributeValue == maxValue) {
    			drawnPlayers.add(player);
    		}
    	}
    	
    	if (drawnPlayers.size() == 0) {
    		System.out.println("Winning player is: " + winningPlayer.getName());
    		winningPlayer.addRoundWin();
    		
    	} else {
    		System.out.println("DRAW: " + winningPlayer.getName());
    		winningPlayer.addDrawToStats();
    		winningPlayer = null;
    		for (CardPlayer player : drawnPlayers) {
    			System.out.println("DRAW: " + player.getName());
    			player.addDrawToStats();
    		}
    	}
		return winningPlayer;
    	
    }
    
    
    
    public static void giveCardsToPlayers(List<CardPlayer> players, List<Card> cards, int numPlayers) {

    	for (int i = 0; i < numPlayers; i++) {
    		if (i == 0) {
    			players.add(new CardPlayer("Player"));
    		} else {
    			players.add(new CardPlayer("AI-" + i));
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
    
    private static void startGame(List<CardPlayer> players) {
    	CardPlayer activePlayer = players.get(new Random().nextInt(players.size()));
    	List<Card> cardsAfterDraw = new ArrayList<Card>();
    	CardPlayer winningPlayer = null;
    	while (true) {
    		System.out.println("Trumping player is: " + activePlayer.getName());
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
        	}try{
                 Thread.sleep(12);
             }
             catch(InterruptedException ex){
                 Thread.currentThread().interrupt();
             }
    	}
    	
    	
    }
   
    
    private static void checkIfGameHasEnded(List<CardPlayer> players) {
    	int activePlayers = 0;
    	CardPlayer lastActivePlayer = null;
    	for (CardPlayer player : players) {
    		if (!player.hasLost()) {
    			activePlayers++;
    			lastActivePlayer = player;
    		}
    	}
    	
    	if (activePlayers == 1) {
    		System.out.println("THE GAME HAS ENDED!");
    		System.out.println("The winner is: " + lastActivePlayer.getName());
    		writePlayerStatsToFile(players);
    		System.exit(0);
    	}
    }
    
    private static void sortOutCardsAfterBattle(List<CardPlayer> players, CardPlayer winningPlayer, List<Card> cardsAfterDraw) {
    	if (winningPlayer == null) {
    		for (CardPlayer player : players) {
    			if (player.hasLost()) {
    				continue;
    			}
    			cardsAfterDraw.add(player.getFirstCard());
    			player.removeFirstCard();
    		}
    	} else {
    		winningPlayer.getDeck().addAll(cardsAfterDraw);
    		cardsAfterDraw.clear();
    		for (CardPlayer player : players) {
    			if (player.hasLost()) {
    				continue;
    			}
    			winningPlayer.addCard(player.getFirstCard());
    			player.removeFirstCard();
    		}
    	}
    	for (CardPlayer player : players) {
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
    
    private static void writePlayerStatsToFile(List<CardPlayer> players) {
    	for (CardPlayer player : players) {
    		System.out.println(player.getName() + ": W-" + player.getNumOfRoundsWon() + " D-" + player.getDraws());
    	}
    }
    

} 