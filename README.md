# Crypto Price Target Alerts
### Introduction
Crypto Price Target Alerts is an Android application designed to help cryptocurrency enthusiasts track their investment targets effortlessly.It allows users to browse, select, and search for crypto assets and set price targets for them. When a price target is reached, the app sends a notification and plays a sound to alert the user.

**Google Play Store:** [https://play.google.com/store/apps/details?id=com.owusu.cryptosignalalert&hl=en-IN](https://play.google.com/store/apps/details?id=com.owusu.cryptosignalalert&hl=en-IN)

### Features
* **Browse and Search:** Easily find and select crypto assets from a comprehensive list.
* **Set Price Targets:** Define price targets for your chosen assets.
* **Real-time Notifications:** Receive instant notifications and sound alerts when your price targets are met.
* **Background Sync:** The app automatically syncs in the background to check for price updates.
* **User-Friendly Interface:** Built with Jetpack Compose for a modern and intuitive experience.

### Architecture
The app follows Clean Architecture principles, ensuring a clear separation of concerns and maintainability. It is divided into three main layers:

* **Presentation Layer:**  Implemented with Jetpack Compose, handling UI rendering, navigation, and user interactions. Utilizes ViewModels and Unidirectional Data Flow (UDF) for state management.
* **Domain Layer:**  Contains the core business logic in the form of Use Cases, independent of any specific Android framework.
* **Data Layer:**  Manages data persistence and retrieval, interacting with APIs and Room database.

### Installation
Download and install the app from the Google Play Store: [https://play.google.com/store/apps/details?id=com.owusu.cryptosignalalert&hl=en-IN](https://play.google.com/store/apps/details?id=com.owusu.cryptosignalalert&hl=en-IN)

### Usage
1. **Browse or Search:** Find the crypto asset you want to track.
2. **Set Price Targets:**  Add price targets for the asset.
3. **Receive Alerts:**  Get notifiedwhen your price targets are reached.

### API Rate Limit Workaround
Due to the use of an unpaid API with a rate limit, the app syncs price targets in batches to avoid exceeding the limit. A sync icon is displayed on the Price Targets screen during this process.

### Testing
* **Instrumented Unit Tests:**  Test the data layer against a mock web server.
* **Unit Tests:**  Cover all use cases in the domain layer.
* **UI Tests:**  Planned for future development.

### Contributing
Contributions are welcome! Please feel free to submit issues or pull requests.

### License
This project is currently closed source.