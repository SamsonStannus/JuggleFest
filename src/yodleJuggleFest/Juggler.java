package yodleJuggleFest;

class Juggler {
	
	private String name;
	private int[] skills;
	private String[] circuitPref;
	
	public Juggler(String n, int[] s, String[] cP){
		this.name = n;
		this.skills = s;
		this.circuitPref = cP;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public int[] getSkills(){
		return skills;
	}
	public void setSkills(int h, int e, int p){
		this.skills[0] = h;
		this.skills[1] = e;
		this.skills[2] = p;
	}
	public String[] getPref(){
		return circuitPref;
	}
	public void setPref(String[] pref){
		for(int i = 0; i<10; i++){
			this.circuitPref[i] = pref[i];
		}
		
		
	}

}
