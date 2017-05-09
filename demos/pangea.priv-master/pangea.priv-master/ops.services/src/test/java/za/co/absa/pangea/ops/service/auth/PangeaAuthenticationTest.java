package za.co.absa.pangea.ops.service.auth;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = PangeaServiceApplication.class)
//@WebAppConfiguration
//@TestPropertySource(properties = { "local.server.port=8081", "local.management.port=8081" })
//@IntegrationTest({ "server.port=0", "management.port=0" })
//@DirtiesContext
//@ActiveProfiles("test")
public class PangeaAuthenticationTest {

//	@Value("${local.server.port}")
//	private int port;
//
//	@Value("${local.management.port}")
//	private int mgt;
//
//	//@Test
//	public void test_Unauthorised() throws Exception {
//		ResponseEntity<String> entity = new TestRestTemplate().getForEntity("http://localhost:" + this.port + "/pangea", String.class);
//		assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
//	}
//
//	//@Test
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void test_Authorised() throws Exception {
//		AccessTokenResponse response = getToken();
//		org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(getHeaders(response.getToken()));
//		ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:" + this.port + "/pangea",HttpMethod.GET, requestEntity, String.class);
//		assertEquals(HttpStatus.OK, entity.getStatusCode());
//	}
//
//	@Test
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void test_BasicAuthorised() throws Exception {
//		org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(getBasicHeaders());
//		ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:" + this.port + "/pangea",HttpMethod.GET, requestEntity, String.class);
//		assertEquals(HttpStatus.OK, entity.getStatusCode());
//	}
//
//	@Test
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void test_WEBINF_Access() throws Exception {
//		org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(getBasicHeaders());
//		ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:" + this.port + "/keycloak.json",HttpMethod.GET, requestEntity, String.class);
//		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
//	}
//	
//	HttpHeaders getHeaders(String token) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.add("Authorization", "Bearer " + new String(token));
//		return headers;
//	}
//
//	HttpHeaders getBasicHeaders() {
//		HttpHeaders headers = new HttpHeaders();
//		String encoding = Base64.encodeBytes("PANGEA:PASSWORD".getBytes());
//		headers.add("Authorization", "Basic " + encoding);
//		return headers;
//	}
//	
//	private void logoutToken() throws ClientProtocolException, IOException{
//		HttpClient client = new HttpClientBuilder().disableTrustManager().build();
//		try{
//		URI url = KeycloakUriBuilder.fromUri("http://localhost:8080/auth").path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH).build("pangea-barc");
//		HttpGet get = new HttpGet(url);
//		HttpResponse response = client.execute(get);
//		int status = response.getStatusLine().getStatusCode();
//		HttpEntity entity = response.getEntity();
//		if (status != 200) {
//			throw new IOException("Bad status: " + status);
//		}
//		if (entity == null) {
//			throw new IOException("No Entity");
//		}
//		} finally {
//			client.getConnectionManager().shutdown();
//		}
//		
//	}
//
//	private AccessTokenResponse getToken() throws ClientProtocolException, IOException {
//		HttpClient client = new HttpClientBuilder().disableTrustManager().build();
//		try {
//			HttpPost post = new HttpPost(KeycloakUriBuilder.fromUri("http://localhost:8080/auth")
//					.path(ServiceUrlConstants.TOKEN_PATH).build("pangea-barc"));
//			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//			formparams.add(new BasicNameValuePair(OAuth2Constants.GRANT_TYPE, "password"));
//			formparams.add(new BasicNameValuePair("username", "pangea"));
//			formparams.add(new BasicNameValuePair("password", "password"));
//
//			// will obtain a token on behalf of angular-product-app
//			formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_ID, "pangea-barc"));
//
//			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
//			post.setEntity(form);
//			HttpResponse response = client.execute(post);
//			int status = response.getStatusLine().getStatusCode();
//			HttpEntity entity = response.getEntity();
//			if (status != 200) {
//				throw new IOException("Bad status: " + status);
//			}
//			if (entity == null) {
//				throw new IOException("No Entity");
//			}
//			InputStream is = entity.getContent();
//			try {
//				AccessTokenResponse tokenResponse = JsonSerialization.readValue(is, AccessTokenResponse.class);
//				return tokenResponse;
//			} finally {
//				try {
//					is.close();
//				} catch (IOException ignored) {
//				}
//			}
//		} finally {
//			client.getConnectionManager().shutdown();
//		}
//	}

}

