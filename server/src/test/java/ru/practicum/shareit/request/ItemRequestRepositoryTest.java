package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllByRequestorId() {
        User user = new User();
        user.setName("Test User");
        userRepository.save(user);

        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Request 1");
        request1.setRequestor(user);
        request1.setCreated(LocalDateTime.now().minusDays(1));
        itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Request 2");
        request2.setRequestor(user);
        request2.setCreated(LocalDateTime.now());
        itemRequestRepository.save(request2);

        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorId(user.getId());

        assertThat(requests).hasSize(2);
        assertThat(requests.get(0).getDescription()).isEqualTo("Request 2");
        assertThat(requests.get(1).getDescription()).isEqualTo("Request 1");
        requests.forEach(req -> assertThat(req.getRequestor().getId()).isEqualTo(user.getId()));
    }

    @Test
    void testFindAllOfOthers() {
        User user1 = new User();
        user1.setName("User 1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User 2");
        userRepository.save(user2);

        ItemRequest req1 = new ItemRequest();
        req1.setDescription("User1's Request");
        req1.setRequestor(user1);
        req1.setCreated(LocalDateTime.now().minusDays(2));
        itemRequestRepository.save(req1);

        ItemRequest req2 = new ItemRequest();
        req2.setDescription("User2's Request");
        req2.setRequestor(user2);
        req2.setCreated(LocalDateTime.now());
        itemRequestRepository.save(req2);

        List<ItemRequest> othersRequests = itemRequestRepository.findAllOfOthers(user1.getId());

        assertThat(othersRequests).hasSize(1);
        assertThat(othersRequests.getFirst().getDescription()).isEqualTo("User2's Request");
        assertThat(othersRequests.getFirst().getRequestor().getId()).isEqualTo(user2.getId());
    }
}