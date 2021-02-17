package net.australmc.economy.data.sources

import java.util.function.BiConsumer
import java.util.function.Consumer

abstract class DataSource<K, T> {
    abstract val name: String

    abstract fun find(key: K, callback: BiConsumer<K, T?>)

    abstract fun findAll(callback: Consumer<List<T>>)

    /**
     * @param criteria Criteria used to sort items, should be the object property name
     * @param ascOrder Should sorting be in the ascendant order
     */
    abstract fun findAllBy(criteria: String, ascOrder: Boolean, callback: Consumer<List<T>>)

    /**
     * @param limit Limit of entries to be returned
     * @param criteria Criteria used to sort items, should be the object property name
     * @param ascOrder Should sorting be in the ascendant order
     */
    abstract fun findFirstBy(limit: Long, criteria: String, ascOrder: Boolean, callback: Consumer<List<T>>)

    abstract fun save(key: K, value: T, callback: BiConsumer<K, T>?)

    abstract fun delete(key: K, callback: Consumer<K>?)

    abstract fun onDisable()
}