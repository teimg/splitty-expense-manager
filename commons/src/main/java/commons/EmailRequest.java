package commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class EmailRequest {

    private String to;

    private String subject;

    private String body;

    private String username;

    private String password;

    public EmailRequest() {}

    public EmailRequest(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(
                    "client/src/main/resources/client/config/mail.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return;
        }
        try {
            this.username = properties.getProperty("mail.username");
            this.password = properties.getProperty("mail.password");
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailRequest request = (EmailRequest) o;

        if (!Objects.equals(to, request.to)
                || !Objects.equals(subject, request.subject)) return false;
        if (!Objects.equals(body, request.body)) return false;
        if (!Objects.equals(username, request.username)) return false;
        return Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {
        int result = to != null ? to.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
