package ch.viary.service.impl;

import ch.viary.service.PictureService;
import ch.viary.domain.Picture;
import ch.viary.repository.PictureRepository;
import ch.viary.repository.search.PictureSearchRepository;
import ch.viary.service.dto.PictureDTO;
import ch.viary.service.mapper.PictureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Picture.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService{

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final PictureRepository pictureRepository;

    private final PictureMapper pictureMapper;

    private final PictureSearchRepository pictureSearchRepository;

    public PictureServiceImpl(PictureRepository pictureRepository, PictureMapper pictureMapper, PictureSearchRepository pictureSearchRepository) {
        this.pictureRepository = pictureRepository;
        this.pictureMapper = pictureMapper;
        this.pictureSearchRepository = pictureSearchRepository;
    }

    /**
     * Save a picture.
     *
     * @param pictureDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PictureDTO save(PictureDTO pictureDTO) {
        log.debug("Request to save Picture : {}", pictureDTO);
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        PictureDTO result = pictureMapper.toDto(picture);
        pictureSearchRepository.save(picture);
        return result;
    }

    /**
     *  Get all the pictures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PictureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll(pageable)
            .map(pictureMapper::toDto);
    }

    /**
     *  Get one picture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PictureDTO findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        Picture picture = pictureRepository.findOneWithEagerRelationships(id);
        return pictureMapper.toDto(picture);
    }

    /**
     *  Delete the  picture by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.delete(id);
        pictureSearchRepository.delete(id);
    }

    /**
     * Search for the picture corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PictureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pictures for query {}", query);
        Page<Picture> result = pictureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(pictureMapper::toDto);
    }
}
