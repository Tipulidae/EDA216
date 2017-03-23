package exceptions;

@SuppressWarnings("serial")
public class CookieEmptyTimeFieldException extends CookieException {
	public CookieEmptyTimeFieldException() {
		super();
		msg = "Time field empty.";
	}
}
