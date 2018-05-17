package live.twitterfight;

public class Hashtag {
    private final String hashtag;
    private final int cnt;

    public Hashtag(String hashtag, int cnt) {
        this.hashtag = hashtag;
        this.cnt = cnt;
    }

    public String getHashtag() {
        return hashtag;
    }

    public int getCnt() {
        return cnt;
    }

    @Override
    public String toString() {
        return hashtag + ":" + cnt;
    }
}
