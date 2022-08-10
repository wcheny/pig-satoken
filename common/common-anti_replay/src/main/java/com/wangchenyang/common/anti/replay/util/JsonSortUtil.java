package com.wangchenyang.common.anti.replay.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: 维斯
 * @Date: 2022/6/30 14:55
 * @Description: Json排序工具
 * @Version: v1.0
 */
public class JsonSortUtil {
	/**
	 * 对单词列表进行冒泡排序
	 * 直接操作对象地址 无需返回
	 *
	 * @param words ["name","age"]
	 */
	private static void wordSort(ArrayList<String> words) {
		for (int i = words.size() - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (words.get(j).compareToIgnoreCase(words.get(j + 1)) > 0) {
					String temp = words.get(j);
					words.set(j, words.get(j + 1));
					words.set(j + 1, temp);
				}
			}
		}
	}

	/**
	 * 对单层json排序
	 *
	 * @param json
	 */
	private static JSONObject getAloneKeys(JSONObject json) {
		ArrayList<String> aloneKeys = new ArrayList<>();
		for (String key : json.keySet()) {
			aloneKeys.add(key);
		}
		// 排序
		JsonSortUtil.wordSort(aloneKeys);
		// 整理排序后的json
		JSONObject newJson = new JSONObject(new LinkedHashMap<>());
		for (String key : aloneKeys) {
			newJson.put(key, json.get(key));
		}
		return newJson;
	}

	/**
	 * 递归每一层（当前是判断下一层是JSONObject类型的场景）
	 *
	 * @param json
	 * @return
	 */
	private static JSONObject getAloneKeysRec(JSONObject json) {
		JSONObject newJson = getAloneKeys(json);

		for (Map.Entry<String, Object> entry : newJson.entrySet()) {
			// JSONObject类型
			if (entry.getValue().getClass().equals(JSONObject.class)) {
				newJson.put(entry.getKey(), getAloneKeysRec((JSONObject) entry.getValue()));
			}
		}

		return newJson;
	}

	/**
	 * 对JSONObject的key根据首字母排序 若首字母相同对比下一个字母 依次类推
	 * 备注：当前未覆盖JSONArray的场景
	 *
	 * @param json
	 * @return 排序后的新json
	 */
	public static JSONObject startSort(JSONObject json) {
		// 第1层
		JSONObject jsonAlone = getAloneKeys(json);
		// 第2-n层
		for (Map.Entry<String, Object> entry : jsonAlone.entrySet()) {
			// 这里仅判断JSONObject类型的场景（若需要覆盖JSONArray场景 对应添加即可）
			if (entry.getValue().getClass().equals(JSONObject.class)) {
				jsonAlone.put(entry.getKey(), getAloneKeysRec((JSONObject) entry.getValue()));
			}
		}
		return jsonAlone;
	}


	public static void main(String[] args) {
		String jsonStr = "{\"code\":200,\"message\":\"success\",\"data\":{\"bate\":\"bate值\",\"baae\":{\"case\":\"case值\",\"casa\":{\"ask\":\"ask值\",\"bsk\":{\"abcdefghijklmn\":\"abcdefghijklmn值\",\"abcdefghijklma\":\"abcdefghijklma值\"}}}}}\n";
		System.out.println(String.format("排序前：%s", jsonStr));
		System.out.println(String.format("排序后：%s", JsonSortUtil.startSort((JSONObject) JSONObject.parse(jsonStr))));
	}

}
