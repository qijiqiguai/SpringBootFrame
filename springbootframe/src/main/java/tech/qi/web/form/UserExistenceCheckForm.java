package tech.qi.web.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



public class UserExistenceCheckForm {

    @NotNull
    @Size(min = 5, max = 15)
    private String phone;

    private int userType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
