package code;

public class Candidate {
    String name;
    int voteCount;

    public Candidate(String name) {
        this.name = name;
        this.voteCount = 0;
    }

    public void incrementVote() {
        this.voteCount++;
    }
}