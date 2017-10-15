package ch.viary.web.rest;

import ch.viary.PpmApp;

import ch.viary.domain.Metadata;
import ch.viary.repository.MetadataRepository;
import ch.viary.service.MetadataService;
import ch.viary.repository.search.MetadataSearchRepository;
import ch.viary.service.dto.MetadataDTO;
import ch.viary.service.mapper.MetadataMapper;
import ch.viary.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.viary.domain.enumeration.MetadataType;
/**
 * Test class for the MetadataResource REST controller.
 *
 * @see MetadataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PpmApp.class)
public class MetadataResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final MetadataType DEFAULT_TYPE = MetadataType.TAG;
    private static final MetadataType UPDATED_TYPE = MetadataType.EXIF;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private MetadataSearchRepository metadataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetadataMockMvc;

    private Metadata metadata;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetadataResource metadataResource = new MetadataResource(metadataService);
        this.restMetadataMockMvc = MockMvcBuilders.standaloneSetup(metadataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metadata createEntity(EntityManager em) {
        Metadata metadata = new Metadata()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .type(DEFAULT_TYPE);
        return metadata;
    }

    @Before
    public void initTest() {
        metadataSearchRepository.deleteAll();
        metadata = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetadata() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);
        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isCreated());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeCreate + 1);
        Metadata testMetadata = metadataList.get(metadataList.size() - 1);
        assertThat(testMetadata.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testMetadata.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMetadata.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Metadata in Elasticsearch
        Metadata metadataEs = metadataSearchRepository.findOne(testMetadata.getId());
        assertThat(metadataEs).isEqualToComparingFieldByField(testMetadata);
    }

    @Test
    @Transactional
    public void createMetadataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata with an existing ID
        metadata.setId(1L);
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        // Get all the metadataList
        restMetadataMockMvc.perform(get("/api/metadata?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadata/{id}", metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metadata.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetadata() throws Exception {
        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadata/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);
        metadataSearchRepository.save(metadata);
        int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Update the metadata
        Metadata updatedMetadata = metadataRepository.findOne(metadata.getId());
        updatedMetadata
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .type(UPDATED_TYPE);
        MetadataDTO metadataDTO = metadataMapper.toDto(updatedMetadata);

        restMetadataMockMvc.perform(put("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isOk());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeUpdate);
        Metadata testMetadata = metadataList.get(metadataList.size() - 1);
        assertThat(testMetadata.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testMetadata.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMetadata.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Metadata in Elasticsearch
        Metadata metadataEs = metadataSearchRepository.findOne(testMetadata.getId());
        assertThat(metadataEs).isEqualToComparingFieldByField(testMetadata);
    }

    @Test
    @Transactional
    public void updateNonExistingMetadata() throws Exception {
        int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Create the Metadata
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetadataMockMvc.perform(put("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isCreated());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);
        metadataSearchRepository.save(metadata);
        int databaseSizeBeforeDelete = metadataRepository.findAll().size();

        // Get the metadata
        restMetadataMockMvc.perform(delete("/api/metadata/{id}", metadata.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean metadataExistsInEs = metadataSearchRepository.exists(metadata.getId());
        assertThat(metadataExistsInEs).isFalse();

        // Validate the database is empty
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);
        metadataSearchRepository.save(metadata);

        // Search the metadata
        restMetadataMockMvc.perform(get("/api/_search/metadata?query=id:" + metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metadata.class);
        Metadata metadata1 = new Metadata();
        metadata1.setId(1L);
        Metadata metadata2 = new Metadata();
        metadata2.setId(metadata1.getId());
        assertThat(metadata1).isEqualTo(metadata2);
        metadata2.setId(2L);
        assertThat(metadata1).isNotEqualTo(metadata2);
        metadata1.setId(null);
        assertThat(metadata1).isNotEqualTo(metadata2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetadataDTO.class);
        MetadataDTO metadataDTO1 = new MetadataDTO();
        metadataDTO1.setId(1L);
        MetadataDTO metadataDTO2 = new MetadataDTO();
        assertThat(metadataDTO1).isNotEqualTo(metadataDTO2);
        metadataDTO2.setId(metadataDTO1.getId());
        assertThat(metadataDTO1).isEqualTo(metadataDTO2);
        metadataDTO2.setId(2L);
        assertThat(metadataDTO1).isNotEqualTo(metadataDTO2);
        metadataDTO1.setId(null);
        assertThat(metadataDTO1).isNotEqualTo(metadataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(metadataMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(metadataMapper.fromId(null)).isNull();
    }
}
