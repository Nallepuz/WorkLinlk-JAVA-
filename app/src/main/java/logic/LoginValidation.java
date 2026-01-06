package logic;

public class LoginValidation {

    public enum Result {
        OK,
        EMPTY_FIELDS,
        INVALID_CREDENTIALS
    }

    public Result checkLogin (String username, String password) {
        if (username == null || password == null) return Result.EMPTY_FIELDS;
        if (username.trim().isEmpty() || password.trim().isEmpty()) return Result.EMPTY_FIELDS;

        if(username.equals("admin") && password.equals("1234")) {
            return Result.OK;
        };

        return Result.INVALID_CREDENTIALS;
    }
}
