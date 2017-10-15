package ch.viary.service.mapper;

import ch.viary.domain.*;
import ch.viary.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class, PictureMapper.class, })
public interface CommentMapper extends EntityMapper <CommentDTO, Comment> {

    @Mapping(source = "author.id", target = "authorId")

    @Mapping(source = "picture.id", target = "pictureId")
    CommentDTO toDto(Comment comment); 

    @Mapping(source = "authorId", target = "author")

    @Mapping(source = "pictureId", target = "picture")
    Comment toEntity(CommentDTO commentDTO); 
    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
