package project.api.rest.domain.sale;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import project.api.rest.domain.client.Client;
import project.api.rest.domain.product.Product;
import project.api.rest.domain.user.AppUser;

@Entity
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate whenToDo = LocalDate.now();

	@ManyToOne
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	
	@ManyToMany
	@JoinColumn(name = "product_id")
	private List<Product> product;

	public Sale() {

	}

	public Sale(LocalDate whenToDo, AppUser appUser, Client client, List<Product> product) {
		this.whenToDo = whenToDo;
		this.appUser = appUser;
		this.client = client;
		this.product = product;
	}

	public LocalDate getWhenToDo() {
		return whenToDo;
	}

	public void setWhenToDo(LocalDate whenToDo) {
		this.whenToDo = whenToDo;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	public Long getId() {
		return id;
	}

}
