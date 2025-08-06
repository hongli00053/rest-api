package acmemedical.security;

import java.security.Principal;

import acmemedical.entity.SecurityUser;

public class WrappingCallerPrincipal implements Principal {
    private SecurityUser sUser;

    public WrappingCallerPrincipal(SecurityUser sUser) {
        this.sUser = sUser;
    }

    @Override
    public String getName() {
        return sUser.getUsername(); // or any field you use for username
    }

    public SecurityUser getWrapped() {
        return sUser;
    }
}
