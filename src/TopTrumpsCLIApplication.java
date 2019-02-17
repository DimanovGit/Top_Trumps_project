import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.sql.*;

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
    	int currentRound = 1;
    	int draws = 0;
    	System.out.println("----------------------------------------------");
    	while (true) {
    		System.out.println("Round: " + currentRound);
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
        	if (winningPlayer != null) {
        		activePlayer = winningPlayer;
        	} else {
        		draws++;
        	}
        	checkIfGameHasEnded(players, currentRound++, draws);
        	try {
                 Thread.sleep(12);
             } catch(InterruptedException ex){
                 Thread.currentThread().interrupt();
             }
    	}
    	
    	
    }
   
    
    private static void checkIfGameHasEnded(List<CardPlayer> players, int currentRound, int draws) {
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
    		saveToDatabase(players, lastActivePlayer, currentRound, draws);
    		saveToFile(players, lastActivePlayer, currentRound, draws);
    		System.exit(0);
    	}
    }
    
    
    private static void saveToDatabase(List<CardPlayer> players, CardPlayer winningPlayer, int currentRound, int draws) {
        
    	System.out.println("----------------------------------------------");
        System.out.println("INFO FOR DB");
        System.out.println("Player that won: " + winningPlayer.getName());
        System.out.println("Rounds played: " + currentRound);
        System.out.println("Number of draws: " + draws);
        for (CardPlayer player : players) {
    		System.out.println(player.getName() + ": W-" + player.getNumOfRoundsWon() + " D-" + player.getDraws());
    	}
        
        
        
        
        
        
//        String url = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/m_18_2416192l";
//        String user = "m_18_2416192l";
//        String password = "2416192l";
//        
//        try (Connection connection = DriverManager.getConnection(url, user, password)){
//            
//        	Class.forName("org.postgresql.Driver");
//        	
//            System.out.println("! Database Connection Established !");
//            
//            Statement statement = connection.createStatement();
//            System.out.println("Executing query:");
//            
//            
//            /** Query for Updating values in the table **/
//            PreparedStatement st = connection.prepareStatement("INSERT INTO public.game_info (n_rounds, n_draws, winner, player_rounds_won, ai1_rounds_won, ai2_rounds_won, ai3_rounds_won, ai4_rounds_won) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//            st.setInt(1, currentRound);
//            st.setInt(2, draws);
//            st.setString(3, winningPlayer.getName());
//            st.setInt(4, players.get(0).getNumOfRoundsWon());
//            st.setInt(5, players.get(1).getNumOfRoundsWon());
//            st.setInt(6, players.get(2).getNumOfRoundsWon());
//            st.setInt(7, players.get(3).getNumOfRoundsWon());
//            st.setInt(8, players.get(4).getNumOfRoundsWon());
//            st.executeUpdate();
//            
//            
//            /** Query for Selecting data from the table and printing it **/
//            String query = "SELECT * FROM public.game_info ORDER BY game_id DESC LIMIT 1";
//            ResultSet resultSet = statement.executeQuery(query);
//            
//            String game_id = null;
//            String n_rounds = null;
//            String n_draws = null;
//            String winner = null;
//            String player_wins = null;
//            String ai1_wins = null;
//            String ai2_wins = null;
//            String ai3_wins = null;
//            String ai4_wins = null;
//            
//            while (resultSet.next()) {
//            	game_id = resultSet.getString("game_id");
//                n_rounds = resultSet.getString("n_rounds");
//                n_draws = resultSet.getString("n_draws");
//                winner = resultSet.getString("winner");
//                player_wins = resultSet.getString("player_rounds_won");
//                ai1_wins = resultSet.getString("ai1_rounds_won");
//                ai2_wins = resultSet.getString("ai2_rounds_won");
//                ai3_wins = resultSet.getString("ai3_rounds_won");
//                ai4_wins = resultSet.getString("ai4_rounds_won");
//            	
//            	System.out.println("Game ID: " + game_id);
//            	System.out.println("Winner: "+ winner);
//            	System.out.println("Number of rounds: " + n_rounds);
//            	System.out.println("-------------------");
//            }
//            
//            System.out.println("Finished fetching the data.");
//            
//        } catch (SQLException e) {
//        	System.out.println("Connection Failure!");
//        	e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//        	System.out.println("PostgreSQL JDBC driver not found!");
//        	e.printStackTrace();
//        }
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
        		Attribute attribute = Attribute.getValue(input);
        		System.out.println("You chose: " + attribute.toString());
        		return attribute;
        	} catch (Exception e) {
        		System.out.println("That's an invalid attribute, try again!");
        	}
    	}
    }
    
    private static void saveToFile(List<CardPlayer> players, CardPlayer winningPlayer, int currentRound, int draws) {
    	System.out.println("----------------------------------------------");
        System.out.println("INFO FOR FILE");
        System.out.println("Player that won: " + winningPlayer.getName());
        System.out.println("Rounds played: " + currentRound);
        System.out.println("Number of draws: " + draws);
        for (CardPlayer player : players) {
    		System.out.println(player.getName() + ": W-" + player.getNumOfRoundsWon() + " D-" + player.getDraws());
    	}
    }
    

} 
