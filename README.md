# Product Favorites App

This is a **Product Favorites App** built with modern Android development practices. It allows users to browse products, add/remove them from favorites, and view product details. The app demonstrates the use of **MVVM architecture**, **Room database**, **Retrofit**, **Koin for dependency injection**, and more.

---

## Features

- **Home Screen**:
  - View a list of products.
  - Add or remove products from favorites.
  - Shimmer effect while loading data.

- **Favorites Screen**:
  - View a list of favorited products.
  - Remove products from favorites.

- **Product Details Screen**:
  - View detailed information about a product.
  - Add or remove the product from favorites.
  - View product images in a **ViewPager**.

- **Splash Screen**:
  - Lottie animation for a smooth and engaging splash screen.

- **Offline Support**:
  - Products and favorites are stored locally using **Room Database**.

- **API Integration**:
  - Fetch products from a remote API using **Retrofit**.

- **Image Loading**:
  - Load product images using **Coil** or **Glide**.

- **State Management**:
  - Use **LiveData** and **StateFlow** for managing UI state.

---

## Technologies Used

- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room
- **Dependency Injection**: Koin
- **Networking**: Retrofit
- **Image Loading**: Coil / Glide
- **UI Components**: RecyclerView, ViewPager, Shimmer Effect
- **Animation**: Lottie
- **State Management**: LiveData, StateFlow
- **Other Libraries**:
  - ViewBinding
  - Navigation Component
  - Kotlin Coroutines

---

## Project Structure

The project is organized into the following packages:

- **`data`**:
  - `local`: Room database entities, DAOs, and repositories.
  - `remote`: Retrofit API service, DTOs, and repositories.
  - `repository`: Repository implementations for handling data.

- **`di`**:
  - Koin modules for dependency injection.

- **`ui`**:
  - `home`: Home screen with product list and favorites functionality.
  - `favorites`: Favorites screen.
  - `details`: Product details screen with ViewPager for images.
  - `splash`: Splash screen with Lottie animation.

- **`utils`**:
  - Utility classes and extensions.

- **`viewmodel`**:
  - ViewModel classes for each screen.

## Screenshots

<p float="left">
  <img src="https://github.com/user-attachments/assets/b75f85ac-2067-472e-a6b7-59ade7535636" width="200" />
  <img src="https://github.com/user-attachments/assets/4c2a9283-430d-411d-93e9-878385362cf9" width="200" /> 
  <img src="https://github.com/user-attachments/assets/f4701cef-ba42-418c-b309-bcf6bf72151a" width="200" /> 
</p>
<p float="left">
  <img src="https://github.com/user-attachments/assets/1a70a5ae-b8d7-4688-a30d-058fa837d75d" width="200" />
  <img src="https://github.com/user-attachments/assets/8b12a267-b889-4615-986a-c19d183e9b65" width="200" /> 
  <img src="https://github.com/user-attachments/assets/8382bfb4-04ae-4d6e-b0dc-6cb11b822111" width="200" /> 
</p>
<p float="left">
  <img src="https://github.com/user-attachments/assets/953ca10d-ba07-4075-b02f-a588b302082c" width="200" />
  <img src="https://github.com/user-attachments/assets/128add3b-08fd-4881-a4e1-579d234738b9" width="200" /> 
  <img src="https://github.com/user-attachments/assets/f009936e-875e-457f-902e-ffe9f621eda7" width="200" /> 
</p>


## Setup Instructions

### Prerequisites

- Android Studio (latest version recommended)
- Kotlin 1.8 or higher
- Android SDK 33 or higher

