package ufrn.imd.jv.springissue;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IssueResilience {
    private final UserServiceInterface userService;
    private final ColumnServiceInterface columnService;
    private final IssueRepository repository;

    @Autowired
    public IssueResilience(UserServiceInterface userService,
                           ColumnServiceInterface columnService,
                           IssueRepository repository) {
        this.userService = userService;
        this.columnService = columnService;
        this.repository = repository;
    }

    @CircuitBreaker(name = "isUserValid_cb", fallbackMethod = "isUserKnown")
    @Bulkhead(name = "isUserValid_bh", fallbackMethod = "isUserKnown")
    public boolean isUserValid(Long id) {
        ResponseEntity<Map<String, String>> response = userService.getUser(id);
        return response.getStatusCode().is2xxSuccessful();
    }

    @CircuitBreaker(name = "isColumnValid_cb", fallbackMethod = "isColumnKnown")
    @Bulkhead(name = "isColumnValid_bh", fallbackMethod = "isColumnKnown")
    public boolean isColumnValid(Long id) {
        ResponseEntity<Map<String, String>> response = columnService.getColumn(id);
        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean isUserKnown(Long id, Throwable t) {
        System.err.println(
                "Não foi possível consultar o service de usuários devido a: " +
                        t.getMessage() +
                        "Consultando issues locais em busca do usuário"
        );
        if (repository.existsByUserId(id)) {
            System.err.println("Usuário foi encontrado, portanto é válido");
            return true;
        } else {
            System.err.println("Não foi encontrada issue criada pelo usuário de id="+id);
            return false;
        }
    }

    public boolean isColumnKnown(Long id, Throwable t) {
        System.err.println(
                "Não foi possível consultar o service de columns devido a: " +
                        t.getMessage() +
                        "Consultando issues locais em busca da column"
        );
        if (repository.existsByColumnId(id)) {
            System.err.println("Column foi encontrado, portanto é válida");
            return true;
        } else {
            System.err.println("Não foi encontrada issue associado a column de id="+id);
            return false;
        }
    }
}
