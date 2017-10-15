package ch.viary.service;

import ch.viary.service.dto.AlbumDTO;
import java.util.List;

/**
 * Service Interface for managing Album.
 */
public interface AlbumService {

    /**
     * Save a album.
     *
     * @param albumDTO the entity to save
     * @return the persisted entity
     */
    AlbumDTO save(AlbumDTO albumDTO);

    /**
     *  Get all the albums.
     *
     *  @return the list of entities
     */
    List<AlbumDTO> findAll();

    /**
     *  Get the "id" album.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AlbumDTO findOne(Long id);

    /**
     *  Delete the "id" album.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the album corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<AlbumDTO> search(String query);
}
