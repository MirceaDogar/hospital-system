package ro.hospitalmanagement.system.model;

public enum RoomType {

    SURGICAL, INTENSIVE, MATERNITY, EXAMINATION, EMERGENCY, PHARMACY;

    public static RoomType safeValueOf(String value){
        try {
            return RoomType.valueOf(value);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}