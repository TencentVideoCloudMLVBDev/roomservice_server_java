package com.tencent.qcloud.roomservice.logic;

import com.tencent.qcloud.roomservice.common.Config;
import com.tencent.qcloud.roomservice.pojo.Response.GetTestPushUrlRsp;
import com.tencent.qcloud.roomservice.pojo.StreamIDS;
import com.tencent.qcloud.roomservice.utils.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class LiveUtil {
    @Autowired
    RestTemplate restTemplate;

    private static Logger log= LoggerFactory.getLogger(LiveUtil.class);

    /**
     * 生成推流地址
     */
    public String genPushUrl(String userID) {
        return genPushUrl(userID, System.currentTimeMillis() / 1000  + Config.Live.validTime);
    }

    public String genPushUrl(String userID, long txTime) {
        String liveCode = Config.Live.APP_BIZID + "_" + userID;
        String txSecret = Utils.getMD5(Config.Live.PUSH_SECRET_KEY + liveCode + Long.toHexString(txTime).toUpperCase());
        String ext = "?bizid=" + Config.Live.APP_BIZID + "&txSecret=" + txSecret + "&txTime=" + Long.toHexString(txTime).toUpperCase();
        String pushUrl = "rtmp://" + Config.Live.APP_BIZID + ".livepush.myqcloud.com/live/" + liveCode + ext;
        log.info("genPushUrl , url: " + pushUrl);
        return pushUrl;
    }

    /**
     * 从推流地址中提取流ID，完整的(推流状态检查) + 去掉bizid前缀的(生成对应的播放地址)
     */
    public StreamIDS getStreamIdFromPushUrl(String pushUrl) {
        int index = pushUrl.indexOf("?");
        if (index == -1)
            return null;
        String substr = pushUrl.substring(0, index);
        int index_2 = substr.lastIndexOf("/");
        String streamID = substr.substring(index_2 + 1, index);
        String prefix = Config.Live.APP_BIZID + "_";
        String subID = streamID.substring(prefix.length(), streamID.length());
        StreamIDS ids = new StreamIDS();
        ids.setStreamID(streamID);
        ids.setSubID(subID);
        return ids;
    }


    /**
     * 生成混流地址
     */
    public String genMixedPlayUrl(String subID, String suffix) {
        String liveCode = Config.Live.APP_BIZID + "_" + subID;
        return "https://" + Config.Live.APP_BIZID + ".liveplay.myqcloud.com/live/" + liveCode + "." + suffix;
    }

    /**
     * 生成加速拉流播放地址
     */
    public String genAcceleratePlayUrl(String subID) {
        return genAcceleratePlayUrl(subID, System.currentTimeMillis() / 1000  + Config.Live.validTime);
    }

    /**
     * 生成加速拉流播放地址
     */
    public String genAcceleratePlayUrl(String subID, long txTime) {
        String liveCode = Config.Live.APP_BIZID + "_" + subID;
        String txSecret = Utils.getMD5(Config.Live.PUSH_SECRET_KEY + liveCode + Long.toHexString(txTime).toUpperCase());
        String ext = "?bizid=" + Config.Live.APP_BIZID + "&txSecret=" + txSecret + "&txTime=" + Long.toHexString(txTime).toUpperCase();
        String accPlayUrl = "rtmp://" + Config.Live.APP_BIZID + ".liveplay.myqcloud.com/live/" + liveCode + ext;
        return accPlayUrl;
    }

    /**
     * 生成一组播放地址
     */
    public GetTestPushUrlRsp getTestPushUrl(String userID) {
        GetTestPushUrlRsp rsp = new GetTestPushUrlRsp();
        long txTime = System.currentTimeMillis() / 1000  + Config.Live.validTime;
        String liveCode = Config.Live.APP_BIZID + "_" + userID;
        String txSecret = Utils.getMD5(Config.Live.PUSH_SECRET_KEY + liveCode + Long.toHexString(txTime).toUpperCase());
        String ext = "?bizid=" + Config.Live.APP_BIZID + "&txSecret=" + txSecret + "&txTime=" + Long.toHexString(txTime).toUpperCase();
        String pushUrl = "rtmp://" + Config.Live.APP_BIZID + ".livepush.myqcloud.com/live/" + liveCode + ext;
        String flvPlayUrl = "http://" + Config.Live.APP_BIZID + ".liveplay.myqcloud.com/live/" + liveCode + ".flv";
        String rtmpPlayUrl = "rtmp://" + Config.Live.APP_BIZID + ".liveplay.myqcloud.com/live/" + liveCode;
        String hlsPlayUrl = "http://" + Config.Live.APP_BIZID + ".liveplay.myqcloud.com/live/" + liveCode + ".m3u8";
        String accPlayUrl = "rtmp://" + Config.Live.APP_BIZID + ".liveplay.myqcloud.com/live/" + liveCode + ext;
        rsp.setUrl_push(pushUrl);
        rsp.setUrl_play_flv(flvPlayUrl);
        rsp.setUrl_play_rtmp(rtmpPlayUrl);
        rsp.setUrl_play_hls(hlsPlayUrl);
        rsp.setUrl_play_acc(accPlayUrl);
        return rsp;
    }

    /**
     * 向云直播后台请求混流操作
     */
    public String mergeStream(Map map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://fcgi.video.qcloud.com/common_access" + getQueryString());

        HttpEntity<String> entity = new HttpEntity<String>(JSONObject.valueToString(map.get("mergeParams")), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST,
                entity,
                String.class);

        log.info("mergeStream response: " + response.toString());

        return response.getBody();
    }

    private String getQueryString() {
        long txTime = System.currentTimeMillis() / 1000 + 60;
        String txSecret = Utils.getMD5(Config.Live.APIKEY + txTime);
        String query = "?appid=" + Config.Live.APP_ID +
                "&interface=mix_streamv2.start_mix_stream_advanced&t=" + txTime + "&sign=" + txSecret;
        return query;
    }
}
