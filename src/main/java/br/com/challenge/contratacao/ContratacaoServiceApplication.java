package br.com.challenge.contratacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ContratacaoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContratacaoServiceApplication.class, args);
	}

}
