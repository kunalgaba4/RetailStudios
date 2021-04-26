package com.example.retailstudios.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.retailstudios.R
import com.example.retailstudios.firestore.FirestoreClass
import com.example.retailstudios.models.Product
import com.example.retailstudios.ui.activities.SettingsActivity

class DashboardFragment : BaseFragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if we want to use the option menu we need to add it
        setHasOptionsMenu(true)}

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id){
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sucessDashboardItemsList(dashboardItemsList: ArrayList<Product>){
        hideProgressDialog()
        for (i in dashboardItemsList){
            Log.i("Item Title",i.title)
        }
    }

    private fun getDashboardItemList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashBoardItemsList(this@DashboardFragment)
    }
}




