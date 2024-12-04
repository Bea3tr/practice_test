package ssf.practice_test.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

// Task 3
public class Todo {

    @NotNull(message="ID cannot be null")
    @NotEmpty(message="ID cannot be empty")
    @Size(max=50)
    private String id;

    @NotNull(message="Name cannot be null")
    @NotEmpty(message="Name cannot be empty")
    @Size(min=10, max=50, message="Name must be between 10 and 50 characters")
    private String name;

    @Size(max=255)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message="Date must be in the present or future")
    private Date due_date;

    private String priority;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Date getDue_date() {
        return due_date;
    }
    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Todo() {}
    public Todo(@NotNull(message = "ID cannot be null") @NotEmpty(message = "ID cannot be empty") @Max(50) String id,
            @NotNull(message = "Name cannot be null") @NotEmpty(message = "Name cannot be empty") @Size(min = 10, max = 50, message = "Name must be between 10 and 50 characters") String name,
            @Max(255) String description,
            @FutureOrPresent(message = "Date must be in the present or future") Date due_date, String priority,
            String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due_date = due_date;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Todo [id=" + id + ", name=" + name + ", description=" + description + ", due_date=" + due_date
                + ", priority=" + priority + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

    
    

    
}
