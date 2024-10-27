package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toEntity(CommentDto commentDto);

    @Mappings({
            @Mapping(source = "author.name", target = "authorName"),
            @Mapping(source = "item.name", target = "itemName")
    })
    CommentDto toDto(Comment comment);
}
