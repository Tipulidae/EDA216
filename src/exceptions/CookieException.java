package exceptions;

@SuppressWarnings("serial")
public class CookieException extends Exception {
	
	private static final String DEFAULT_MSG = "There was an unexpected problem with the database.";
	protected String msg;
	
	public CookieException() {
		super();
		msg = DEFAULT_MSG;
	}
	
	public CookieException(String msg) {
		super();
		this.msg = msg;
	}
	
	public CookieException(Exception e, String msg) {
		super(e);
		setMsg(e,msg);
	}
	
	public CookieException(Exception e) {
		super(e);
		setMsg(e,DEFAULT_MSG);
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
	protected void setMsg(Exception e, String msg) {
		this.msg = String.format("%s (%s)", msg, e.getClass());
	}
}
