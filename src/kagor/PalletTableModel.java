package kagor;

import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PalletTableModel extends DefaultTableModel implements TableModel {
	
	public PalletTableModel() {
		super();
		
	}
	
	private final String[] columnNames = {"palletId", "orderId","cookieName","location","timestamp","blocked"};
	
	public void setData(List<Object[]> pallets) {
		Object[][] objs = new Object[pallets.size()][];
		pallets.toArray(objs);
		setDataVector(objs,columnNames);
	}

}
