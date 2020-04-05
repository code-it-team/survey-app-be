package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.entities.Choice;
import com.codeit.survey.services.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminChoiceController {
    private ChoiceService choiceService;

    @Autowired
    public AdminChoiceController (ChoiceService choiceService){
        this.choiceService = choiceService;
    }

    @PostMapping("/admin/addChoice")
    public ResponseEntity<?> addChoice(@RequestBody Choice choice){
        return choiceService.addChoice_admin(choice);
    }

    @DeleteMapping("/admin/deleteChoice")
    public ResponseEntity<?> deleteChoice(@RequestParam Integer choiceId){
        return choiceService.deleteChoice_admin(choiceId);
    }

    @PutMapping("/admin/updateChoice")
    public ResponseEntity<?> updateChoice(@RequestBody Choice choice){
        return choiceService.updateChoice_admin(choice);
    }

}
