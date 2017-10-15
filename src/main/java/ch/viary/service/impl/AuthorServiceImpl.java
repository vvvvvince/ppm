package ch.viary.service.impl;

import ch.viary.service.AuthorService;
import ch.viary.domain.Author;
import ch.viary.repository.AuthorRepository;
import ch.viary.repository.search.AuthorSearchRepository;
import ch.viary.service.dto.AuthorDTO;
import ch.viary.service.mapper.AuthorMapper;
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
 * Service Implementation for managing Author.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService{

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final AuthorSearchRepository authorSearchRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, AuthorSearchRepository authorSearchRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.authorSearchRepository = authorSearchRepository;
    }

    /**
     * Save a author.
     *
     * @param authorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AuthorDTO save(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        AuthorDTO result = authorMapper.toDto(author);
        authorSearchRepository.save(author);
        return result;
    }

    /**
     *  Get all the authors.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        log.debug("Request to get all Authors");
        return authorRepository.findAllWithEagerRelationships().stream()
            .map(authorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one author by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AuthorDTO findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        Author author = authorRepository.findOneWithEagerRelationships(id);
        return authorMapper.toDto(author);
    }

    /**
     *  Delete the  author by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.delete(id);
        authorSearchRepository.delete(id);
    }

    /**
     * Search for the author corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> search(String query) {
        log.debug("Request to search Authors for query {}", query);
        return StreamSupport
            .stream(authorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(authorMapper::toDto)
            .collect(Collectors.toList());
    }
}
