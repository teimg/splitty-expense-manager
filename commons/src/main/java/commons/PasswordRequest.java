package commons;

import java.util.Objects;

public class PasswordRequest {

    private String password;

    public PasswordRequest() {
    }

    public PasswordRequest(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordRequest that = (PasswordRequest) o;
        return Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordRequest{");
        sb.append("password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
