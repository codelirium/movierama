package io.codelirium.workable.movierama.repository;

import io.codelirium.workable.movierama.model.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;


@Repository
@Transactional
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

}
