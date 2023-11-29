package fr.polytech.restcontroller;

import fr.polytech.annotation.IsRecruiter;
import fr.polytech.model.JobCategory;
import fr.polytech.service.JobCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job-category")
public class JobCategoryController {

    @Autowired
    private JobCategoryService jobCategoryService;

    /**
     * Get all job categories.
     *
     * @return A list of job categories.
     */
    @GetMapping("/")
    public ResponseEntity<List<JobCategory>> getAllJobCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllJobCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategory> getJobCategoryById(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(jobCategoryService.getJobCategoryById(id));
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a job category.
     *
     * @param jobCategory The job category to create.
     * @return The created job category.
     */
    @IsRecruiter
    @PostMapping("/")
    public ResponseEntity<JobCategory> createJobCategory(@RequestBody JobCategory jobCategory) {
        return ResponseEntity.ok(jobCategoryService.createJobCategory(jobCategory));
    }

}
