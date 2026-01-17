package model.logic;

public class LoginValidation {

    public enum Result {
        OK,
        EMPTY_FIELDS
    }

    public Result checkLogin (String email, String password) {
        if (email == null || password == null) return Result.EMPTY_FIELDS;
        if (email.trim().isEmpty() || password.trim().isEmpty()) return Result.EMPTY_FIELDS;

        return Result.OK;
    }
}
