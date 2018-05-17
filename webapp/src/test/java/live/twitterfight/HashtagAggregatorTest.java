package live.twitterfight;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class HashtagAggregatorTest {

    private HashtagAggregator aggregator = new HashtagAggregator();

    @Test
    public void properly_increments_hashtag_count_for_one_entry() {
        aggregator.aggregate("foo");
        assertThat(aggregator.countForHashtag("foo"), is(1));
    }

    @Test
    public void properly_increments_hashtag_count_for_two_entries() {
        aggregator.aggregate("foo", "foo");
        assertThat(aggregator.countForHashtag("foo"), is(2));
    }

    @Test
    public void maintains_counts_after_multiple_invocations_with_different_hashtags() {
        aggregator.aggregate("foo", "bar");
        assertThat(aggregator.countForHashtag("foo"), is(1));
        assertThat(aggregator.countForHashtag("bar"), is(1));
        aggregator.aggregate("foo", "bar");
        assertThat(aggregator.countForHashtag("foo"), is(2));
        assertThat(aggregator.countForHashtag("bar"), is(2));
    }



}