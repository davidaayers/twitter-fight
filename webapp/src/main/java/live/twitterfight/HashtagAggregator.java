package live.twitterfight;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Component
public class HashtagAggregator {

    private final ConcurrentHashMap<String, Integer> hashTagCounts = new ConcurrentHashMap<>();

    public void aggregate(String... hashtags) {
        stream(hashtags)
                .forEach(ht -> hashTagCounts.merge(ht, 1, (oldVal, newVal) -> oldVal + newVal));
        topMatches(10);
    }

    public int countForHashtag(String hashtag) {
        return hashTagCounts.get(hashtag);
    }

    public List<Hashtag> topMatches(int numberOfMatches) {
        List<Hashtag> matches = hashTagCounts.entrySet()
                .stream()
                .sorted(reverseOrder(Entry.comparingByValue()))
                .limit(numberOfMatches)
                .map(entry -> new Hashtag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        System.out.println(">>>>\n" + matches + "\n<<<<");
        return matches;
    }
}
