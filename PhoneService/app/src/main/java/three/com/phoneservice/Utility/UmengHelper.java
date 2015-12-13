package three.com.phoneservice.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import three.com.phoneservice.Umeng.AndroidBroadcast;
import three.com.phoneservice.Umeng.AndroidCustomizedcast;
import three.com.phoneservice.Umeng.AndroidGroupcast;
import three.com.phoneservice.Umeng.AndroidNotification;
import three.com.phoneservice.Umeng.AndroidUnicast;
import three.com.phoneservice.Umeng.PushClient;

/**
 * Created by Administrator on 2015/12/6.
 */
public class UmengHelper {

    private String appkey = null;
    private String appMasterSecret = null;
    private String timestamp = null;
    private PushClient client = new PushClient();

    public UmengHelper(String key, String secret) {
        try {
            appkey = key;
            appMasterSecret = secret;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sendAndroidBroadcast() throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
        broadcast.setTicker( "Android broadcast ticker");
        broadcast.setTitle(  "中文的title");
        broadcast.setText(   "Android broadcast text");
        broadcast.goAppAfterOpen();
        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        broadcast.setProductionMode();
        // Set customized fields
        broadcast.setExtraField("test", "helloworld");
        client.send(broadcast);
    }

    public void sendAndroidUnicast() throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
        // TODO Set your device token
        unicast.setDeviceToken( "your device token");
        unicast.setTicker( "Android unicast ticker");
        unicast.setTitle(  "中文的title");
        unicast.setText(   "Android unicast text");
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        unicast.setProductionMode();
        // Set customized fields
        unicast.setExtraField("test", "helloworld");
        client.send(unicast);
    }

    public void sendAndroidGroupcast() throws Exception {
        AndroidGroupcast groupcast = new AndroidGroupcast(appkey,appMasterSecret);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where":
		 *	{
    	 *		"and":
    	 *		[
      	 *			{"tag":"test"},
      	 *			{"tag":"Test"}
    	 *		]
		 *	}
		 */
        JSONObject filterJson = new JSONObject();
        JSONObject whereJson = new JSONObject();
        JSONArray tagArray = new JSONArray();
        JSONObject testTag = new JSONObject();
        JSONObject TestTag = new JSONObject();
        testTag.put("tag", "test");
        TestTag.put("tag", "Test");
        tagArray.put(testTag);
        tagArray.put(TestTag);
        whereJson.put("and", tagArray);
        filterJson.put("where", whereJson);
        System.out.println(filterJson.toString());

        groupcast.setFilter(filterJson);
        groupcast.setTicker( "Android groupcast ticker");
        groupcast.setTitle(  "中文的title");
        groupcast.setText(   "Android groupcast text");
        groupcast.goAppAfterOpen();
        groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        groupcast.setProductionMode();
        client.send(groupcast);
    }

    public void sendAndroidCustomizedcast(String alias,String message) throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
        // TODO Set your alias here, and use comma to split them if there are multiple alias.
        // And if you have many alias, you can also upload a file containing these alias, then
        // use file_id to send customized notification.
        customizedcast.setAlias(alias, "KYZS");
        customizedcast.setTicker( "Android customizedcast ticker");
        customizedcast.setTitle(  "通知");
        customizedcast.setText(message);
        customizedcast.goAppAfterOpen();
        customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        customizedcast.setProductionMode();
        client.send(customizedcast);
    }
}
