package fr.polytech.restcontroller;

import fr.polytech.model.JobCategory;
import fr.polytech.service.JobCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/job-category")
public class JobCategoryController {

    @Autowired
    private JobCategoryService jobCategoryService;

    @GetMapping("/")
    public ResponseEntity<List<JobCategory>> getAllJobCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllJobCategories());
    }

    @PostMapping("/")
    public ResponseEntity<JobCategory> createJobCategory(@RequestBody JobCategory jobCategory) {
        return ResponseEntity.ok(jobCategoryService.createJobCategory(jobCategory));
    }

}
