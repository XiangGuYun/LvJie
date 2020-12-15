package com.yxd.lvjie.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import com.yxd.baselib.FLLP
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.NetUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.header.*


@LayoutId(R.layout.activity_html)
class LvJieHtmlActivity : ProjectBaseActivity() {

    private lateinit var webUrl: String//加载地址
    private lateinit var flWeb: FrameLayout//用于动态添加WebView
    private lateinit var webView: WebView
    private lateinit var webViewClient: WebViewClient//网页客户端
    private lateinit var settings: WebSettings//网页设置

    override fun init(bundle: Bundle?) {
        webUrl = intent.getStringExtra(WEB_URL)
        initWebView()
    }

    private fun initWebView() {
        webView = WebView(this)
        webView.lp(FLLP(MP, MP).apply { setMargins(0, 44.dp, 0, 0) })
        //有效果
        flWeb = findViewById(R.id.flWeb)
        flWeb.addView(webView, 1)

        /*
        设置网页浏览器客户端
         */
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                tvTitle.text = title
            }
        }
        /*
        设置网页视图客户端
         */
        initWebViewClient()

        webView.webViewClient = webViewClient

        /*
        //方式1. 加载一个网页：
        webView.loadUrl("http://www.google.com/");

        //方式2：加载apk包中的html页面
        webView.loadUrl("file:///android_asset/test.html");

        //方式3：加载手机本地的html页面
        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");

        // 方式4： 加载 HTML 页面的一小段内容
        WebView.loadData(String data, String mimeType, String encoding)
         */

        webView.loadUrl(webUrl, mapOf("Referer" to "https://h5.hbei.vip/"))
        /*
        设置支持JS
         */
        webView.settings.javaScriptEnabled = true
        settings = webView.settings
        // 设置可以支持缩放
        settings.setSupportZoom(true)
        // 设置出现缩放工具
        settings.builtInZoomControls = false
        //扩大比例的缩放
        settings.useWideViewPort = true
        //自适应屏幕
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.loadWithOverviewMode = true
        settings.setAppCacheEnabled(true)
        settings.databaseEnabled = true
        settings.domStorageEnabled = true//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        /*
        设置缓存模式
         */
        if (NetUtils.isConnected()) {//判断是否有网络链接
            settings.cacheMode = WebSettings.LOAD_DEFAULT
        } else {
            settings.cacheMode = WebSettings.LOAD_CACHE_ONLY
        }
        /*！
        把图片加载放在最后来加载渲染
         */
        settings.blockNetworkImage = false
        /*
        设置渲染优先级
         */
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        /*
        支持多窗口
         */
        settings.setSupportMultipleWindows(true)
        /*
        开启 DOM storage API 功能
         */
        settings.domStorageEnabled = true
        /*
        开启 Application Caches 功能
         */
        settings.setAppCacheEnabled(true)
    }

    private fun initWebViewClient() {
        webViewClient = object : WebViewClient() {
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    try {
                        if (url.startsWith("weixin://") //微信

                                || url.startsWith("alipays://") //支付宝

                                || url.startsWith("mailto://") //邮件

                                || url.startsWith("tel://")//电话

                                || url.startsWith("cmbmobilebank://")//招商银行
                                || url.startsWith("https://")
                                || !url.contains("http://")
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                            return true
                        }//其他自定义的scheme
                    } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return true//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                    }
                }

                webView.loadUrl(url) //设置不用系统浏览器打开,直接显示在当前Webview
                return false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
//                busPost { msg.w(MsgWhat.START_PROGRESS) }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
//                busPost { msg.w(MsgWhat.STOP_PROGRESS) }
            }
        }
    }


    override fun onDestroy() {
        webView.visibility = View.GONE
        webView.removeAllViews()
        webView.destroy()
        flWeb.removeView(webView)
        super.onDestroy()
    }

    companion object {
        private val WEB_URL = "webUrl"
        fun start(ctx: Context, webUrl: String) {
            ctx.startActivity(Intent(ctx, LvJieHtmlActivity::class.java).putExtra(WEB_URL, webUrl))
        }
    }

    override fun onBackPressedSupport() {
        if(actList.find { it.javaClass.simpleName == "HomeActivity" }!=null){
            super.onBackPressedSupport()
        } else {
            goTo<HomeActivity>()
            super.onBackPressedSupport()
        }
    }

}




