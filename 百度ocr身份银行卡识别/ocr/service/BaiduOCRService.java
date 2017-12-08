package com.pricl.biz.ocr.service;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.aip.ocr.AipOcr;
import com.pricl.biz.ocr.cons.Cons;
import com.pricl.biz.ocr.dto.BankCardInfo;
import com.pricl.biz.ocr.dto.IdentityInfo;
import com.pricl.biz.ocr.util.OCRUtil;
import com.pricl.biz.ocr.util.ThumbnailUtilService;
import com.pricl.frame.core.R;

/**
 * 百度文字识别服务，目前支持身份证识别、银行卡识别
 * Created by yangxin on 2017/8/11.
 */
@Service
public class BaiduOCRService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_LENGTH = 1024 * 150;

    private AipOcr aipOcr = new AipOcr(Cons.APP_ID, Cons.APP_KEY, Cons.APP_SECRET);

    @Autowired
    private ThumbnailUtilService thumbnailUtilService;
    
    /**
     * 读取身份证信息
     *
     * @param bytes   文件
     * @param isFront 是否为身份证正面，是为true，不是为false
     * @return
     * @throws OCRException
     */
    public IdentityInfo getIdentityInfo(byte[] bytes, boolean isFront) throws IOException {

        if (bytes.length > MAX_LENGTH) {
            bytes = thumbnailUtilService.createThumbnail(bytes);
            logger.info("压缩过后的文件大小为：{} KB", bytes.length / 1024);
        }

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        JSONObject idCard = aipOcr.idcard(bytes, isFront, options);
        R<?> r = checkIsOurSystemFail(idCard);
        return OCRUtil.getIdentityInfo(idCard);
    }

    /**
     * 如果出现请求次数超过。那么更换key
     * @param idCard
     * @return
     */
    private R<?> checkIsOurSystemFail(JSONObject idCard) {
		if (idCard.has("error_code")) {
			String errorCode = idCard.getString("error_code");
			if (StringUtils.equals(errorCode, "17") || StringUtils.equals(errorCode, "19")) { // 如果报错是因为请求总量超额，则更换授权id重新
				Cons.changeAuth();
			}
		}
		return null;
	}

	/**
     * 读取身份证正面信息
     *
     * @param bytes
     * @return
     * @throws OCRException
     */
    public IdentityInfo getIdentityInfo(byte[] bytes) throws IOException {
        return getIdentityInfo(bytes, true);
    }

    /**
     * 读取银行卡信息
     *
     * @param bytes
     * @return
     * @throws OCRException
     */
    public BankCardInfo getBankCardInfo(byte[] bytes) throws IOException {
        if (bytes.length > MAX_LENGTH) {
            bytes = thumbnailUtilService.createThumbnail(bytes);
            logger.info("压缩银行卡图片过后的文件大小为：{} KB", bytes.length / 1024);
        }
        JSONObject response = aipOcr.bankcard(bytes);
        return OCRUtil.getBankCardInfo(response);
    }

}
