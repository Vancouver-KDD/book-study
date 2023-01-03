package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian.lib;

import org.modelmapper.internal.util.Assert;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class GoogleAuthService {

    public GoogleAccount authGoogleAccount(String email, String key) {
        Assert.notNull(key);
        Assert.notNull(email);

        String storedKey = null;
        String brianEmail = "brianlee@gmail.com";
        try {
            // 520460946EE727EE354F0D3DF0856482
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update("이병수".getBytes());
            byte[] digest = md.digest();
            storedKey = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            throw new InternalError("md5 error");
        }

        if(key.equals(storedKey) && brianEmail.equals(email)) {
            return new GoogleAccount(email, "이병수");
        } else {
            throw new IllegalArgumentException("There is no user information");
        }
    }
}
