package fr.polytech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        // Vérifiez si l'utilisateur avec l'ID spécifié existe
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return null; // Vous pouvez gérer cela de manière appropriée, par exemple, en renvoyant une erreur 404
        }

        // Mettez à jour les propriétés de l'utilisateur existant avec les données du nouveau utilisateur
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        // Enregistrez les modifications dans la base de données
        return userService.createUser(existingUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

