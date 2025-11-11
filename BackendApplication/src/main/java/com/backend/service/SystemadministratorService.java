package com.backend.service;

import com.backend.model.Systemadministrator;
import com.backend.model.User;
import com.backend.repository.MovieRepository;
import com.backend.repository.SystemadministratorRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;

@Service
public class SystemadministratorService {

    @Autowired
    SystemadministratorRepository systemadministratorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    public ResponseEntity<String> createSystemadministratorAccount(Systemadministrator systemadministratorAccount) {

        List<Systemadministrator> systemadministratorList = systemadministratorRepository.findAll();
        List<User> userList = userRepository.findAll();


        for (User user : userList) {

            if (systemadministratorAccount.getEmail().equals(user.getEmail())) {

                return new ResponseEntity<>("This E-Mail is already in use!", HttpStatus.BAD_REQUEST);

            }



        }

        if (systemadministratorRepository.count() == 0) {

            systemadministratorRepository.save(systemadministratorAccount);
            return new ResponseEntity<>("Account was created successfully!", HttpStatus.OK);
        }


        else {

            for (Systemadministrator systemadministrator : systemadministratorList) {

                if (systemadministratorAccount.getEmail().equals(systemadministrator.getEmail())) {

                    return new ResponseEntity<>("Account already exists!", HttpStatus.BAD_REQUEST);

                }
            }

            systemadministratorRepository.save(systemadministratorAccount);
            return new ResponseEntity<>("Account was created successfully!", HttpStatus.OK);


        }

    }


    public ResponseEntity<String> loginSystemadministrator(Systemadministrator systemadministratorAccount) {


        List<Systemadministrator> systemadministratorList = systemadministratorRepository.findAll();


            for (Systemadministrator systemadministrators : systemadministratorList) {

                if (systemadministrators.getEmail().equals(systemadministratorAccount.getEmail())) {

                    if (systemadministrators.getPassword() == systemadministratorAccount.getPassword()) {

                        Random randomCodeGenerator = new Random();
                        int randomCode = 1000+ randomCodeGenerator.nextInt(9999);
                        emailService.sendCode(systemadministratorAccount.getEmail(),"2FA-Code","Hello "+
                                    systemadministrators.getForename()+
                                " "+systemadministrators.getSurname()+"\n\n"+
                                "This is your two-factor authentication code: " +randomCode);

                        return new ResponseEntity<>(Integer.toString(randomCode), HttpStatus.OK);
                    } else {

                        return new ResponseEntity<>("Wrong password!", HttpStatus.BAD_REQUEST);

                    }
                }
            }

        return new ResponseEntity<>("E-Mail wrong or does not exist!", HttpStatus.BAD_REQUEST);
        }



    public List getAllSystemadministratorAccounts() {

        return systemadministratorRepository.findAll();
    }


    public ResponseEntity<Systemadministrator> getSystemadministratorById(int id) {

        return new ResponseEntity<>(systemadministratorRepository.findById(id).get(),HttpStatus.OK);
    }
}

