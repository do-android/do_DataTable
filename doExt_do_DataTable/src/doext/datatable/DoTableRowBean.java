package doext.datatable;

import java.util.ArrayList;
import java.util.List;

public class DoTableRowBean {

	private String id;
	private List<DoTableCellBean> rowCell;
	
	public DoTableRowBean(){
		this.rowCell = new ArrayList<DoTableCellBean>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<DoTableCellBean> getRowCell() {
		return rowCell;
	}

	public void setRowCell(List<DoTableCellBean> rowCell) {
		this.rowCell = rowCell;
	}
	
	public void addCell(DoTableCellBean cellBean){
		this.rowCell.add(cellBean);
	}

}
