package com.zajskc.zajskcftpclient.Common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created With Idea.
 * Project: zajskc-ftp-client
 * Name: CommonUtils
 * User: tanglemo
 * Date: 2018/7/13
 * Description: 
 */
public class CommonUtils {

    /**
     * 求时间段的相差分钟数
     * @param begin
     * @param end
     * @return
     */
    public static int minuteDiff(Date begin,Date end){
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
        double min=Math.floor(between/60);
        return (int)min;
    }
}