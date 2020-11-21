package com.yxd.lvjie.activity

import android.os.Bundle
import cn.jzvd.Jzvd
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_video.*


@LayoutId(R.layout.activity_video)
class VideoActivity : BaseActivity() {
    override fun init(bundle: Bundle?) {
        jz_video.setUp(
            "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
            , "饺子闭眼睛"
        )
//        jz_video.posterImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
    }

    override fun onBackPressedSupport() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressedSupport()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

}