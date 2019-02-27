package com.example.android.homepharmacy.DataModel;

import java.io.Serializable;

public class DrugAlert implements Serializable {

    private Integer __id;
    private String member_name;
    private String drug_name;
    private String alert_time;
    private Integer dose_q;
    private Integer dose_r;
    private String _end_date;
    private String _start_date;

    public DrugAlert(Integer __id, String member_name, String drug_name, String alert_time,
                     Integer dose_q, Integer dose_r, String _end_date, String _start_date) {
        this.__id = __id;
        this.member_name = member_name;
        this.drug_name = drug_name;
        this.alert_time = alert_time;
        this.dose_q = dose_q;
        this.dose_r = dose_r;
        this._end_date = _end_date;
        this._start_date = _start_date;
    }

    public String get_start_date() {
        return _start_date;
    }

    public void set_start_date(String _start_date) {
        this._start_date = _start_date;
    }

    public String get_end_date() {
        return _end_date;
    }

    public void set_end_date(String _end_date) {
        this._end_date = _end_date;
    }

    public Integer getDose_q() {
        return dose_q;
    }

    public void setDose_q(Integer dose_q) {
        this.dose_q = dose_q;
    }

    public Integer getDose_r() {
        return dose_r;
    }

    public void setDose_r(Integer dose_r) {
        this.dose_r = dose_r;
    }

    public Integer get__id() {
        return __id;
    }

    public void set__id(Integer __id) {
        this.__id = __id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(String alert_time) {
        this.alert_time = alert_time;
    }


}
