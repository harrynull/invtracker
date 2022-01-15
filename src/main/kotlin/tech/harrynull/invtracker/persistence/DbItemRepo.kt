package tech.harrynull.invtracker.persistence

import org.springframework.data.repository.CrudRepository

interface DbItemRepo : CrudRepository<DbItem?, Long?> {
    fun findBySku(sku: String): DbItem?
    fun findAllByActiveIsTrue(): List<DbItem>
}
