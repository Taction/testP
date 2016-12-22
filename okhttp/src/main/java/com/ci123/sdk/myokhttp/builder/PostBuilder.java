package com.ci123.sdk.myokhttp.builder;

import com.ci123.sdk.myokhttp.MyOkHttp;
import com.ci123.sdk.myokhttp.callback.MyCallback;
import com.ci123.sdk.myokhttp.response.IResponseHandler;
import com.ci123.sdk.myokhttp.util.LogUtils;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * post builder
 * Created by tsy on 16/9/18.
 */
public class PostBuilder extends OkHttpRequestBuilderHasParam<PostBuilder> {

    public PostBuilder(MyOkHttp myOkHttp) {
        super(myOkHttp);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if(mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            FormBody.Builder encodingBuilder = new FormBody.Builder();
            appendParams(encodingBuilder, mParams);

            builder.post(encodingBuilder.build());

            Request request = builder.build();

            mMyOkHttp.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new MyCallback(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Post enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params to form builder
    private void appendParams(FormBody.Builder builder, Map<String, String> params) {

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
