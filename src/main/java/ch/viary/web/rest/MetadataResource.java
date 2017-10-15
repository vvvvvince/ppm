package ch.viary.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.viary.service.MetadataService;
import ch.viary.web.rest.util.HeaderUtil;
import ch.viary.service.dto.MetadataDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Metadata.
 */
@RestController
@RequestMapping("/api")
public class MetadataResource {

    private final Logger log = LoggerFactory.getLogger(MetadataResource.class);

    private static final String ENTITY_NAME = "metadata";

    private final MetadataService metadataService;

    public MetadataResource(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * POST  /metadata : Create a new metadata.
     *
     * @param metadataDTO the metadataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metadataDTO, or with status 400 (Bad Request) if the metadata has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metadata")
    @Timed
    public ResponseEntity<MetadataDTO> createMetadata(@RequestBody MetadataDTO metadataDTO) throws URISyntaxException {
        log.debug("REST request to save Metadata : {}", metadataDTO);
        if (metadataDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new metadata cannot already have an ID")).body(null);
        }
        MetadataDTO result = metadataService.save(metadataDTO);
        return ResponseEntity.created(new URI("/api/metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metadata : Updates an existing metadata.
     *
     * @param metadataDTO the metadataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metadataDTO,
     * or with status 400 (Bad Request) if the metadataDTO is not valid,
     * or with status 500 (Internal Server Error) if the metadataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metadata")
    @Timed
    public ResponseEntity<MetadataDTO> updateMetadata(@RequestBody MetadataDTO metadataDTO) throws URISyntaxException {
        log.debug("REST request to update Metadata : {}", metadataDTO);
        if (metadataDTO.getId() == null) {
            return createMetadata(metadataDTO);
        }
        MetadataDTO result = metadataService.save(metadataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metadata : get all the metadata.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
     */
    @GetMapping("/metadata")
    @Timed
    public List<MetadataDTO> getAllMetadata() {
        log.debug("REST request to get all Metadata");
        return metadataService.findAll();
        }

    /**
     * GET  /metadata/:id : get the "id" metadata.
     *
     * @param id the id of the metadataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metadataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/metadata/{id}")
    @Timed
    public ResponseEntity<MetadataDTO> getMetadata(@PathVariable Long id) {
        log.debug("REST request to get Metadata : {}", id);
        MetadataDTO metadataDTO = metadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metadataDTO));
    }

    /**
     * DELETE  /metadata/:id : delete the "id" metadata.
     *
     * @param id the id of the metadataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metadata/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetadata(@PathVariable Long id) {
        log.debug("REST request to delete Metadata : {}", id);
        metadataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/metadata?query=:query : search for the metadata corresponding
     * to the query.
     *
     * @param query the query of the metadata search
     * @return the result of the search
     */
    @GetMapping("/_search/metadata")
    @Timed
    public List<MetadataDTO> searchMetadata(@RequestParam String query) {
        log.debug("REST request to search Metadata for query {}", query);
        return metadataService.search(query);
    }

}
