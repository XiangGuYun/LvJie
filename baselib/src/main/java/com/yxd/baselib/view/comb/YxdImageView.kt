package com.yxd.baselib.view.comb

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import com.yxd.baselib.R
import com.yxd.baselib.ex.BaseEx

class YxdImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr), BaseEx {

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.YxdImageView)
        val isCircle = ta.getBoolean(R.styleable.YxdImageView_isCircle, false)
        val cornerRadius = ta.getDimension(R.styleable.YxdImageView_corner_radius_iv, 0f)
        val cornerRadiusPercent = ta.getFloat(R.styleable.YxdImageView_corner_radius_percent_iv, 0f)

        when {
            isCircle -> {
                shapeAppearanceModel = ShapeAppearanceModel.Builder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
                    .setTopRightCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
                    .setBottomLeftCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
                    .setBottomRightCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
                    .build()
            }
            cornerRadius != 0f -> {
                shapeAppearanceModel = ShapeAppearanceModel.Builder()
                    .setAllCorners(getCornerType(ta, R.styleable.YxdImageView_corner_type_iv), cornerRadius)
                    .build()
            }
            cornerRadiusPercent != 0f -> {
                shapeAppearanceModel = ShapeAppearanceModel.Builder()
                    .setTopLeftCorner(getCornerType(ta, R.styleable.YxdImageView_corner_type_iv), RelativeCornerSize(minOf(cornerRadiusPercent, 1f)))
                    .setTopRightCorner(getCornerType(ta, R.styleable.YxdImageView_corner_type_iv), RelativeCornerSize(minOf(cornerRadiusPercent, 1f)))
                    .setBottomLeftCorner(getCornerType(ta, R.styleable.YxdImageView_corner_type_iv), RelativeCornerSize(minOf(cornerRadiusPercent, 1f)))
                    .setBottomRightCorner(getCornerType(ta, R.styleable.YxdImageView_corner_type_iv), RelativeCornerSize(minOf(cornerRadiusPercent, 1f)))
                    .build()
            }
            else -> {
                shapeAppearanceModel = ShapeAppearanceModel.Builder()
                    .setTopLeftCorner(getCornerType(ta, R.styleable.YxdImageView_tl_corner_type_iv), ta.getDimension(R.styleable.YxdImageView_tl_corner_radius_iv, 0f))
                    .setTopRightCorner(getCornerType(ta, R.styleable.YxdImageView_tr_corner_type_iv), ta.getDimension(R.styleable.YxdImageView_tr_corner_radius_iv, 0f))
                    .setBottomLeftCorner(getCornerType(ta, R.styleable.YxdImageView_bl_corner_type_iv), ta.getDimension(R.styleable.YxdImageView_bl_corner_radius_iv, 0f))
                    .setBottomRightCorner(getCornerType(ta, R.styleable.YxdImageView_br_corner_type_iv), ta.getDimension(R.styleable.YxdImageView_br_corner_radius_iv, 0f))
                    .build()
            }
        }

        ta.recycle()
    }

    private fun getCornerType(ta:TypedArray, styleId: Int): Int {
        val value = ta.getInt(styleId, 2)
        return if(value == 2)
            CornerFamily.ROUNDED
        else
            CornerFamily.CUT
    }

}
/*
ivTest.shapeAppearanceModel = ShapeAppearanceModel.Builder()
//            .setAllCorners(CornerFamily.ROUNDED, )
            .setTopLeftCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
            .setTopRightCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
            .setBottomLeftCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))
            .setBottomRightCorner(CornerFamily.ROUNDED, RelativeCornerSize(0.5f))

//            .setTopRightCorner(CornerFamily.CUT,RelativeCornerSize(0.3f))
//            .setBottomRightCorner(CornerFamily.CUT,RelativeCornerSize(0.3f))
//            .setBottomLeftCorner(CornerFamily.CUT,RelativeCornerSize(0.3f))
//            .setAllCornerSizes(ShapeAppearanceModel.PILL)
//            .setTopLeftCornerSize(20f)
//            .setTopRightCornerSize(RelativeCornerSize(0.5f))
//            .setBottomLeftCornerSize(10f)
//            .setBottomRightCornerSize(AbsoluteCornerSize(30f))
            .build()
 */