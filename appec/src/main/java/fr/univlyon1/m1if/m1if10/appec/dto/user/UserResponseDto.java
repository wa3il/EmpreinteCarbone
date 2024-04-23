package fr.univlyon1.m1if.m1if10.appec.dto.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * User response.
 */
@JacksonXmlRootElement(localName = "user")
public class UserResponseDto {
    private String login;
    private String name;

    public UserResponseDto() {
    }

    public UserResponseDto(String login, String name) {
        this.login = login;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
