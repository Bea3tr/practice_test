package ssf.practice_test.repo;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import ssf.practice_test.model.Todo;

@Repository
public class RedisRepo {

    private static final Logger logger = Logger.getLogger(RedisRepo.class.getName());

    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, Object> template;

    public void loadData(JsonArray array) throws ParseException {
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for(int i = 0; i < array.size(); i++) {
            JsonObject obj = array.getJsonObject(i);
            
            Date due_date = df.parse(obj.getString("due_date").split(", ")[1]);
            Date created_at = df.parse(obj.getString("created_at").split(", ")[1]);
            Date updated_at = df.parse(obj.getString("updated_at").split(", ")[1]);
            Map<String, String> values = new HashMap<>();
            values.put("id", obj.getString("id"));
            values.put("name", obj.getString("name"));
            values.put("description", obj.getString("description"));
            values.put("due_date", Long.toString(due_date.getTime()));
            values.put("priority_level", obj.getString("priority_level"));
            values.put("status", obj.getString("status"));
            values.put("created_at", Long.toString(created_at.getTime()));
            values.put("updated_at", Long.toString(updated_at.getTime()));
            hashOps.putAll(obj.getString("id"), values);
        }
    }

    public List<Todo> getTasks(String filter) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<Todo> taskList = new LinkedList<>();
        List<String> keys = template.keys("*").stream().toList();
        HashOperations<String, String, String> hashOps = template.opsForHash();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            try {
                Date due_date = df.parse(df.format(new Date(Long.parseLong(hashOps.get(key, "due_date")))));
                Date createdAt = df.parse(df.format(new Date(Long.parseLong(hashOps.get(key, "created_at")))));
                Date updatedAt = df.parse(df.format(new Date(Long.parseLong(hashOps.get(key, "updated_at")))));
                Todo task = new Todo(hashOps.get(key,"id"), hashOps.get(key, "name"), hashOps.get(key, "description"),
                    due_date, hashOps.get(key, "priority_level"), hashOps.get(key, "status"),
                    createdAt, updatedAt);
    
                if(task.getStatus().equals(filter))
                    taskList.add(task);

            } catch (Exception ex) {
                logger.warning("[Repo] ParseException in getTasks");
                ex.printStackTrace();
            }
            
        }
        return taskList;
    }

    public void insertTodo(Todo todo) {
        logger.info("[Repo] Inserting new task");
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        Map<String, String> values = new HashMap<>();
        values.put("id", todo.getId());
        values.put("name", todo.getName());
        values.put("description", todo.getDescription());
        values.put("due_date", Long.toString(todo.getDue_date().getTime()));
        values.put("priority_level", todo.getPriority());
        values.put("status", todo.getStatus());
        values.put("created_at", Long.toString(todo.getCreatedAt().getTime()));
        values.put("updated_at", Long.toString(todo.getUpdatedAt().getTime()));
        hashOps.putAll(todo.getId(), values);
    }

    public Todo getTodo(String id) {
        logger.info("[Repo] Getting task of id: " + id);
        HashOperations<String, String, String> hashOps = template.opsForHash();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Todo task = new Todo();
        try {
            Date due_date = df.parse(df.format(new Date(Long.parseLong(hashOps.get(id, "due_date")))));
            Date createdAt = df.parse(df.format(new Date(Long.parseLong(hashOps.get(id, "created_at")))));
            Date updatedAt = df.parse(df.format(new Date(Long.parseLong(hashOps.get(id, "updated_at")))));
            task = new Todo(hashOps.get(id,"id"), hashOps.get(id, "name"), hashOps.get(id, "description"),
                due_date, hashOps.get(id, "priority_level"), hashOps.get(id, "status"),
                createdAt, updatedAt);

        } catch (ParseException ex) {
            logger.warning("[Repo] ParseException in getTodo");
            ex.printStackTrace();
        }
     
        return task;
    }

    public void deleteTodo(String id) {
        logger.info("[Repo] Deleting " + id);
        template.delete(id);
    }
    
}
 