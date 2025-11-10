package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequestDto create(long userId, NewItemRequestDto dto) {
        return null;
    }

    @Override
    public List<ItemRequestItemsDto> findAllByUser(long userId) {
        return List.of();
    }

    @Override
    public List<ItemRequestItemsDto> findRequestsOfOthers(long excludedUserId) {
        return List.of();
    }

    @Override
    public ItemRequestItemsDto find(long userId, long requestId) {
        return null;
    }
}