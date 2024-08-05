# clientGDX

clientGDX is a multi-platform game client built using libGDX, a popular cross-platform game development framework. This project provides the foundation for creating games that can run on various platforms such as desktop, Android, iOS, and web. Below is an overview of the project's features, technologies used, installation instructions, and usage guide.

## Features

- **Cross-Platform Compatibility**: Runs on desktop, Android, iOS, and HTML5.
- **Modular Architecture**: Organized into distinct modules for easy management and extension.
- **Customizable Game Logic**: Core module designed for custom game logic implementation.
- **Resource Management**: Efficient handling of game assets like textures, sounds, and fonts.

## Technologies Used

- Java
- libGDX Framework
- Gradle Build System

## Installation

1. **Clone the repository**:
   ```sh
   git clone https://github.com/andrii033/clientGDX.git
   ```
2. **Navigate to the project directory**:
   ```sh
   cd clientGDX
   ```
3. **Import the project into your IDE** (IntelliJ IDEA, Eclipse, etc.) as a Gradle project.
4. **Build the project**:
   ```sh
   ./gradlew build
   ```
5. **Run the application**:
   - For desktop:
     ```sh
     ./gradlew desktop:run
     ```
   - For Android:
     ```sh
     ./gradlew android:installDebug android:run
     ```

## Usage

1. **Launch the game client** on your preferred platform.
2. **Customize and extend** the game logic by modifying the core module.
3. **Add new assets** to the assets directory and reference them in your game code.

## Contribution

Contributions are welcome! To contribute:

1. **Fork the repository**.
2. **Create a new branch**:
   ```sh
   git checkout -b feature/new-feature
   ```
3. **Commit your changes**:
   ```sh
   git commit -am 'Add new feature'
   ```
4. **Push to the branch**:
   ```sh
   git push origin feature/new-feature
   ```
5. **Submit a pull request**.

## Acknowledgements

Special thanks to the libGDX community for their extensive documentation and support.
