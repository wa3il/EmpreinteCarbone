package fr.univlyon1.m1if.m1if10.appec.dto.user;

import java.sql.Date;

public class AddEcRequestDto {
    private int alimentId;
    private String login;
    private float quantity;

    private Date date;

    public AddEcRequestDto() {
    }

    public AddEcRequestDto(int alimentId, float quantity, String login, Date date) {
        this.alimentId = alimentId;
        this.quantity = quantity;
        this.login = login;
        this.date = date;
    }

    public int getAlimentId() {
        return alimentId;
    }

    public void setAlimentId(int alimentId) {
        this.alimentId = alimentId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}
}
