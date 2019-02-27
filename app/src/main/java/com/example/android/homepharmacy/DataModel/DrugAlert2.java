package com.example.android.homepharmacy.DataModel;

import java.io.Serializable;

public class DrugAlert2 implements Serializable {
    private int idEx;
    private String drug_name;
    private String drug_ex_date;


    public DrugAlert2(int idEx, String drug_name, String drug_ex_date) {
        this.idEx = idEx;
        this.drug_name = drug_name;
        this.drug_ex_date = drug_ex_date;
    }

    public int getIdEx() {
        return idEx;
    }

    public void setIdEx(int idEx) {
        this.idEx = idEx;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getDrug_ex_date() {
        return drug_ex_date;
    }

    public void setDrug_ex_date(String drug_ex_date) {
        this.drug_ex_date = drug_ex_date;
    }
}
