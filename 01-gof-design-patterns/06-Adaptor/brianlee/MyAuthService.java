package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian;

import me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian.lib.GoogleAuthService;

import java.util.HashMap;
import java.util.Map;

public class MyAuthService {

    Map<String, Account> accounts;
    GoogleAuthService googleAuthService;

    public MyAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
        this.accounts = new HashMap<>();
        this.accounts.put("dohunkim@pattern.com", new MyAccount("dohunkim@pattern.com", "김두현", "1234", "Vancouver"));
        this.accounts.put("havenkim@pattern.com", new MyAccount("havenkim@pattern.com", "김천국", "5678", "Burnaby"));
    }

    public Account login(String email, String password) {
        if(accounts.containsKey(email)) {
            Account account = accounts.get(email);
            if(account.getPassword().isPresent() &&
                account.getPassword().get().equals(password)) {
                return account;
            }
        } else {
            return new MyGoogleAccount(this.googleAuthService.authGoogleAccount(email, password));
        }
        throw new IllegalArgumentException("There is no user");
    }
}
