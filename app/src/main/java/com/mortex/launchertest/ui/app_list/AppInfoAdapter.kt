package com.mortex.launchertest.ui.app_list

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.databinding.AppItemBinding

class AppInfoAdapter() :
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
        return AppInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) =
        holder.bind(items[position])

}

class AppInfoViewHolder(
    private val itemBinding: AppItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var appInfo: AppInfoToShow

    fun bind(item: AppInfoToShow) {
        this.appInfo = item
        itemBinding.listAppName.text = item.label
        itemBinding.appPackage.text = item.packageName
        itemBinding.appIcon.setImageDrawable(item.icon)

        itemBinding.listAppName.setOnClickListener {
            val launchIntent: Intent = itemBinding.listAppName.context.packageManager
                .getLaunchIntentForPackage(item.packageName.toString())!!
            itemBinding.listAppName.context.startActivity(launchIntent)
        }
    }


}

