package net.jacobpeterson.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtil {

    /**
     * Sort a map by value by ascending or descending order (while allowing duplicate keys).
     *
     * @param unsortedMap the unsorted map
     * @param sortOrder   the sorting order
     *
     * @return the map
     *
     * @see <a href="https://stackoverflow.com/a/13913206/4352701">https://stackoverflow.com/a/13913206/4352701</a>
     */
    public static <K, V> LinkedHashMap<K, V> sortByValue(Map<K, V> unsortedMap, SortOrder sortOrder,
                                                         Comparator<K> keyComparator, Comparator<V> valueComparator) {
        List<Map.Entry<K, V>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort((o1, o2) -> {
            if (sortOrder == SortOrder.ASCENDING) {
                int valueComparision = valueComparator.compare(o1.getValue(), o2.getValue());
                if (keyComparator == null) { // Don't bother comparing keys
                    return valueComparision;
                } else {
                    return valueComparision == 0 ? keyComparator.compare(o1.getKey(), o2.getKey()) : valueComparision;
                }
            } else { // descending
                int valueComparision = valueComparator.compare(o2.getValue(), o1.getValue());
                if (keyComparator == null) { // Don't bother comparing keys
                    return valueComparision;
                } else {
                    return valueComparision == 0 ? keyComparator.compare(o2.getKey(), o1.getKey()) : valueComparision;
                }
            }
        });

        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b,
                                                      LinkedHashMap::new));
    }

    enum SortOrder {
        ASCENDING,
        DESCENDING
    }
}
