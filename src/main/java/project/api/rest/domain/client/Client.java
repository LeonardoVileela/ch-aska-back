package project.api.rest.domain.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "the description is mandatory")
	@Length(min = 3, max = 50, message = "the description size is invalid")
	private String name;

	@NotEmpty(message = "the description is mandatory")
	@Length(min = 0, max = 14, message = "the description size is invalid")
	private String cpf;

	public Client() {

	}

	public Client(String name, String cpf) {

		this.name = name;
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Long getId() {
		return id;
	}
}
