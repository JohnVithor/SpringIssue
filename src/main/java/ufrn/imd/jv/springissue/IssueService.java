package ufrn.imd.jv.springissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class IssueService {

    @Value("${service.user}")
    private String userService;

    @Value("${service.column}")
    private String columnService;

    private final IssueRepository repository;

    private final RestTemplate restTemplate;

    @Autowired
    public IssueService(IssueRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public boolean entidadeEhValida(String path, Long id) {
        ResponseEntity<String> response = restTemplate.exchange(
                path+"/"+id,
                HttpMethod.GET,
                null,
                String.class);
        return response.getStatusCode().is2xxSuccessful();
    }

    public IssueEntity save(IssueEntity issueEntity) {
        if(issueEntity == null) {
            throw new RuntimeException("Entidade não informada");
        }

        if (issueEntity.getUserId() == null) {
            throw new RuntimeException("Usuário não informado");
        }
        if(!entidadeEhValida(userService, issueEntity.getUserId())) {
            throw new RuntimeException("Usuário informado não existe");
        }

        if (issueEntity.getColumnId() == null) {
            throw new RuntimeException("Coluna não informada");
        }
        if(!entidadeEhValida(columnService, issueEntity.getColumnId())) {
            throw new RuntimeException("Coluna informada não existe");
        }

        if (issueEntity.getName() == null) {
            throw new RuntimeException("Nome da issue não informada");
        }
        if(!issueEntity.getName().trim().equals("")) {
            throw new RuntimeException("Nome da issue informada é inválido");
        }
        Optional<IssueEntity> optValue = repository.findByNameAndColumnId(issueEntity.getName(), issueEntity.getColumnId());
        if(optValue.isPresent()) {
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
