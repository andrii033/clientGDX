clientGDX
clientGDX is a multi-platform game client built using libGDX, a popular cross-platform game development framework. This project provides the foundation for creating games that can run on various platforms such as desktop, Android, iOS, and web. Below is an overview of the project's features, technologies used, installation instructions, and usage guide.

Features
Cross-Platform Compatibility: Runs on desktop, Android, iOS, and HTML5.
Modular Architecture: Organized into distinct modules for easy management and extension.
Customizable Game Logic: Core module designed for custom game logic implementation.
Resource Management: Efficient handling of game assets like textures, sounds, and fonts.
Technologies Used
Java
libGDX Framework
Gradle Build System
Installation
Clone the repository:
sh
Copy code
git clone https://github.com/andrii033/clientGDX.git
Navigate to the project directory:
sh
Copy code
cd clientGDX
Import the project into your IDE (IntelliJ IDEA, Eclipse, etc.) as a Gradle project.
Build the project:
sh
Copy code
./gradlew build
Run the application:
For desktop:
sh
Copy code
./gradlew desktop:run
For Android:
sh
Copy code
./gradlew android:installDebug android:run
Usage
Launch the game client on your preferred platform.
Customize and extend the game logic by modifying the core module.
Add new assets to the assets directory and reference them in your game code.
Contribution
Contributions are welcome! To contribute:

Fork the repository.
Create a new branch:
sh
Copy code
git checkout -b feature/new-feature
Commit your changes:
sh
Copy code
git commit -am 'Add new feature'
Push to the branch:
sh
Copy code
git push origin feature/new-feature
Submit a pull request.
License
This project is licensed under the MIT License. See the LICENSE file for details.

Acknowledgements
Special thanks to the libGDX community for their extensive documentation and support.
