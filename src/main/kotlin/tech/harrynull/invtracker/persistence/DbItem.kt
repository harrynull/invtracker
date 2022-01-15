package tech.harrynull.invtracker.persistence

import tech.harrynull.invtracker.proto.InventoryItem
import javax.persistence.*

@Entity(name = "item")
@Table(
    name = "items", indexes = [
        Index(name = "idx_sku", columnList = "sku"),
        Index(name = "idx_title", columnList = "title"),
        Index(name = "idx_quantity", columnList = "quantity"),
    ]
)
data class DbItem(
    @Column
    var sku: String,
    @Column
    var title: String,
    @Column
    var description: String,
    @Column
    var quantity: Int,
    @Column
    var priceCents: Int,
    @Column
    var active: Boolean = true,
    @Id @GeneratedValue
    var id: Long? = null
) {
    fun toProto(): InventoryItem {
        return InventoryItem(
            sku = sku,
            title = title,
            description = description,
            quantity = quantity,
            priceCents = priceCents,
            active = active,
        )
    }

    companion object {
        // the caller is responsible for store the db object
        fun create(item: InventoryItem): DbItem {
            return DbItem(
                sku = item.sku ?: throw IllegalArgumentException("sku cannot be null"),
                title = item.title ?: throw IllegalArgumentException("title cannot be null"),
                description = item.description ?: "",
                quantity = item.quantity?.takeIf { it > 0 } ?: throw IllegalArgumentException("quantity illegal"),
                priceCents = item.priceCents ?: throw IllegalArgumentException("price cannot be null")
            )
        }
    }
}

