package com.example.loginandregister.controller;

import com.example.loginandregister.model.User;
import com.example.loginandregister.service.TodoService;
import com.example.loginandregister.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private TodoService todoService;

    // GET register page
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // POST register and redirect to login
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }

    // GET login page
    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // POST login
//    @PostMapping("/do-login")
//    public String processLogin(@ModelAttribute User loginUser, Model model) {
//        try {
//            User user = userService.loginUser(loginUser.getUsername(), loginUser.getPassword());
//            model.addAttribute("user", user);
//            model.addAttribute("todos", todoService.getTodosForUser(user.getId()));
//            return "todos";
//        } catch (RuntimeException e) {
//            model.addAttribute("user", new User());
//            model.addAttribute("error", e.getMessage());
//            return "login"; // this is safe now
//        }
//    }

    @PostMapping("/do-login")
    public String processLogin(@ModelAttribute User loginUser, Model model, HttpSession session) {
        try {
            User user = userService.loginUser(loginUser.getUsername(), loginUser.getPassword());

            // Save user in session
            session.setAttribute("user", user);

            model.addAttribute("user", user);
            model.addAttribute("todos", todoService.getTodosForUser(user.getId()));
            return "todos";
        } catch (RuntimeException e) {
            model.addAttribute("user", new User());
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
    @GetMapping("/todos/{userId}")
    public String showTodos(@PathVariable Long userId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getId().equals(userId)) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("todos", todoService.getTodosForUser(userId));
        return "todos";
    }


    // POST create todo from form
    @PostMapping("/todos/{userId}")
    public String createTodo(@PathVariable Long userId, @RequestParam String title, Model model) {
       todoService.addTodo(userId, title);
        User user = userService.findById(userId);
       model.addAttribute("user", user);
        model.addAttribute("todos", todoService.getTodosForUser(userId));
       return "todos";
   }
//    @GetMapping("/about")
//    public String aboutPage() {
//        return "about";
//    }
@GetMapping("/about")
public String aboutPage(@RequestParam("userId") Long userId, Model model) {
    User user = userService.findById(userId);
    model.addAttribute("user", user);
    return "about";
}
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroy the session
        return "redirect:/login"; // Redirect to login page
    }

}
