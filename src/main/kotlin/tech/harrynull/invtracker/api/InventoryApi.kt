package tech.harrynull.invtracker.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.harrynull.invtracker.persistence.DbItem
import tech.harrynull.invtracker.persistence.DbItemRepo
import tech.harrynull.invtracker.proto.InventoryItem
import tech.harrynull.invtracker.proto.SearchOptions

@RestController
class InventoryApi(
    private val dbItemRepo: DbItemRepo,
) {
    @PostMapping("/inventory/create")
    fun createItem(@RequestParam("item") item: InventoryItem): ResponseEntity<Boolean> {
        dbItemRepo.save(DbItem.create(item))
        return ResponseEntity.ok(true)
    }

    @GetMapping("/inventory/{sku}")
    fun getItem(@PathVariable sku: String): InventoryItem? {
        return dbItemRepo.findBySku(sku)?.toProto()
    }

    @DeleteMapping("/inventory/{sku}")
    fun deleteItem(@PathVariable sku: String): ResponseEntity<Boolean> {
        val dbItem = dbItemRepo.findBySku(sku) ?: return ResponseEntity.notFound().build()
        dbItemRepo.delete(dbItem)
        return ResponseEntity.ok(true)
    }

    @GetMapping("/inventory/")
    fun listInventoryItems(@RequestParam("options") options: SearchOptions?): List<InventoryItem> {
        val items = if (options?.showInactive == true) {
            dbItemRepo.findAll()
        } else {
            dbItemRepo.findAllByActiveIsTrue()
        }
        return items.filter { item ->
            options?.outOfStockOnly != true || item!!.quantity <= 0
        }.map { it!!.toProto() }
    }

    @PostMapping("/inventory/{sku}")
    fun editItem(
        @PathVariable sku: String,
        @RequestParam("item") item: InventoryItem
    ): ResponseEntity<Boolean> {
        val dbItem = dbItemRepo.findBySku(sku) ?: return ResponseEntity.notFound().build()

        item.title?.let { dbItem.title = it }
        item.description?.let { dbItem.description = it }
        item.active?.let { dbItem.active = it }
        item.priceCents?.let { dbItem.priceCents = it }
        item.quantity?.let { dbItem.quantity = it }

        dbItemRepo.save(dbItem)
        return ResponseEntity.ok(true)
    }

    @PostMapping("/inventory/{sku}/quantity")
    fun quantityChange(
        @PathVariable sku: String,
        @RequestParam("delta") delta: Int
    ): ResponseEntity<Boolean> {
        // we allow negative quantity items since they are
        // possible in real-time due to some cross-platform desync
        // or just they want to keep selling when items are out-of-stock
        val dbItem = dbItemRepo.findBySku(sku) ?: return ResponseEntity.notFound().build()
        dbItem.quantity += delta
        dbItemRepo.save(dbItem)
        return ResponseEntity.ok(true)
    }
}
