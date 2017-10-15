package ch.viary.service.mapper;

import ch.viary.domain.*;
import ch.viary.service.dto.MetadataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Metadata and its DTO MetadataDTO.
 */
@Mapper(componentModel = "spring", uses = {PictureMapper.class, })
public interface MetadataMapper extends EntityMapper <MetadataDTO, Metadata> {

    @Mapping(source = "picture.id", target = "pictureId")
    MetadataDTO toDto(Metadata metadata); 

    @Mapping(source = "pictureId", target = "picture")
    Metadata toEntity(MetadataDTO metadataDTO); 
    default Metadata fromId(Long id) {
        if (id == null) {
            return null;
        }
        Metadata metadata = new Metadata();
        metadata.setId(id);
        return metadata;
    }
}
