package com.backend.controller;

import com.backend.model.Account;
import com.backend.model.Systemadministrator;
import com.backend.service.SystemadministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SystemadministratorController {

    @Autowired
    SystemadministratorService systemadministratorService;


    @PostMapping(value = "/systemadministrators/create/")
    public ResponseEntity<String> createSystemadministratorAccount(@RequestBody Systemadministrator systemadministratorAccount) {

       return systemadministratorService.createSystemadministratorAccount(systemadministratorAccount);



    }


    @PostMapping(value = "/systemadministrators/login/")
    public ResponseEntity<String> loginSystemadministrator(@RequestBody Systemadministrator systemadministratorAccount) {

        return systemadministratorService.loginSystemadministrator(systemadministratorAccount);



    }


    @GetMapping(value = "/systemadministrators/")
    public List getAllSystemadministratorAccounts() {

        return systemadministratorService.getAllSystemadministratorAccounts();

    }

    @GetMapping(value="systemadministrators/{id}")
    public ResponseEntity<Systemadministrator> getSystemadministratorById(@PathVariable int id)     {

        return systemadministratorService.getSystemadministratorById(id);
    }

}
