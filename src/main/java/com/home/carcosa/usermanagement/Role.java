package com.home.carcosa.usermanagement;

public enum Role {
    ADMIN, WATCHER;

    public static Role fromString(String role){
        for (Role r : Role.values()){
            if (r.name().equalsIgnoreCase(role)){
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + "." + role);
    }
}
