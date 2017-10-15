package ch.viary.repository;

import ch.viary.domain.Picture;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Picture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Query("select distinct picture from Picture picture left join fetch picture.albums")
    List<Picture> findAllWithEagerRelationships();

    @Query("select picture from Picture picture left join fetch picture.albums where picture.id =:id")
    Picture findOneWithEagerRelationships(@Param("id") Long id);

}
