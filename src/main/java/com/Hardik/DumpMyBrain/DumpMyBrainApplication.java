package com.Hardik.DumpMyBrain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(excludeName = {
        "org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration"
})
public class DumpMyBrainApplication {

	public static void main(String[] args) {
		SpringApplication.run(DumpMyBrainApplication.class, args);
	}

}
