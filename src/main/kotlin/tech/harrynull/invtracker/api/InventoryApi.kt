package tech.harrynull.invtracker.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.harrynull.invtracker.persistence.DbItem
import tech.harrynull.invtracker.persistence.DbItemRepo
import tech.harrynull.invtracker.proto.InventoryItem
import tech.harrynull.invtracker.proto.SearchOptions
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse

@RestController
class InventoryApi(
    private val dbItemRepo: DbItemRepo,
) {
    @PostMapping("/inventory")
    fun createItem(@RequestBody item: InventoryItem): ResponseEntity<Boolean> {
        val sku = item.sku ?: throw IllegalArgumentException("sku not provided")
        // prevent sku == list because /list is used as an endpoint
        // not a super elegant solution though.
        if (sku == "list") throw IllegalArgumentException("sku cannot be named list")
        if (dbItemRepo.findBySku(sku) != null) {
            throw IllegalArgumentException("Item with sku ${item.sku} already existed.")
        }
        dbItemRepo.save(DbItem.create(item))
        return ResponseEntity.ok(true)
    }

    @GetMapping("/inventory/{sku}")
    fun getItem(@PathVariable sku: String): InventoryItem? {
        return dbItemRepo.findBySku(sku)?.toProto()
    }

    @GetMapping("/export_csv", produces = ["text/csv"])
    fun exportCsv(response: HttpServletResponse): String {
        val items = dbItemRepo.findAll()
        val header = listOf("SKU", "Active", "Quantity", "Title", "Description", "Price").joinToString(",")
        val rows =
            items.joinToString("\n") {
                listOf(it!!.sku, it.active, it.quantity, it.title, it.description, it.priceCents / 100).map {
                    '"' + it.toString().replace("\"", "\\\"") + '"' // escape " and wrap with ""
                }.joinToString(",")
            }

        val currentDate = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
        response.setHeader("Content-disposition", "attachment; filename=inventory_$currentDate.csv")

        return header + "\n" + rows
    }

    @DeleteMapping("/inventory/{sku}")
    fun deleteItem(@PathVariable sku: String): ResponseEntity<Boolean> {
        val dbItem = dbItemRepo.findBySku(sku) ?: return ResponseEntity.notFound().build()
        dbItemRepo.delete(dbItem)
        return ResponseEntity.ok(true)
    }

    @PostMapping("/list_inventory") // must be post because it has body
    fun listInventoryItems(@RequestBody options: SearchOptions?): List<InventoryItem> {
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
        @RequestBody item: InventoryItem
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

    @PostMapping("/inventory/{sku}/quantity_delta")
    fun quantityChange(
        @PathVariable sku: String,
        @RequestBody delta: Int
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
