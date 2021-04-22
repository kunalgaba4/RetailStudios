package com.example.retailstudios.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retailstudios.R
import com.example.retailstudios.firestore.FirestoreClass
import com.example.retailstudios.models.Product
import com.example.retailstudios.ui.activities.AddProductActivity
import com.example.retailstudios.ui.activities.SettingsActivity
import com.example.retailstudios.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    //private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if we want to use the option menu we need to add it
        setHasOptionsMenu(true)}

    fun successProductsListFromFireStore(productList: ArrayList<Product>){
        hideProgressDialog()

        if (productList.size > 0){
            rv_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_product_items.layoutManager = LinearLayoutManager(activity)
            rv_product_items.setHasFixedSize(true)
            val adapterProducts = MyProductsListAdapter(requireActivity(),productList)
            rv_product_items.adapter = adapterProducts
        }else{
            rv_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }
    }

    private fun getProductListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)

            return root
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id){
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    }
