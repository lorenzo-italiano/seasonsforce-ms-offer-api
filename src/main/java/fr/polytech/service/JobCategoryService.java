package fr.polytech.service;

import fr.polytech.model.JobCategory;
import fr.polytech.repository.JobCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobCategoryService {

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    /**
     * Create a job category.
     *
     * @param jobCategory The job category to create.
     * @return The created job category.
     */
    public JobCategory createJobCategory(JobCategory jobCategory) {
        return jobCategoryRepository.save(jobCategory);
    }

    /**
     * Get all job categories.
     *
     * @return A list of job categories.
     */
    public List<JobCategory> getAllJobCategories() {
        return jobCategoryRepository.findAll();
    }

    /**
     * Get a job category by its ID.
     *
     * @param id ID of the job category to get.
     * @return The job category.
     */
    public JobCategory getJobCategoryById(UUID id) {
        JobCategory jobCategory = jobCategoryRepository.findById(id).orElse(null);
        if (jobCategory == null) {
            throw new RuntimeException("Job category not found");
        }
        return jobCategory;
    }

    /**
     * Update a job category.
     *
     * @param jobCategory The job category to update.
     * @return The updated job category.
     */
    public JobCategory updateJobCategory(JobCategory jobCategory) {
        return jobCategoryRepository.save(jobCategory);
    }

    /**
     * Delete the job category with the specified ID
     *
     * @param id ID of the job category to delete
     */
    public void deleteJobCategory(UUID id) {
        jobCategoryRepository.deleteById(id);
    }
}
