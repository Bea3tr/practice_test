package ssf.practice_test.utils;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ssf.practice_test.service.RedisService;

// Task 2 - Command Line Runner
@Component
public class LoadData implements CommandLineRunner {

    @Autowired 
    private RedisService redisSvc;

    @Override
    public void run(String ... args) {
        try {
            redisSvc.loadToRedis("todos.txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

    }
    
}
