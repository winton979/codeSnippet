package com.pricl.biz.ocr;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.aip.ocr.AipOcr;
import com.pricl.biz.ocr.cons.Cons;
import com.pricl.biz.ocr.dto.BankCardInfo;
import com.pricl.biz.ocr.dto.IdentityInfo;
import com.pricl.biz.ocr.util.OCRUtil;
import com.pricl.biz.ocr.util.ThumbnailUtilService;

/**
 * Created by yangxin on 2017/8/12.
 */
public class OCRClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_LENGTH = 1024 * 150;

    private AipOcr aipOcr;

    private byte[] bytes;

    private ThumbnailUtilService thumbnailUtilService;

    public OCRClient(byte[] bytes) {
        this.bytes = bytes;
        this.aipOcr = new AipOcr(Cons.APP_ID, Cons.APP_KEY, Cons.APP_SECRET);
        this.aipOcr.setConnectionTimeoutInMillis(6000);
        thumbnailUtilService = new ThumbnailUtilService();
    }

    public IdentityInfo getIdentityInfo(boolean isFront) throws  IOException {
        HashMap<String, String> options = new HashMap<String, String>();
        if (bytes.length > MAX_LENGTH) {
            bytes = thumbnailUtilService.createThumbnail(bytes);
            logger.info("压缩过后的文件大小为：{} KB", bytes.length / 1024);
        }
        options.put("detect_direction", "true");
        JSONObject idCard = aipOcr.idcard(bytes, isFront, options);
        return OCRUtil.getIdentityInfo(idCard);
    }

    public IdentityInfo getIdentityInfo() throws  IOException {
        return this.getIdentityInfo(true);
    }

    /**
     * 读取银行卡信息
     *
     * @param bytes
     * @return
     * @throws OCRException
     */
    public BankCardInfo getBankCardInfo(byte[] bytes) throws  IOException {
        if (bytes.length > MAX_LENGTH) {
            bytes = thumbnailUtilService.createThumbnail(bytes);
            logger.info("压缩银行卡图片过后的文件大小为：{} KB", bytes.length / 1024);
        }
        JSONObject response = aipOcr.bankcard(bytes);
        return OCRUtil.getBankCardInfo(response);
    }
}
