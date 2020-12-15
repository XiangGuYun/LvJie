package com.yxd.baselib.view.doc

class Material {
/**
 * MaterialButton
 *
 * 继承AppCompatButton，在原来Button的基础上做了一些扩展，
 * 如圆角、描边、前置和后置icon（icon支持设置Size、Tint、Padding、Gravity等），
 * 还支持按压水波纹并且设置color，基本能满足日常的需求。
 *
 * app:backgroundTint	    背景着色
 * 如果按钮背景是纯色，可以通过app:backgroundTint指定；
 * 如果按钮背景是渐变色，则需要自己定义drawable，然后通过android:background设置。
 * 注意：如果要使用android:background设置背景，则需要将backgroundTint设置为@empty，否则background不会生效。
 *
 * app:backgroundTintMode	着色模式
 * app:strokeColor	        描边颜色
 * app:strokeWidth	        描边宽度
 * app:cornerRadius	        圆角大小
 * app:rippleColor	        按压水波纹颜色
 * app:icon	                图标icon
 * app:iconSize	            图标大小
 * app:iconGravity	        图标重心
 * app:iconTint	            图标着色
 * app:iconTintMode	        图标着色模式
 * app:iconPadding	        图标和文本之间的间距
 *
 * MaterialButton默认在style指定了insetTop和insetBottom为6dp，使得height看起来并没有Button实际设置值一样高，
 * 可以在xml将MaterialButton的insetTop和insetBottom都设置为0dp，这样MaterialButton的高度就和实际设置的高度一致了。
 *
 * MD组件默认都是自带阴影的，MaterialButton也不例外。但是有时候我们并不想要按钮有阴影，那么这时候可以指定style为
 * style="@style/Widget.MaterialComponents.Button.UnelevatedButton"，这样就能去掉阴影，让视图看起来扁平化。
 *
 */

}

