package tech.harrynull.invtracker.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import tech.harrynull.invtracker.persistence.DbItemRepo
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class InventoryApiTests {
    @Autowired
    private lateinit var dbItemRepo: DbItemRepo

    @Test
    fun `can create, list and view items`() {

    }


}