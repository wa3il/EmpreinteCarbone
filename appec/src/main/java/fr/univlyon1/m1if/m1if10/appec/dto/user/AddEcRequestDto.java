package fr.univlyon1.m1if.m1if10.appec.dto.user;

public class AddEcRequestDto {
    private int alimentId;
    private String login;
    private float quantity;

    public AddEcRequestDto() {
    }

    public AddEcRequestDto(int alimentId, float quantity, String login) {
        this.alimentId = alimentId;
        this.quantity = quantity;
        this.login = login;
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
}
