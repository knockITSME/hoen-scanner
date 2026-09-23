package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {

    }

    @Override
    public void run(final HoenScannerConfiguration configuration,
                    final Environment environment) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        List<SearchResult> searchResults = new ArrayList<>();

        InputStream hotelsFile = getClass()
                .getClassLoader()
                .getResourceAsStream("hotels.json");

        InputStream rentalCarsFile = getClass()
                .getClassLoader()
                .getResourceAsStream("rental_cars.json");

        List<SearchResult> hotels = objectMapper.readValue(
                hotelsFile,
                new TypeReference<List<SearchResult>>() {}
        );

        List<SearchResult> rentalCars = objectMapper.readValue(
                rentalCarsFile,
                new TypeReference<List<SearchResult>>() {}
        );

        for (SearchResult hotel : hotels) {
            searchResults.add(
                    new SearchResult(
                            hotel.getCity(),
                            "hotel",
                            hotel.getTitle()
                    )
            );
        }

        for (SearchResult rentalCar : rentalCars) {
            searchResults.add(
                    new SearchResult(
                            rentalCar.getCity(),
                            "rental_car",
                            rentalCar.getTitle()
                    )
            );
        }
        environment.jersey().register(new SearchResource(searchResults));
    }
}