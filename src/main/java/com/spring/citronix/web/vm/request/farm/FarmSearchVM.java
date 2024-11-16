package com.spring.citronix.web.vm.request.farm;


import java.time.LocalDate;

public class FarmSearchVM {

    public String name;
    public String location;
    public LocalDate date;

    public FarmSearchVM(String name, String location, LocalDate date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
