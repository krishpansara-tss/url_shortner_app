package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.services.interfaces.IUserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Data
@RestController
@RequestMapping("/api/v1")
public class UrlController {

    private IUserService userService;


        @PostMapping
        public ResponseEntity<?> createUser() {

            // userService.createUser();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User created successfully");
        }


        // GET /api/v1/users
        @GetMapping
        public ResponseEntity<?> getAllUsers() {

            // userService.getAllUsers();

            return ResponseEntity.ok("Get all users");
        }


        // GET /api/v1/users/{id}
        @GetMapping("/{id}")
        public ResponseEntity<?> getUserById(
                @PathVariable Long id) {

            // userService.getUserById(id);

            return ResponseEntity.ok("Get user with id: " + id);
        }


        // PUT /api/v1/users/{id}
        @PutMapping("/{id}")
        public ResponseEntity<?> updateUser(
                @PathVariable Long id) {

            // userService.updateUser(id);

            return ResponseEntity.ok("Update user with id: " + id);
        }


        // DELETE /api/v1/users/{id}
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteUser(
                @PathVariable Long id) {

            // userService.deleteUser(id);

            return ResponseEntity.noContent().build();
        }
    }

