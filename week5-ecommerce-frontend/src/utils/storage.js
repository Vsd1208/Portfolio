export const storage = {
    read(key, fallback) {
        try {
            return JSON.parse(localStorage.getItem(key)) ?? fallback;
        } catch {
            return fallback;
        }
    },
    write(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    }
};

export const money = value => new Intl.NumberFormat("en-US", { style: "currency", currency: "USD" }).format(value);
