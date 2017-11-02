package doext.datatable;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.graphics.Color;

public class DoTableContentBean {

	private int width = 100;
	private int height = 80;
	private JSONArray bgColor;
	private int fontColor = Color.BLACK;
	private String fontStyle = "normal";
	private String textFlag = "normal";
	private int fontSize = 17;
	private JSONArray data;

	private Map<String, DoTableRowBean> rows;

	public DoTableContentBean() {
		this.rows = new HashMap<String, DoTableRowBean>();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public JSONArray getBgColor() {
		return bgColor;
	}

	public void setBgColor(JSONArray bgColor) {
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

	public void setRows(Map<String, DoTableRowBean> rows) {
		this.rows = rows;
	}

	public Map<String, DoTableRowBean> getRows() {
		return this.rows;
	}

	public void addRow(DoTableRowBean rowBean) {
		this.rows.put(rowBean.getId(), rowBean);
	}
}
