package guru.springframework.spring6restmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * If we want to scan packages that located outside the SpringApplication class main package
 * then we can use this below configuration [ scanBasePackages ] in the @SpringBootApplication annotation
 * @SpringBootApplication(scanBasePackages = {
		"package1",
		"package2",
		"etc..."
})*/

@SpringBootApplication
public class Spring6RestMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring6RestMvcApplication.class, args);
	}

}
