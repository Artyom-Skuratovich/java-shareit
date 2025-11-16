package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
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
        final User requestor = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        final ItemRequest saved = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(dto, requestor));
        return ItemRequestMapper.mapToDto(saved);
    }

    @Override
    public List<ItemRequestItemsDto> findAllByUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        final List<ItemRequest> requests = itemRequestRepository.findAllByRequestorId(userId);
        return requests.stream().map(this::toItemRequestItemsDto).toList();
    }

    @Override
    public List<ItemRequestItemsDto> findRequestsOfOthers(long excludedUserId) {
        if (!userRepository.existsById(excludedUserId)) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", excludedUserId));
        }
        final List<ItemRequest> requests = itemRequestRepository.findAllOfOthers(excludedUserId);
        return requests.stream().map(this::toItemRequestItemsDto).toList();
    }

    @Override
    public ItemRequestItemsDto find(long userId, long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        final ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format("Запрос с id='%d' не найден", requestId))
        );
        return toItemRequestItemsDto(request);
    }

    private ItemRequestItemsDto toItemRequestItemsDto(ItemRequest itemRequest) {
        final List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
        return ItemRequestMapper.mapToDtoWithResponses(itemRequest, items);
    }
}