class WeatherUI {
    constructor() {
        this.elements = {
            form: document.getElementById("searchForm"),
            input: document.getElementById("searchInput"),
            suggestions: document.getElementById("suggestions"),
            locationButton: document.getElementById("locationButton"),
            unitButtons: [...document.querySelectorAll(".unit-button")],
            themeButton: document.getElementById("themeButton"),
            themeLabel: document.getElementById("themeLabel"),
            shareButton: document.getElementById("shareButton"),
            status: document.getElementById("statusPanel"),
            statusSpinner: document.getElementById("statusSpinner"),
            statusTitle: document.getElementById("statusTitle"),
            statusMessage: document.getElementById("statusMessage"),
            retry: document.getElementById("retryButton"),
            content: document.getElementById("weatherContent"),
            locationName: document.getElementById("locationName"),
            updatedAt: document.getElementById("updatedAt"),
            favorite: document.getElementById("favoriteButton"),
            currentIcon: document.getElementById("currentIcon"),
            currentTemp: document.getElementById("currentTemp"),
            currentCondition: document.getElementById("currentCondition"),
            feelsLike: document.getElementById("feelsLike"),
            humidity: document.getElementById("humidity"),
            wind: document.getElementById("wind"),
            pressure: document.getElementById("pressure"),
            visibility: document.getElementById("visibility"),
            uvIndex: document.getElementById("uvIndex"),
            forecast: document.getElementById("forecastGrid"),
            cacheNote: document.getElementById("cacheNote"),
            favorites: document.getElementById("favoritesList"),
            emptyFavorites: document.getElementById("emptyFavorites")
        };
    }

    render(result, unit, isFavorite) {
        const { weather, location } = result;
        const current = weather.current;
        const daily = weather.daily;
        const unitMark = unit === "celsius" ? "C" : "F";

        this.elements.locationName.textContent = [location.name, location.admin1, location.country].filter(Boolean).join(", ");
        this.elements.updatedAt.textContent = `Updated ${new Date(result.cachedAt).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}`;
        this.elements.currentTemp.textContent = `${this.temp(current.temperature_2m, unit)}\u00B0`;
        this.elements.currentCondition.textContent = this.weatherLabel(current.weather_code);
        this.elements.currentIcon.className = `weather-icon hero-icon ${this.iconClass(current.weather_code)}`;
        this.elements.feelsLike.textContent = `${this.temp(current.apparent_temperature, unit)}\u00B0${unitMark}`;
        this.elements.humidity.textContent = `${current.relative_humidity_2m}%`;
        this.elements.wind.textContent = `${Math.round(current.wind_speed_10m)} km/h ${this.windDirection(current.wind_direction_10m)}`;
        this.elements.pressure.textContent = `${Math.round(current.pressure_msl)} hPa`;
        this.elements.visibility.textContent = `${(current.visibility / 1000).toFixed(1)} km`;
        this.elements.uvIndex.textContent = `${Math.round(daily.uv_index_max[0])} ${this.uvLabel(daily.uv_index_max[0])}`;
        this.elements.cacheNote.textContent = result.fromCache ? "Loaded from 10-minute cache" : "Live weather data";
        this.setFavorite(isFavorite);

        this.elements.forecast.replaceChildren(...daily.time.map((date, index) => {
            const card = document.createElement("article");
            card.className = `forecast-card${index === 0 ? " today" : ""}`;
            const day = index === 0 ? "Today" : new Date(`${date}T12:00:00`).toLocaleDateString("en-US", { weekday: "short" });
            card.innerHTML = `
                <span class="forecast-day">${day}</span>
                <span class="weather-icon ${this.iconClass(daily.weather_code[index])}" aria-hidden="true"></span>
                <div><span class="forecast-high">${this.temp(daily.temperature_2m_max[index], unit)}&deg;</span> <span class="forecast-low">${this.temp(daily.temperature_2m_min[index], unit)}&deg;</span></div>
                <p class="forecast-condition">${this.weatherLabel(daily.weather_code[index])}</p>
            `;
            return card;
        }));

        this.hideStatus();
        this.elements.content.classList.remove("hidden");
    }

    renderSuggestions(cities) {
        this.elements.suggestions.replaceChildren(...cities.map(city => {
            const item = document.createElement("li");
            const button = document.createElement("button");
            button.type = "button";
            button.dataset.location = JSON.stringify({
                name: city.name,
                admin1: city.admin1 || "",
                country: city.country || "",
                latitude: city.latitude,
                longitude: city.longitude
            });
            button.textContent = [city.name, city.admin1, city.country].filter(Boolean).join(", ");
            item.append(button);
            return item;
        }));
        this.elements.input.setAttribute("aria-expanded", String(cities.length > 0));
    }

    renderFavorites(favorites) {
        this.elements.favorites.replaceChildren(...favorites.map(location => {
            const button = document.createElement("button");
            button.className = "favorite-chip";
            button.type = "button";
            button.dataset.location = JSON.stringify(location);
            button.textContent = location.name;
            return button;
        }));
        this.elements.emptyFavorites.classList.toggle("hidden", favorites.length > 0);
    }

    showLoading(message = "Fetching the latest conditions...") {
        this.elements.status.className = "status-panel";
        this.elements.statusSpinner.classList.remove("hidden");
        this.elements.retry.classList.add("hidden");
        this.elements.statusTitle.textContent = "Loading weather";
        this.elements.statusMessage.textContent = message;
    }

    showError(message) {
        this.elements.status.className = "status-panel error";
        this.elements.statusSpinner.classList.add("hidden");
        this.elements.retry.classList.remove("hidden");
        this.elements.statusTitle.textContent = "Could not load weather";
        this.elements.statusMessage.textContent = message;
    }

    hideStatus() { this.elements.status.classList.add("hidden"); }

    setUnit(unit) {
        this.elements.unitButtons.forEach(button => button.classList.toggle("active", button.dataset.unit === unit));
    }

    setTheme(theme) {
        document.documentElement.dataset.theme = theme;
        this.elements.themeLabel.textContent = theme === "dark" ? "Light" : "Dark";
    }

    setFavorite(active) {
        this.elements.favorite.classList.toggle("active", active);
        this.elements.favorite.innerHTML = active ? "&#9733;" : "&#9734;";
        this.elements.favorite.setAttribute("aria-label", active ? "Remove city from favorites" : "Add city to favorites");
    }

    temp(value, unit) { return Math.round(unit === "fahrenheit" ? (value * 9 / 5) + 32 : value); }
    windDirection(degrees) { return ["N", "NE", "E", "SE", "S", "SW", "W", "NW"][Math.round(degrees / 45) % 8]; }
    uvLabel(value) { return value < 3 ? "Low" : value < 6 ? "Moderate" : value < 8 ? "High" : "Very high"; }

    weatherLabel(code) {
        if (code === 0) return "Clear sky";
        if (code <= 2) return "Partly cloudy";
        if (code === 3) return "Overcast";
        if ([45, 48].includes(code)) return "Foggy";
        if ([51, 53, 55, 56, 57].includes(code)) return "Drizzle";
        if ([61, 63, 65, 66, 67, 80, 81, 82].includes(code)) return "Rain showers";
        if ([71, 73, 75, 77, 85, 86].includes(code)) return "Snow showers";
        if (code >= 95) return "Thunderstorms";
        return "Mixed conditions";
    }

    iconClass(code) {
        if (code === 0) return "icon-sun";
        if (code <= 2) return "icon-partly";
        if (code === 3) return "icon-cloud";
        if ([45, 48].includes(code)) return "icon-fog";
        if ([71, 73, 75, 77, 85, 86].includes(code)) return "icon-snow";
        if (code >= 95) return "icon-storm";
        return "icon-rain";
    }
}
