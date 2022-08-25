package com.wangchenyang.common.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Key Value 的键值对
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K, V> {

	private K key;

	private V value;

}
