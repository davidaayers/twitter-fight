package live.twitterfight;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.stream.StreamSupport.*;

@Service
public class TwitterStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterStream.class);

    @Value("${consumer.key}")
    private String consumerKey;
    @Value("${consumer.secret}")
    private String consumerSecret;
    @Value("${token}")
    private String token;
    @Value("${token.secret}")
    private String tokenSecret;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final HashtagAggregator hashtagAggregator;

    @Autowired
    public TwitterStream(KafkaTemplate<String, String> kafkaTemplate, HashtagAggregator hashtagAggregator) {
        this.kafkaTemplate = kafkaTemplate;
        this.hashtagAggregator = hashtagAggregator;
    }

    @Async
    void readTwitterStream() throws InterruptedException {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);

        // Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth)
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        List<String> terms = Lists.newArrayList("comic", "comics");
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(
                consumerKey,
                consumerSecret,
                token,
                tokenSecret
        );

        ClientBuilder builder = new ClientBuilder()
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        Client hosebirdClient = builder.build();
        hosebirdClient.connect();

        // on a different thread, or multiple different threads....
        while (!hosebirdClient.isDone()) {
            String msg = msgQueue.take();

            JSONObject obj = new JSONObject(msg);

            String lang = obj.getString("lang");
            String text = obj.getString("text");

            JSONObject entities = obj.getJSONObject("entities");
            JSONArray hashtags = entities.getJSONArray("hashtags");

            // only send messages that have hashtags and are in english to kafka
            if (hashtags.length() > 0 && "en".equals(lang)) {
                String[] tags = stream(hashtags.spliterator(), false)
                        .map(JSONObject.class::cast)
                        .map(o -> o.getString("text").toLowerCase())
                        .toArray(String[]::new);

                hashtagAggregator.aggregate(tags);

                LOGGER.info("tweet: {} {}" , text, tags);

                kafkaTemplate.send("helloworld.t", msg);
            }

            Thread.sleep(1);
        }
    }

}
