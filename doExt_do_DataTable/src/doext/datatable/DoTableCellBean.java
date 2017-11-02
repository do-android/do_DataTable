package doext.datatable;

import android.graphics.Color;
import android.widget.TextView;

public class DoTableCellBean {

	private String id; //column的id  
	private int x;
	private int y;
	private String text; //column的内容 
//	private String type; //column的类型，用于排序。其中type支持三种"int","boolean"和"string"格式 文本类型
	private float width; //column的宽度   
	private int height = 80;
	private int bgColor;
	private int fontColor = Color.BLACK;
	private String fontStyle = "normal";
	private String textFlag = "normal";
	private int fontSize = 17;
	private TextView view;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
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

	public TextView getView() {
		return view;
	}

	public void setView(TextView view) {
		this.view = view;
	}

}
