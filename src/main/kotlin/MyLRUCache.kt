import kotlin.collections.*
import org.junit.jupiter.api.Assertions.*

class MyLRUCache<K, V>(private val MAX_SIZE: Int) {
    private val hashTable: HashMap<K, V> = HashMap(MAX_SIZE)
    private val priorityQueue: ArrayDeque<K> = ArrayDeque(MAX_SIZE)
    val size: Int
        get() = priorityQueue.size
    val keys: List<K>
        get() = priorityQueue.toList()
    val values: List<V>
        get() = priorityQueue.map { hashTable.getValue(it) }.toList()

    private fun checkSizeConsistency() = assertEquals(hashTable.size, priorityQueue.size)

    operator fun get(key: K): V? {
        checkSizeConsistency()
        val oldSize = size

        var res: V? = null
        if (hashTable.containsKey(key)) {
            assertTrue(priorityQueue.contains(key))
            priorityQueue.remove(key)
            priorityQueue.add(key)
            res = hashTable[key]
        }

        assertEquals(oldSize, size)
        checkSizeConsistency()
        return res
    }

    operator fun set(k: K, v: V): Boolean {
        checkSizeConsistency()
        val oldSize = size

        val wasCached = priorityQueue.remove(k)
        if (!wasCached && hashTable.size == MAX_SIZE) {
            assertTrue(priorityQueue.isNotEmpty())
            val minKey = priorityQueue.removeFirst()
            hashTable.remove(minKey)
        }
        hashTable[k] = v
        priorityQueue.add(k)

        if (!wasCached && oldSize < MAX_SIZE) {
            assertEquals(oldSize + 1, size)
        } else {
            assertEquals(oldSize, size)
        }
        checkSizeConsistency()
        return wasCached
    }
}
