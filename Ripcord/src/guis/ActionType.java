package guis;

public enum ActionType {

	NONE(0), HREF(1), BEGIN_GAME(2), NEW_ENTITY(3);
	
	public final int type;
	
	ActionType(int type){
		this.type = type;
	}
}
