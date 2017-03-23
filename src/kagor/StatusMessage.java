package kagor;

import java.util.Observable;

public class StatusMessage extends Observable{
	private String msg = "Welcome to Krusty Cookies.";
	
	public void set(String msg){
		this.msg = msg;
		setChanged();
		notifyAll();
	}
	
	public String get(){
		return msg;
	}
}
