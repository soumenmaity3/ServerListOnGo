package com.ListOnGo.ServerListOnGo.Model;
import java.time.LocalDateTime;
import java.util.List;

public class GroupedListDTO {
    private LocalDateTime dateTime;
    private List<AllListModel> items;
    private String listName;

    public GroupedListDTO(LocalDateTime dateTime, List<AllListModel> items, String listName) {
        this.dateTime = dateTime;
        this.items = items;
        this.listName = listName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<AllListModel> getItems() {
        return items;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setItems(List<AllListModel> items) {
        this.items = items;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
