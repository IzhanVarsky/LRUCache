import java.util.LinkedHashMap

class RealLRUCache<K, V>(
    private val initialCapacity: Int,
) : LinkedHashMap<K, V>(initialCapacity, 0.75f, true) {
    val keysList: List<K>
        get() = keys.toList()

    val valuesList: List<V>
        get() = values.toList()

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > initialCapacity
    }
}