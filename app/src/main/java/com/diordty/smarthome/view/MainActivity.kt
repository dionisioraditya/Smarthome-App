package com.diordty.smarthome.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.diordty.smarthome.R
import com.diordty.smarthome.crypto.CryptoManager
import com.diordty.smarthome.view.adapter.SectionsPagerAdapter
import com.diordty.smarthome.databinding.ActivityMainBinding
import com.diordty.smarthome.models.Relay
import com.diordty.smarthome.viewModel.RelayViewModel
import com.diordty.smarthome.viewModel.UserViewModel
import com.diordty.smarthome.showToastShort
import com.diordty.smarthome.view.adapter.RelayAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // view binding init
    private lateinit var firebaseAuth: FirebaseAuth // firebase auth init
    private lateinit var relayViewModel : RelayViewModel
    private lateinit var usernameViewModel: UserViewModel
    private lateinit var relayRecyclerView : RecyclerView
    private lateinit var adapter: RelayAdapter
    private var relayState = false
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideActionBar()
        //logOut()
        dashboard()
        getUsername()
        viewPager()
    }
    override fun onStart() {
        super.onStart()

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null){ // condition if user not sign in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        getUsername()
        dashboard()
        viewPager()
    }
    private fun viewPager(){ // function for showing view pager temperature and humidity
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
    //function for hide action bar
    private fun hideActionBar(){
        supportActionBar?.hide()
    }
    private fun getUsername(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usernameViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        usernameViewModel.username.observe(this){ value ->
            val text = "Welcome, $value!"
            val ttb = AnimationUtils.loadAnimation(this@MainActivity, R.anim.ttb)
            val stb = AnimationUtils.loadAnimation(this@MainActivity, R.anim.stb)
            binding.profilePicture.startAnimation(stb)
            binding.tabs.startAnimation(stb)
            binding.headerTitle.startAnimation(ttb)
            binding.headerTitle.text = text
        }
    }
    private fun dashboard(){ // function for showing list of relays
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        relayRecyclerView = findViewById(R.id.rv_relay)
        relayRecyclerView.layoutManager = GridLayoutManager(this,2)
        relayRecyclerView.isVerticalScrollBarEnabled
        relayRecyclerView.setHasFixedSize(true)
        adapter = RelayAdapter()
        relayRecyclerView.adapter = adapter
        relayViewModel = ViewModelProvider(this)[RelayViewModel::class.java]
        relayViewModel.allRelays.observe(this) {
            adapter.updateRelayList(it)
            isLoadingGone(binding)
        }


        binding.profilePicture.setOnClickListener {
            //startActivity(Intent(this, AboutActivity::class.java))

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Logout")
            alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    if (firebaseAuth.currentUser == null) {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    }
                }
            alertDialogBuilder.setNeutralButton("cancel") { _, _ ->
                showToastShort("Canceled")
            }
            val dialog: AlertDialog = alertDialogBuilder.create()
            dialog.show()


        }
    }
    private fun isLoadingGone(binding: ActivityMainBinding){
        binding.progressBarMain.visibility = View.GONE
    }
}