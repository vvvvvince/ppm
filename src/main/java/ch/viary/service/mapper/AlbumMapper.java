package ch.viary.service.mapper;

import ch.viary.domain.*;
import ch.viary.service.dto.AlbumDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Album and its DTO AlbumDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlbumMapper extends EntityMapper <AlbumDTO, Album> {
    
    @Mapping(target = "picures", ignore = true)
    Album toEntity(AlbumDTO albumDTO); 
    default Album fromId(Long id) {
        if (id == null) {
            return null;
        }
        Album album = new Album();
        album.setId(id);
        return album;
    }
}
