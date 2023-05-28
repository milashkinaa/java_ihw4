package org.example.home.help;

import org.example.home.domain.entity.Role;

public class Valid {

    public static Role validate(String value) throws IllegalArgumentException {
        String roleStr = value.toUpperCase();
        if (!roleStr.equals("CUSTOMER") && !roleStr.equals("CHEF") && !roleStr.equals("MANAGER")) {
            throw new IllegalArgumentException();
        }
        return Role.valueOf(roleStr);
    }
}
