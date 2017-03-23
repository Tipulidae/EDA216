package kagor;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import database.Database;

public class CrustyMain {

    public static void main(String[] args) {
        Database db = new Database();
        try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new CookieFrame("Crusty Cookies", db);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
    }
}
