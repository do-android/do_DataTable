package doext.datatable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.graphics.Color;

public class DoTableHeaderBean {

	private JSONArray width;
	private int height = 80;
	private int bgColor;
	private int fontColor = Color.BLACK;
	private String fontStyle = "normal";
	private String textFlag = "normal";
	private int fontSize = 17;
	private JSONArray data;

	private List<DoTableCellBean> cells;

	public JSONArray getWidth() {
		return width;
	}

	public void setWidth(JSONArray width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getTextFlag() {
		return textFlag;
	}

	public void setTextFlag(String textFlag) {
		this.textFlag = textFlag;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	public DoTableHeaderBean() {
		this.cells = new ArrayList<DoTableCellBean>();
	}

	public List<DoTableCellBean> getCells() {
		return cells;
	}

	public void setCells(List<DoTableCellBean> cells) {
		this.cells = cells;
	}

	public void addCell(DoTableCellBean cellBean) {
		this.cells.add(cellBean);
	}
}
