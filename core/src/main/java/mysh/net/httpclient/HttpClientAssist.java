package mysh.net.httpclient;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import mysh.collect.Colls;
import mysh.util.Encodings;
import mysh.util.FilesUtil;
import mysh.util.Strings;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * HTTP 客户端组件.
 *
 * @author ZhangZhx
 */
@ThreadSafe
public class HttpClientAssist implements Closeable {
	private static final Logger log = LoggerFactory.getLogger(HttpClientAssist.class);
	private static final HttpClientConfig defaultHcc = new HttpClientConfig();
	private HttpClientConfig hcc;
	private OkHttpClient client;

	public HttpClientAssist() {
		this(null, null);
	}

	public HttpClientAssist(@Nullable HttpClientConfig hcc) {
		this(hcc, null);
	}

	public HttpClientAssist(@Nullable HttpClientConfig conf, @Nullable ProxySelector proxySelector) {
		hcc = conf == null ? defaultHcc : conf.clone();

		client = new OkHttpClient.Builder()
				.connectTimeout(hcc.connectionTimeout, TimeUnit.MILLISECONDS)
				.readTimeout(hcc.soTimeout, TimeUnit.MILLISECONDS)
				.connectionPool(new ConnectionPool(hcc.maxIdolConnections, hcc.connPoolKeepAliveSec, TimeUnit.SECONDS))
				.proxySelector(ObjectUtils.firstNonNull(proxySelector, ProxySelector.getDefault()))
				.cookieJar(hcc.cookieJar)
				.build();
	}

	/**
	 * get url entity by get method.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @throws IOException 连接异常.
	 */
	public UrlEntity access(String url) throws IOException {
		return access(url, null, null);
	}

	/**
	 * get url entity by get method.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers, can be null. use header name in {@link com.google.common.net.HttpHeaders}
	 * @throws IOException 连接异常.
	 */
	public UrlEntity access(String url, Map<String, ?> headers) throws IOException {
		return access(url, headers, null);
	}

	/**
	 * get url entity by get method.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers, can be null. use header name in {@link com.google.common.net.HttpHeaders}
	 * @param params  request params, can be null.
	 * @throws IOException 连接异常.
	 */
	public UrlEntity access(String url, Map<String, ?> headers, Map<String, ?> params) throws IOException {
		if (params != null && params.size() > 0) {
			StringBuilder usb = new StringBuilder(url);
			if (!url.contains("?")) {
				usb.append('?');
			} else {
				usb.append('&');
			}
			for (Map.Entry<String, ?> pe : params.entrySet()) {
				usb.append(pe.getKey()).append('=').append(pe.getValue()).append('&');
			}
			if (usb.charAt(usb.length() - 1) == '&') {
				usb.deleteCharAt(usb.length() - 1);
			}
			url = usb.toString();
		}

		Request.Builder rb = new Request.Builder().url(url);
		if (hcc.headers != null) {
			for (Map.Entry<String, ?> e : hcc.headers.entrySet()) {
				rb.addHeader(e.getKey(), String.valueOf(e.getValue()));
			}
		}
		return access(rb, headers);
	}

	/**
	 * get url entity by post form data.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers, can be null. use header name in {@link com.google.common.net.HttpHeaders}
	 * @param params  request params. upload type: multipart/form-data, support files
	 * @throws IOException 连接异常.
	 */
	public UrlEntity accessPostMultipartForm(
			String url, @Nullable Map<String, ?> headers, @Nullable Map<String, ?> params) throws IOException {
		return accessPostMultipartForm(url, headers, params, null);
	}

	/**
	 * get url entity by post form data.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers, can be null. use header name in {@link com.google.common.net.HttpHeaders}
	 * @param params  request params. upload type: multipart/form-data, support files
	 * @param enc     param value encoding
	 * @throws IOException 连接异常.
	 */
	public UrlEntity accessPostMultipartForm(
			String url, @Nullable Map<String, ?> headers, @Nullable Map<String, ?> params,
			@Nullable Charset enc) throws IOException {
		Request.Builder rb = new Request.Builder().url(url);
		if (Colls.isNotEmpty(params)) {
			MultipartBody.Builder mb = new MultipartBody.Builder();
			for (Map.Entry<String, ?> e : params.entrySet()) {
				String name = e.getKey();
				Object value = e.getValue();

				if (value instanceof File) {
					File file = (File) value;
					mb.addFormDataPart(name, file.getName(), RequestBody.create(null, file));
				} else {
					mb.addFormDataPart(name, String.valueOf(value));
				}
			}
			rb.post(mb.build());
		}
		return access(rb, headers);
	}

	/**
	 * @see #accessPostUrlEncodedForm(String, Map, Map, Charset)
	 */
	public UrlEntity accessPostUrlEncodedForm(
			String url, @Nullable Map<String, ?> headers, @Nullable Map<String, ?> params) throws IOException {
		return accessPostUrlEncodedForm(url, headers, params, null);
	}

	/**
	 * get url entity by post url encoded data.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers, can be null. use header name in {@link com.google.common.net.HttpHeaders}
	 * @param params  request params. upload type: multipart/form-data, support files
	 * @param enc     param value encoding
	 * @throws IOException 连接异常.
	 */
	public UrlEntity accessPostUrlEncodedForm(
			String url, @Nullable Map<String, ?> headers, @Nullable Map<String, ?> params,
			@Nullable Charset enc) throws IOException {
		Request.Builder rb = new Request.Builder().url(url);
		if (Colls.isNotEmpty(params)) {
			FormBody.Builder fb = new FormBody.Builder(enc);
			for (Map.Entry<String, ?> e : params.entrySet()) {
				fb.add(e.getKey(), String.valueOf(e.getValue()));
			}
			rb.post(fb.build());
		}
		return access(rb, headers);
	}

	/**
	 * post raw content. content type can be set using header: {@link HttpHeaders#CONTENT_TYPE}<br/>
	 * get url entity by post method.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers.
	 * @throws IOException 连接异常.
	 */
	public UrlEntity accessPostBytes(
			String url, @Nullable Map<String, ?> headers, String contentType, @Nullable byte[] buf) throws IOException {
		Request.Builder rb = new Request.Builder().url(url);
		if (buf != null) {
			rb.post(RequestBody.create(
					MediaType.get(ObjectUtils.firstNonNull(contentType, "application/octet-stream")),
					buf));
		}
		return access(rb, headers);

	}

	/**
	 * get url entity.<br/>
	 * WARNING: the entity must be closed in time,
	 * because an unclosed entity will hold a connection from connection-pool.
	 *
	 * @param headers request headers, can be null. use header name in {@link com.google.common.net.HttpHeaders}
	 * @throws IOException 连接异常.
	 */
	private UrlEntity access(Request.Builder rb, Map<String, ?> headers) throws IOException {
		// 响应中断
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedIOException("access interrupted: " + rb.build());
		}

		if (Colls.isNotEmpty(hcc.headers)) {
			for (Map.Entry<String, Object> e : hcc.headers.entrySet()) {
				rb.addHeader(e.getKey(), String.valueOf(e.getValue()));
			}
		}

		if (Colls.isNotEmpty(headers)) {
			for (Map.Entry<String, ?> e : headers.entrySet()) {
				rb.addHeader(e.getKey(), String.valueOf(e.getValue()));
			}
		}

		rb.addHeader("Connection", hcc.isKeepAlive ? "Keep-Alive" : "close");
		rb.addHeader("User-Agent", hcc.userAgent);

		return new UrlEntity(rb);
	}

	@Override
	public void close() {
		try {
			Cache cache = client.cache();
			if (cache != null)
				cache.delete();
			client.connectionPool().evictAll();
		} catch (Exception e) {
			log.debug("hca close error", e);
		}
	}

	/**
	 * download big resource and save to file.
	 *
	 * @param url       数据文件地址
	 * @param headers   请求头, 可为 null
	 * @param overwrite overwrite exist file or rename new file
	 * @return write successfully or not
	 * @throws Exception IO异常
	 */
	public boolean saveDirectlyToFile(
			String url, @Nullable Map<String, ?> headers, File file, boolean overwrite, @Nullable Callable<Boolean> stopChk
	) throws Exception {
		try (UrlEntity ue = this.access(url, headers)) {
			return ue.downloadDirectlyToFile(file, overwrite, stopChk);
		}
	}

	/**
	 * download resource to memory then save to file.
	 *
	 * @param url       数据文件地址
	 * @param headers   请求头, 可为 null
	 * @param overwrite overwrite exist file or rename new file
	 * @return whether file is overwritten
	 * @throws Exception IO异常
	 */
	public boolean saveToFile(
			String url, @Nullable Map<String, ?> headers, File file, boolean overwrite
	) throws IOException {
		try (UrlEntity ue = this.access(url, headers)) {
			return ue.saveToFile(file, overwrite);
		}
	}

	/**
	 * 将带有 ./ 或 ../ 或 // 或 \ 的 URI 转换成简短的 URL 形式.
	 * uriString needs schema
	 */
	public static String getShortURL(String uriString) {

		if (Strings.isBlank(uriString)) {
			return "";
		}

		uriString = uriString.replace('\\', '/');
		if (!uriString.contains("./") && uriString.indexOf("//", 8) < 0) {
			return uriString;
		}

		URI uri;
		try {
			uri = new URI(uriString);
		} catch (URISyntaxException e) {
			return uriString;
		}

		StringBuilder url = new StringBuilder();
		if (uri.getScheme() != null) {
			url.append(uri.getScheme());
			url.append("://");
		}
		if (uri.getHost() != null) {
			url.append(uri.getHost());
			if (uri.getPort() != -1
					&& !("http".equalsIgnoreCase(uri.getScheme()) && uri.getPort() == 80)
					&& !("https".equalsIgnoreCase(uri.getScheme()) && uri.getPort() == 443)) {
				url.append(":");
				url.append(uri.getPort());
			}
		}

		// 处理 URL Path 中的 . 和 ..
		String[] tPath = uri.getRawPath().split("/");
		// int lastUnNullBlankIndex = -1;
		Deque<Integer> lastUnNullBlankIndex = new LinkedList<>();
		for (int index = 0; index < tPath.length; index++) {
			switch (tPath[index]) {
				case "":
				case ".":
					tPath[index] = null;
					break;
				case "..":
					tPath[index] = null;
					try {
						tPath[lastUnNullBlankIndex.pop()] = null;
					} catch (NoSuchElementException e) {
						// String msg = "URI 简化失败: " + uriString;
						// Exception ex = new Exception(msg);
						return uriString;
					}
					break;
				default:
					lastUnNullBlankIndex.push(index);
					break;
			}
		}

		for (String aTPath : tPath) {
			if (aTPath != null) {
				url.append("/");
				url.append(aTPath);
			}
		}
		if (uri.getPath().endsWith("/")) {
			url.append("/");
		}

		// 处理参数
		if (uri.getRawQuery() != null) {
			url.append("?");
			url.append(uri.getRawQuery());
		}

		// 处理形如 g.cn/search?q=2 的情况
		if (uri.getScheme() == null && !uri.getRawPath().startsWith("/")) {
			url.delete(0, 1);
		}

		return url.toString();
	}

	/**
	 * parse headers string in lines
	 */
	public static Map<String, String> parseHeaders(String headerStr) {
		if (Strings.isNotBlank(headerStr)) {
			Map<String, String> hm = Maps.newHashMap();
			for (String line : headerStr.split("[\\r\\n]+")) {
				String[] header = line.split(": *", 2);
				hm.put(header[0], header[1]);
			}
			return hm;
		} else {
			return Collections.emptyMap();
		}
	}

	/**
	 * parse params string like key=value&k=v
	 */
	public static Map<String, String> parseParams(String paramStr) {
		if (Strings.isNotBlank(paramStr)) {
			Map<String, String> pm = new HashMap<>();
			for (String kv : paramStr.split("&")) {
				String[] kav = kv.split("=", 2);
				pm.put(kav[0], kav[1]);
			}
			return pm;
		} else {
			return Collections.emptyMap();
		}
	}

	private static final byte[] EMPTY_BUF = new byte[0];
	private static final int DOWNLOAD_BUF_LEN = 100_000;
	private static final ThreadLocal<byte[]> threadDownloadBuf = new ThreadLocal<>();

	private static byte[] getDownloadBuf() {
		byte[] buf = threadDownloadBuf.get();
		if (buf == null) {
			threadDownloadBuf.set(buf = new byte[DOWNLOAD_BUF_LEN]);
		}
		return buf;
	}

	@ThreadSafe
	public final class UrlEntity implements Closeable {

		private final Request req;
		private final String reqUrl;
		private final Call call;
		private final Response rsp;
		private String currentUrl;
		private MediaType contentType;
		private byte[] entityBuf;
		private String entityStr;
		private Charset entityEncoding;

		public UrlEntity(Request.Builder rb) throws IOException {
			req = rb.build();
			// can't lazy init, because it changes after rb.execute()
			reqUrl = req.url().toString();

			call = client.newCall(req);
			rsp = call.execute();

			int statusCode = rsp.code();
			if (statusCode >= 400) {
				log.warn("access unsuccessful, status={}, msg={}, url={}",
						statusCode, rsp.message(), this.reqUrl);
			}
			contentType = rsp.body().contentType();
		}

		/**
		 * close the connection immediately, unfinished download will be aborted.
		 */
		@Override
		public void close() {
			try {
				call.cancel();
				rsp.close();
			} catch (Exception e) {
				log.debug("close connection error. " + e);
			}
		}

		/**
		 * @return original request url.
		 */
		public final String getReqUrl() {
			return reqUrl;
		}

		/**
		 * @return response protocol version. e.g. https
		 */
		public String getProtocol() {
			return rsp.request().url().scheme();
		}

		/**
		 * @return request reqUrl may jump several times, get the real access url.
		 */
		public String getCurrentURL() {
			if (this.currentUrl == null) {
				this.currentUrl = rsp.request().url().toString();
			}

			return this.currentUrl;
		}

		/**
		 * get response status.
		 */
		public int getStatusCode() {
			return rsp.code();
		}

		/**
		 * a request is text by default if the header(content-type) is not given.
		 * see {@link #isContentType(String)}
		 */
		public boolean isText() {
			return contentType == null || Objects.equals(contentType.type(), "text");
		}

		/**
		 * <b>IMPORTANT:</b> see {@link #isContentType(String)}
		 */
		public boolean isHtml() {
			return contentType != null && Objects.equals(contentType.subtype(), "html");
		}

		/**
		 * <b>IMPORTANT:</b> see {@link #isContentType(String)}
		 */
		public boolean isImage() {
			return contentType != null && Objects.equals(contentType.type(), "image");
		}

		/**
		 * content-type(FROM response header) check.<br/>
		 * if the header is not given or incorrect, the judgement will be incorrect.
		 * So use this ONLY if file extension judgement can not work.
		 */
		public boolean isContentType(String type) {
			return contentType != null && (
					contentType.type().contains(type) || contentType.subtype().contains(type)
			);
		}

		/**
		 * content length in byte size. return -1 if length not given (response header).
		 */
		public long getContentLength() {
			return rsp.body().contentLength();
		}

		/**
		 * buf then convert to string. the buf is saved and can be reused.
		 */
		public synchronized String getEntityStr() throws IOException {
			if (entityStr != null) {
				return entityStr;
			}

			downloadEntityAndParseEncoding();
			entityStr = new String(entityBuf, entityEncoding);

			return entityStr;
		}

		private void downloadEntityAndParseEncoding() throws IOException {
			downloadEntity2Buf();

			if (this.entityEncoding == null) {

				Charset enc = null;
				if (contentType != null) {
					enc = contentType.charset();
				}
				if (enc == null) {
					enc = Encodings.isUTF8Bytes(entityBuf) ? Encodings.UTF_8 : Encodings.GBK;
				}
				this.entityEncoding = enc;
			}
		}

		/**
		 * download entity content then parse encoding. useful in text content.
		 */
		public Charset getEntityEncoding() throws IOException {
			this.downloadEntityAndParseEncoding();
			return this.entityEncoding;
		}

		/**
		 * download entire entity to memory. download will run only once.
		 */
		public synchronized void downloadEntity2Buf() throws IOException {
			if (entityBuf == null) {
				entityBuf = rsp.body().bytes();
				entityBuf = entityBuf == null ? EMPTY_BUF : entityBuf;
			}
		}

		/**
		 * buf entire entity then write. the buf is saved and can be reused.
		 */
		public synchronized void bufWriteTo(OutputStream out) throws IOException {
			downloadEntity2Buf();
			out.write(entityBuf);
			out.flush();
		}

		/**
		 * buf entire entity then save to file. the buf is saved and can be reused.<br>
		 * <b>WARNING</b>: make sure entire entity can be save to vm heap, or <code>OutOfMemoryError</code> will be
		 * thrown
		 *
		 * @return whether file is overwritten
		 * @throws IOException
		 */
		public synchronized boolean saveToFile(File file, boolean overwrite) throws IOException {
			if (!overwrite && file.exists()) {
				return false;
			}

			downloadEntity2Buf();
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			FilesUtil.writeFile(file, entityBuf);
			return true;
		}

		/**
		 * get entity buf. NOTICE: the buf is not copied, so it should be READ-ONLY.
		 */
		public byte[] getEntityBuf() throws IOException {
			downloadEntity2Buf();
			return this.entityBuf;
		}

		public String getCookies() {
			return ObjectUtils.firstNonNull(rsp.header("Cookie"), rsp.header("cookie"));
		}

		@Override
		public String toString() {
			return getCurrentURL();
		}

		/**
		 * download resource directly to file, without cache to entityBuf.
		 *
		 * @param file      file will not be overwritten, if this file exists, a new file will be created.
		 * @param stopChk   check stop download or not.
		 * @param overwrite overwrite exist file or rename new file
		 * @return write successfully or not
		 */
		public synchronized boolean downloadDirectlyToFile(
				File file, boolean overwrite, @Nullable Callable<Boolean> stopChk) throws Exception {
			if (stopChk != null && Objects.equals(Boolean.TRUE, stopChk.call())) {
				return false;
			}

			File writableFile = overwrite ? file : FilesUtil.getWritableFile(file);
			File writeFile = new File(writableFile.getPath() + ".~write~");
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			try (OutputStream out = Files.newOutputStream(writeFile.toPath());
			     InputStream is = rsp.body().byteStream()) {
				Thread thread = Thread.currentThread();

				byte[] buf = getDownloadBuf();
				int rl;
				while (!thread.isInterrupted() && (rl = is.read(buf)) > -1) {
					out.write(buf, 0, rl);
					if (stopChk != null && Objects.equals(Boolean.TRUE, stopChk.call())) {
						return false;
					}
				}
				if (thread.isInterrupted()) {
					throw new InterruptedIOException("download interrupted: " + reqUrl);
				}
			}
			writableFile.delete();
			writeFile.renameTo(writableFile);
			return true;
		}
	}
}
