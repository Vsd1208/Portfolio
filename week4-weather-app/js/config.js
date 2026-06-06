const WeatherConfig = {
    forecastUrl: "https://api.open-meteo.com/v1/forecast",
    geocodingUrl: "https://geocoding-api.open-meteo.com/v1/search",
    reverseGeocodingUrl: "https://nominatim.openstreetmap.org/reverse",
    defaultLocation: {
        name: "Hyderabad",
        country: "India",
        admin1: "Telangana",
        latitude: 17.384,
        longitude: 78.4564
    },
    cacheDuration: 10 * 60 * 1000,
    autocompleteDelay: 300
};
