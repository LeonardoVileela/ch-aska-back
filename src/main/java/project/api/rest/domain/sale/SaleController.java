package project.api.rest.domain.sale;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.api.rest.domain.client.Client;
import project.api.rest.domain.client.ClientRepository;
import project.api.rest.domain.product.Product;
import project.api.rest.domain.product.ProductRepository;
import project.api.rest.domain.user.AppUser;
import project.api.rest.domain.user.AppUserRepository;

@RestController
public class SaleController {

	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;
	private final ClientRepository clientRepository;
	private final AppUserRepository appUserRepository;

	public class SalesTotalJson {
		public String salesTotal;
		public Integer percentageTotal;
		public Boolean percentage;

		public SalesTotalJson(String salesTotal, Integer percentageTotal2, Boolean percentage) {
			this.salesTotal = salesTotal;
			this.percentageTotal = percentageTotal2;
			this.percentage = percentage;
		}

	}

	public class SalesContJson {
		public Integer salesCont;
		public Integer percentageTotal;
		public Boolean percentage;

		public SalesContJson(Integer salesCont, Integer percentageTotal, Boolean percentage) {
			this.salesCont = salesCont;
			this.percentageTotal = percentageTotal;
			this.percentage = percentage;
		}

	}

	SaleController(SaleRepository saleRepository, ClientRepository clientRepository,
			ProductRepository productRepository, AppUserRepository appUserRepository) {
		this.saleRepository = saleRepository;
		this.clientRepository = clientRepository;
		this.productRepository = productRepository;
		this.appUserRepository = appUserRepository;
	}

	@GetMapping("/api/sales")
	public Iterable<Sale> all() {
		return saleRepository.findAll();
	}
	@GetMapping("/api/sales/{id}")
	public Optional<Sale> one(@PathVariable Long id) {
		return saleRepository.findById(id);
	}

	@PostMapping("/api/sales")
	public Sale createSale(@RequestBody Object sale) {
		String saleString = sale.toString();
		saleString = saleString.substring(1, saleString.length() - 1);
		String vet[] = saleString.split(",", 2);

		Optional<Client> client = clientRepository.findById(Long.parseLong(vet[0].split("=")[1].trim()));
		Client client2 = client.get();

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		AppUser appUser = appUserRepository.findByUsername(username);

		String vet2 = vet[1].trim();
		String vet3[] = vet2.split("=", 0);
		String products = vet3[1].substring(1, vet3[1].length() - 1);
		String productsVet[] = products.split(",");
		List<Product> listProduct = new ArrayList<>();
		for (int i = 0; i < productsVet.length; i++) {
			Optional<Product> product = productRepository.findById(Long.parseLong(productsVet[i].trim()));
			Product product2 = product.get();
			listProduct.add(product2);
		}

		Sale saleSave = new Sale(LocalDate.now(), appUser, client2, listProduct);

		return saleRepository.save(saleSave);
	}

	@DeleteMapping(value = "/api/sales/{id}")
	public ResponseEntity<Object> deletePost(@PathVariable Long id) {

		saleRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);

	}

	@GetMapping("/api/salesCont/{id}")
	public SalesContJson salesCont(@PathVariable Long id) {
		Iterable<Sale> testeIterable = saleRepository.findAll();
		int cont = 0;
		Iterable<Sale> testeIterable2 = saleRepository.findAll();
		int cont2 = 0;
		Double result = 0.0;
		Boolean positive = false;
		for (Sale sale : testeIterable) {
			Month monthUser = sale.getWhenToDo().getMonth();
			Month nowMonth = LocalDate.now().getMonth();
			if (sale.getAppUser().getId() == id && monthUser == nowMonth) {
				cont++;
			}
		}

		for (Sale sale : testeIterable2) {
			int monthUser = sale.getWhenToDo().getMonth().getValue();
			int nowMonth = LocalDate.now().getMonth().getValue() - 1;

			if (sale.getAppUser().getId() == id && monthUser == nowMonth) {
				cont2++;
			}
		}

		if (cont > cont2) {
			if (cont == 0.0 || cont2 == 0.0) {
				result = 100.0;
				positive = true;
			} else {
				result = Double.valueOf(((cont * 100) / cont2) - 100);
				positive = true;
			}

		} else if (cont < cont2) {
			if (cont == 0.0 || cont2 == 0.0) {
				result = 100.0;
				positive = false;
			} else {
				result = Double.valueOf(((cont * 100) / cont2) - 100);
				result = Math.abs(result);
				positive = false;
			}

		} else if (cont == cont2) {
			result = 0.0;
			positive = true;
		}
		int resultInt = (int) Math.round(result);

		SalesContJson salesContJson = new SalesContJson(cont, resultInt, positive);
		return salesContJson;
	}

	@GetMapping("/api/salesTotal/{id}")
	public SalesTotalJson salesTotal(@PathVariable Long id) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		Double cont = 0.0;
		Iterable<Sale> testeIterable = saleRepository.findAll();
		Double cont2 = 0.0;
		Iterable<Sale> testeIterable2 = saleRepository.findAll();
		Double result = 0.0;
		Boolean positive = false;

		for (Sale sale : testeIterable) {
			Month monthUser = sale.getWhenToDo().getMonth();
			Month nowMonth = LocalDate.now().getMonth();

			if (sale.getAppUser().getId() == id && monthUser == nowMonth) {
				for (Product product : sale.getProduct()) {
					cont = cont + product.getPrice();
				}
			}
		}

		for (Sale sale : testeIterable2) {
			int monthUser = sale.getWhenToDo().getMonth().getValue();
			int nowMonth = LocalDate.now().getMonth().getValue() - 1;

			if (sale.getAppUser().getId() == id && monthUser == nowMonth) {
				for (Product product : sale.getProduct()) {
					cont2 = cont2 + product.getPrice();
				}
			}
		}

		if (cont > cont2) {
			if (cont == 0.0 || cont2 == 0.0) {
				result = 100.0;
				positive = true;
			} else {
				result = ((cont * 100) / cont2) - 100;
				positive = true;
			}

		} else if (cont < cont2) {
			if (cont == 0.0 || cont2 == 0.0) {
				result = 100.0;
				positive = false;
			} else {
				result = ((cont * 100) / cont2) - 100;
				result = Math.abs(result);
				positive = false;
			}

		} else if (cont == cont2) {
			result = 0.0;
			positive = true;
		}
		int resultInt = (int) Math.round(result);

		SalesTotalJson salesTotalJson = new SalesTotalJson(formatter.format(cont), resultInt, positive);

		return salesTotalJson;

	}

}
