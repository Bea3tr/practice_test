package ssf.practice_test.service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import ssf.practice_test.model.Todo;
import ssf.practice_test.repo.RedisRepo;

@Service
public class RedisService {

    @Autowired 
    private RedisRepo redisRepo;

    @Value("${filepath}")
    private String filePath;

    public void loadToRedis(String fileName) throws IOException, ParseException {

        String url = filePath + "/" + fileName;
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String contents = resp.getBody();

        // Create json array
        JsonReader reader = Json.createReader(new StringReader(contents));
        JsonArray array = reader.readValue().asJsonArray();

        redisRepo.loadData(array);
    }

    public List<Todo> getTasks(String filter) {
        return redisRepo.getTasks(filter);
    }

    public Todo getTodo(String id) {
        return redisRepo.getTodo(id);
    }

    public void insertTodo(Todo todo) {
        redisRepo.insertTodo(todo);
    }

    public void deleteTodo(String id) {
        redisRepo.deleteTodo(id);
    }
    
}
