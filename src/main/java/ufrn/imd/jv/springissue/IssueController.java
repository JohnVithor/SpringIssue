package ufrn.imd.jv.springissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "issues")
public class IssueController {

    private final IssueService service;

    @Autowired
    public IssueController(IssueService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IssueEntity> createIssue(@RequestBody IssueEntity issueEntity) {
        return ResponseEntity.ok(service.save(issueEntity));
    }

    @GetMapping
    public ResponseEntity<Page<IssueEntity>> getPage(
            @RequestParam(name = "pg", required = false) Optional<Integer> page,
            @RequestParam(name = "lim", required = false) Optional<Integer> limit) {
        return service.getPage(page.orElse(0), limit.orElse(10));
    }

    @GetMapping(path = "column/{id}")
    public ResponseEntity<Page<IssueEntity>> getByColumnId(@PathVariable Long id,
                                                           @RequestParam(name = "pg", required = false) Optional<Integer> page,
                                                           @RequestParam(name = "lim", required = false) Optional<Integer> limit) {
        return service.getByColumnId(id, page.orElse(0), limit.orElse(10));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<IssueEntity> getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
