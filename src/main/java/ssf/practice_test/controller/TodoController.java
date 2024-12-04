package ssf.practice_test.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.practice_test.model.Todo;
import ssf.practice_test.service.RedisService;

@Controller
@RequestMapping
public class TodoController {

    private static final Logger logger = Logger.getLogger(TodoController.class.getName());

    @Autowired
    private RedisService redisSvc;

    @GetMapping("/listing")
    public String getListing(HttpSession sess) {
        if(sess.getAttribute("name") != null && sess.getAttribute("age") != null) {
            return "listing";
        }
        return "refused";
    }

    @PostMapping("/listing")
    public String postListing(Model model, @RequestParam String filter,
        HttpSession sess) {

        if(sess.getAttribute("name") != null && sess.getAttribute("age") != null) {
            logger.info("Filter: " + filter);
            List<Todo> taskList = redisSvc.getTasks(filter);
            model.addAttribute("taskList", taskList);
            return "listing";
        }
        return "refused";
    }

    @GetMapping("/listing/new")
    public String getNew(Model model) {
        model.addAttribute("todo", new Todo());
        return "add";
    }

    @PostMapping("/listing/new")
    public String postNew(Model model,
        @Valid @ModelAttribute Todo todo,
        BindingResult bindings) {

        if(bindings.hasErrors())
            return "add";
        
        redisSvc.insertTodo(todo);
        model.addAttribute("todo", new Todo());
        return "add";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String postUser(@RequestBody MultiValueMap<String, String> form,
        HttpSession sess) {

        String name = (String) sess.getAttribute("name");
        if(name == null) {
            name = form.getFirst("name");
            sess.setAttribute("name", name);
        }

        int age;
        if(sess.getAttribute("age") == null) {
            age = Integer.parseInt(form.getFirst("age"));
            sess.setAttribute("age", age);
        } else {
            age = (Integer) sess.getAttribute("age");
        }

        if(age < 10) {
            return "underage";
        }

        return "listing";
    }

    @PostMapping("/logout")
    public String logout(HttpSession sess) {
        sess.invalidate();
        return "login";
    }

    @PostMapping("/delete")
    public String deleteTodo(@RequestBody MultiValueMap<String, String> form) {

        String id = form.getFirst("delete");
        logger.info(id);
        redisSvc.deleteTodo(id);

        return "listing";
    }

    @PostMapping("/update")
    public String update(Model model, @RequestBody MultiValueMap<String, String> form) {
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String id = form.getFirst("update");
        logger.info("[Controller] ID: " + id);
        Todo originalTodo = redisSvc.getTodo(id);
        logger.info("[Controller] ogTodo: " + originalTodo);
        model.addAttribute("id", originalTodo.getId());
        model.addAttribute("name", originalTodo.getName());
        model.addAttribute("description", originalTodo.getDescription());
        model.addAttribute("priority", originalTodo.getPriority());
        model.addAttribute("status", originalTodo.getStatus());
        model.addAttribute("due_date", originalTodo.getDue_date());
        model.addAttribute("createdAt", originalTodo.getCreatedAt());
        model.addAttribute("updatedAt", originalTodo.getUpdatedAt());
        model.addAttribute("todo", new Todo());
        return "update";
    }

    @PostMapping("/listing/update")
    public String postUpdate(Model model, 
        @Valid @ModelAttribute Todo todo,
        BindingResult bindings,
        @ModelAttribute String id) {

        if(bindings.hasErrors()) {
            return "update";
        }
        
        if(!id.equals(todo.getId())) {
            redisSvc.deleteTodo(id);
        }
        redisSvc.insertTodo(todo);
        return "listing";
    }
    
}
