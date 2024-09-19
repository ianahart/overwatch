package com.hart.overwatch.dropdownoption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/dropdown-options")
public class DropDownOptionController {

    private final DropDownOptionService dropDownOptionService;

    @Autowired
    public DropDownOptionController(DropDownOptionService dropDownOptionService) {
        this.dropDownOptionService = dropDownOptionService;
    }
}
