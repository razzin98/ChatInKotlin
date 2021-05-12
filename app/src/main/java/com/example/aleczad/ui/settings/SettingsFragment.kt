package com.example.aleczad.ui.settings

//import com.example.aleczad.ui.Communicator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.aleczad.MainActivity
import com.example.aleczad.R
import com.example.aleczad.ui.MyObseravble


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var viewModel: MyObseravble
    private lateinit var savedLogin:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val editText: EditText = root.findViewById(R.id.input_edittext)

        if((activity as MainActivity?)!!.loadSharedData()=="") {

        } else{
            editText.setText((activity as MainActivity?)!!.loadSharedData())
        }
        val writeLogin = editText.text
        val sendLogin: Button = root.findViewById(R.id.UpdateComment);
        sendLogin.setOnClickListener {
            if(writeLogin.toString() =="") {
                Toast.makeText(getActivity(), "Nie podano loginu!", Toast.LENGTH_SHORT).show();
            }
            else{
                if ((activity as MainActivity?)!!.checkInternetConnection()) {   //standardowe wysyłanie zapytania
                    viewModel = activity?.run {
                        ViewModelProviders.of(this).get(MyObseravble::class.java)
                    } ?: throw Exception("Invalid Activity")
                    viewModel.data.value = writeLogin.toString()
                    (activity as MainActivity?)!!.saveSharedData(writeLogin.toString())
                    Toast.makeText(getActivity(),"Logged as: "+writeLogin.toString(),Toast.LENGTH_SHORT).show();
                    requireView().findNavController().navigate(R.id.action_nav_settings_to_nav_shoutbox)
                } else {
                    Toast.makeText(getActivity(),"No Internet access!",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}
