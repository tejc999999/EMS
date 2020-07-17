package jp.ac.ems.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.security.LoginUserDetails;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserBean> opt = userRepository.findById(username);
        UserBean user = opt.orElseThrow(() -> new UsernameNotFoundException("The requested user is not found."));
        String[] array = { RoleCode.getCode(user.getRoleId()) };
        return new LoginUserDetails(user, AuthorityUtils.createAuthorityList(array));
	}
}
