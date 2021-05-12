package com.example.aleczad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.aleczad.ui.CommentsList
import com.example.aleczad.ui.MyObseravble
import com.example.aleczad.ui.home.ShoutboxFragment
import com.example.aleczad.ui.home.ShoutboxViewModel
import com.example.aleczad.ui.settings.SettingsViewModel



class RecyclerAdapter(private val parentFragment: Fragment, private var recycleList: MutableList<RecycleItem>):RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_items,
            parent, false)

        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = recycleList[position]

        holder.loadComment.text = currentItem.content
        holder.loadMyLogin.text = currentItem.login
        holder.loadDate.text = currentItem.date
        //holder.loadId.text = currentItem.id

        //<======== DODANE do lab10    // działa
        //holder.itemView.setOnClickListener(View.OnClickListener)
        holder.itemView.setOnClickListener {
            val item_login: String = recycleList[position].login
            val item_date: String = recycleList[position].date
            val item_comment: String = recycleList[position].content
            val item_id: String = recycleList[position].id
            //System.out.println("======================================================="+item_id+", "+item_login)


            //dodane w lab10 x2
            val bundle = Bundle()
            bundle.putString("login", item_login)
            bundle.putString("date", item_date)
            bundle.putString("comment", item_comment)
            bundle.putString("id", item_id)

            parentFragment.findNavController().navigate(R.id.action_nav_shoutbox_to_nav_shoutboxII,bundle)
        }
        //>
    }

    override fun getItemCount() = recycleList.size

    class RecyclerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val loadMyLogin: TextView = itemView.findViewById(R.id.loadMyLogin)
        val loadComment: TextView = itemView.findViewById(R.id.loadComment)
        val loadDate: TextView = itemView.findViewById(R.id.loadDate)
        //val loadId: TextView = itemView.findViewById(R.id.loadId)
    }

    fun getToDelete(position: Int): String{
        return recycleList[position].id
    }

    fun getDeletedLogin(position: Int): String{
        return recycleList[position].login
    }

    fun removeAt(position: Int) {
        recycleList.removeAt(position)
        notifyItemRemoved(position)

    }
}
