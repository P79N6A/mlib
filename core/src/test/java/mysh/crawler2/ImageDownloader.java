package mysh.crawler2;

import mysh.net.httpclient.HttpClientAssist;
import mysh.net.httpclient.HttpClientConfig;
import mysh.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

/**
 */
public class ImageDownloader implements CrawlerSeed {
	private static final long serialVersionUID = 498361912566529068L;
	private static final Logger log = LoggerFactory.getLogger(ImageDownloader.class);
	private static final File saveFile = new File("l:/idStore");
	private static final String v = "";

	transient List<String> seeds = new ArrayList<>();
	Queue<String> unhandledSeeds = new ConcurrentLinkedQueue<>();
	Map<String, Serializable> repo = new ConcurrentHashMap<>(8000);

	public static void main(String[] args) throws Exception {
		HttpClientConfig hcc = new HttpClientConfig();
//		hcc.setUserAgent(HttpClientConfig.UA_BAIDU);
		hcc.setMaxConnPerRoute(4);
//		hcc.setUseProxy(true);
		hcc.setProxyHost("127.0.0.1");
		hcc.setProxyPort(8058);

		Crawler c = new Crawler(new ImageDownloader(), hcc);
		c.start();

		while (c.getStatus() != Crawler.Status.STOPPED) {
			// offer an opportunity to run c.stop()
			Thread.sleep(5000);
		}

		System.out.println("end");
	}

	public ImageDownloader() {
		String u = "http://dryicons.com/";
		seeds.add(u);
	}

	public static final int WAIT_TIME = 700;

	@Override
	public boolean accept(String url) {
//		if (1 == 1)
//			return true;
		return !repo.containsKey(url)
						&& url.contains("dryicons.com/")
						;
	}

	@Override
	public Stream<String> afterDistillingUrl(HttpClientAssist.UrlEntity ue, Set<String> distilledUrl) {
		return distilledUrl.stream()
						.filter(url -> !url.endsWith(".jpg"))
						.filter(url -> !url.endsWith("png") || url.contains("128x128"))
						;
	}

	@Override
	public boolean onGet(HttpClientAssist.UrlEntity e) {
		repo.put(e.getCurrentURL(), v);
		repo.put(e.getReqUrl(), v);

		try {
			if (e.getStatusLine().getStatusCode() == 200 && e.isImage()) {
				String imgName = new File(e.getCurrentURL()).getName();
				File f = new File("l:/a", imgName);
				if (!f.getAbsoluteFile().getParentFile().exists())
					f.getAbsoluteFile().getParentFile().mkdirs();
				if (f.exists() && f.length() > 0) return true;
//				if (new File("F:\\temp\\a", imgName).exists()) return true;

				try (OutputStream out = new FileOutputStream(f)) {
					e.bufWriteTo(out);
				} catch (Exception ex) {
					log.error("error download: " + e.getCurrentURL(), ex);
					repo.remove(e.getCurrentURL());
					repo.remove(e.getReqUrl());
					return false;
				}
			}
			return true;
		} finally {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e1) {
			}
		}
	}

	@Override
	public void init() throws IOException, ClassNotFoundException {
		if (saveFile.exists()) {
			Object[] savedObj = FileUtil.getObjectFromFile(saveFile.getAbsolutePath());

			Map tRepo = (Map) savedObj[0];
			if (tRepo != null && tRepo.size() > 0)
				repo = tRepo;

			Queue<String> tUnhandledSeeds = (Queue<String>) savedObj[1];
			if (tUnhandledSeeds != null && tUnhandledSeeds.size() > 0) {
				seeds.clear();
				seeds.addAll(tUnhandledSeeds);
			}
		}

		log.info("seeds.size=" + this.seeds.size());
	}

	@Override
	public void onCrawlerStopped(Queue<String> unhandledSeeds) {
		this.unhandledSeeds = unhandledSeeds;
		try {
			FileUtil.writeObjectToFile(saveFile.getAbsolutePath(),
							new Object[]{this.repo, this.unhandledSeeds});
		} catch (IOException e) {
			log.error("save unhandled seeds failed.", e);
		}
	}

	@Override
	public List<String> getSeeds() {
		return seeds;
	}

	@Override
	public int requestThreadSize() {
		return 12;
	}
}


