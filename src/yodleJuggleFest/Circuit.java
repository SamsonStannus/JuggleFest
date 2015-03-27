package yodleJuggleFest;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;




class Circuit {
	private String name;
	private int[] requiredSkills;
	private ArrayList<Juggler> assignment;
	
	public Circuit(String n, int[] reqSkill, ArrayList<Juggler> ass){
		this.name = n;
		this.requiredSkills = reqSkill;
		this.assignment = ass;
		
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public int[] getReqSkills(){
		return requiredSkills;
	}
	public void setReqSkills(int h, int e, int p){
		this.requiredSkills[0] = h;
		this.requiredSkills[1] = e;
		this.requiredSkills[2] = p;
		
	}
	public ArrayList<Juggler> getAssignment(){
		return assignment;
	}
	public void setAssignment(Juggler j){
		this.assignment.add(j);
	}
	public int getSize(){
		return assignment.size();
	}
	public void sort(){
		Collections.sort(assignment, new Comparator<Juggler>(){ 
			@Override
			public int compare(Juggler j1, Juggler j2){
				int match1 = 0;
				int match2 = 0;
				for(int i = 0; i<3; i++){
					match1 = match1 + (j1.getSkills()[i] * requiredSkills[i]);
					match2 = match2 + (j2.getSkills()[i] * requiredSkills[i]);
				}
				if(match1 < match2){
					return 1;
				} else {
					return -1;
				}
			}
		} );
	}
	

	

}
