package ch.viary.service.impl;

import ch.viary.service.AlbumService;
import ch.viary.domain.Album;
import ch.viary.repository.AlbumRepository;
import ch.viary.repository.search.AlbumSearchRepository;
import ch.viary.service.dto.AlbumDTO;
import ch.viary.service.mapper.AlbumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Album.
 */
@Service
@Transactional
public class AlbumServiceImpl implements AlbumService{

    private final Logger log = LoggerFactory.getLogger(AlbumServiceImpl.class);

    private final AlbumRepository albumRepository;

    private final AlbumMapper albumMapper;

    private final AlbumSearchRepository albumSearchRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, AlbumMapper albumMapper, AlbumSearchRepository albumSearchRepository) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.albumSearchRepository = albumSearchRepository;
    }

    /**
     * Save a album.
     *
     * @param albumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AlbumDTO save(AlbumDTO albumDTO) {
        log.debug("Request to save Album : {}", albumDTO);
        Album album = albumMapper.toEntity(albumDTO);
        album = albumRepository.save(album);
        AlbumDTO result = albumMapper.toDto(album);
        albumSearchRepository.save(album);
        return result;
    }

    /**
     *  Get all the albums.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlbumDTO> findAll() {
        log.debug("Request to get all Albums");
        return albumRepository.findAll().stream()
            .map(albumMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one album by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AlbumDTO findOne(Long id) {
        log.debug("Request to get Album : {}", id);
        Album album = albumRepository.findOne(id);
        return albumMapper.toDto(album);
    }

    /**
     *  Delete the  album by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Album : {}", id);
        albumRepository.delete(id);
        albumSearchRepository.delete(id);
    }

    /**
     * Search for the album corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlbumDTO> search(String query) {
        log.debug("Request to search Albums for query {}", query);
        return StreamSupport
            .stream(albumSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(albumMapper::toDto)
            .collect(Collectors.toList());
    }
}
