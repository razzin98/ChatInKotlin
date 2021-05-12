package com.example.aleczad.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aleczad.*
import com.example.aleczad.ui.MyObseravble
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


class ShoutboxFragment : Fragment() {
    var active = true

    private lateinit var shoutboxViewModel: ShoutboxViewModel
    private lateinit var viewModel: MyObseravble
    var Login =""
    var recycle_list : MutableList<RecycleItem> = mutableListOf<RecycleItem>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        shoutboxViewModel =
                ViewModelProviders.of(this).get(ShoutboxViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shoutbox, container, false)

        val postComment: ImageButton = root.findViewById(R.id.postComment);
        postComment.setOnClickListener {
            if ((activity as MainActivity?)!!.checkInternetConnection()) {
                post()
                get()
            } else {
                Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
            }
        }


        val pullToRefresh: SwipeRefreshLayout = root.findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = false
            refresher()
        }


        var recycler_view = root.findViewById(R.id.recycler_view) as RecyclerView
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = RecyclerAdapter(parentFragment as Fragment,recycle_list)
        /*       val adap=recycler_view.adapter
                 val holder= RecyclerAdapter.RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_items, container, false))
                 , Login,adap as RecyclerAdapter, holder as RecyclerView.ViewHolder*/

        val swipeHandler = object : SwipeToDeleteCallback(context as Context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recycler_view.adapter as RecyclerAdapter
                val deletedId=adapter.getToDelete(viewHolder.adapterPosition)

                //val deletedLogin=adapter.getDeletedLogin(viewHolder.adapterPosition)
                //if (deletedLogin == Login) {
                    adapter.removeAt(viewHolder.adapterPosition)
                    delete(deletedId)
                //}
                //else{
                    //Toast.makeText(getActivity(),"You can't delete this comment\nbecause it belongs to: "+deletedLogin,Toast.LENGTH_SHORT).show();
                //}
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (active) {
                    refresher()
                    handler.postDelayed(this, 30000) //30 000 =30 sekund;
                }
            }
        }, 6000)

        return root
    }

    private fun refresher() {
        if ((activity as MainActivity?)!!.checkInternetConnection()) {
            get()
            Toast.makeText(getActivity(),"Data updated",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
        }
    }

    fun get(){
        if(recycle_list.isNotEmpty()) recycle_list.clear()
        val t = Thread {
            val client = OkHttpClient()
            val url = URL("http://tgryl.pl/shoutbox/messages")
            val request = Request.Builder().url(url).get().build()
            val response = client.newCall(request).execute()
            val responseBody = response.body!!.string()
            println("Response Body: $responseBody")
            val mapperAll = ObjectMapper()
            val objData = mapperAll.readTree(responseBody)
            val iter = objData.elements().iterator()
            while(iter.hasNext()){
                var itera = iter.next().elements().iterator()
                var recycle_item= RecycleItem(itera.next().toString().replace("\"",""), itera.next().toString().replace("\"",""), itera.next().toString().replace("\"",""), itera.next().toString().replace("\"",""))
                recycle_list.add(recycle_item)
                println(recycle_item)
            }
            println(recycle_list)
        }
        t.start()
        t.join()
    }

    fun post(){
        val compose: EditText = requireView().findViewById(R.id.compose);
        if(compose.text.toString() =="") {
            Toast.makeText(getActivity(), "Empty comment!", Toast.LENGTH_SHORT).show();
        }
        else {
            val p = Thread {
                val client = OkHttpClient()
                val formBody: FormBody = FormBody.Builder()
                    .add("content", compose.text.toString())
                    .add("login", Login)
                    .build()

                val request = Request.Builder()
                    .url("http://tgryl.pl/shoutbox/message")
                    .post(formBody)
                    .build()

                    val response=client.newCall(request).execute()
                    System.out.print("=================================================================================="+response)
            }
            p.start()
            Toast.makeText(getActivity(), "Comment has been sent!", Toast.LENGTH_SHORT).show()
            p.join()
        }
    }

    fun delete(deletedId: String){
        if ((activity as MainActivity?)!!.checkInternetConnection()) {
            val d = Thread {
                val client = OkHttpClient()

                val request = Request.Builder().url("http://tgryl.pl/shoutbox/message/"+deletedId).delete().build()
                val response = client.newCall(request).execute()
                System.out.print("===============================================================DELETED:"+response)
            }
            d.start()
            d.join()
        } else {
            Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
        listenResponse();
        if ((activity as MainActivity?)!!.checkInternetConnection()) {
            get()
        } else {
            Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
        }
        active = true
    }
    override fun onStop() {
        super.onStop()
        active = false
    }

    private fun listenResponse() {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MyObseravble::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.data.observe(viewLifecycleOwner, Observer {
            Login = viewModel.data.value.toString()    //przesłany login
        })
    }
}
