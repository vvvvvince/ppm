package ch.viary.repository.search;

import ch.viary.domain.Metadata;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Metadata entity.
 */
public interface MetadataSearchRepository extends ElasticsearchRepository<Metadata, Long> {
}
