package umf;

import com.umpay.api.common.Base64;
import com.umpay.api.util.HttpMerParserUtil;
import com.umpay.api.util.PlainUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SecurityDemo {

//	public static void main(String[] args) throws FileNotFoundException, CertificateException, UnsupportedEncodingException {
//		File file = new File("C:\\Users\\hp\\Desktop\\m\\m\\cert_2d59.crt");
//		InputStream inStream = new FileInputStream(file);
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		X509Certificate oCert = (X509Certificate)cf.generateCertificate(inStream);
//		String plain = "mer_id=69008579&order_id=220190620182739000000840&ret_code=0000&ret_msg=操作成功&sign_type=RSA&version=4.0";
//		String sign = "xQJfCqH1XYAG77U6b9w3KfiNIzJCGr4VHg6lLFNVlVNSaPMDMBi4iPpYIwCC8fITXZIlxcn3/Zfzs+vXOGXn8q0FdEwV+51rxq/J8z83qLVDz+Nx6CpRFfHPn9ri74788V+g41PQreY46AzMXDGBCIlSEn3pAAIJ0I/VUFKFWSs=";
////		boolean result = verifySign(oCert, plain.getBytes("utf-8"), Base64.decode(sign.getBytes("utf-8")));
//		boolean result = verifySign(oCert, plain.getBytes("utf-8"), Base64.decode(sign));
//
//		System.out.println(result);
//	}


	/**
	 * 获取签名信息和代签名信息
	 * @param html	响应的html
	 * String plain = map.get("plain").toString();
	 * String sign = map.get("sign").toString();
	 * @return
	 */
	public static Map getPlain(String html){
		String content = HttpMerParserUtil.getMeta(html);
		Map map = PlainUtil.getResPlain(content);

		return map;
	}

	/**
	 * 输出结果
	 * @param data
	 */
	public static void outputResult(Map data) {
		Set set = data.keySet();
		Iterator dataIter = set.iterator();
		//遍历结果
		while (dataIter.hasNext()) {
			String key = (String) dataIter.next();
			String value = (String) data.get(key);
			System.out.println(key + "--->" + value);
		}
	}


	/**
	 * 加载公钥
	 * @param filePath
	 * @return
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 */
	public static X509Certificate getCert(String filePath) throws CertificateException, FileNotFoundException {
		File file = new File(filePath);
		InputStream inStream = new FileInputStream(file);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate oCert = (X509Certificate)cf.generateCertificate(inStream);
		return oCert;
	}


	/**
	 * 自定的验签方法
	 * @param filePath
	 * @param plain
	 * @param signData
	 * @return
	 */
	public static boolean verifySign(String filePath, String plain, String signData) {
		try {
			Signature e = Signature.getInstance("SHA1withRSA");
			e.initVerify(SecurityDemo.getCert(filePath));
			e.update(plain.getBytes("utf-8"));
			return e.verify(Base64.decode(signData.getBytes("utf-8")));
		} catch (Exception arg3) {
			return false;
		}
	}
	
}
