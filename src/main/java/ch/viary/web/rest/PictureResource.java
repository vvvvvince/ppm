package ch.viary.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.viary.service.PictureService;
import ch.viary.web.rest.util.HeaderUtil;
import ch.viary.web.rest.util.PaginationUtil;
import ch.viary.service.dto.PictureDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Picture.
 */
@RestController
@RequestMapping("/api")
public class PictureResource {

    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    private static final String ENTITY_NAME = "picture";

    private final PictureService pictureService;

    public PictureResource(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    /**
     * POST  /pictures : Create a new picture.
     *
     * @param pictureDTO the pictureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pictureDTO, or with status 400 (Bad Request) if the picture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pictures")
    @Timed
    public ResponseEntity<PictureDTO> createPicture(@RequestBody PictureDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to save Picture : {}", pictureDTO);
        if (pictureDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new picture cannot already have an ID")).body(null);
        }
        PictureDTO result = pictureService.save(pictureDTO);
        return ResponseEntity.created(new URI("/api/pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pictures : Updates an existing picture.
     *
     * @param pictureDTO the pictureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pictureDTO,
     * or with status 400 (Bad Request) if the pictureDTO is not valid,
     * or with status 500 (Internal Server Error) if the pictureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pictures")
    @Timed
    public ResponseEntity<PictureDTO> updatePicture(@RequestBody PictureDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to update Picture : {}", pictureDTO);
        if (pictureDTO.getId() == null) {
            return createPicture(pictureDTO);
        }
        PictureDTO result = pictureService.save(pictureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pictureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pictures : get all the pictures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pictures in body
     */
    @GetMapping("/pictures")
    @Timed
    public ResponseEntity<List<PictureDTO>> getAllPictures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Pictures");
        Page<PictureDTO> page = pictureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pictures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pictures/:id : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pictureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pictures/{id}")
    @Timed
    public ResponseEntity<PictureDTO> getPicture(@PathVariable Long id) {
        log.debug("REST request to get Picture : {}", id);
        PictureDTO pictureDTO = pictureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pictureDTO));
    }

    /**
     * DELETE  /pictures/:id : delete the "id" picture.
     *
     * @param id the id of the pictureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pictures/{id}")
    @Timed
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        log.debug("REST request to delete Picture : {}", id);
        pictureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pictures?query=:query : search for the picture corresponding
     * to the query.
     *
     * @param query the query of the picture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pictures")
    @Timed
    public ResponseEntity<List<PictureDTO>> searchPictures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Pictures for query {}", query);
        Page<PictureDTO> page = pictureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pictures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
