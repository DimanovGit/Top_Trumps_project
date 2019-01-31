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
            connectToDatabase(lastActivePlayer.getName());
            System.exit(0);
        }
    }
    
//    private static void connectToDatabase(String lastActivePlayer) {
//        
//        String url = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/m_18_2416192l";
//        String user = "m_18_2416192l";
//        String password = "2416192l";
//        
//        try (Connection connection = DriverManager.getConnection(url, user, password)){
//            
//        	Class.forName("org.postgresql.Driver");
//        	
//            System.out.println("!!! DB Connection established !!!");
//            
//            Statement statement = connection.createStatement();
//            System.out.println("Executing query:");
//            
//            
//            /** Query for Updating values in the table **/
//            PreparedStatement st = connection.prepareStatement("INSERT INTO public.game_info (game_id, n_rounds, n_draws, winner, player1_rounds_won, player2_rounds_won, player3_rounds_won, player4_rounds_won, player5_rounds_won) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
//            st.setInt(1, 9);
//            st.setInt(2, 52);
//            st.setInt(3, 4);
//            st.setString(4, lastActivePlayer);
//            st.setInt(5, 4);
//            st.setInt(6, 8);
//            st.setInt(7, 14);
//            st.setInt(8, 5);
//            st.setInt(9, 3);
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
//            String player1_wins = null;
//            String player2_wins = null;
//            String player3_wins = null;
//            String player4_wins = null;
//            String player5_wins = null;
//            
//            while (resultSet.next()) {
//            	game_id = resultSet.getString("game_id");
//                n_rounds = resultSet.getString("n_rounds");
//                n_draws = resultSet.getString("n_draws");
//                winner = resultSet.getString("winner");
//                player1_wins = resultSet.getString("player1_rounds_won");
//                player2_wins = resultSet.getString("player2_rounds_won");
//                player3_wins = resultSet.getString("player3_rounds_won");
//                player4_wins = resultSet.getString("player4_rounds_won");
//                player5_wins = resultSet.getString("player5_rounds_won");
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
//        	System.out.println("Connection Failure !!!");
//        	e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//        	System.out.println("PostgreSQL JDBC driver not found !!!");
//        	e.printStackTrace();
//        }
//    }
    

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
