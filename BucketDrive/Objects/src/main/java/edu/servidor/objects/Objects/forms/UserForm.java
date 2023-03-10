package edu.servidor.objects.Objects.forms;

import org.hibernate.validator.constraints.Length;

public class UserForm {
    @Length(min = 3, max = 10)
    private String username;

    @Length(min = 3, max = 10)
    private String password;

    @Length(min = 3, max = 30)
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
