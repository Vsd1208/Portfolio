class WeatherService {
    async searchCities(query) {
        const params = new URLSearchParams({
            name: query,
            count: "6",
            language: "en",
            format: "json"
        });
        const response = await fetch(`${WeatherConfig.geocodingUrl}?${params}`);
        if (!response.ok) throw new Error("City search is unavailable right now.");
        const data = await response.json();
        return data.results || [];
    }

    async getWeather(location) {
        const cacheKey = `${location.latitude.toFixed(3)},${location.longitude.toFixed(3)}`;
        const cached = WeatherStorage.getCached(cacheKey);
        if (cached) return { ...cached.data, fromCache: true, cachedAt: cached.savedAt };

        const params = new URLSearchParams({
            latitude: location.latitude,
            longitude: location.longitude,
            timezone: "auto",
            forecast_days: "7",
            current: "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,pressure_msl,wind_speed_10m,wind_direction_10m,visibility",
            daily: "weather_code,temperature_2m_max,temperature_2m_min,uv_index_max,sunrise,sunset"
        });
        const response = await fetch(`${WeatherConfig.forecastUrl}?${params}`);
        if (!response.ok) throw new Error("The weather service did not respond.");
        const data = await response.json();
        const result = { location, weather: data, fromCache: false, cachedAt: Date.now() };
        WeatherStorage.setCached(cacheKey, result);
        return result;
    }

    async reverseGeocode(latitude, longitude) {
        try {
            const params = new URLSearchParams({ lat: latitude, lon: longitude, format: "json", zoom: "10" });
            const response = await fetch(`${WeatherConfig.reverseGeocodingUrl}?${params}`, {
                headers: { "Accept-Language": "en" }
            });
            if (!response.ok) throw new Error();
            const data = await response.json();
            return {
                name: data.address.city || data.address.town || data.address.village || "Current location",
                admin1: data.address.state || "",
                country: data.address.country || "",
                latitude,
                longitude
            };
        } catch {
            return { name: "Current location", admin1: "", country: "", latitude, longitude };
        }
    }
}
