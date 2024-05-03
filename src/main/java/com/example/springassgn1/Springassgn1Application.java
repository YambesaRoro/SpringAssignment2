package com.example.springassgn1;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotEmpty;


import java.util.*;


@SpringBootApplication
@RestController
@Validated
public class Springassgn1Application {

    private Map<String, List<String>> courses = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(Springassgn1Application.class, args);
    }

    @PostMapping("/courses")
    public ResponseEntity<String> createCourse(
            @RequestParam @NotEmpty String category,
            @RequestParam @NotEmpty String courseCode
    ) {
        courses.putIfAbsent(category, new ArrayList<>());
        List<String> coursesInCategory = courses.get(category);
        if (coursesInCategory.contains(courseCode)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists.");
        }
        coursesInCategory.add(courseCode);
        return ResponseEntity.status(HttpStatus.CREATED).body("Course created successfully.");
    }

    @GetMapping("/courses/{category}")


    public ResponseEntity<List<String>> getCoursesByCategory(
            @PathVariable @NotEmpty String category
    ) {
        List<String> coursesInCategory = courses.get(category);
        if (coursesInCategory == null || coursesInCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(coursesInCategory);
    }

    @PutMapping("/courses/{category}/{oldCourseCode}")
    public ResponseEntity<String> updateCourse(
            @PathVariable @NotEmpty String category,
            @PathVariable @NotEmpty String oldCourseCode,
            @RequestParam @NotEmpty String newCourseCode
    ) {
        List<String> coursesInCategory = courses.get(category);
        if (coursesInCategory == null || !coursesInCategory.contains(oldCourseCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        if (coursesInCategory.contains(newCourseCode)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("New course code already exists.");
        }
        coursesInCategory.remove(oldCourseCode);
        coursesInCategory.add(newCourseCode);
        return ResponseEntity.ok("Course updated successfully.");
    }

    @DeleteMapping("/courses/{category}/{courseCode}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable @NotEmpty String category,
            @PathVariable @NotEmpty String courseCode
    ) {
        List<String> coursesInCategory = courses.get(category);
        if (coursesInCategory == null || !coursesInCategory.contains(courseCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        coursesInCategory.remove(courseCode);
        return ResponseEntity.ok("Course deleted successfully.");
    }
}