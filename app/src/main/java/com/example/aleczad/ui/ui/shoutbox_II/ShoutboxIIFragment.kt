package com.example.aleczad.ui.ui.shoutbox_II

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.aleczad.MainActivity
import com.example.aleczad.R
import com.example.aleczad.ui.MyObseravble
import com.example.aleczad.ui.home.ShoutboxViewModel
import kotlinx.android.synthetic.main.recycler_items.*
import okhttp3.*
import java.io.IOException


class ShoutboxIIFragment : Fragment() {

    private lateinit var shoutboxViewModel: ShoutboxViewModel
    private lateinit var LoginInfo: MyObseravble

    var Login =""



    companion object {
        fun newInstance() = ShoutboxIIFragment()
    }

    private lateinit var viewModel: ShoutboxIIViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoutboxViewModel =
            ViewModelProviders.of(this).get(ShoutboxViewModel::class.java)
        val root = inflater.inflate(R.layout.shoutbox_i_i_fragment, container, false)


        val sendLogin: Button = root.findViewById(R.id.UpdateComment)
        sendLogin.setOnClickListener {
            if ((activity as MainActivity?)!!.checkInternetConnection()) {
                put()
                requireView().findNavController().navigate(R.id.action_nav_shoutboxII_to_nav_shoutbox)
            } else {
                Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
            }
        }

        val deleteComment: ImageButton = root.findViewById(R.id.deleteComment)
        deleteComment.setOnClickListener {
            if ((activity as MainActivity?)!!.checkInternetConnection()) {
                delete(arguments?.getString("id") as String)
                requireView().findNavController().navigate(R.id.action_nav_shoutboxII_to_nav_shoutbox)
            } else {
                Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShoutboxIIViewModel::class.java)

    }


    override fun onStart() {
        super.onStart()
        listenResponse();
    }
    private fun listenResponse() {

        val loadMyLogin = requireView().findViewById<TextView>(R.id.loadMyLogin)
        val loadDate = requireView().findViewById<TextView>(R.id.loadDate)
        val loadComment = requireView().findViewById<TextView>(R.id.loadComment)
        loadMyLogin.text = arguments?.getString("login")
        loadDate.text = arguments?.getString("date")
        loadComment.text = arguments?.getString("comment")


        LoginInfo = activity?.run {
            ViewModelProviders.of(this).get(MyObseravble::class.java)
        } ?: throw Exception("Invalid Activity")
        LoginInfo.data.observe(viewLifecycleOwner, Observer {

            Login = LoginInfo.data.value.toString()    //przesłany login
        })
    }


    fun put(){
        val comm = loadComment.text.toString()
        val login = loadMyLogin.text.toString()
        if(login==Login){

            val t = Thread {
                val client = OkHttpClient()

                val formBody: RequestBody = FormBody.Builder()
                    .add("content",comm)
                    .add("login", login)
                    .build()

                val request = Request.Builder()
                    .url("http://tgryl.pl/shoutbox/message/"+arguments?.getString("id"))
                    .put(formBody)
                    .build()

                try { System.out.println(client.newCall(request).execute())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            t.start()
            Toast.makeText(getActivity(),"Comment has been updated.",Toast.LENGTH_SHORT).show();
            t.join()
        }
        else Toast.makeText(getActivity(),"You can't edit this comment\nbecause it belongs to: "+login,Toast.LENGTH_SHORT).show();
    }


    fun delete(deletedId: String) {
        val login = loadMyLogin.text.toString()
        if (login == Login) {

            val d = Thread {
                val client = OkHttpClient()

                val request =
                    Request.Builder().url("http://tgryl.pl/shoutbox/message/" + deletedId).delete()
                        .build()
                val response = client.newCall(request).execute()
                System.out.print("===============================================================DELETED:" + response)

            }
            d.start()
            d.join()
        }
        else{
            Toast.makeText(getActivity(),"You can't delete this comment\nbecause it belongs to: "+login,Toast.LENGTH_SHORT).show();
        }
    }

}
