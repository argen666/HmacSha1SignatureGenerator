import sun.misc.BASE64Encoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha1Signature {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "your_secret_key";
    private static final String API_KEY = "your_api_key";

    public static String calculateChecksum(String endPoint, String date) throws NoSuchAlgorithmException, InvalidKeyException {
        String contentType = "application/json";
        String contentMd5 = "";
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(keySpec);
        String checksumString = contentType + ',' + contentMd5 + ',' + endPoint + ',' + date;
        byte[] result = mac.doFinal(checksumString.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        String checksum = encoder.encode(result);
        return checksum;
    }

    public static void main(String[] args) throws Exception {
        String endPoint = "/accounts";
        SimpleDateFormat sf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        sf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = sf.format(new Date());
        String checksum = calculateChecksum(endPoint, date);
        System.out.println("Checksum: " + checksum);
        System.out.println("Authorization HTTP header signature: " + "APIAuth " + API_KEY + ":" + checksum);
    }
}
