package com.mortex.launchertest.ui.app_list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.databinding.AppItemBinding

class AppInfoAdapter(private val appListener: AppListener, private val isForParent: Boolean) :
    RecyclerView.Adapter<AppInfoViewHolder>() {
    private val items = ArrayList<AppInfoToShow>()

    fun setItems(items: List<AppInfoToShow>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val binding: AppItemBinding =
            AppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppInfoViewHolder(binding, appListener, isForParent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) =
        holder.bind(items[position])

}

class AppInfoViewHolder(
    private val itemBinding: AppItemBinding,
    private val appListener: AppListener,
    private val isForParent: Boolean
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var appInfo: AppInfoToShow

    fun bind(item: AppInfoToShow) {
        this.appInfo = item
        itemBinding.listAppName.text = item.label
        itemBinding.appPackage.text = item.packageName
        itemBinding.appIcon.setImageDrawable(item.icon)
        if (isForParent) {
            itemBinding.checkbox.visibility = View.VISIBLE
        }

        itemBinding.root.setOnClickListener {
            if (isForParent) {
                if (!itemBinding.checkbox.isChecked) {
                    itemBinding.checkbox.isChecked = true
                    appListener.appCheckedForBlockList(item)
                } else {
                    itemBinding.checkbox.isChecked = false
                    appListener.removeFromBlockList(item)
                }

            } else {
                val launchIntent: Intent = itemBinding.listAppName.context.packageManager
                    .getLaunchIntentForPackage(item.packageName.toString())!!
                itemBinding.listAppName.context.startActivity(launchIntent)
            }
        }
    }


}

