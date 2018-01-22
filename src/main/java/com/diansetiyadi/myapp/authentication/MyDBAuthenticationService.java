package com.diansetiyadi.myapp.authentication;

import com.diansetiyadi.myapp.dao.AccountDAO;
import com.diansetiyadi.myapp.entity.AccountsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyDBAuthenticationService implements UserDetailsService {

    @Autowired
    AccountDAO accountDAO;

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AccountsEntity account = accountDAO.findAccount(userName);
        System.out.println("Account = " + account);

        if (account == null) {
            throw new UsernameNotFoundException("User " + userName + " tidak ditemukan di dalam Database");
        }

        String role = account.getUserRole();

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();


        //ROLE_EMPLYEE,ROLE_MANAGER
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        grantList.add(authority);

        boolean enabled = account.isActive();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        UserDetails userDetails = new User(account.getUserName(), account.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantList);

        return userDetails;
    }
}
