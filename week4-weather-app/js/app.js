class WeatherApp {
    constructor() {
        this.service = new WeatherService();
        this.ui = new WeatherUI();
        this.settings = WeatherStorage.getSettings();
        this.favorites = WeatherStorage.getFavorites();
        this.currentLocation = WeatherConfig.defaultLocation;
        this.currentResult = null;
        this.searchTimer = null;
        this.init();
    }

    init() {
        this.settings.unit = this.settings.unit || "celsius";
        this.settings.theme = this.settings.theme || (new Date().getHours() >= 18 || new Date().getHours() < 6 ? "dark" : "light");
        this.ui.setUnit(this.settings.unit);
        this.ui.setTheme(this.settings.theme);
        this.ui.renderFavorites(this.favorites);
        this.bindEvents();

        const urlCity = new URLSearchParams(location.search).get("city");
        if (urlCity) {
            this.searchAndLoad(urlCity);
        } else {
            this.loadWeather(this.currentLocation);
        }
    }

    bindEvents() {
        this.ui.elements.form.addEventListener("submit", event => {
            event.preventDefault();
            const query = this.ui.elements.input.value.trim();
            if (query) this.searchAndLoad(query);
        });

        this.ui.elements.input.addEventListener("input", event => this.handleAutocomplete(event.target.value.trim()));
        this.ui.elements.suggestions.addEventListener("click", event => this.handleLocationClick(event));
        this.ui.elements.favorites.addEventListener("click", event => this.handleLocationClick(event));
        this.ui.elements.locationButton.addEventListener("click", () => this.useLocation());
        this.ui.elements.retry.addEventListener("click", () => this.loadWeather(this.currentLocation));
        this.ui.elements.favorite.addEventListener("click", () => this.toggleFavorite());
        this.ui.elements.shareButton.addEventListener("click", () => this.share());
        this.ui.elements.themeButton.addEventListener("click", () => this.toggleTheme());
        this.ui.elements.unitButtons.forEach(button => button.addEventListener("click", () => this.changeUnit(button.dataset.unit)));
        document.addEventListener("click", event => {
            if (!event.target.closest(".search-wrap")) this.ui.renderSuggestions([]);
        });
    }

    async loadWeather(location) {
        this.currentLocation = location;
        this.ui.showLoading(`Checking conditions in ${location.name}...`);
        try {
            this.currentResult = await this.service.getWeather(location);
            this.render();
        } catch (error) {
            this.ui.showError(error.message || "Check your connection and try again.");
        }
    }

    async searchAndLoad(query) {
        this.ui.showLoading(`Finding ${query}...`);
        try {
            const cities = await this.service.searchCities(query);
            if (!cities.length) throw new Error("No matching city was found. Try a nearby large city.");
            const city = cities[0];
            this.ui.elements.input.value = "";
            this.ui.renderSuggestions([]);
            await this.loadWeather({
                name: city.name,
                admin1: city.admin1 || "",
                country: city.country || "",
                latitude: city.latitude,
                longitude: city.longitude
            });
        } catch (error) {
            this.ui.showError(error.message || "City search failed.");
        }
    }

    handleAutocomplete(query) {
        window.clearTimeout(this.searchTimer);
        if (query.length < 2) {
            this.ui.renderSuggestions([]);
            return;
        }
        this.searchTimer = window.setTimeout(async () => {
            try {
                this.ui.renderSuggestions(await this.service.searchCities(query));
            } catch {
                this.ui.renderSuggestions([]);
            }
        }, WeatherConfig.autocompleteDelay);
    }

    handleLocationClick(event) {
        const button = event.target.closest("[data-location]");
        if (!button) return;
        this.ui.elements.input.value = "";
        this.ui.renderSuggestions([]);
        this.loadWeather(JSON.parse(button.dataset.location));
    }

    useLocation() {
        if (!navigator.geolocation) {
            this.ui.showError("Location detection is not supported by this browser.");
            return;
        }
        this.ui.showLoading("Waiting for location permission...");
        navigator.geolocation.getCurrentPosition(async position => {
            const location = await this.service.reverseGeocode(position.coords.latitude, position.coords.longitude);
            this.loadWeather(location);
        }, () => this.ui.showError("Location permission was denied or unavailable."), { timeout: 10000 });
    }

    changeUnit(unit) {
        this.settings.unit = unit;
        WeatherStorage.saveSettings(this.settings);
        this.ui.setUnit(unit);
        if (this.currentResult) this.render();
    }

    toggleTheme() {
        this.settings.theme = this.settings.theme === "dark" ? "light" : "dark";
        WeatherStorage.saveSettings(this.settings);
        this.ui.setTheme(this.settings.theme);
    }

    toggleFavorite() {
        const key = this.locationKey(this.currentLocation);
        const exists = this.favorites.some(item => this.locationKey(item) === key);
        this.favorites = exists
            ? this.favorites.filter(item => this.locationKey(item) !== key)
            : [this.currentLocation, ...this.favorites].slice(0, 8);
        WeatherStorage.saveFavorites(this.favorites);
        this.ui.renderFavorites(this.favorites);
        this.render();
    }

    async share() {
        const text = this.currentResult
            ? `${this.currentLocation.name}: ${this.ui.elements.currentTemp.textContent}, ${this.ui.elements.currentCondition.textContent}`
            : "Weather Dashboard";
        const url = `${location.href.split("?")[0]}?city=${encodeURIComponent(this.currentLocation.name)}`;
        try {
            if (navigator.share) {
                await navigator.share({ title: "Weather Dashboard", text, url });
            } else {
                await navigator.clipboard.writeText(`${text} ${url}`);
                this.ui.elements.shareButton.textContent = "Copied";
                window.setTimeout(() => { this.ui.elements.shareButton.textContent = "Share"; }, 1800);
            }
        } catch {
            // Closing the native share dialog is not an error the user needs to see.
        }
    }

    render() {
        const favorite = this.favorites.some(item => this.locationKey(item) === this.locationKey(this.currentLocation));
        this.ui.render(this.currentResult, this.settings.unit, favorite);
    }

    locationKey(location) {
        return `${Number(location.latitude).toFixed(3)},${Number(location.longitude).toFixed(3)}`;
    }
}

new WeatherApp();
