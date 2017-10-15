package ch.viary.service.mapper;

import ch.viary.domain.*;
import ch.viary.service.dto.AuthorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Author and its DTO AuthorDTO.
 */
@Mapper(componentModel = "spring", uses = {PictureMapper.class, })
public interface AuthorMapper extends EntityMapper <AuthorDTO, Author> {
    
    @Mapping(target = "pictures", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Author toEntity(AuthorDTO authorDTO); 
    default Author fromId(Long id) {
        if (id == null) {
            return null;
        }
        Author author = new Author();
        author.setId(id);
        return author;
    }
}
