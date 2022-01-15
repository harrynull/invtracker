// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: inventory.proto
package tech.harrynull.invtracker.proto

import com.squareup.wire.FieldEncoding
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import com.squareup.wire.ProtoReader
import com.squareup.wire.ProtoWriter
import com.squareup.wire.WireField
import com.squareup.wire.internal.sanitize
import kotlin.Any
import kotlin.AssertionError
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.DeprecationLevel
import kotlin.Int
import kotlin.Nothing
import kotlin.String
import kotlin.hashCode
import kotlin.jvm.JvmField
import okio.ByteString

class InventoryItem(
  /**
   * unique identifier of the item
   */
  @field:WireField(
    tag = 1,
    adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  val sku: String? = null,
  @field:WireField(
    tag = 2,
    adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  val title: String? = null,
  @field:WireField(
    tag = 3,
    adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  val description: String? = null,
  @field:WireField(
    tag = 4,
    adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  val quantity: Int? = null,
  @field:WireField(
    tag = 5,
    adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  val priceCents: Int? = null,
  @field:WireField(
    tag = 6,
    adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  val active: Boolean? = null,
  unknownFields: ByteString = ByteString.EMPTY
) : Message<InventoryItem, Nothing>(ADAPTER, unknownFields) {
  @Deprecated(
    message = "Shouldn't be used in Kotlin",
    level = DeprecationLevel.HIDDEN
  )
  override fun newBuilder(): Nothing = throw AssertionError()

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is InventoryItem) return false
    return unknownFields == other.unknownFields
        && sku == other.sku
        && title == other.title
        && description == other.description
        && quantity == other.quantity
        && priceCents == other.priceCents
        && active == other.active
  }

  override fun hashCode(): Int {
    var result = super.hashCode
    if (result == 0) {
      result = unknownFields.hashCode()
      result = result * 37 + sku.hashCode()
      result = result * 37 + title.hashCode()
      result = result * 37 + description.hashCode()
      result = result * 37 + quantity.hashCode()
      result = result * 37 + priceCents.hashCode()
      result = result * 37 + active.hashCode()
      super.hashCode = result
    }
    return result
  }

  override fun toString(): String {
    val result = mutableListOf<String>()
    if (sku != null) result += """sku=${sanitize(sku)}"""
    if (title != null) result += """title=${sanitize(title)}"""
    if (description != null) result += """description=${sanitize(description)}"""
    if (quantity != null) result += """quantity=$quantity"""
    if (priceCents != null) result += """priceCents=$priceCents"""
    if (active != null) result += """active=$active"""
    return result.joinToString(prefix = "InventoryItem{", separator = ", ", postfix = "}")
  }

  fun copy(
    sku: String? = this.sku,
    title: String? = this.title,
    description: String? = this.description,
    quantity: Int? = this.quantity,
    priceCents: Int? = this.priceCents,
    active: Boolean? = this.active,
    unknownFields: ByteString = this.unknownFields
  ): InventoryItem = InventoryItem(sku, title, description, quantity, priceCents, active,
      unknownFields)

  companion object {
    @JvmField
    val ADAPTER: ProtoAdapter<InventoryItem> = object : ProtoAdapter<InventoryItem>(
      FieldEncoding.LENGTH_DELIMITED, 
      InventoryItem::class, 
      "type.googleapis.com/tech.harrynull.invtracker.proto.InventoryItem"
    ) {
      override fun encodedSize(value: InventoryItem): Int = 
        ProtoAdapter.STRING.encodedSizeWithTag(1, value.sku) +
        ProtoAdapter.STRING.encodedSizeWithTag(2, value.title) +
        ProtoAdapter.STRING.encodedSizeWithTag(3, value.description) +
        ProtoAdapter.INT32.encodedSizeWithTag(4, value.quantity) +
        ProtoAdapter.INT32.encodedSizeWithTag(5, value.priceCents) +
        ProtoAdapter.BOOL.encodedSizeWithTag(6, value.active) +
        value.unknownFields.size

      override fun encode(writer: ProtoWriter, value: InventoryItem) {
        ProtoAdapter.STRING.encodeWithTag(writer, 1, value.sku)
        ProtoAdapter.STRING.encodeWithTag(writer, 2, value.title)
        ProtoAdapter.STRING.encodeWithTag(writer, 3, value.description)
        ProtoAdapter.INT32.encodeWithTag(writer, 4, value.quantity)
        ProtoAdapter.INT32.encodeWithTag(writer, 5, value.priceCents)
        ProtoAdapter.BOOL.encodeWithTag(writer, 6, value.active)
        writer.writeBytes(value.unknownFields)
      }

      override fun decode(reader: ProtoReader): InventoryItem {
        var sku: String? = null
        var title: String? = null
        var description: String? = null
        var quantity: Int? = null
        var priceCents: Int? = null
        var active: Boolean? = null
        val unknownFields = reader.forEachTag { tag ->
          when (tag) {
            1 -> sku = ProtoAdapter.STRING.decode(reader)
            2 -> title = ProtoAdapter.STRING.decode(reader)
            3 -> description = ProtoAdapter.STRING.decode(reader)
            4 -> quantity = ProtoAdapter.INT32.decode(reader)
            5 -> priceCents = ProtoAdapter.INT32.decode(reader)
            6 -> active = ProtoAdapter.BOOL.decode(reader)
            else -> reader.readUnknownField(tag)
          }
        }
        return InventoryItem(
          sku = sku,
          title = title,
          description = description,
          quantity = quantity,
          priceCents = priceCents,
          active = active,
          unknownFields = unknownFields
        )
      }

      override fun redact(value: InventoryItem): InventoryItem = value.copy(
        unknownFields = ByteString.EMPTY
      )
    }
  }
}
