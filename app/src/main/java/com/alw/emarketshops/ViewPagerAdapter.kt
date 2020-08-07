package com.alw.emarketshops

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.alw.emarketshops.ui.home.HomeFragment
import com.squareup.picasso.Picasso


class ViewPagerAdapter(var context: HomeFragment, private var imageUrls: Array<String>): PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context.context)
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        Picasso.get()
            .load(imageUrls[position])
//            .fit()
//            .centerCrop()
            .resize(600,400)
            .into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}