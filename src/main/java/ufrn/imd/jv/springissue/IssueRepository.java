package ufrn.imd.jv.springissue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

    Page<IssueEntity> findByColumnIdIs(Long id, PageRequest pageRequest);

    Optional<IssueEntity> findByNameAndColumnId(String name, Long id);

    boolean existsByUserId(Long id);

    boolean existsByColumnId(Long id);
}
