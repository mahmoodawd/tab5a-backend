package dev.awd.tab5abackend.dto;

import lombok.Getter;

@Getter
public enum ImageType {

    CATEGORY("categories"),
    MEAL("meals"),
    INGREDIENT("ingredients"),
    CHEF("chefs");

    private final String folder;

    ImageType(String folder) {
        this.folder = folder;
    }

}
