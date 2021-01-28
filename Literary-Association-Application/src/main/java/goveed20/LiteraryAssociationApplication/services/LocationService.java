package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    // hardcoded latitude and longitude
    public Location createLocation(String country, String city) {
        return Location.builder()
                .country(country)
                .city(city)
                .latitude(45.0)
                .longitude(45.0)
                .build();
    }
}
