# TCG Tournament Software - Submission README

## 1. Project Overview

### Description
TCG Tournament Software is a console-based tournament management system designed for Trading Card Game (TCG) tournament organizers and players. The software tracks tournament results across multiple age divisions (Junior, Senior, Master) and tournament types (Casual, Challenge, Cup), automatically calculating and persisting championship points for players based on their performance.

### Core Features
- **Tournament Management**: Create and store tournaments with multiple age divisions
- **Championship Point Calculation**: Automatically calculates points using different algorithms based on tournament type (Casual, Challenge, Cup)
- **Player Tracking**: Maintains persistent player records with cumulative championship points across all tournaments
- **Search & Comparison**: Search tournaments by name or ID, compare player performance, and view leaderboards
- **SQLite Persistence**: All tournament and player data is stored in a local SQLite database

### Assumptions and Constraints
- Player IDs are unique and manually assigned (not auto-generated)
- Tournament dates are not currently tracked in the system
- Championship points are calculated immediately upon tournament entry and added to player totals
- The system uses SQLite JDBC driver version 3.45.1.0
- Currently, if the program asks for a number and the user enters letters, the program may crash or behave unexpectedly

---

## 2. Build & Run Instructions

### Tools and Versions
- **JDK Version**: Java 17 or higher
- **IDE**: JetBrains IntelliJ IDEA (recommended) or any Java IDE
- **Build Tool**: Standard Java compilation (no Maven/Gradle required)
- **Database**: SQLite JDBC Driver 3.45.1.0 (included in `lib/sqlite-jdbc-3.45.1.0.jar`)
- **Additional Libraries**: 
  - slf4j-api-2.0.9.jar
  - slf4j-nop-2.0.9.jar

### Compilation and Execution Steps

#### From IntelliJ IDEA:
1. Clone or extract the project to your local machine
2. Open the project in IntelliJ IDEA
3. Ensure the JDBC driver is in the classpath:
   - Project Structure → Libraries → Add `lib/sqlite-jdbc-3.45.1.0.jar`
   - Add `lib/slf4j-api-2.0.9.jar` and `lib/slf4j-nop-2.0.9.jar`
4. Set the main class to `TCGTournamentSoftware.TCGTournamentSoftware`
5. Run the application (Shift+F10)

#### From Command Line:
```bash
# Navigate to project directory
cd /path/to/TCGTournamentSoftware

# Compile all source files
javac -cp "lib/*" -d out src/TCGTournamentSoftware/*.java

# Run the application
java -cp "out:lib/*" TCGTournamentSoftware.TCGTournamentSoftware

# if running the jar file, use the following command instead:
java -jar TCGTournamentSoftware.jar
```

### Configuration
- No configuration files are required
- The database file `tournament.db` will be created automatically on first run in the project root directory
- If you need to suppress warnings about native access, add this JVM argument: `--enable-native-access=ALL-UNNAMED`


### Provided Artifacts
- A pre-built jar file is available in the `docs` folder if there are any issues getting the files to compile.
- Although the application is set up to automatically create the database file, there is a database file already 
included in the project root directory (`tournament.db`) pre-populated with real data.
- If one wants to test the application with blank data, simply delete the `tournament.db` file and it will be recreated on the next run.
- Test Player IDs: 1, 2, 3, 4

### Test Data
There is preloaded test data. The IDs are as follows 
- ID: 1 Name: Test Cup | This is test data
- ID: 2 Name: Trailside Challenge | This is real data from a tournament I ran
- ID: 3 Name: Trailside Weekly | This is real data from a tournament I ran

---

## 3. Required OOP Features (with File & Line References)

| OOP Feature | File Name | Line Numbers | Reasoning / Purpose |
|-------------|-----------|--------------|---------------------|
| **Inheritance #1** | `CasualPointCalculator.java` | 5 | CasualPointCalculator implements the `pointCalculator` interface, inheriting the contract to calculate championship points |
| **Inheritance #2** | `ChallengePointCalculator.java` | 5 | ChallengePointCalculator implements the `pointCalculator` interface, providing a different implementation for Challenge tournaments |
| **Inheritance #3** | `CupPointCalculator.java` | 5 | CupPointCalculator implements the `pointCalculator` interface, providing the highest point values for Cup tournaments |
| **Interface #1** | `pointCalculator.java` | 3-5 | Defines a contract for championship point calculation algorithms, allowing different tournament types to use different strategies |
| **Interface #2** | `iTournament.java` | 7-13 | Defines a contract for tournament objects to ensure consistent structure (getId, getName, getDate, getResults, calculateChampionshipPoints) |
| **Interface #3** | `iDatabase.java` | 3-8 | Defines a contract for database operations (connect, disconnect, saveTournament, savePlayer), enabling potential database implementation swapping |
| **Polymorphism #1** | `DivisionTournament.java` | 46 | The `pointCalculator` reference can hold any implementation (Casual, Challenge, Cup), demonstrating runtime polymorphism when calling calculateChampionshipPoints() |
| **Polymorphism #2** | `DivisionTournament.java` | 39-45 | Factory method `createPointCalculator()` returns different `pointCalculator` implementations based on tournament type, enabling dynamic dispatch |
| **Access Modifiers** | `Player.java` | 6-8, 11-20 | Private fields (id, name, championshipPoints) encapsulate player data; package-private constructors prevent external instantiation; public static `getOrCreate()` method controls object creation |
| **Access Modifiers** | `database.java` | 9-11 | Private constructor and static INSTANCE field implement Singleton pattern, preventing multiple database connections |
| **Enum** | `AgeDivision.java` | 3 | Defines three age divisions (Junior, Senior, Master) as type-safe constants for tournament classification |
| **Data Structure #1** | `Tournament.java` | 8 | Uses `List<DivisionTournament>` to store multiple division tournaments within a single tournament event |
| **Data Structure #2** | `database.java` | 146-148 | Uses `Map<Integer, Player>` to efficiently store and retrieve players by their ID |
| **Data Structure #3** | `ResultEntryService.java` | 18-19 | Uses `ArrayList<PlayerResult>` and `ArrayList<DivisionTournament>` to collect results and divisions during data entry |
| **I/O - Console Input** | `menuHandler.java` | 15-17 | Uses `Scanner` to read user menu selections from console input |
| **I/O - Console Output** | `SearchService.java` | 66-76 | Uses `System.out.printf()` to display formatted tournament results to the console |
| **I/O - File/Database** | `database.java` | 21-35 | Uses JDBC to connect to SQLite database file, providing persistent storage for tournaments and players |

---

## 4. Design Patterns (with File & Line References)

| Pattern Name | Category | File Name | Line Numbers | Rationale |
|--------------|----------|-----------|--------------|-----------|
| **Singleton** | Creational | `database.java` | 9-18 | The database class uses the Singleton pattern to ensure only one database connection exists throughout the application lifecycle. This prevents multiple connections from being opened, which could cause concurrency issues or resource exhaustion. The private constructor and static INSTANCE field guarantee that `getInstance()` always returns the same instance. |
| **Strategy** | Behavioral | `pointCalculator.java`, `CasualPointCalculator.java`, `ChallengePointCalculator.java`, `CupPointCalculator.java` | pointCalculator: 3-5<br>Casual: 6-14<br>Challenge: 6-40<br>Cup: 6-46 | The Strategy pattern encapsulates different championship point calculation algorithms into separate classes that implement the `pointCalculator` interface. This allows `DivisionTournament` to select the appropriate algorithm at runtime based on tournament type without using complex conditional logic. Each strategy (Casual, Challenge, Cup) can be modified independently. |
| **Factory Method** | Creational | `DivisionTournament.java` | 39-45 | The `createPointCalculator()` method acts as a factory method that instantiates the correct `pointCalculator` implementation based on the tournament type string. This encapsulates object creation logic, making it easy to add new tournament types without modifying client code. The factory method isolates the decision-making process for which concrete class to instantiate. |
| **Static Factory** | Creational | `Player.java`, `Tournament.java` | Player: 24-34<br>Tournament: 18-41 | Both Player and Tournament classes use static factory methods (`getOrCreate()`) instead of public constructors. This pattern provides more flexibility than constructors by allowing the methods to return existing objects from the database if they already exist, or create new ones if needed. This ensures object uniqueness and prevents duplicate entries while maintaining clean separation of concerns. |

---

## 5. Design Decisions

### Architecture Overview
The application follows a **service-oriented architecture** with clear separation between data access, business logic, and presentation layers:

- **Data Layer**: The `database` class (implementing `iDatabase`) handles all persistence operations using SQLite
- **Domain Layer**: `Player`, `Tournament`, `DivisionTournament`, and `PlayerResult` represent core business entities
- **Service Layer**: `ResultEntryService`, `SearchService`, and `ComparisonService` encapsulate business operations
- **Presentation Layer**: `menuHandler` manages console UI and user interaction

### Key Abstractions

#### 1. Point Calculation Strategy
The decision to use the Strategy pattern for point calculation was driven by the need to support three distinct tournament types with significantly different point structures. This approach allows:
- Easy addition of new tournament types without modifying existing code
- Clear separation of calculation logic for each tournament type
- Testability of individual calculation algorithms

#### 2. Singleton Database Connection
Using the Singleton pattern for database management ensures:
- Single connection throughout the application lifecycle
- Prevention of connection leaks and resource exhaustion
- Centralized database initialization and cleanup
- Global access point without passing references through multiple layers

#### 3. Factory Methods for Object Creation
The `getOrCreate()` pattern for Player and Tournament objects solves several problems:
- **Deduplication**: Prevents creating duplicate players or tournaments by checking the database first
- **Encapsulation**: Hides complex initialization logic from client code
- **Consistency**: Ensures all objects are properly persisted upon creation

#### 4. Interface-Based Design
Interfaces (`pointCalculator`, `iTournament`, `iDatabase`) provide:
- **Flexibility**: Easy to swap implementations (e.g., different database backends)
- **Testability**: Mock implementations can be created for unit testing
- **Contract Enforcement**: Guarantees that all implementations provide required methods

### Tradeoffs

#### 1. Auto-commit vs. Transactional Saves
The system uses transactions when saving tournaments (see `database.saveTournament()` lines 177-208) to ensure atomicity. However, individual player saves use auto-commit for simplicity. This tradeoff prioritizes tournament data integrity while accepting some risk for individual player operations.

#### 2. Eager Loading vs. Lazy Loading
The current implementation eagerly loads all divisions and results when retrieving a tournament. While this increases initial load time, it simplifies the code and ensures all data is available immediately, which suits the small-scale nature of tournament data.

#### 3. In-Memory vs. Database Queries
Player and tournament comparisons load data into memory (using `Map<Integer, Player>`) rather than using complex SQL queries. This trades memory usage for simpler code and better Java performance, which is acceptable given the expected dataset size.

#### 4. Console UI vs. GUI
The console-based interface was chosen for:
- Rapid development and testing
- Lower complexity (no GUI framework dependencies)
- Suitability for demonstration purposes
A GUI could be added later by keeping the service layer intact and adding a new presentation layer.

### Future Enhancements
- Add tournament date tracking (infrastructure already exists in interfaces)
- Add functionality for importing tournament data from official .tdf (XML) files
- Implement player statistics (win rate, average placement)
- Add tournament editing and deletion capabilities
- Create a GUI using JavaFX or Swing
- Add export functionality (CSV, PDF reports)
- Implement user authentication for multi-user environments

