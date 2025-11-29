package meldexun.renderlib.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.util.ResourceLocation;

public class ResourceLocationMap<K, V> {

	private final Map<String, V> namespace2valueMap = new HashMap<>();
	private final Map<String, V> key2valueMap = new HashMap<>();		//resourceLocation2valueMap
	private final Map<Class<? extends K>, V> class2valueMap = new HashMap<>();
	private final Function<Class<K>, String> keyFunc;	//resourceLocationFunc
	private final V defaultValue;
	private final Function<String[], V> valueParser;

	public ResourceLocationMap(Function<Class<K>, String> func, V defaultValue, Function<String[], V> valueParser) {
		this.keyFunc = func;
		this.defaultValue = defaultValue;
		this.valueParser = valueParser;
	}

	@SuppressWarnings("unchecked")
	public V get(K t) {
		if (namespace2valueMap.isEmpty() && key2valueMap.isEmpty()) {
			return defaultValue;
		}
		return class2valueMap.computeIfAbsent((Class<? extends K>) t.getClass(), k -> {
			String key = keyFunc.apply((Class<K>) k);		//ResourceLocation resourceLocation = resourceLocationFunc.apply((Class<K>) k)
			if (key == null) {
				return defaultValue;
			}
			V value = key2valueMap.get(key.toString());
			if (value != null) {
				return value;
			}
			// value = namespace2valueMap.get(resourceLocation.getNamespace());		no namespace usage in 1.8.9 :(
			// if (value != null) {
			// 	return value;
			// }
			return defaultValue;
		});
	}

	public void load(String[] data) {
		this.namespace2valueMap.clear();
		this.key2valueMap.clear();
		this.class2valueMap.clear();

		for (String entry : data) {
			//int indexKeyDelimiter = entry.indexOf(':');
			int indexEntryDelimiter = entry.indexOf('=');
			String key = indexEntryDelimiter != -1 ? entry.substring(0, indexEntryDelimiter) : entry;
			String[] rawValue;
			if (indexEntryDelimiter != -1) {
				rawValue = Arrays.stream(entry.substring(indexEntryDelimiter + 1).split(",")).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
			} else {
				rawValue = new String[0];
			}
			V value = valueParser.apply(rawValue);
			if (value == null) {
				continue;
			}

			// if (indexKeyDelimiter == -1 || indexEntryDelimiter != -1 && indexKeyDelimiter > indexKeyDelimiter) {		
			// 	namespace2valueMap.put(key, value);
			// } else {
			// 	key2valueMap.put(key, value);
			// }

			key2valueMap.put(key, value);
		}
	}

}
