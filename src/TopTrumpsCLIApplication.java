
import java.util.Scanner;
import java.io.BufferedReader;
import java.util.logging.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.sql.*;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {
     
	
	static Scanner input = new Scanner (System.in);
	
	private static final String PLAYER = "Player";
	
	public static void main(String[] args) throws SecurityException, IOException {
		Logger log = Logger.getLogger("testlog");
		FileHandler fh=new FileHandler("toptrumps.log");
		testLog(log,fh) ;

		int numPlayers = 5;
        List<Card> cards = getDeckFromFile();
        writeContentsofCard(cards,log);
        Collections.shuffle(cards);
        List<CardPlayer> players = new ArrayList<CardPlayer>();
       
		
		TopTrumpsCLIApplication topTrumps = new TopTrumpsCLIApplication();
		
		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		//if (args[0].equalsIgnoreCase("true")) writeGameLogsToFile=true; // Command line selection
		
		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application
		
		// Loop until the user wants to exit the game
		while(!userWantsToQuit) {
	        System.out.println("\nDo you want to see past results or play a game?\n"
	                + "\t1: Print Game Statistics\n\t2: Play game\n"
	                + "Enter the number for your selection"
	                + "\nor type 'quit' to exit application:");
	        String choice = input.next();
	        if(choice.equals("1")) {
	            try {
	            topTrumps.printDatabaseStats();
	            }catch(NumberFormatException e) {
	                System.out.println("You need to complete a game first!");
	            }
	        }
	        else if(choice.equals("2")) {
	        	topTrumps.giveCardsToPlayers(players, cards, numPlayers, log);
	            topTrumps.startGame(players, numPlayers, log);
	           
	        }
	        else if(choice.equals("quit")) {
	            System.out.println("You exited the game");
	            userWantsToQuit = true;
	        }
	        }
	        input.close();
	        System.exit(0);
	        fh.close();
	}
			// ----------------------------------------------------
			// import java.io.BufferedReader
	            
	          
	             public static void testLog(Logger log,FileHandler fh) throws SecurityException, IOException {	    	   
 	                 //log.setLevel(Level.ALL);	    	   
                     fh.setLevel(Level.ALL);
                     //log.addHandler(fh);	          
                 }

                 private static void writeContentsofCard(List<Card> cards,Logger log) {
 	                 //log.info("Contents of constructed cards: ");
 	                 for(Card card:cards) {
 		                 //log.info(card.toString());
 	                 }    	   
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
	            
	            
	            
	            private static CardPlayer doBattle(List<CardPlayer> players, Attribute attribute, Logger log) {
	            	List<CardPlayer> drawnPlayers = new ArrayList<>();
	                CardPlayer winningPlayer = null;
	            	int maxValue = 0;
	            	
	            	for(CardPlayer player : players) {
	            		if (player.hasLost()) {
	            			continue;
	            		}
	            		Card card = player.getFirstCard();
	            		System.out.println("Player deck size:{" + player.getDeck().size() + "} " + player.getName() + " " + card);
	            		log.info(player.getName()+": "+card.toString());  //current card
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
	            
	            
	            
	            public void giveCardsToPlayers(List<CardPlayer> players, List<Card> cards, int numPlayers, Logger log) {
	           
	            	
	            	 System.out.println("----------------------------------------------"
	            	  +	"\n----------------------------------------------"
	            	  + "\n            | Top Trumps Game |"
	            	  + "\n----------------------------------------------");
	            	
	            	 
	            	 
	            	for (int i = 0; i < numPlayers; i++) {
	            		if (i == 0) {
	            			players.add(new CardPlayer("Player"));
	            		} else {
	            			players.add(new CardPlayer("AI-" + i));
	            		}
	                	for (int j = i * (cards.size()/numPlayers); j < (i+1) * (cards.size()/numPlayers); j++) {
	                		players.get(i).addCard(cards.get(j));
	                		
	                		log.info("Contents of shuffled cards: ");
	                		log.info(players.get(i).getName()+": "+cards.get(j).toString()); //contents of each player's card after shuffled
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
	            
	            private void startGame(List<CardPlayer> players, int numPlayers, Logger log) {
	            	CardPlayer activePlayer = players.get(new Random().nextInt(numPlayers));
	            	List<Card> cardsAfterDraw = new ArrayList<Card>();
	            	CardPlayer winningPlayer = null;
	            	int currentRound = 1;
	            	int draws = 0;
	            	System.out.println("----------------------------------------------");
	            	while (true) {
	            		System.out.println("Round: " + currentRound);
	            		log.info("----------------------------------------------"); //each round is recorded separately in the log
	            		System.out.println("Trumping player is: " + activePlayer.getName());
	                	if (PLAYER.equals(activePlayer.getName())) {
	                		System.out.println("Your card is: " + activePlayer.getFirstCard());
	                		System.out.println("It's your turn to choose an attribute:\n"
									+ "\t1. Size\n"
									+ "\t2. Speed\n"
									+ "\t3. Range\n"
									+ "\t4. Firepower\n"
									+ "\t5. Cargo\n"
									+ "Enter the number of the chosen attribute: ");
	                		Attribute attribute = getAttributeFromPlayer(log);
	                		winningPlayer = doBattle(players, attribute,log);
	                	} else {
	                		System.out.println("Their card is: " + activePlayer.getFirstCard());
	                		System.out.println("Their attribute choice is: " + activePlayer.getFirstCard().getHighestAttribute());
	                		winningPlayer = doBattle(players, activePlayer.getFirstCard().getHighestAttribute(),log);
	                	}
	                	sortOutCardsAfterBattle(players, winningPlayer, cardsAfterDraw,log);
	                	System.out.println("Number of cards in the pile: {" + cardsAfterDraw.size() + "}");
	                	log.info("contents of communal pile: ");
	                	for(Card card:cardsAfterDraw) {   //show contents of communal pile
	                		log.info(card.toString());
	                	}
	                	System.out.println("----------------------------------------------");
	                	if (winningPlayer != null) {
	                		activePlayer = winningPlayer;
	                	} else {
	                		draws++;
	                	}
	                	checkIfGameHasEnded(players, currentRound++, draws, log);
	                	//make the thread sleep for 2 seconds between executions
	                	//so the human player can follow the flow of events
	                	try {
	                         Thread.sleep(2);
	                     } catch(InterruptedException ex){
	                         Thread.currentThread().interrupt();
	                     }
	            	}
	            	
	            	
	            }
	           
	            
	            private static void checkIfGameHasEnded(List<CardPlayer> players, int currentRound, int draws, Logger log) {
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
	            		log.info("Winner: "+lastActivePlayer.getName());   //log of winner 
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
	                
	                
	                
	                
	                
	                
//	                String url = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/m_18_2416192l";
//	                String user = "m_18_2416192l";
//	                String password = "2416192l";
//	                
//	                try (Connection connection = DriverManager.getConnection(url, user, password)){
//	                    
//	                	Class.forName("org.postgresql.Driver");
//	                	
//	                    System.out.println("! Database Connection Established !");
//	                    
//	                    Statement statement = connection.createStatement();
//	                    System.out.println("Executing query:");
//	                    
//	                    
//	                    /** Query for Updating values in the table **/
//	                    PreparedStatement st = connection.prepareStatement("INSERT INTO public.game_info (n_rounds, n_draws, winner, player_rounds_won, ai1_rounds_won, ai2_rounds_won, ai3_rounds_won, ai4_rounds_won) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//	                    st.setInt(1, currentRound);
//	                    st.setInt(2, draws);
//	                    st.setString(3, winningPlayer.getName());
//	                    st.setInt(4, players.get(0).getNumOfRoundsWon());
//	                    st.setInt(5, players.get(1).getNumOfRoundsWon());
//	                    st.setInt(6, players.get(2).getNumOfRoundsWon());
//	                    st.setInt(7, players.get(3).getNumOfRoundsWon());
//	                    st.setInt(8, players.get(4).getNumOfRoundsWon());
//	                    st.executeUpdate();
//	                    
//	                    
//	                    /** Query for Selecting data from the table and printing it **/
//	                    String query = "SELECT * FROM public.game_info ORDER BY game_id DESC LIMIT 1";
//	                    ResultSet resultSet = statement.executeQuery(query);
//	                    
//	                    String game_id = null;
//	                    String n_rounds = null;
//	                    String n_draws = null;
//	                    String winner = null;
//	                    String player_wins = null;
//	                    String ai1_wins = null;
//	                    String ai2_wins = null;
//	                    String ai3_wins = null;
//	                    String ai4_wins = null;
//	                    
//	                    while (resultSet.next()) {
//	                    	game_id = resultSet.getString("game_id");
//	                        n_rounds = resultSet.getString("n_rounds");
//	                        n_draws = resultSet.getString("n_draws");
//	                        winner = resultSet.getString("winner");
//	                        player_wins = resultSet.getString("player_rounds_won");
//	                        ai1_wins = resultSet.getString("ai1_rounds_won");
//	                        ai2_wins = resultSet.getString("ai2_rounds_won");
//	                        ai3_wins = resultSet.getString("ai3_rounds_won");
//	                        ai4_wins = resultSet.getString("ai4_rounds_won");
//	                    	
//	                    	System.out.println("Game ID: " + game_id);
//	                    	System.out.println("Winner: "+ winner);
//	                    	System.out.println("Number of rounds: " + n_rounds);
//	                    	System.out.println("-------------------");
//	                    }
//	                    
//	                    System.out.println("Finished fetching the data.");
//	                    
//	                } catch (SQLException e) {
//	                	System.out.println("Connection Failure!");
//	                	e.printStackTrace();
//	                } catch (ClassNotFoundException e) {
//	                	System.out.println("PostgreSQL JDBC driver not found!");
//	                	e.printStackTrace();
//	                }
	            }
	            
	            private void printDatabaseStats(){
	            	String url = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/m_18_2416192l";
	                String user = "m_18_2416192l";
	                String password = "2416192l";
	                
	                try (Connection connection = DriverManager.getConnection(url, user, password)){
	                    
	                	Class.forName("org.postgresql.Driver");	                    
	                    Statement statement = connection.createStatement();
	                    
	                    //Query 1: Number of total games.
	                    String totalGamesQuery = "SELECT COUNT(*) FROM public.game_info";
	                    ResultSet resultSet = statement.executeQuery(totalGamesQuery);
	                    int totalGames = resultSet.getInt(0);
	                    System.out.println("Total games played so far: " + Integer.toString(totalGames));
	                    
	                    //Query 2: Human vs AI wins.
	                    String humanWinsQuery = "SELECT COUNT(*) FROM public.game_info WHERE winner=Player";
	                    resultSet = statement.executeQuery(humanWinsQuery);
	                    int humanWins = resultSet.getInt(0);
	                    int AIWins = totalGames - humanWins;
	                    System.out.println("Human wins: " + Integer.toString(humanWins));
	                    System.out.println("AI wins: " + Integer.toString(AIWins));
	                    
	                    //Query 3: Average number of draws.
	                    String drawnGamesQuery = "SELECT SUM(n_draws) FROM public.game_info";
	                    resultSet = statement.executeQuery(drawnGamesQuery);
	                    double totalDraws = resultSet.getInt(0);
	                    double averageDraws = totalDraws / (double) totalGames ;
	                    System.out.println("Average draws per game: " + Double.toString(averageDraws));
	                    
	                    //Query 4: Longest game played so far.
	                    String longestGameQuery = "SELECT MAX(n_rounds) FROM public.game_info";
	                    resultSet = statement.executeQuery(longestGameQuery);
	                    int longestGame = resultSet.getInt(0);
	                    System.out.println("Most rounds played in a game: " + Integer.toString(longestGame));
	                    
	                } catch (SQLException e) {
	                	System.out.println("Connection Failure!");
	                	e.printStackTrace();
	                } catch (ClassNotFoundException e) {
	                	System.out.println("PostgreSQL JDBC driver not found!");
	                	e.printStackTrace();
	                }
	            }

	            private static void sortOutCardsAfterBattle(List<CardPlayer> players, CardPlayer winningPlayer, List<Card> cardsAfterDraw, Logger log) {
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
	            	log.info("contents of decks after allocated: ");  //log
	            	List<Card> cards=null;
	            	for(CardPlayer player : players) {
	            		cards=player.getDeck();
	            		for(Card card:cards) {
	            			log.info(player.getName()+": "+card.toString());
	            		}
	            	}
	            }
	            
	            
	            
	            private static Attribute getAttributeFromPlayer(Logger log) {
	            	
	            	while (true) {
	            		Scanner reader = new Scanner(System.in);
	                	String input = reader.nextLine();
	            		try {
	                		Attribute attribute = Attribute.getValue(input);
	                		System.out.println("You chose: " + attribute.toString());
	                		log.info("selected category and vaule: "+attribute.toString());
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

/*				// ----------------------------------------------------
				
				userWantsToQuit=true; // use this when the user wants to exit the game
				
			}
	
	
		}
*/