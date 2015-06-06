package com.zxly.market.entity;

import java.io.File;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;


/**
 * 文件下载信息类
 * @author fengruyi
 *
 */
public class DownLoadTaskInfo {
    public DownLoadTaskInfo() {
    }
    @Transient//不作为数据库字段
    private HttpHandler<File> handler;
    /**apk包名*/
    @Id
    private String packageName;
    /**文件下载状态*/
    private HttpHandler.State state;
    /**下载地址*/
    private String downloadUrl;
    /**文件名*/
    private String fileName;
    /**保存的文字路径*/
    private String fileSavePath;
    /**当前进度*/
    private long progress;
    /**文件长度，字节*/
    private long fileLength;
    /**自动恢复下载*/
    private boolean autoResume;
    /**是否自动命名*/
    private boolean autoRename;
    /**应用图标地址*/
    private String iconUrl;
    /**标记该任务是否已经安装*/
    private boolean isInstalled;
    /**下载速度*/
    @Transient//不作为数据库字段
    private int rate;
    /**版本*/
    private String versionName;
    /**版本号*/
    private int versionCode;
    
    private String classCode;
    private String source;
    /**后台传来的*/
    private int packType;
    /**上报用的，0下载，1下载完成，2安装，3打开*/
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    //    public int getPackType() {
//        return packType;
//    }
//
//    public void setPackType(int packType) {
//        this.packType = packType;
//    }

    public HttpHandler<File> getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler<File> handler) {
        this.handler = handler;
    }

    public HttpHandler.State getState() {
        return state;
    }

    public void setState(HttpHandler.State state) {
        this.state = state;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    @Override
    public boolean equals(Object o) {
    	 if (this == o) return true;
         if (!(o instanceof DownLoadTaskInfo)) return false;

         DownLoadTaskInfo that = (DownLoadTaskInfo) o;

         if (!this.getPackageName().equals(that.getPackageName())) return false;

         return true;
    }

   

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}
