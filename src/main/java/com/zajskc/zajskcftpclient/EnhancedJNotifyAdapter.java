package com.zajskc.zajskcftpclient;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created With Idea.
 * Project: zajskc-ftp-client
 * Name: EnhancedJNotifyAdapter
 * User: tanglemo
 * Date: 2018/7/9
 * Description:
 */
@Component
public class EnhancedJNotifyAdapter extends JNotifyAdapter implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${monitor.path}")
    private String path;
    @Value("${monitor.backup}")
    private String backup;
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.user}")
    private String user;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.port}")
    private int port;
    @Value("${ftp.sync.realTime}")
    private boolean realTime;
    @Value("${ftp.sync.total}")
    private int total;
    @Value("${ftp.sync.taskTime}")
    private int taskTime;

    private int totalFlag = 0;
    private int fwTotal = 0;
    private int yzTotal = 0;
    private int fkTotal = 0;
    private int mjskTotal = 0;
    private int mjkkTotal = 0;
    private int tccTotal = 0;

    Map<String, String> typeMap = new HashMap();


    //关注的事件
    int mask = JNotify.FILE_CREATED;
    //是否监视子目录,及级联监视
    boolean watchSubtree = true;
    //监听程序id
    public int watchId;

    /**
     * 容器启动时,启动监视程序
     */
    public void beginWatch() {
        //添加到监视队列
        try {
            this.watchId = JNotify.addWatch(path, mask, watchSubtree, this);
            System.out.println("-----------------初始化完成------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * 死循环,线程一直执行,休眠一分钟后继续执行,主要是为了让主线程一直执行
         * 休眠时间和监测文件发生的效率无关(就是说不是监视目录文件改变一分钟后才监测到,监测几乎是实时的
         * 调用本地系统库)
         */
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 当监听目录下一旦有新的文件被创建,测即触发该事件
     * wd:监控线程id
     * rootPath:监听目录
     * name:文件名称
     */
    public void fileCreated(int wd, String rootPath, String name) {
        System.out.println("the name is " + name);
        StringBuffer sb = new StringBuffer();

        if (realTime && name.endsWith(".csv")) {
            String remotePath = name.substring(0, name.length() - 36);
            sb.append("ncftpput -u wyxq -p wyxq2018 -P 21 -m -R 59.39.179.76 /");
            sb.append(remotePath);
            System.out.println("the file name is " + name);
            //ncftpput -u wyxq -p wyxq2018 -P 21 -m -R 59.39.179.76 / /home/demon/test
            String localfile = path + name;
            sb.append(localfile);
            String command = sb.toString();// "sh "+script+" "+name+" "+backup+" "+backup+"/result";
            System.out.println("the command is " + command);
            try {
                Runtime r = Runtime.getRuntime();
                //执行命令
                r.exec(command);

                System.out.println("执行完毕");
                //File file = new File(localfile);
                //String backupFile = backup + "/" + file.getName();

                //FileUtils.moveFile(new File(file.getAbsolutePath()), new File(backupFile));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("over");
            }
        } /*else if (!realTime && total==fkTotal || total==fwTotal || total == yzTotal || total == mjkkTotal || total == mjskTotal || total == tccTotal ) {
            try {
                if(name.contains("XQ_MJSKXX")){

                    if(mjskTotal==5){mjskTotal=1;return;}
                    ++mjskTotal;
                }
                if(name.contains("XQ_FKXX")){
                    if(fkTotal==5){fkTotal=1;return;}
                    ++fkTotal;
                }
                if(name.contains("XQ_TCCCRXX")){
                    if(tccTotal==5){tccTotal=1;return;}
                    ++tccTotal;
                }
                if(name.contains("XQ_MJKKSQXX")){
                    if(mjkkTotal==5){mjkkTotal=1;return;}
                    ++mjkkTotal;
                }
                if(name.contains("XQ_YZXX")){
                    if(yzTotal==5){yzTotal=1;return;}
                    ++yzTotal;
                }
                if(name.contains("XQ_FWXX")){
                    if(fwTotal==5){fwTotal=1;return;}
                    ++fwTotal;
                }


                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                String sDateTime = sdf.format(new Date());
                int i = (int) (Math.random() * 900) + 100;
                String fileName = sDateTime + i + "_" + "125" + "_441900_" + typeMap.get(split[0]) + ".zip";
                String srcPath = path+remotePath;
                ZipUtil.zip(srcPath.substring(0,srcPath.length()-1),backup+fileName);

                sb.append(backup+fileName);
                Runtime r = Runtime.getRuntime();
                System.out.println("the command is "+sb.toString());
                //执行命令
                r.exec(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(!realTime && taskTime==CommonUtils.minuteDiff(lastDate,new Date())){

        }*/

    }

    /**
     * 重命名事件
     */
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        //   FileOut.addFile(oldName, newName,false);
    }

    /**
     * 修改事件
     */
    public void fileModified(int wd, String rootPath, String name) {
//        FileOut.addFile( null, name,false);
    }

    /**
     * 删除事件
     */
    public void fileDeleted(int wd, String rootPath, String name) {
//        FileOut.addFile(null, name,true);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        typeMap.put("XQ_MJSKXX", "004");
        typeMap.put("XQ_FKXX", "005");
        typeMap.put("XQ_TCCCRXX", "007");
        typeMap.put("XQ_MJKKSQXX", "003");
        typeMap.put("XQ_YZXX", "002");
        typeMap.put("XQ_FWXX", "001");
        beginWatch();
    }
}