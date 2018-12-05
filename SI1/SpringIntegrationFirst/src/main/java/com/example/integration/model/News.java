package com.example.integration.model;

import java.io.Serializable;

public class News implements Serializable {

    private String text;

    private String author;

    public News(String text, String author) {
        this.text = text;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (text != null ? !text.equals(news.text) : news.text != null) return false;
        return author != null ? author.equals(news.author) : news.author == null;
    }

    @Override
    public int hashCode() {
        return author != null ? author.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "News{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}