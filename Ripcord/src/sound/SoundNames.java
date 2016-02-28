package sound;

public enum SoundNames {

	GUN_SHOT(0),	 BG_MUSIC(1),	 SCREAM1(2), GUN_OUT(3), RELOADING1(4),
	RELOADING2(5),	 RELOADING3(6),	 SHOTGUN(7), SNIPER(8),	 PLING(9),
	SCREAM2(10),	 BITE(11),		 MOAN1(12),	 MOAN2(13),	 MOAN3(14);
	
	public final int ID;
	
	SoundNames(int ID){
		this.ID = ID;
	}
}
