package com.example.medicinesearch;


public class NewsRecyclerItem {

    private String title;       //기사 제목
    private String description; //기사 내용
    private String link;        //기사 RUL
    private String category;    //기사 카테고리


    public String toString()
    {
        return title + ",,," + description + ",,," + link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

