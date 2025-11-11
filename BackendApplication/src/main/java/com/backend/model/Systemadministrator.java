package com.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "Systemadministrator")
public class Systemadministrator extends Account {


    public Systemadministrator() {
    }

    public Systemadministrator(String forename, String surname, String email, long password) {
        super(forename, surname, email, password);



    }

    public Systemadministrator(int accountID, String forename, String surname, String email, long password) {
        super(accountID, forename, surname, email, password);
    }
}
