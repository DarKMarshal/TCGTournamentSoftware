package TCGTournamentSoftware;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class TdfParser {

    public static Tournament parseTdfFile(String filepath) throws Exception {
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // Extract tournament name and ID
        String tournamentName = getElementText(doc, "name");
        String tournamentId = getElementText(doc, "id");

        System.out.println("Parsing tournament: " + tournamentName + " (" + tournamentId + ")");

        // Determine tournament type from mode
        String tournamentType = determineTournamentType(doc);
        System.out.println("Tournament type: " + tournamentType);

        // Parse players first
        Map<String, Player> playerMap = parsePlayers(doc);
        System.out.println("Parsed " + playerMap.size() + " players");

        // Calculate match statistics per player from rounds
        Map<String, PlayerMatchStats> playerStats = calculatePlayerStats(doc);

        // Parse divisions and standings
        List<DivisionTournament> divisions = parseDivisions(doc, playerMap, playerStats, tournamentType);
        System.out.println("Parsed " + divisions.size() + " divisions");

        Tournament tournament = Tournament.getOrCreate(tournamentName + " (" + tournamentId + ")", divisions);
        return tournament;
    }

    private static String determineTournamentType(Document doc) {
        String mode = doc.getDocumentElement().getAttribute("mode");
        if (mode.contains("TCG1DAY")) return "Cup";
        if (mode.contains("CHALLENGE")) return "Challenge";
        return "Casual";
    }

    private static Map<String, Player> parsePlayers(Document doc) {
        Map<String, Player> playerMap = new HashMap<>();
        NodeList playerNodes = doc.getElementsByTagName("player");

        for (int i = 0; i < playerNodes.getLength(); i++) {
            Node node = playerNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            if (!node.getParentNode().getNodeName().equals("players")) continue;

            Element playerElement = (Element) node;
            String userId = playerElement.getAttribute("userid");
            if (userId.isEmpty()) continue;

            String firstName = getElementText(playerElement, "firstname");
            String lastName = getElementText(playerElement, "lastname");
            String fullName = firstName + " " + lastName;

            int playerId = Integer.parseInt(userId);
            Player player = Player.getOrCreate(playerId, fullName);
            playerMap.put(userId, player);
        }

        return playerMap;
    }

    private static Map<String, PlayerMatchStats> calculatePlayerStats(Document doc) {
        Map<String, PlayerMatchStats> statsMap = new HashMap<>();

        NodeList roundNodes = doc.getElementsByTagName("round");

        for (int i = 0; i < roundNodes.getLength(); i++) {
            Element roundElement = (Element) roundNodes.item(i);
            NodeList matchNodes = roundElement.getElementsByTagName("match");

            for (int j = 0; j < matchNodes.getLength(); j++) {
                Element matchElement = (Element) matchNodes.item(j);
                String outcome = matchElement.getAttribute("outcome");

                // outcome: 1 = player1 wins, 2 = player2 wins, 3 = tie, 5 = bye
                if (outcome.equals("5")) {
                    // Handle bye
                    String playerId = getElementAttribute(matchElement, "player", "userid");
                    if (!playerId.isEmpty()) {
                        PlayerMatchStats stats = statsMap.computeIfAbsent(playerId, k -> new PlayerMatchStats());
                        stats.addMatchPoints(3); // Bye gives 3 points
                    }
                } else {
                    String player1Id = getElementAttribute(matchElement, "player1", "userid");
                    String player2Id = getElementAttribute(matchElement, "player2", "userid");

                    if (!player1Id.isEmpty() && !player2Id.isEmpty()) {
                        PlayerMatchStats stats1 = statsMap.computeIfAbsent(player1Id, k -> new PlayerMatchStats());
                        PlayerMatchStats stats2 = statsMap.computeIfAbsent(player2Id, k -> new PlayerMatchStats());

                        stats1.addMatch();
                        stats2.addMatch();

                        if (outcome.equals("1")) {
                            stats1.addMatchPoints(3);
                            stats1.addWin();
                            stats2.addMatchPoints(0);
                        } else if (outcome.equals("2")) {
                            stats2.addMatchPoints(3);
                            stats2.addWin();
                            stats1.addMatchPoints(0);
                        } else if (outcome.equals("3")) {
                            stats1.addMatchPoints(1);
                            stats2.addMatchPoints(1);
                        }

                        stats1.addOpponent(player2Id);
                        stats2.addOpponent(player1Id);
                    }
                }
            }
        }

        // Calculate opponent win percentages
        for (Map.Entry<String, PlayerMatchStats> entry : statsMap.entrySet()) {
            PlayerMatchStats stats = entry.getValue();
            double totalOpponentWinPct = 0;
            int opponentCount = 0;

            for (String opponentId : stats.getOpponents()) {
                PlayerMatchStats opponentStats = statsMap.get(opponentId);
                if (opponentStats != null) {
                    totalOpponentWinPct += opponentStats.getWinPercentage();
                    opponentCount++;
                }
            }

            if (opponentCount > 0) {
                stats.setOpponentWinPercentage(totalOpponentWinPct / opponentCount);
            }
        }

        return statsMap;
    }

    private static List<DivisionTournament> parseDivisions(Document doc, Map<String, Player> playerMap,
                                                            Map<String, PlayerMatchStats> playerStats,
                                                            String tournamentType) {
        List<DivisionTournament> divisions = new ArrayList<>();
        NodeList standingsNodes = doc.getElementsByTagName("standings");

        if (standingsNodes.getLength() == 0) {
            System.out.println("No standings found");
            return divisions;
        }

        Element standingsElement = (Element) standingsNodes.item(0);
        NodeList podNodes = standingsElement.getElementsByTagName("pod");

        for (int i = 0; i < podNodes.getLength(); i++) {
            Element pod = (Element) podNodes.item(i);
            String type = pod.getAttribute("type");
            String category = pod.getAttribute("category");

            // Only process "finished" pods (not "dnf" - did not finish)
            if (!type.equals("finished")) continue;

            // Check if there are any players in this pod
            NodeList playerNodes = pod.getElementsByTagName("player");
            if (playerNodes.getLength() == 0) continue;

            String ageDivision = getAgeDivisionFromCategory(category);
            List<PlayerResult> results = parseStandings(pod, playerMap, playerStats);

            if (!results.isEmpty()) {
                divisions.add(new DivisionTournament(ageDivision, tournamentType, results));
                System.out.println("  - " + ageDivision + " division: " + results.size() + " players");
            }
        }

        return divisions;
    }

    private static String getAgeDivisionFromCategory(String category) {
        return switch (category) {
            case "0" -> "Junior";
            case "1" -> "Senior";
            case "2" -> "Master";
            default -> "Master";
        };
    }

    private static List<PlayerResult> parseStandings(Element pod, Map<String, Player> playerMap,
                                                      Map<String, PlayerMatchStats> playerStats) {
        List<PlayerResult> results = new ArrayList<>();
        NodeList playerNodes = pod.getElementsByTagName("player");

        for (int i = 0; i < playerNodes.getLength(); i++) {
            Element playerElement = (Element) playerNodes.item(i);
            String playerId = playerElement.getAttribute("id");
            String placeStr = playerElement.getAttribute("place");

            if (playerId.isEmpty() || placeStr.isEmpty()) continue;

            int placement = Integer.parseInt(placeStr);
            Player player = playerMap.get(playerId);

            if (player != null) {
                PlayerMatchStats stats = playerStats.get(playerId);
                int matchPoints = 0;
                double opponentWinPct = 0.0;

                if (stats != null) {
                    matchPoints = stats.getMatchPoints();
                    opponentWinPct = stats.getOpponentWinPercentage();
                }

                results.add(new PlayerResult(player, placement, matchPoints, opponentWinPct));
            }
        }

        return results;
    }

    private static String getElementText(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }

    private static String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }

    private static String getElementAttribute(Element parent, String tagName, String attributeName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0 && nodes.item(0).getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) nodes.item(0);
            return element.getAttribute(attributeName);
        }
        return "";
    }

    // Helper class to track player statistics during parsing
    private static class PlayerMatchStats {
        private int matchPoints = 0;
        private int matchesPlayed = 0;
        private int wins = 0;
        private List<String> opponents = new ArrayList<>();
        private double opponentWinPercentage = 0.0;

        public void addMatchPoints(int points) {
            this.matchPoints += points;
        }

        public void addMatch() {
            this.matchesPlayed++;
        }

        public void addWin() {
            this.wins++;
        }

        public void addOpponent(String opponentId) {
            this.opponents.add(opponentId);
        }

        public int getMatchPoints() {
            return matchPoints;
        }

        public double getWinPercentage() {
            if (matchesPlayed == 0) return 0.0;
            return (double) wins / matchesPlayed;
        }

        public List<String> getOpponents() {
            return opponents;
        }

        public void setOpponentWinPercentage(double pct) {
            this.opponentWinPercentage = pct;
        }

        public double getOpponentWinPercentage() {
            return opponentWinPercentage;
        }
    }
}

