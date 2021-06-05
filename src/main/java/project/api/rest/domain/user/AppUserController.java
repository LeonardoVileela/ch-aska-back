package project.api.rest.domain.user;

import java.util.Optional;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppUserController {

	private final AppUserRepository appUserRepository;

	public AppUserController(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@PostMapping("/cadaster")
	AppUser newEmployee(@RequestBody AppUser appUser) {
		appUser.setUsername(appUser.getUsername().toLowerCase().trim());
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		appUser.setPassword(encoder.encode(appUser.getPassword()));
		return appUserRepository.save(appUser);
	}

	@PutMapping("/admin/{id}")
	AppUser admin(@RequestBody Object admin, @PathVariable Long id) {
		String adString1 = admin.toString().split("=")[1];
		String adString = adString1.split("}")[0];

		if (adString.equals("true")) {
			Optional<AppUser> appUser = appUserRepository.findById(id);
			AppUser appUser2 = appUser.get();
			appUser2.setAdmin(true);
			return appUserRepository.save(appUser2);

		} else if (adString.equals("false")) {
			Optional<AppUser> appUser = appUserRepository.findById(id);
			AppUser appUser2 = appUser.get();
			appUser2.setAdmin(false);
			return appUserRepository.save(appUser2);

		} else {
			return null;
		}

	}

}
