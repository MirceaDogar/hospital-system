package ro.hospitalmanagement.system.model;

public enum Gender {

    M,F;

    public static Gender safeValueOf(String value){
        try {
            return Gender.valueOf(value);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}