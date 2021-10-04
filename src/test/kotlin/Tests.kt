import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random
import org.junit.jupiter.api.Assertions.*


class Tests {
    private lateinit var myCache: MyLRUCache<Int, Int>
    private lateinit var realCache: RealLRUCache<Int, Int>

    private fun initWithCapacity(n: Int) {
        myCache = MyLRUCache(n)
        realCache = RealLRUCache(n)
    }

    @Test
    fun sixElementsWithSize5() {
        initWithCapacity(5)
        (1..6).forEach { myCache[it] = it }
        val expected = mutableListOf(2, 3, 4, 5, 6)
        assertEquals(expected, myCache.keys, "Different elements")
        assertEquals(myCache.size, 5, "Different size")
    }

    @Test
    fun eightElementsWithSize5() {
        initWithCapacity(5)
        (1..8).forEach { myCache[it] = it }
        val expected = mutableListOf(4, 5, 6, 7, 8)
        assertEquals(expected, myCache.keys, "Different elements")
        assertEquals(myCache.size, 5, "Different size")
    }

    @Test
    fun addingOnlyWithSameKey() {
        initWithCapacity(5)
        (1..5).forEach { myCache[1] = it }
        assertEquals(mutableListOf(5), myCache.values, "Different elements")
        assertEquals(myCache.size, 1, "Different size")
    }

    @Test
    fun noAdding() {
        initWithCapacity(5)
        assertEquals(myCache[0], null, "Expected null when nothing added")
        assertEquals(listOf<Int>(), myCache.values, "Different elements")
        assertEquals(myCache.size, 0, "Different size")
    }

    @Test
    fun addingSameAndDifferent() {
        initWithCapacity(5)

        myCache[0] = 0
        assertEquals(myCache[0], 0)
        assertEquals(myCache.size, 1)

        myCache[0] = 1
        assertEquals(myCache[0], 1)
        assertEquals(myCache.size, 1)

        myCache[1] = 5
        assertEquals(myCache[1], 5)
        assertEquals(myCache.size, 2)

        assertEquals(myCache[5], null, "Expected null when nothing added by key 5")
        assertEquals(listOf(0, 1), myCache.keys, "Different keys")
        assertEquals(listOf(1, 5), myCache.values, "Different values")
        assertEquals(myCache.size, 2, "Different size")

        myCache[2] = 20
        myCache[3] = 30
        myCache[4] = 40
        myCache[5] = 50

        assertEquals(myCache[0], null, "Expected null because an old entry must have beeen removed")
        assertEquals(listOf(1, 2, 3, 4, 5), myCache.keys, "Different keys")
        assertEquals(listOf(5, 20, 30, 40, 50), myCache.values, "Different values")
        assertEquals(myCache.size, 5, "Different size")

        myCache[3] = 60
        assertEquals(listOf(1, 2, 4, 5, 3), myCache.keys, "Different keys")
        assertEquals(listOf(5, 20, 40, 50, 60), myCache.values, "Different values")
        assertEquals(myCache.size, 5, "Different size")
    }

    @Test
    fun bigRandomTest() {
        initWithCapacity(1000)
        val random = Random(Date().time)
        repeat(1000000) {
            val k = random.nextInt(-1000, 1000)
            val v = random.nextInt(-1000, 1000)
            myCache[k] = v
            realCache[k] = v
            assertEquals(myCache.size, realCache.size)
        }
        repeat(1000000) {
            val r = random.nextInt(-1000, 1000)
            assertEquals(myCache[r], realCache[r])
        }
        assertEquals(myCache.keys, realCache.keysList, "Different keys")
        assertEquals(myCache.values, realCache.valuesList, "Different values")
    }
}