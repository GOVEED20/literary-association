package goveed20.LiteraryAssociationApplication.model;

public enum Genre {
    ADVENTURE("Avantura"),
    FANTASY("Fantastika"),
    MYSTERY("Misterija"),
    HISTORICAL("Istorijska"),
    HORROR("Horor"),
    ROMANCE("Ljubavna"),
    SCIFI("Naučna fanstastika"),
    THRILLER("Triler"),
    COOKBOOKS("Kuvar"),
    CRIME("Krimi"),
    EROTIC("Erotika");

    public final String serbianName;

    Genre(String serbianName) {
        this.serbianName = serbianName;
    }
}
