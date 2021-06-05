package project.api.rest.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.api.rest.domain.user.AppUser;
import project.api.rest.domain.user.AppUserRepository;

//classe para buscar e comparar o usuario e caso ele não exista retorna uma exception
@Service
public class UserDetailsServiceimpl implements UserDetailsService {

	private AppUserRepository appUserRepository;

	@Autowired
	public UserDetailsServiceimpl(AppUserRepository appUserRepository) {

		this.appUserRepository = appUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = this.appUserRepository.findByUsername(username);

		if (appUser == null) {
			throw new UsernameNotFoundException(username);
		}

		return new UserDetailsImpl(appUser);

	}

}
