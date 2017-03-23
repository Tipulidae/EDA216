package exceptions;

@SuppressWarnings("serial")
public class CookieRollbackException extends CookieException {
	public CookieRollbackException(Exception e) {
		super(e);
		setMsg(e,"Fatal: Unable to rollback transaction!");
	}
}
