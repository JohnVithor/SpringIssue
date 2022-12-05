package ufrn.imd.jv.springissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class IssueService {
    private final UserServiceInterface userService;
    private final ColumnServiceInterface columnService;
    private final IssueRepository repository;

    @Autowired
    public IssueService(UserServiceInterface userServiceInterface,
                        ColumnServiceInterface columnServiceInterface,
                        IssueRepository repository) {
        this.userService = userServiceInterface;
        this.columnService = columnServiceInterface;
        this.repository = repository;
    }

    public IssueEntity save(IssueEntity issueEntity) {
        if (issueEntity == null) {
            throw new RuntimeException("Entidade não informada");
        }
        if (issueEntity.getUserId() == null) {
            throw new RuntimeException("Usuário não informado");
        }
        ResponseEntity<Map<String, String>> responseUser = userService.getUser(issueEntity.getUserId());
        if (!responseUser.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Usuário informado não existe");
        }

        if (issueEntity.getColumnId() == null) {
            throw new RuntimeException("Coluna não informada");
        }
        ResponseEntity<Map<String, String>> responseColumn = columnService.getColumn(issueEntity.getColumnId());
        if (!responseColumn.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Coluna informada não existe");
        }
        if (issueEntity.getName() == null) {
            throw new RuntimeException("Nome da issue não informada");
        }
        if (issueEntity.getName().trim().equals("")) {
            throw new RuntimeException("Nome da issue informada é inválido");
        }
        Optional<IssueEntity> optValue = repository.findByNameAndColumnId(issueEntity.getName(), issueEntity.getColumnId());
        if (optValue.isPresent()) {
            throw new RuntimeException("Nome da issue já está em uso nessa coluna");
        }
        return repository.save(issueEntity);
    }

    public ResponseEntity<Page<IssueEntity>> getPage(int page, int limit) {
        return ResponseEntity.ok(repository.findAll(PageRequest.of(page, limit)));
    }

    public ResponseEntity<Page<IssueEntity>> getByColumnId(Long id, int page, int limit) {
        return ResponseEntity.ok(repository.findByColumnIdIs(id, PageRequest.of(page, limit)));
    }

    public ResponseEntity<IssueEntity> getById(Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }
}
