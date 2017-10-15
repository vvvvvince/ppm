package ch.viary.service.mapper;

import ch.viary.domain.*;
import ch.viary.service.dto.PictureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Picture and its DTO PictureDTO.
 */
@Mapper(componentModel = "spring", uses = {AlbumMapper.class, AuthorMapper.class, })
public interface PictureMapper extends EntityMapper <PictureDTO, Picture> {

    @Mapping(source = "raw.id", target = "rawId")

    @Mapping(source = "author.id", target = "authorId")
    PictureDTO toDto(Picture picture); 

    @Mapping(source = "rawId", target = "raw")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "metadata", ignore = true)

    @Mapping(source = "authorId", target = "author")
    @Mapping(target = "likers", ignore = true)
    Picture toEntity(PictureDTO pictureDTO); 
    default Picture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setId(id);
        return picture;
    }
}
