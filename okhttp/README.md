# MyOkhttp Wiki

> 该模块对Okhttp3进行二次封装,对外提供了POST请求,GET请求,上传文件,下载文件,取消请求和Gson转换等功能.

## 0 重要版本更新

|日期|更新内容|
|---|---|
|2016-08-17|完成POST请求,GET请求,上传文件,下载文件,取消请求和Gson转换功能|
|2016-08-18|结果回调增加RawResponseHandler-直接返回字符串|
|2016-09-18|Tag2.0 重构MyOKhttp,改为链式调用,并增加header的添加|
|2016-09-32|增加Patch Put请求方式|
|2016-12-02|增加下载管理后台|
|2016-12-06|增加Delete请求方式|

## 文章推荐

对于Okhttp3的封装参考了:

1. [https://github.com/hongyangAndroid/okhttputils](https://github.com/hongyangAndroid/okhttputils)
1. [https://github.com/ZhaoKaiQiang/OkHttpPlus](https://github.com/ZhaoKaiQiang/OkHttpPlus)

## Older Version

[V1.0 WIKI](https://gitlab.oneitfarm.com/BaseAndroid/MyOkhttp/blob/master/README1.0.md)

## 1 总体简介

在项目入口创建唯一MyOkhttp实例

```java
MyOkHttp mMyOkhttp = new MyOkHttp();
```

也可以自行配置OkhttpClient.

```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
                  .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                  .readTimeout(10000L, TimeUnit.MILLISECONDS)
                  //其他配置
                 .build();

MyOkHttp mMyOkhttp = new MyOkHttp(okHttpClient);
```

### 1.1 调用方式

整个调用采用链式调用的方式. 方便以后扩展.

### 1.2 请求类型

现在有Get, Post, Patch, Put, Delete, Upload, Download 这些功能

### 1.3 添加参数方式

添加参数可以使用addParam一个个添加, 也可以使用params一次性添加

### 1.4 添加Header方式

添加参数可以使用addHeader一个个添加, 也可以使用headers一次性添加

### 1.5 回调格式

现在回调格式有以下几种:

1. Raw原生数据 RawResponseHandler
1. Json JsonResponseHandler
1. Gson GsonResponseHandler

## 2 调用示例

### 2.1 POST请求 + Json回调示例

```java
mMyOkhttp.post()
        .url("http://192.168.2.135/test_okhttp.php")
        .addParam("name", "tsy")
        .tag(tag)       //建议直接将页面的this作为tag传入
        .enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                LogUtils.d("onSuccess:" + response);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                LogUtils.d("onFailure:" + error_msg);
            }
        });
```

### 2.2 GET请求 + Raw回调示例

```java
mMyOkhttp.get()
        .url("http://192.168.2.135/test_okhttp.php")
        .addParam("name", "tsy")
        .tag(tag)
        .enqueue(new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                LogUtils.d("onSuccess:" + response);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                LogUtils.d("onFailure:" + error_msg);
            }
        });
```

### 2.3 Patch请求 + Json回调示例

```java
mMyOkhttp.patch()
        .url("http://192.168.2.135/test_okhttp.php/id/1/username/tsy")
        .tag(tag)
        .enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                LogUtils.d("onSuccess:" + response);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                LogUtils.d("onFailure:" + error_msg);
            }
        });
```

### 2.4 Put请求 + Json回调示例

```java
mMyOkhttp.put()
        .url("http://192.168.2.135/test_okhttp.php/id/1/username/tsy")
        .tag(tag)
        .enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                LogUtils.d("onSuccess:" + response);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                LogUtils.d("onFailure:" + error_msg);
            }
        });
```

### 2.5 Delete请求 + Json回调示例

```java
mMyOkhttp.delete()
        .url("http://192.168.2.135/test_okhttp.php/id/1")
        .tag(tag)
        .enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                LogUtils.d("onSuccess:" + response);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                LogUtils.d("onFailure:" + error_msg);
            }
        });
```

### 2.6 上传文件 + Gson回调示例

```java
mMyOkhttp.upload()
        .url("http://192.168.2.135/test_post.php")
        .addParam("name", "tsy")
        .addFile("avatar",
                new File(Environment.getExternalStorageDirectory()
                        + "/com.ci123.service.splashandroid/sasas.jpg"))        //上传已经存在的File
        .addFile("avatar2", "asdsda.png", byteContents)    //直接上传File bytes
        .tag(tag)
        .enqueue(new GsonResponseHandler<BB> {
            @Override
            public void onSuccess(int statusCode, BB response) {
                LogUtils.d("onSuccess:" + response.toString);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                LogUtils.d("onFailure:" + error_msg);
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                LogUtils.d("onProgress:" + currentBytes + "/" + totalBytes);
            }
        });
```

### 2.7 下载文件

```java
mMyOkhttp.download()
            .url("http://192.168.2.135/head.jpg")
            .filePath(Environment.getExternalStorageDirectory() + "/ahome/a.jpg")
            .tag(tag)
            .enqueue(new DownloadResponseHandler() {

                @Override
                public void onStart() {
                    LogUtils.d("onStart");
                }

                @Override
                public void onFinish(File downloadFile) {
                    LogUtils.d("onFinish:");
                }

                @Override
                public void onProgress(long currentBytes, long totalBytes) {
                    LogUtils.d("onProgress:" + currentBytes + "/" + totalBytes);
                }

                @Override
                public void onFailure(String error_msg) {
                    LogUtils.d("onFailure:" + error_msg);
                }
            });
```

## 3 取消请求(建议放在BaseActivity,BaseFragment的onDestroy中)

```java
mMyOkhttp.cancel(tag);     //tag 即之前请求时传入的tag 建议直接将页面作为object传入
```

## 4 下载管理

实现了下载管理的核心逻辑，包括添加下载任务、开始任务、暂停任务、删除任务、任务下载等待、下载进度和状态监听等功能。依赖于MyOkhttp

### 4.1 实现AbstractDownloadMgr

项目中创建DownloadMgr继承AbstractDownloadMgr，并实现其中的状态，进度的本地持久化保存。（AbstractDownloadMgr只负责在内存中存储所有任务，
需要项目自行实现本地持续化存储，重新进入后的任务恢复等）

示例：

```java
/**
 * 实现下载管理 AbstractDownloadMgr
 * Created by tsy on 2016/11/24.
 */

public class DownloadMgr extends AbstractDownloadMgr {

    private DownloadMgr(Builder builder) {
        super(builder);
    }

    /**
     * 初始进入app 恢复所有未完成的任务
     */
    @Override
    public void resumeTasks() {
        if(DEBUG) {
            Log.i(TAG, "start resumeTasks");
        }

        //获取所有未完成的任务（已完成的不需要添加）
        DownloadContract.Interactor downloadInteractor = new DownloadInteractor();
        ArrayList<Task> tasks = downloadInteractor.parseTask(downloadInteractor.getAllUnfinishedDownloadTasks());

        //将任务加到下载管理队列中
        if(tasks != null && tasks.size() > 0) {
            for(int i = 0; i < tasks.size(); i ++) {
                Task task = tasks.get(i);
                task.setDefaultStatus(DEFAULT_TASK_STATUS_PAUSE);       //所有任务初始设置为暂停
                addTask(tasks.get(i));
            }

            downloadInteractor.pauseAllTask();
        }
    }

    /**
     * 保存进度
     * @param taskId taskId
     * @param currentBytes 已经下载的bytes
     * @param totalBytes 总共bytes
     */
    @Override
    protected void saveProgress(String taskId, long currentBytes, long totalBytes) {
        DownloadContract.Interactor downloadInteractor = new DownloadInteractor();
        downloadInteractor.updateProgress(taskId, currentBytes, totalBytes);
    }

    /**
     * 下载任务开始
     * @param taskId task id
     */
    @Override
    protected void onTaskStart(String taskId) {
        DownloadContract.Interactor downloadInteractor = new DownloadInteractor();
        downloadInteractor.startTask(taskId);
    }

    /**
     * 下载任务暂停
     * @param taskId task id
     */
    @Override
    protected void onTaskPause(String taskId) {
        DownloadContract.Interactor downloadInteractor = new DownloadInteractor();
        downloadInteractor.pauseTask(taskId);
    }

    /**
     * 下载任务完成
     * @param taskId task id
     */
    @Override
    protected void onTaskFinish(String taskId) {
        DownloadContract.Interactor downloadInteractor = new DownloadInteractor();
        downloadInteractor.finishTask(taskId);
    }

    /**
     * 下载任务失败
     * @param taskId task id
     */
    @Override
    protected void onTaskFail(String taskId) {
        //失败设置为暂停 允许用户再次尝试开始
        DownloadContract.Interactor downloadInteractor = new DownloadInteractor();
        downloadInteractor.pauseTask(taskId);
    }

    //实现Builder
    public static class Builder extends AbstractDownloadMgr.Builder {

        @Override
        public AbstractDownloadMgr build() {
            return new DownloadMgr(this);
        }
    }
}
```

### 4.2 使用后台下载管理

#### 4.2.1 项目开始时初始化唯一实例DownloadMgr 并自定义配置

```java
mDownloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                .myOkHttp(mMyOkhttp)
                .maxDownloadIngNum(5)       //设置最大同时下载数量（不设置默认5）
                .saveProgressBytes(50 * 1024)  //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                .build();
```

### 4.2.2 恢复本地存储的所有未完成的任务

```java
mDownloadMgr.resumeTasks();
````

### 4.2.3 创建一个下载任务

```java
DownloadMgr.Task task = new DownloadMgr.Task();
task.setTaskId(mDownloadMgr.genTaskId());       //生成一个taskId
task.setUrl("xxxxx");   //下载地址
task.setFilePath("xxxxxxx");    //下载后文件保存位置
task.setCompleteBytes(1234L);       //设置已完成的bytes （用于恢复任务时添加，如果新添加的任务不需要设置该字段）
task.setDefaultStatus(DownloadMgr.DEFAULT_TASK_STATUS_START);       //任务添加后开始状态 如果不设置 默认任务添加后就自动开始

mDownloadMgr.addTask(task);
```

### 4.2.4 开始 暂停任务

```java
mDownloadMgr.startTask("taskId");

mDownloadMgr.pauseTask("taskId");
```

### 4.2.5 页面添加下载监听

**PS：不要使用匿名类，会造成内存泄露。在onResume时初始化监听，在onPause时销毁监听**

```java
@Override
public void onResume() {
    super.onResume();

    //每次显示页面的时候加上监听
    mDownloadTaskListener = new DownloadTaskListener() {
        @Override
        public void onStart(String taskId, long completeBytes, long totalBytes) {
            //开始下载
        }

        @Override
        public void onProgress(String taskId, long currentBytes, long totalBytes) {
            //下载进度

            //建议使用handler延迟刷新下载进度 不然过于频繁刷新
            if(!mMyHandler.hasMessages(MSG_DELAY_NOTIFICAION)) {
                Message message = new Message();
                message.what = MSG_DELAY_NOTIFICAION;
                mMyHandler.sendMessageDelayed(message, 300);
            }
        }

        @Override
        public void onPause(String taskId, long currentBytes, long totalBytes) {
            //下载暂停
        }

        @Override
        public void onFinish(String taskId, File file) {
            //下载完成
        }

        @Override
        public void onFailure(String taskId, String error_msg) {
            //下载失败
        }
    };

    //加入监听
    mDownloadMgr.addListener(mDownloadTaskListener);
}

@Override
public void onPause() {

    //释放监听 防止内存泄露
    mDownloadMgr.removeListener(mDownloadTaskListener);
    mDownloadTaskListener = null;

    super.onPause();
}
```

### 4.3 API

#### 1. 进入应用后恢复所有本地任务

```java
/**
 * 初始进入app 恢复所有未完成的任务
 */
void resumeTasks()
```

#### 2. 添加下载任务

```java
/**
 * 添加下载任务
 * @param task Task
 */
DownloadTask addTask(Task task)
```

#### 3. 开始任务

```java
/**
 * 开始任务
 * @param taskId task id
 */
void startTask(String taskId)
```

#### 4. 开始所有任务

```java
/**
 * 开始所有任务
 */
void startAllTask()
```

#### 5. 暂停任务

```java
/**
 * 暂停任务
 * @param taskId task id
 */
void pauseTask(String taskId)
```

#### 6. 暂停所有任务

```java
/**
 * 暂停所有任务
 */
void pauseAllTask()
```

#### 7. 删除任务

```java
/**
 * 删除任务
 * @param taskId 任务id
 */
void deleteTask(String taskId)
```

#### 8. 添加下载监听

```java
/**
 * 添加下载监听
 * @param downloadTaskListener
 */
void addListener(DownloadTaskListener downloadTaskListener)
```

#### 9. 移除下载监听

```java
/**
 * 移除下载监听
 * @param downloadTaskListener
 */
void removeListener(DownloadTaskListener downloadTaskListener)
```

#### 10. 生成taskId

```java
/**
 * 生成taskId yyyyMMddHHmmss+3位随机数字
 * @return
 */
String genTaskId()
```

#### 11. 获取当前任务的下载任务信息

```java
/**
 * 获取当前任务的下载任务信息
 * @param taskId 任务id
 * @return
 */
DownloadTask getDownloadTask(String taskId)
```

### 4.4 下载任务状态说明

```java
public class DownloadStatus {
    public static final int STATUS_DEFAULT = -1;        //初始状态
    public static final int STATUS_WAIT = 0;            //队列等待中
    public static final int STATUS_PAUSE = 1;           //暂停
    public static final int STATUS_DOWNLOADING = 2;     //下载中
    public static final int STATUS_FINISH = 3;          //下载完成
    public static final int STATUS_FAIL = 4;            //下载失败
}
```

### 4.5 下载任务监听说明

```java
public interface DownloadTaskListener {
    /**
     * 任务开始
     * @param taskId task id
     * @param completeBytes 断点续传 已经完成的bytes
     * @param totalBytes total bytes
     */
    void onStart(String taskId, long completeBytes, long totalBytes);

    /**
     * 任务下载中
     * @param taskId task id
     * @param currentBytes 当前已经下载的bytes
     * @param totalBytes total bytes
     */
    void onProgress(String taskId, long currentBytes, long totalBytes);

    /**
     * 任务暂停
     * @param taskId task id
     * @param currentBytes 当前已经下载的bytes
     * @param totalBytes total bytes
     */
    void onPause(String taskId, long currentBytes, long totalBytes);

    /**
     * 任务完成
     * @param taskId task id
     * @param file 下载完成后的file
     */
    void onFinish(String taskId, File file);

    /**
     * 任务失败
     * @param taskId task id
     * @param error_msg error_msg
     */
    void onFailure(String taskId, String error_msg);
}
```