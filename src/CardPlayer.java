import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CardPlayer {
	
	private List<Card> deck = new ArrayList<Card>();
	private String name;
	private boolean hasLost;
	private int numOfRoundsWon; 
	private int drawnGames;
	
	
	
	
	public CardPlayer(String name) {
		this.name = name;
		this.hasLost = false;
		this.drawnGames=0;
		this.numOfRoundsWon=0;
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

	

	

	public void addRoundWin() {
		numOfRoundsWon++;
			
	}
	
	public void addDrawToStats() {
		drawnGames++;
	}
	
	public int getNumOfRoundsWon() {
		return numOfRoundsWon;
	}
	
	public int getDraws() {
		return drawnGames;
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
	
	BufferedWriter writer;

	public void writeStats(String line)
	{
	    try
	    {
	        File file = new File("GameStats.txt");
	        file.createNewFile();

	        writer = new BufferedWriter(new FileWriter(file));

	        writer.write("Number of draws: " + drawnGames );
	        writer.newLine();
	        writer.write("Number of sentences: "   );
	        writer.newLine();
	        writer.write("Shortest sentence: " +  " words");
	        writer.newLine();
	        writer.write("Longest sentence: " +  " words");
	        writer.newLine();
	        writer.write("Average sentence: " +   " words");    
	    }
	    catch(FileNotFoundException e)
	    {
	        System.out.println("File Not Found");
	        System.exit( 1 );
	    }
	    catch(IOException e)
	    {
	        System.out.println("something messed up");
	        System.exit( 1 );
	    }
	}
}