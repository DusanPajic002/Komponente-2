package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallDto {

    private String name;
    private Long managerID;

    public HallDto(String name, Long managerID) {
        this.name = name;
        this.managerID = managerID;
    }

    public HallDto() {
    }

    @Override
    public String toString() {
        return "HallDto{" +
                "name='" + name + '\'' +
                ", managerID=" + managerID +
                '}';
    }
}
