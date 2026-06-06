const WeatherStorage = {
    keys: {
        cache: "week4-weather.cache",
        favorites: "week4-weather.favorites",
        settings: "week4-weather.settings"
    },

    read(key, fallback) {
        try {
            const value = JSON.parse(localStorage.getItem(key));
            return value ?? fallback;
        } catch {
            return fallback;
        }
    },

    write(key, value) {
        try {
            localStorage.setItem(key, JSON.stringify(value));
        } catch {
            // The app still works when private browsing blocks storage.
        }
    },

    getCached(key) {
        const cache = this.read(this.keys.cache, {});
        const item = cache[key];
        if (!item || Date.now() - item.savedAt > WeatherConfig.cacheDuration) {
            return null;
        }
        return item;
    },

    setCached(key, data) {
        const cache = this.read(this.keys.cache, {});
        cache[key] = { data, savedAt: Date.now() };
        this.write(this.keys.cache, cache);
    },

    getFavorites() {
        return this.read(this.keys.favorites, []);
    },

    saveFavorites(favorites) {
        this.write(this.keys.favorites, favorites);
    },

    getSettings() {
        return this.read(this.keys.settings, { unit: "celsius", theme: null });
    },

    saveSettings(settings) {
        this.write(this.keys.settings, settings);
    }
};
