package doext.datatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class DoDataTableUtils {

	private DoDataTableUtils() {
	}

	private DoDataTableUtils instance;

	public DoDataTableUtils getInstance() {
		if (instance == null) {
			instance = new DoDataTableUtils();
		}
		return instance;
	}

	/**
	 * 
	 * @param contentBean
	 *            当前显示的所有数据
	 * @param index
	 *            哪一行需要排序
	 * @param type
	 *            排序的数据类型
	 * @param asc
	 *            true 升序 , false 降序
	 */
	public void sortData(DoTableContentBean contentBean, int index, String type, boolean asc) {

		Map<String, DoTableRowBean> rows = contentBean.getRows();
		Set<Entry<String, DoTableRowBean>> entrySet = rows.entrySet();

		if (type.equals("int")) {
			Map<String, Integer> sortCells = new HashMap<String, Integer>();
			for (Entry<String, DoTableRowBean> entry : entrySet) {
				String id = entry.getKey();
				Integer value = Integer.parseInt(entry.getValue().getRowCell().get(index).getText());
				sortCells.put(id, value);
			}
			sortCells = sortIntegerMap(sortCells, asc);
		} else if (type.equals("boolean")) {

			Map<String, Boolean> sortCells = new HashMap<String, Boolean>();
			for (Entry<String, DoTableRowBean> entry : entrySet) {
				String id = entry.getKey();
				Boolean value = Boolean.parseBoolean(entry.getValue().getRowCell().get(index).getText());
				sortCells.put(id, value);
			}
			sortCells = sortBooleanMap(sortCells, asc);
		} else if (type.equals("string")) {

			Map<String, String> sortCells = new HashMap<String, String>();
			for (Entry<String, DoTableRowBean> entry : entrySet) {
				String id = entry.getKey();
				String value = entry.getValue().getRowCell().get(index).getText();
				sortCells.put(id, value);
			}
			sortCells = sortStringMap(sortCells, asc);
		}
	}

	private Map<String, Integer> sortIntegerMap(Map<String, Integer> map, final boolean asc) {
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if (asc) {// 升序排序
					return o1.getValue() - o2.getValue();
				} else {
					return o2.getValue() - o1.getValue();
				}
			}
		});

		Map<String, Integer> newMap = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}

		return newMap;
	}

	private Map<String, String> sortStringMap(Map<String, String> map, final boolean asc) {
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
		// 然后通过比较器来实现排序
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				if (asc) {// 升序排序
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());
				}
			}
		});

		Map<String, String> newMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}

		return newMap;
	}

	private Map<String, Boolean> sortBooleanMap(Map<String, Boolean> map, final boolean asc) {
		List<Map.Entry<String, Boolean>> list = new ArrayList<Map.Entry<String, Boolean>>(map.entrySet());
		// 然后通过比较器来实现排序
		Collections.sort(list, new Comparator<Map.Entry<String, Boolean>>() {
			public int compare(Entry<String, Boolean> o1, Entry<String, Boolean> o2) {
				if (asc) {// 升序排序
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());
				}
			}
		});

		Map<String, Boolean> newMap = new HashMap<String, Boolean>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}

		return newMap;
	}
}
