package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotingSystems {

    private static VotingSystems instance; // Singleton instance
    Map<String, User> users;
    ArrayList<Candidate> candidates;
    ArrayList<String> votedUsersInfo;  // List to store the name and ID of users who voted
    Map<String, Integer> cityVotes; // Track votes by province
    User currentUser;
    int nextUserId;
    private Map<String, Map<String, Integer>> provinceCandidateVotes;

    private VotingSystems() {
        users = new HashMap<>();
        candidates = new ArrayList<>();
        votedUsersInfo = new ArrayList<>();
        cityVotes = new HashMap<>();
        nextUserId = 1;  // Starting ID from 1

        // Predefined candidates
        candidates.add(new Candidate("PTI"));
        candidates.add(new Candidate("PPP"));
        candidates.add(new Candidate("JI"));
        candidates.add(new Candidate("PMLN"));
        provinceCandidateVotes = new HashMap<>();
        // Initialize votes for all provinces and candidates
        for (String province : new String[]{"Punjab", "Sindh", "KPK", "Balochistan"}) {
            Map<String, Integer> candidateVotes = new HashMap<>();
            for (Candidate candidate : candidates) {
                candidateVotes.put(candidate.name, 0);
            }
            provinceCandidateVotes.put(province, candidateVotes);
        }
    }

    // Get the singleton instance
    public static VotingSystems getInstance() {
        if (instance == null) {
            instance = new VotingSystems();
        }
        return instance;
    }

    // Register a new user (now using HashMap for fast lookup)
    public boolean register(int cnic, String password, int age, String province, String firstName, String lastName) {
        if (users.containsKey(cnic)) {
            return false;  // Username already exists
        }
        User newUser = new User(cnic, password, age, province, nextUserId++, firstName, lastName);
        System.out.println("CNIC :" + cnic);
        users.put(String.valueOf(cnic), newUser);
        System.out.println("User Registered Succesfully");
        // Track province votes
        cityVotes.put(province, cityVotes.getOrDefault(province, 0));

        return true;
    }

    // Login functionality (searching for the user in HashMap)
    public boolean login(int cnic, String password) {
        User user = users.get(String.valueOf(cnic));
        System.out.println("User : " + user);
        if (user != null && user.password.equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    // Check if the user is eligible to vote
    public boolean canVote() {
        return currentUser.age >= 18 && !currentUser.hasVoted;
    }

    // Cast a vote for a candidate
    public void castVote(int candidateIndex) {
        if (canVote()) {
            Candidate selectedCandidate = candidates.get(candidateIndex);
            selectedCandidate.incrementVote();
            currentUser.hasVoted = true;
            votedUsersInfo.add("Name: " + currentUser.cnic + ", ID: " + currentUser.userId);

            // Update votes for the candidate in the user's province
            Map<String, Integer> candidateVotes = provinceCandidateVotes.get(currentUser.province);
            if (candidateVotes != null) {
                candidateVotes.put(selectedCandidate.name, candidateVotes.get(selectedCandidate.name) + 1);
            }
        }
    }

    // Updated getVotingResultsByProvince method
    public Map<String, Map<String, Integer>> getVotingResultsByProvince() {
        return provinceCandidateVotes;
    }

    // Get info about all users who have voted
    public String getVotedUsersInfo() {
        StringBuilder info = new StringBuilder("<html>");
        if (votedUsersInfo.isEmpty()) {
            info.append("No users have voted yet.");
        } else {
            for (String votedUser : votedUsersInfo) {
                info.append(votedUser).append("<br>");
            }
        }
        info.append("</html>");
        return info.toString();
    }

    // Sort candidates by vote count in descending order
    public void sortCandidatesByVotes() {
        candidates.sort((c1, c2) -> Integer.compare(c2.voteCount, c1.voteCount));  // Sort in descending order
    }

    // Search for a user by cnic (using HashMap for fast lookup)
    public User findUserByCNIC(int cnic) {
        return users.get(cnic);
    }

    // Find the province with the most votes
    public String getCityWithMostVotes() {
        String cityWithMostVotes = "";
        int maxVotes = -1;
        for (Map.Entry<String, Integer> entry : cityVotes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                cityWithMostVotes = entry.getKey();
            }
        }
        return cityWithMostVotes;
    }
    
        public int getTotalVotesAcrossProvinces() {
        int totalVotes = 0;
        for (Map<String, Integer> candidateVotes : provinceCandidateVotes.values()) {
            for (int votes : candidateVotes.values()) {
                totalVotes += votes;
            }
        }
        return totalVotes;
    }

    // Function to calculate total votes for a specific province
    public int getTotalVotesForProvince(String province) {
        Map<String, Integer> candidateVotes = provinceCandidateVotes.get(province);
        if (candidateVotes == null) {
            System.out.println("Province not found.");
            return 0; // Return 0 if the province doesn't exist
        }

        int totalVotes = 0;
        for (int votes : candidateVotes.values()) {
            totalVotes += votes;
        }
        return totalVotes;
    }
}
