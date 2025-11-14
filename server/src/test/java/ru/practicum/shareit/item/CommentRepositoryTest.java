package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item;

    @BeforeEach
    public void setUp() {
        User author = new User();
        author.setName("Автор");
        author.setEmail("author@example.com");
        author = userRepository.save(author);

        item = new Item();
        item.setName("Инструмент");
        item.setDescription("Молоток для ремонта");
        item = itemRepository.save(item);

        Comment comment1 = new Comment();
        comment1.setText("Отличный инструмент!");
        comment1.setAuthor(author);
        comment1.setItem(item);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Работает хорошо");
        comment2.setAuthor(author);
        comment2.setItem(item);
        commentRepository.save(comment2);
    }

    @Test
    public void findCommentsByItemIdShouldReturnComments() {
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());

        assertNotNull(comments);
        assertFalse(comments.isEmpty());

        for (Comment comment : comments) {
            assertEquals(item.getId(), comment.getItem().getId());
        }

        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Отличный инструмент!")));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Работает хорошо")));
    }

    @Test
    public void findCommentsByItemIdShouldReturnEmptyIfNoComments() {
        Item newItem = new Item();
        newItem.setName("Пила");
        newItem.setDescription("Ручная пила");
        newItem = itemRepository.save(newItem);

        List<Comment> comments = commentRepository.findCommentsByItemId(newItem.getId());

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }
}