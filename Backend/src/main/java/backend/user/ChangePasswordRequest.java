package backend.user;

public class ChangePasswordRequest {
    private final String newPassword;

    public ChangePasswordRequest(String newPw) {
        this.newPassword = newPw;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
