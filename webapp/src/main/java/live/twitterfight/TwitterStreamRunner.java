package live.twitterfight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TwitterStreamRunner implements CommandLineRunner {

    private TwitterStream twitterStream;

    @Autowired
    public TwitterStreamRunner(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public void run(String... strings) throws Exception {
        twitterStream.readTwitterStream();
    }
}
