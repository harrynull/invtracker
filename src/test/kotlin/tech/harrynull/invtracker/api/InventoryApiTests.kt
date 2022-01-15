package tech.harrynull.invtracker.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import tech.harrynull.invtracker.proto.InventoryItem
import tech.harrynull.invtracker.proto.SearchOptions
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class InventoryApiTests {
    @Autowired
    private lateinit var inventoryApi: InventoryApi

    @Test
    fun `can create get list and export items`() {
        // First create 2 items
        inventoryApi.createItem(
            InventoryItem(
                sku = "testSku",
                title = "test item",
                description = null, // description can be null
                quantity = 0,
                priceCents = 1000,
                active = false,
            )
        )
        inventoryApi.createItem(
            InventoryItem(
                sku = "testSku2",
                title = "test item2",
                description = "test description2",
                quantity = 10,
                priceCents = 1000,
            )
        )
        // List items
        with(inventoryApi.getItem("testSku2")!!) {
            assertThat(sku).isEqualTo("testSku2")
            assertThat(title).isEqualTo("test item2")
            assertThat(description).isEqualTo("test description2")
            assertThat(quantity).isEqualTo(10)
            assertThat(priceCents).isEqualTo(1000)
            assertThat(active).isEqualTo(true)
        }
        assertThat(inventoryApi.getItem("testSku")).isNotNull()
        assertThat(inventoryApi.getItem("non-existent")).isNull()

        // test listing
        assertThat(inventoryApi.listInventoryItems(null).map { it.sku })
            .containsExactlyInAnyOrder("testSku2")
        assertThat(inventoryApi.listInventoryItems(SearchOptions(showInactive = true)).map { it.sku })
            .containsExactlyInAnyOrder("testSku", "testSku2")
        assertThat(
            inventoryApi.listInventoryItems(SearchOptions(showInactive = true, outOfStockOnly = true)).map { it.sku })
            .containsExactlyInAnyOrder("testSku")
        assertThat(
            inventoryApi.listInventoryItems(SearchOptions(showInactive = null, outOfStockOnly = true)).map { it.sku })
            .isEmpty()

        // test exporting
        val export = inventoryApi.exportCsv(response = MockHttpServletResponse())
        assertThat(export).isEqualTo(
            """
            SKU,Active,Quantity,Title,Description,Price
            "testSku","false","0","test item","","10"
            "testSku2","true","10","test item2","test description2","10"
        """.trimIndent())
    }

    @Test
    fun `illegal item creation`() {
        assertThatCode {
            inventoryApi.createItem(
                InventoryItem(
                    sku = "testSku",
                    title = "test item",
                    description = "test",
                    quantity = -5,
                    priceCents = 1000,
                )
            )
        }.hasMessage("quantity cannot be null or negative")
    }

    @Test
    fun `can edit, sell, or stock items`() {
        inventoryApi.createItem(
            InventoryItem(
                sku = "testSku",
                title = "test item",
                quantity = 10,
                priceCents = 1000,
            )
        )
        // then edit
        inventoryApi.editItem(
            sku = "testSku",
            InventoryItem(
                title = "new title",
                description = "new description",
                quantity = 1,
                priceCents = 1,
                active = false
            )
        )
        with(inventoryApi.getItem("testSku")!!) {
            assertThat(sku).isEqualTo("testSku")
            assertThat(title).isEqualTo("new title")
            assertThat(description).isEqualTo("new description")
            assertThat(quantity).isEqualTo(1)
            assertThat(priceCents).isEqualTo(1)
            assertThat(active).isEqualTo(false)
        }
        // stock
        inventoryApi.quantityChange("testSku", 10)
        assertThat(inventoryApi.getItem("testSku")!!.quantity).isEqualTo(11)
        // sell
        inventoryApi.quantityChange("testSku", -5)
        assertThat(inventoryApi.getItem("testSku")!!.quantity).isEqualTo(6)
        // can go negative
        inventoryApi.quantityChange("testSku", -8)
        assertThat(inventoryApi.getItem("testSku")!!.quantity).isEqualTo(-2)
    }

    @Test
    fun `can delete items`() {
        inventoryApi.createItem(
            InventoryItem(
                sku = "testSku",
                title = "test item",
                quantity = 10,
                priceCents = 1000,
            )
        )
        // then edit
        inventoryApi.deleteItem(sku = "testSku")
        // hope gdpr is happy now
        assertThat(inventoryApi.getItem("testSku")).isNull()
        assertThat(inventoryApi.listInventoryItems(options = null)).isEmpty()
    }
}