package doext.implement;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import core.helper.DoIOHelper;
import core.helper.DoJsonHelper;
import core.helper.DoResourcesHelper;
import core.helper.DoTextHelper;
import core.helper.DoUIModuleHelper;
import core.interfaces.DoIModuleTypeID;
import core.interfaces.DoIScriptEngine;
import core.interfaces.DoISourceFS;
import core.interfaces.DoIUIModuleView;
import core.object.DoInvokeResult;
import core.object.DoUIModule;
import doext.datatable.DoSyncHorizontalScrollView;
import doext.datatable.DoTableCellBean;
import doext.datatable.DoTableContentBean;
import doext.datatable.DoTableHeaderBean;
import doext.datatable.DoTableRowBean;
import doext.define.do_DataTable_IMethod;
import doext.define.do_DataTable_MAbstract;

/**
 * 自定义扩展UIView组件实现类，此类必须继承相应VIEW类，并实现DoIUIModuleView,do_DataTable_IMethod接口；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.model.getUniqueKey());
 */
public class do_DataTable_View extends LinearLayout implements DoIUIModuleView, do_DataTable_IMethod, DoIModuleTypeID {

	/**
	 * 每个UIview都会引用一个具体的model实例；
	 */
	private do_DataTable_MAbstract model;
	private int freezeColumn = 1;
	private DoTableHeaderBean mHeaderBean;
	private DoTableContentBean mContentBean;

	private LinearLayout mHeaderLayout;
	private LinearLayout mFirstLayout;
	private TableLayout mHeaderTable;
	private TableLayout mFreezeColumn;
	private TableLayout mDragColumn;

	private Context mContext;
	private double xZoom;
	private double yZoom;

	private int lineSize;

	public do_DataTable_View(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 初始化加载view准备,_doUIModule是对应当前UIView的model实例
	 */
	@Override
	public void loadView(DoUIModule _doUIModule) throws Exception {
		this.model = (do_DataTable_MAbstract) _doUIModule;
		xZoom = _doUIModule.getXZoom();
		yZoom = _doUIModule.getYZoom();
		lineSize = (int) (2 * yZoom);
		mHeaderBean = new DoTableHeaderBean();
		mContentBean = new DoTableContentBean();

		View rootView = View.inflate(mContext, DoResourcesHelper.getIdentifier("do_data_table_view", "layout", this), this);

		// 设置 两个 HorizontalScrollView 滚动
		DoSyncHorizontalScrollView hsv_h = (DoSyncHorizontalScrollView) rootView.findViewById(DoResourcesHelper.getIdentifier("hsv_h", "id", this));
		DoSyncHorizontalScrollView hsv_c = (DoSyncHorizontalScrollView) rootView.findViewById(DoResourcesHelper.getIdentifier("hsv_c", "id", this));
		hsv_h.setScrollView(hsv_c);
		hsv_c.setScrollView(hsv_h);

		mHeaderTable = (TableLayout) rootView.findViewById(DoResourcesHelper.getIdentifier("table_header", "id", this));
		mFirstLayout = (LinearLayout) rootView.findViewById(DoResourcesHelper.getIdentifier("ll_head_frist", "id", this));
		mHeaderLayout = (LinearLayout) rootView.findViewById(DoResourcesHelper.getIdentifier("ll_table_header", "id", this));
		mFreezeColumn = (TableLayout) rootView.findViewById(DoResourcesHelper.getIdentifier("table_freeze_column", "id", this));
		mDragColumn = (TableLayout) rootView.findViewById(DoResourcesHelper.getIdentifier("table_drag_column", "id", this));
		this.setOrientation(LinearLayout.VERTICAL);
	}

	/**
	 * 动态修改属性值时会被调用，方法返回值为true表示赋值有效，并执行onPropertiesChanged，否则不进行赋值；
	 * 
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public boolean onPropertiesChanging(Map<String, String> _changedValues) {
		return true;
	}

	/**
	 * 属性赋值成功后被调用，可以根据组件定义相关属性值修改UIView可视化操作；
	 * 
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public void onPropertiesChanged(Map<String, String> _changedValues) {
		DoUIModuleHelper.handleBasicViewProperChanged(this.model, _changedValues);

		if (_changedValues.containsKey("freezeColumn")) {
			this.freezeColumn = DoTextHelper.strToInt(_changedValues.get("freezeColumn"), 1);
		}

		if (_changedValues.containsKey("isHeaderVisible")) {
			boolean isHeaderVisible = DoTextHelper.strToBool(_changedValues.get("isHeaderVisible"), true);
			if (isHeaderVisible) {
				mHeaderLayout.setVisibility(View.VISIBLE);
			} else {
				mHeaderLayout.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {

		if ("setHeaderData".equals(_methodName)) {
			this.setHeaderData(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("setHeaderStyle".equals(_methodName)) {
			this.setHeaderStyle(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("setRowData".equals(_methodName)) {
			this.setRowData(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("setRowStyle".equals(_methodName)) {
			this.setRowStyle(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("setCellDatas".equals(_methodName)) {
			this.setCellDatas(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}

		return false;
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.model.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		return false;
	}

	/**
	 * 释放资源处理，前端JS脚本调用closePage或执行removeui时会被调用；
	 */
	@Override
	public void onDispose() {
		//...do something
	}

	/**
	 * 重绘组件，构造组件时由系统框架自动调用；
	 * 或者由前端JS脚本调用组件onRedraw方法时被调用（注：通常是需要动态改变组件（X、Y、Width、Height）属性时手动调用）
	 */
	@Override
	public void onRedraw() {
		this.setLayoutParams(DoUIModuleHelper.getLayoutParams(this.model));
	}

	/**
	 * 获取当前model实例
	 */
	@Override
	public DoUIModule getModel() {
		return model;
	}

	/**
	 * 设置表头数据；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setHeaderData(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		mHeaderBean.setData(DoJsonHelper.getJSONArray(_dictParas, "data"));
	}

	/**
	 * 设置表头样式；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setHeaderStyle(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		//可以全局设置，如：100; 也可以支持对每列设置，格式为一个JsonArray，如：[100,50,40,50]，需与表头列数对应
		int _height = DoTextHelper.strToInt(DoJsonHelper.getString(_dictParas, "height", "100"), 100);
		JSONArray _width = new JSONArray();
		String _widthtStr = DoJsonHelper.getString(_dictParas, "width", "80");
		if (_widthtStr.startsWith("[")) {
			try {
				_width = new JSONArray(_widthtStr);
			} catch (Exception e) {
				_width.put(80);
			}
		} else {
			_width.put(DoTextHelper.strToInt(_widthtStr, 80));
		}
		int _bgColor = DoUIModuleHelper.getColorFromString(DoJsonHelper.getString(_dictParas, "bgColor", "00000000"), Color.TRANSPARENT);
		int _fontColor = DoUIModuleHelper.getColorFromString(DoJsonHelper.getString(_dictParas, "fontColor", "000000FF"), Color.BLACK);
		//"包含4种类型：normal：常规bold：粗体italic：斜体bold_italic：粗斜体（iOS平台不支持）"
		String _fontStyle = DoJsonHelper.getString(_dictParas, "fontStyle", "normal");
		//"包含3种类型：normal：常规underline ：下划线strikethrough ：删除线"
		String _textFlag = DoJsonHelper.getString(_dictParas, "textFlag", "normal");
		int _fontSize = DoJsonHelper.getInt(_dictParas, "fontSize", 17);

		mHeaderBean.setWidth(_width);
		mHeaderBean.setHeight(_height);
		mHeaderBean.setBgColor(_bgColor);
		mHeaderBean.setFontColor(_fontColor);
		mHeaderBean.setFontStyle(_fontStyle);
		mHeaderBean.setTextFlag(_textFlag);
		mHeaderBean.setFontSize(_fontSize);
	}

	/**
	 * 设置表格数据；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setRowData(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String _data = DoJsonHelper.getString(_dictParas, "data", "");
		if (TextUtils.isEmpty(_data)) {
			throw new Exception("data参数值不能为空！");
		}
		JSONArray _mData = null;
		if (_data.startsWith(DoISourceFS.DATA_PREFIX)) { //表示要加载一个本地文件
			String _realPath = _scriptEngine.getCurrentApp().getDataFS().getFileFullPathByName(_data);
			if (!DoIOHelper.existFile(_realPath)) {
				throw new Exception(_data + "文件不存在！");
			}
			_mData = new JSONArray(DoIOHelper.readUTF8File(_realPath));
		} else {
			_mData = new JSONArray(_data);
		}
		//清除表头数据，避免重复 
		mFirstLayout.removeAllViews();
		mHeaderTable.removeAllViews();
		///创建表头
		int _totalColumn = 0;
		JSONArray _headerData = mHeaderBean.getData();
		JSONArray _headerWiths = mHeaderBean.getWidth();
		if (_headerData != null) { //如果表头数据小于0，不去画表头
			TableRow _headerRow = new TableRow(mContext);
			_headerRow.setGravity(Gravity.CENTER);
			_totalColumn = _headerData.length();
			//表示设置了表头数据，但是没设置样式，那就设置默认值 100
			if (_headerWiths == null) {
				_headerWiths = new JSONArray();
				_headerWiths.put(100);
				mHeaderBean.setWidth(_headerWiths);
			}
			int _widths = _headerWiths.length();
			//主要是补全表格的宽，依据表头的数据为准
			if (_totalColumn != _widths) {
				if (_widths == 1) { //表示都一样
					for (int i = 1; i < _totalColumn; i++) {
						_headerWiths.put(_headerWiths.get(0));
					}
				} else {
					if (_totalColumn > _widths) {
						int _lastWidth = _headerWiths.getInt(_widths - 1);
						for (int i = _widths - 1; i < _totalColumn; i++) {
							_headerWiths.put(_lastWidth);
						}
					}
				}
			}

			for (int i = 0; i < _totalColumn; i++) {
				String _childData = _headerData.getString(i);
				if (i < freezeColumn) {
					// 需要修改成linerlayout
					mFirstLayout.addView(makeTableHeaderRowWithText(_headerWiths.getInt(i), mHeaderBean.getHeight(), _childData));
				} else {
					_headerRow.addView(makeTableHeaderRowWithText(_headerWiths.getInt(i), mHeaderBean.getHeight(), _childData));
				}
			}
			mHeaderTable.addView(_headerRow, new TableLayout.LayoutParams(-2, -2));
		}

		//创建表格之前先清除所有的数据
		mFreezeColumn.removeAllViews();
		mDragColumn.removeAllViews();
		///创建表格内容
		int _rawCount = _mData.length();
		///解析表格颜色值
		int _rowBgColor = Color.TRANSPARENT;
		int[] _rowBgColors = new int[2];
		JSONArray _rowBgColorArray = mContentBean.getBgColor();
		boolean _isSingleRow = false;
		if (_rowBgColorArray != null) {
			int _len = _rowBgColorArray.length();
			if (_len == 1) {
				_isSingleRow = true;
				_rowBgColor = DoUIModuleHelper.getColorFromString(_rowBgColorArray.getString(0), Color.TRANSPARENT);
			} else if (_len == 2) {
				_isSingleRow = false;
				_rowBgColors[0] = DoUIModuleHelper.getColorFromString(_rowBgColorArray.getString(0), Color.TRANSPARENT);
				_rowBgColors[1] = DoUIModuleHelper.getColorFromString(_rowBgColorArray.getString(1), Color.TRANSPARENT);
			} else {
				_isSingleRow = true;
			}
		}

		for (int i = 0; i < _rawCount; i++) { // 行数
			JSONArray _rowData = _mData.getJSONArray(i);
			int _coulumnLen = 0;
			if (_totalColumn > 0) { //以表头的列为准
				_coulumnLen = _totalColumn;
			} else {
				if (_rowData == null) {
					continue;
				} else {
					//应该以第一行数据为准
					_coulumnLen = _mData.getJSONArray(0).length();
				}
			}

			int _currentBgColor = Color.TRANSPARENT;

			if (_isSingleRow) {
				_currentBgColor = _rowBgColor;
			} else {
				_currentBgColor = _rowBgColors[i % 2];
			}

			//包含一行数据
			DoTableRowBean _rowBean = new DoTableRowBean();
			_rowBean.setId(i + "");

			TableRow _freezeRow = new TableRow(mContext);
			_freezeRow.setGravity(Gravity.CENTER);

			TableRow _dragRow = new TableRow(mContext);
			_dragRow.setGravity(Gravity.CENTER);

			for (int j = 0; j < _coulumnLen; j++) {
				int _rowWidth = 100;
				//设置了表头的样式，就取出表头里面样式设置的width
				if (_headerWiths != null) {
					int _headerWidthLen = _headerWiths.length();
					//先要判断下如果没有表头，并且数据只有一条的情况，那是需要所有的width都是一样的
					if (_headerWidthLen == 1) {
						_rowWidth = _headerWiths.getInt(0);
					} else {
						if (j < _headerWidthLen) {
							_rowWidth = _headerWiths.getInt(j);
						} else { //那就取最后一个width
							_rowWidth = _headerWiths.getInt(_headerWidthLen - 1);
						}
					}
				}

				String _text = "";
				if (_rowData != null) {
					int _rowDataLen = _rowData.length();
					if (j < _rowDataLen) {
						_text = _rowData.getString(j);
					}
				}

				TextView _textView = makeTableRowWithText(i, j, _rowWidth, mContentBean.getHeight(), _text, _currentBgColor);
				if (j < freezeColumn) { // 冻结列数
					_freezeRow.addView(_textView);
				} else {
					_dragRow.addView(_textView);
				}

				//表示一个单元格对象，暂且什么都不保存，以后功能可能会用上
				DoTableCellBean _cellBean = new DoTableCellBean();
				_cellBean.setView(_textView);
				_rowBean.addCell(_cellBean);
			}

			mFreezeColumn.addView(_freezeRow, new TableLayout.LayoutParams(-2, -2));
			mDragColumn.addView(_dragRow, new TableLayout.LayoutParams(-2, -2));
			mContentBean.addRow(_rowBean);
		}
	}

	//创建表头的列
	private TextView makeTableHeaderRowWithText(int width, int height, String text) {
		TextView recyclableTextView = new TextView(mContext);
		recyclableTextView.setGravity(Gravity.CENTER);

		recyclableTextView.setText(text);
		recyclableTextView.setTextColor(mHeaderBean.getFontColor());
		int _fontSize = DoUIModuleHelper.getDeviceFontSize(model, mHeaderBean.getFontSize() + "");
		recyclableTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, _fontSize);
		DoUIModuleHelper.setTextFlag(recyclableTextView, mHeaderBean.getTextFlag());
		DoUIModuleHelper.setFontStyle(recyclableTextView, mHeaderBean.getFontStyle());
		recyclableTextView.setBackgroundColor(mHeaderBean.getBgColor());

		TableRow.LayoutParams p = new TableRow.LayoutParams((int) (width * xZoom), (int) (height * yZoom));
		p.setMargins(lineSize, lineSize, lineSize, lineSize);
		recyclableTextView.setLayoutParams(p);
		return recyclableTextView;
	}

	//创建表的行（表头除外）
	private TextView makeTableRowWithText(final int row, final int column, int width, int height, String text, int bgColor) {
		TextView recyclableTextView = new TextView(mContext);
		recyclableTextView.setGravity(Gravity.CENTER);

		recyclableTextView.setText(text);
		recyclableTextView.setTextColor(mContentBean.getFontColor());
		recyclableTextView.setBackgroundColor(bgColor);
		int _fontSize = DoUIModuleHelper.getDeviceFontSize(model, mContentBean.getFontSize() + "");
		recyclableTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, _fontSize);
		DoUIModuleHelper.setTextFlag(recyclableTextView, mContentBean.getTextFlag());
		DoUIModuleHelper.setFontStyle(recyclableTextView, mContentBean.getFontStyle());
		TableRow.LayoutParams p = new TableRow.LayoutParams((int) (width * xZoom), (int) (height * yZoom));
		p.setMargins(lineSize, lineSize, lineSize, lineSize);
		recyclableTextView.setLayoutParams(p);
		recyclableTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itmeTouch(row, column);
			}
		});
		recyclableTextView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				itmeLongTouch(row, column);
				return true;
			}
		});
		return recyclableTextView;
	}

	/**
	 * 设置数据样式；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setRowStyle(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		int _height = DoTextHelper.strToInt(DoJsonHelper.getString(_dictParas, "height", "80"), 80);
		JSONArray _bgColor = DoJsonHelper.getJSONArray(_dictParas, "bgColor");
		int _fontColor = DoUIModuleHelper.getColorFromString(DoJsonHelper.getString(_dictParas, "fontColor", "000000FF"), Color.BLACK);
		//"包含4种类型：normal：常规bold：粗体italic：斜体bold_italic：粗斜体（iOS平台不支持）"
		String _fontStyle = DoJsonHelper.getString(_dictParas, "fontStyle", "normal");
		//"包含3种类型：normal：常规underline ：下划线strikethrough ：删除线"
		String _textFlag = DoJsonHelper.getString(_dictParas, "textFlag", "normal");
		int _fontSize = DoJsonHelper.getInt(_dictParas, "fontSize", 17);

		mContentBean.setHeight(_height);
		mContentBean.setBgColor(_bgColor);
		mContentBean.setFontColor(_fontColor);
		mContentBean.setFontStyle(_fontStyle);
		mContentBean.setTextFlag(_textFlag);
		mContentBean.setFontSize(_fontSize);
	}

	/**
	 * 设置数据样式；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void setCellDatas(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		Map<String, DoTableRowBean> _rows = mContentBean.getRows();
		if (_rows == null || _rows.size() <= 0) {
			return;
		}

		JSONArray _data = DoJsonHelper.getJSONArray(_dictParas, "data");
		for (int i = 0; i < _data.length(); i++) {

			JSONObject _childData = _data.getJSONObject(i);

			if (_childData == null) {
				continue;
			}

			int _row = DoJsonHelper.getInt(_childData, "row", -1);
			int _col = DoJsonHelper.getInt(_childData, "column", -1);
			if (_row < 0 || _col < 0) {
				_invokeResult.setError("row或column参数值为空或设置不正确！");
				continue;
			}
			//取出对应的cell单元格
			if (_row > _rows.size() - 1) {
				_invokeResult.setError("row参数值设置不正确！");
				continue;
			}

			List<DoTableCellBean> _cells = _rows.get(_row + "").getRowCell();
			if (_cells == null || _cells.size() <= 0 || _col > _cells.size() - 1) {
				_invokeResult.setError("column参数值设置不正确！");
				continue;
			}

			DoTableCellBean _cell = _cells.get(_col);
			if (_cell == null || _cell.getView() == null) {
				continue;
			}

			TextView _mView = _cell.getView();
			if (_childData.has("text")) {
				_mView.setText(_childData.getString("text") + "");
			}

			JSONObject _style = DoJsonHelper.getJSONObject(_childData, "style");
			if (_style == null) {
				return;
			}

			if (_style.has("bgColor")) {
				int _bgColor = DoUIModuleHelper.getColorFromString(DoJsonHelper.getString(_style, "bgColor", "00000000"), Color.TRANSPARENT);
				_mView.setBackgroundColor(_bgColor);
			}
			if (_style.has("fontColor")) {
				int _fontColor = DoUIModuleHelper.getColorFromString(DoJsonHelper.getString(_style, "fontColor", "000000FF"), Color.BLACK);
				_mView.setTextColor(_fontColor);

			}
			if (_style.has("fontStyle")) {
				//"包含4种类型：normal：常规bold：粗体italic：斜体bold_italic：粗斜体（iOS平台不支持）"
				DoUIModuleHelper.setFontStyle(_mView, DoJsonHelper.getString(_style, "fontStyle", "normal"));

			}
			if (_style.has("textFlag")) {
				//"包含3种类型：normal：常规underline ：下划线strikethrough ：删除线"
				DoUIModuleHelper.setTextFlag(_mView, DoJsonHelper.getString(_style, "textFlag", "normal"));

			}
			if (_style.has("fontSize")) {
				int _fontSize = DoUIModuleHelper.getDeviceFontSize(model, DoJsonHelper.getInt(_style, "fontSize", 17) + "");
				_mView.setTextSize(TypedValue.COMPLEX_UNIT_PX, _fontSize);
			}
		}

	}

	private void itmeLongTouch(int row, int column) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.model.getUniqueKey());
		JSONObject _obj = new JSONObject();
		try {
			_obj.put("row", row);
			_obj.put("column", column);
		} catch (Exception e) {
		}
		_invokeResult.setResultNode(_obj);
		this.model.getEventCenter().fireEvent("longTouch", _invokeResult);
	}

	private void itmeTouch(int row, int column) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.model.getUniqueKey());
		JSONObject _obj = new JSONObject();
		try {
			_obj.put("row", row);
			_obj.put("column", column);
		} catch (Exception e) {
		}
		_invokeResult.setResultNode(_obj);
		this.model.getEventCenter().fireEvent("touch", _invokeResult);
	}

	@Override
	public String getTypeID() {
		return model.getTypeID();
	}
}