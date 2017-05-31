package ekol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TaskServiceApplication.class);
        app.run(args);
    }
}
