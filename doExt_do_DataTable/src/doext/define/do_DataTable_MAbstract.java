package doext.define;

import core.object.DoUIModule;
import core.object.DoProperty;
import core.object.DoProperty.PropertyDataType;

public abstract class do_DataTable_MAbstract extends DoUIModule {

	protected do_DataTable_MAbstract() throws Exception {
		super();
	}

	/**
	 * 初始化
	 */
	@Override
	public void onInit() throws Exception {
		super.onInit();
		//注册属性
		this.registProperty(new DoProperty("freezeColumn", PropertyDataType.Number, "1", true));
		this.registProperty(new DoProperty("isHeaderVisible", PropertyDataType.Bool, "true", false));
	}
}