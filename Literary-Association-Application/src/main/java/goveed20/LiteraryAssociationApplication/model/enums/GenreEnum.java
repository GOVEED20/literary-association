package goveed20.LiteraryAssociationApplication.model.enums;

public enum GenreEnum {
    ADVENTURE("Avantura"),
    FANTASY("Fantastika"),
    MYSTERY("Misterija"),
    HISTORICAL("Istorijski"),
    HORROR("Horor"),
    ROMANCE("Ljubavni"),
    SCIFI("Naučna fantastika"),
    THRILLER("Triler"),
    COOKBOOKS("Kuvar"),
    CRIME("Krimi"),
    EROTIC("Erotika");

    public final String serbianName;

    GenreEnum(String serbianName) {
        this.serbianName = serbianName;
    }
}
