package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    private Integer id;
    
    @Column(name = "text")
    private String text;
    
    public Message() {}
    
    public Message(Integer id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
}
