# Currency Converter

This is a simple currency converter Android application built with modern Android development technologies.

## Features

* Converts between different currencies using real-time exchange rates.
* Fetches currency data from the Open Exchange Rates API.
* Stores currency data locally for offline access and to reduce API calls.
* Implements a 30-minute caching mechanism to avoid frequent API hits.
* User-friendly interface built with Jetpack Compose.

## Technologies Used

* **Android Studio:** Koala Feature drop | 2024.1.2
* **UI:** Jetpack Compose
* **Data Fetching:** Open Exchange Rates API
* **Local Database:** Room
* **Data Storage:** Datastore
* **Dependency Injection:** Hilt
* **Asynchronous Operations:** Coroutines and Flows
* **Testing:**
    * MockK
    * Kotlin-coroutines-test
    * Turbine
    * Room-testing
    * JUnit

## Architecture

The app follows a clean architecture pattern with separate layers for presentation, domain, and data.

* **Presentation:** Jetpack Compose UI components and ViewModels.
* **Domain:** Use cases and business logic.
* **Data:** Repositories, data sources (API and local database).

## How to Use

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or device.
4. Enter the amount you want to convert.
5. Select the base currency and the target currency.
6. Click the "Convert" button.
7. The converted amount will be displayed.

## API Key

This app uses the Open Exchange Rates API. You'll need to obtain your own API key and replace the placeholder in the code.

## Contributions

Contributions are welcome! Feel free to open issues or submit pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.