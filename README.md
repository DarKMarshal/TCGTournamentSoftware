# TCG Tournament Software

A Java-based tournament management system for Trading Card Games that handles player registration, tournament organization, and results tracking.

Project is archived to preserve the state as submitted.

## Features

- **Player Management**: Register and track players across multiple tournaments
- **Tournament Organization**: Support for multiple division types (Age-based, Casual, Challenge, Cup)
- **Points Calculation**: Automatic point calculation based on tournament type and placement
- **Results Tracking**: Record and store player results for each tournament
- **File-based database**: Persistent storage of player and tournament data
- **Search & Comparison**: Find players and compare tournament results

## Project Structure

- `Player.java` - Player entity with ID and name
- `Tournament.java` - Main tournament container
- `DivisionTournament.java` - Individual tournament divisions
- `AgeDivision.java` - Age-based division types
- `PlayerResult.java` - Player performance data
- `database.java` - Singleton file-based storage system
- `pointCalculator.java` - Interface for scoring systems
- Point calculator implementations:
  - `CasualPointCalculator.java`
  - `ChallengePointCalculator.java`
  - `CupPointCalculator.java`
- Service classes:
  - `ResultEntryService.java`
  - `SearchService.java`
  - `ComparisonService.java`
- `menuHandler.java` - User interface navigation

## Requirements

- Java 21 or higher

## Usage

The application uses a menu-driven interface. On startup, it automatically connects to the TCGTournamentSoftware.database and loads existing data. All changes are persisted when the application closes.
