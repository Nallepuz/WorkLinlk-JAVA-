package model.logic;

public class RegisterValidation {

    public enum Result {
        OK,
        EMPTY_FIELDS,
        PASSWORD_MISMATCH,
        INVALID_EMAIL
    }

    public Result checkRegister(String name,
                                String phone,
                                String email,
                                String password,
                                String repeatPass) {
        if (name == null || phone == null || email == null ||
                password == null || repeatPass == null) {
            return Result.EMPTY_FIELDS;
        }

        if (name.trim().isEmpty() || phone.trim().isEmpty() ||
                email.trim().isEmpty() || password.trim().isEmpty() ||
                repeatPass.trim().isEmpty()) {
            return Result.EMPTY_FIELDS;
        }

        if (!password.equals(repeatPass)) {
            return Result.PASSWORD_MISMATCH;
        }

        if (!email.contains("@") || !email.contains(".")) {
            return Result.INVALID_EMAIL;
        }

        return Result.OK;

    }
}
