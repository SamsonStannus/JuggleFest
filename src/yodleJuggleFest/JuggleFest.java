package yodleJuggleFest;

import java.util.* ;
import java.io.*;
import java.util.ArrayList;

/**
 * Using a Ford Fulkerson- type Method to find a perfect matching:
 * check Ji's top Circuit Cj, 
			if Cj isn't full 
				add Ji to Ci's Assignment
			else if Cj is full, 
				sort Cj, by Juggler Match Score with Cj, if Ji > Jn (Jn, being the least Match Scored Juggler)
					place Ji in Cj, and remove Jn
				else make Ji's top Ciruit, Cj+1 (his next top circuit)
			continue until no more free Jugglers are left.
			else 
				if Juggler can't be match with any of it's preferences, it goes to the first Circuit with room.
			
 * @author samsonstannus
 *
 */


public class JuggleFest {
	private static ArrayList<Circuit> circuitAssign = new ArrayList<Circuit>();
	public static ArrayList<Juggler> allJugglers = new ArrayList<Juggler>();
	private static Scanner scanner;
	
//	REAL (for jugglefest.txt)
	private static int MAX_SIZE = 6;
	private static int PREF_LENGTH = 10;
	private static int LAST = 5;
	private static int C_LENGTH = 2000;
	private static File input = new File("src/jugglefest.txt");
	
// 	TEST (for the test.txt)	
//	private static int MAX_SIZE = 4;
//	private static int PREF_LENGTH = 3;
//	private static int LAST = 3;
//	private static int C_LENGTH = 3;
//	private static File input = new File("src/test.txt");
	
	
	/**
	 * Reads from the input file, parses it and creates Jugglers and Circuits
	 * @param file
	 * @throws IOException
	 */
	public static void read(File file) throws IOException{
		scanner = new Scanner(file);
		int line = 0;
		while(scanner.hasNext()){
			String[] tokens = scanner.nextLine().split("[\\s:,]");
			if(line < C_LENGTH){
				ArrayList<Juggler> jugglers = new ArrayList<Juggler>();
				int h = Integer.parseInt(tokens[3]);
				int e = Integer.parseInt(tokens[5]);
				int p = Integer.parseInt(tokens[7]);
				int[] reqs = new int[] {h, e, p};;
				
				Circuit circuit = new Circuit(tokens[1], reqs, jugglers);
				circuitAssign.add(circuit);
			}
			if(line > C_LENGTH){
				int h = Integer.parseInt(tokens[3]);
				int e = Integer.parseInt(tokens[5]);
				int p = Integer.parseInt(tokens[7]);
				int[] skills = new int[] {h, e, p};
				
				String[] pref = new String[PREF_LENGTH];
				for(int i = 0; i<PREF_LENGTH; i++){
					pref[i] = tokens[8+i];
				}
				Juggler juggler = new Juggler(tokens[1], skills, pref);
				allJugglers.add(juggler);
			}
			line++;
		}
	}
	
	/**
	 * takes the dot product of the Circuit Requirements and Juggler's Skills
	 * @param Ciruit c
	 * @param Juggler j
	 * @return
	 */
	public static int match(Circuit c, Juggler j){
		int match = 0;
		for(int i = 0; i < 3; i++){
			match = match + (j.getSkills()[i] * c.getReqSkills()[i]);
		}
		
		return match;
		
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{
		
		
		// Read Input
		try{
			read(input);
			System.out.println("Input File Read");
		} catch ( IOException e ) {
			System.out.println(e);
		}
		
		
		// Juggler Index
		int jIndex = 0;
		// Juggler Preference Index
		int prefIndex = 0;
		
		while(!allJugglers.isEmpty()){
			Juggler currentJuggler = allJugglers.get(jIndex);
			Circuit currentCircuit;
			// Circuit Index
			int cIndex = 0;
			
			// If a Juggler can't go into a circuit find the first Circuit with room
			int findOpenIndex = 0;
			if(prefIndex == PREF_LENGTH){
				Circuit hasOpenSpots;
				while(true){
					if(circuitAssign.get(findOpenIndex).getSize() < 6){
						hasOpenSpots = circuitAssign.get(findOpenIndex);
						hasOpenSpots.getAssignment().add(currentJuggler);
						
						break;
					}
					findOpenIndex++;
				}
				allJugglers.remove(jIndex);
				prefIndex = 0;
				continue;
				
			}
			
			//Find the top preference at the preference Index
			while(true){
				if( currentJuggler.getPref()[prefIndex].equals( circuitAssign.get(cIndex).getName() ) ){
					currentCircuit = circuitAssign.get(cIndex);
					break;
				}
				cIndex++;
				
			}

			
			if(currentCircuit.getSize() < MAX_SIZE){
				currentCircuit.getAssignment().add(currentJuggler);
				allJugglers.remove(jIndex);
				prefIndex = 0;
				continue;
			} else {
				currentCircuit.sort();
				
				if(match(currentCircuit, currentCircuit.getAssignment().get(LAST)) >= match(currentCircuit, currentJuggler) ){
					prefIndex++;
					continue;
				} else {
					allJugglers.add(currentCircuit.getAssignment().get(LAST));
					currentCircuit.getAssignment().remove(LAST);
					currentCircuit.getAssignment().add(currentJuggler);
					allJugglers.remove(jIndex);
					prefIndex = 0;


					continue;
				}
					
			}

		}
		
		/***
		 * This writes to the output.txt
		 * Sorry for the mess... it works, I was feeling lazy after I got everything to work
		 */
		PrintWriter writer = new PrintWriter("src/output.txt", "UTF-8");
		// Print first Circuit
		for(int j = 0; j < circuitAssign.size(); j++){
			Circuit circuitWrite = circuitAssign.get(j);
			writer.print(circuitWrite.getName() + " "  );
			// Print Jugglers in Circuit's assignment
			for(int i = 0; i < MAX_SIZE; i++){
				Juggler jugglerWrite = circuitAssign.get(j).getAssignment().get(i);
				writer.print(jugglerWrite.getName() + " ");
				
				// Print the Match Scores for the Juggler's preferences
				for(int k = 0; k < PREF_LENGTH; k++){
					int cIndex = 0;
					while(true){
						if( jugglerWrite.getPref()[k].equals( circuitAssign.get(cIndex).getName() ) ){
							writer.print(circuitAssign.get(cIndex).getName() + ":" + match(circuitAssign.get(cIndex), jugglerWrite) + " ");
							
							break;
						}
						cIndex++;
						
					}
				}
				//Just to add commas where needed
				if(i < MAX_SIZE){
					writer.print(",");
				}
				
			}
			writer.println("");
			
			
		}
		writer.close();
		
		
		System.out.println("Output File Written");
	
		
		
	}
	

}
