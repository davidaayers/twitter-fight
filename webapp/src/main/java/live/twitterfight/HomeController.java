package live.twitterfight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private TwitterStream twitterStream;

	@Autowired
	public HomeController(TwitterStream twitterStream) {
		this.twitterStream = twitterStream;
	}

	@RequestMapping(value = "/")
    public String index() {
//		try {
//			twitterStream.doTwitterStuff();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return "index";
	}

}