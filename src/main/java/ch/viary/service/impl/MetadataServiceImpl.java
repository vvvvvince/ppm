package ch.viary.service.impl;

import ch.viary.service.MetadataService;
import ch.viary.domain.Metadata;
import ch.viary.repository.MetadataRepository;
import ch.viary.repository.search.MetadataSearchRepository;
import ch.viary.service.dto.MetadataDTO;
import ch.viary.service.mapper.MetadataMapper;
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
 * Service Implementation for managing Metadata.
 */
@Service
@Transactional
public class MetadataServiceImpl implements MetadataService{

    private final Logger log = LoggerFactory.getLogger(MetadataServiceImpl.class);

    private final MetadataRepository metadataRepository;

    private final MetadataMapper metadataMapper;

    private final MetadataSearchRepository metadataSearchRepository;

    public MetadataServiceImpl(MetadataRepository metadataRepository, MetadataMapper metadataMapper, MetadataSearchRepository metadataSearchRepository) {
        this.metadataRepository = metadataRepository;
        this.metadataMapper = metadataMapper;
        this.metadataSearchRepository = metadataSearchRepository;
    }

    /**
     * Save a metadata.
     *
     * @param metadataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MetadataDTO save(MetadataDTO metadataDTO) {
        log.debug("Request to save Metadata : {}", metadataDTO);
        Metadata metadata = metadataMapper.toEntity(metadataDTO);
        metadata = metadataRepository.save(metadata);
        MetadataDTO result = metadataMapper.toDto(metadata);
        metadataSearchRepository.save(metadata);
        return result;
    }

    /**
     *  Get all the metadata.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MetadataDTO> findAll() {
        log.debug("Request to get all Metadata");
        return metadataRepository.findAll().stream()
            .map(metadataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one metadata by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MetadataDTO findOne(Long id) {
        log.debug("Request to get Metadata : {}", id);
        Metadata metadata = metadataRepository.findOne(id);
        return metadataMapper.toDto(metadata);
    }

    /**
     *  Delete the  metadata by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Metadata : {}", id);
        metadataRepository.delete(id);
        metadataSearchRepository.delete(id);
    }

    /**
     * Search for the metadata corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MetadataDTO> search(String query) {
        log.debug("Request to search Metadata for query {}", query);
        return StreamSupport
            .stream(metadataSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(metadataMapper::toDto)
            .collect(Collectors.toList());
    }
}
