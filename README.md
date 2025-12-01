# TCG TCGTournamentSoftware.Tournament Software

A Java-based tournament management system for Trading Card Games that handles player registration, tournament organization, and results tracking.

## Features

- **TCGTournamentSoftware.Player Management**: Register and track players across multiple tournaments
- **TCGTournamentSoftware.Tournament Organization**: Support for multiple division types (Age-based, Casual, Challenge, Cup)
- **Points Calculation**: Automatic point calculation based on tournament type and placement
- **Results Tracking**: Record and store player results for each tournament
- **File-based TCGTournamentSoftware.database**: Persistent storage of player and tournament data
- **Search & Comparison**: Find players and compare tournament results

## Project Structure

- `TCGTournamentSoftware.Player.java` - TCGTournamentSoftware.Player entity with ID and name
- `TCGTournamentSoftware.Tournament.java` - TCGTournamentSoftware.Main tournament container
- `TCGTournamentSoftware.DivisionTournament.java` - Individual tournament divisions
- `TCGTournamentSoftware.AgeDivision.java` - Age-based division types
- `TCGTournamentSoftware.PlayerResult.java` - TCGTournamentSoftware.Player performance data
- `TCGTournamentSoftware.database.java` - Singleton file-based storage system
- `TCGTournamentSoftware.pointCalculator.java` - Interface for scoring systems
- Point calculator implementations:
  - `TCGTournamentSoftware.CasualPointCalculator.java`
  - `TCGTournamentSoftware.ChallengePointCalculator.java`
  - `TCGTournamentSoftware.CupPointCalculator.java`
- Service classes:
  - `TCGTournamentSoftware.ResultEntryService.java`
  - `TCGTournamentSoftware.SearchService.java`
  - `TCGTournamentSoftware.ComparisonService.java`
- `TCGTournamentSoftware.menuHandler.java` - User interface navigation

## Requirements

- Java 21 or higher

## Usage

The application uses a menu-driven interface. On startup, it automatically connects to the TCGTournamentSoftware.database and loads existing data. All changes are persisted when the application closes.