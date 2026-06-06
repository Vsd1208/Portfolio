# Weather Dashboard

## Project Description

A responsive weather application that retrieves live weather data from public REST APIs. It includes city autocomplete, current conditions, a seven-day forecast, unit conversion, browser location detection, favorites, sharing, loading and error states, and localStorage caching.

## Features

- Live current weather and seven-day forecast
- City search with autocomplete
- Celsius and Fahrenheit conversion
- Browser geolocation with reverse geocoding
- Favorite cities saved in localStorage
- Ten-minute API response cache
- Share API with clipboard fallback
- Automatic time-based theme and manual theme toggle
- Responsive desktop, tablet, and mobile design
- Accessible loading, error, retry, and empty states

## APIs Used

- [Open-Meteo Forecast API](https://open-meteo.com/en/docs) for weather data
- [Open-Meteo Geocoding API](https://open-meteo.com/en/docs/geocoding-api) for city search
- [OpenStreetMap Nominatim](https://nominatim.org/release-docs/latest/api/Reverse/) for reverse geocoding

These public APIs do not require an API key, so the project works immediately without exposing secrets.

## How to Run

1. Open the `week4-weather-app` folder.
2. Serve the folder with VS Code Live Server or another local web server.
3. Open the provided local URL in a browser.
4. Search for a city, select an autocomplete result, or use browser location.

Opening `index.html` directly can load weather data, but browser location and clipboard features usually require localhost or HTTPS.

## Project Structure

```text
week4-weather-app/
|-- index.html
|-- css/
|   |-- style.css
|   |-- weather-icons.css
|   `-- responsive.css
|-- js/
|   |-- app.js
|   |-- weatherService.js
|   |-- ui.js
|   |-- storage.js
|   `-- config.js
|-- assets/
|   |-- icons/
|   `-- images/
|-- README.md
|-- .env.example
`-- .gitignore
```

## Code Structure

- `weatherService.js` contains API calls, query parameters, response checks, and reverse geocoding.
- `storage.js` handles cached responses, favorites, units, and theme preferences.
- `ui.js` renders weather conditions, forecast cards, favorites, autocomplete, and status states.
- `app.js` coordinates events, API calls, location detection, sharing, and app state.
- `config.js` stores endpoints, the default city, cache duration, and autocomplete delay.

## Technical Notes

Weather data is requested in Celsius and converted in the UI when Fahrenheit is selected. API results are cached by latitude and longitude for ten minutes. Autocomplete requests are debounced to reduce unnecessary network traffic. All API errors are caught and presented with a retry action.

## Testing Evidence

- Loaded default live weather and seven-day forecast
- Searched cities and selected autocomplete suggestions
- Switched between Celsius and Fahrenheit
- Refreshed and confirmed cached responses and settings persist
- Added and removed favorite cities
- Tested error and retry states with the network disabled
- Tested location permission accepted and denied
- Checked responsive layouts at desktop, tablet, and mobile widths

## Quality Checklist

- [x] Real public REST API integration
- [x] Current weather and forecast
- [x] Search with autocomplete
- [x] Temperature unit conversion
- [x] Responsive design
- [x] API error handling and loading states
- [x] localStorage caching and favorites
- [x] Location detection and share functionality
- [x] Dark and light themes
