package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;

    @BeforeEach
    public void setUp() {
        owner = new User();
        owner.setName("Владелец");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        Item item1 = new Item();
        item1.setName("Молоток");
        item1.setDescription("Кирпичный молоток");
        item1.setAvailable(true);
        item1.setOwner(owner);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Пила");
        item2.setDescription("Ручная пила");
        item2.setAvailable(false);
        item2.setOwner(owner);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setName("Лобзик");
        item3.setDescription("Электро лобзик для резки");
        item3.setAvailable(true);
        item3.setOwner(owner);
        itemRepository.save(item3);
    }

    @Test
    public void findAllByOwnerIdShouldReturnItems() {
        List<Item> items = itemRepository.findAllByOwnerId(owner.getId());

        assertNotNull(items);
        assertFalse(items.isEmpty());
        for (Item item : items) {
            assertEquals(owner.getId(), item.getOwner().getId());
        }
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("Молоток")));
    }

    @Test
    public void searchShouldReturnMatchingItems() {
        String searchText = "лобзик";

        List<Item> results = itemRepository.search(searchText);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.getFirst().getName().toLowerCase().contains(searchText));
        assertTrue(results.getFirst().getDescription().toLowerCase().contains(searchText));
        assertTrue(results.getFirst().isAvailable());
    }

    @Test
    public void findAllByRequestIdShouldReturnItems() {
        long requestId = 1L;

        List<Item> itemsByRequest = itemRepository.findAllByRequestId(requestId);

        assertNotNull(itemsByRequest);
        for (Item item : itemsByRequest) {
            assertEquals(requestId, item.getRequest().getId());
        }
    }
}
