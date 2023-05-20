package com.sidratul.finalsubmissionbfaa.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sidratul.finalsubmissionbfaa.R
import com.sidratul.finalsubmissionbfaa.adapter.UserAdapter
import com.sidratul.finalsubmissionbfaa.databinding.ActivityMainBinding
import com.sidratul.finalsubmissionbfaa.db.User
import com.sidratul.finalsubmissionbfaa.ui.detail.DetailActivity
import com.sidratul.finalsubmissionbfaa.ui.favorite.FavoriteActivity
import com.sidratul.finalsubmissionbfaa.ui.theme.ThemeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        //actionbar?.title = "Github User"
        actionbar?.title =  getString(R.string.github_user_mainac)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                with(intent) {
                    putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    putExtra(DetailActivity.EXTRA_ID, user.id)
                    putExtra(DetailActivity.EXTRA_URL, user.avatar_url)
                    putExtra(DetailActivity.EXTRA_URL2, user.url)
                }
                startActivity(intent)
            }

        })

        viewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)


        binding.apply {

            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = adapter

            imgBtnSearch.setOnClickListener {

                searchUser()
            }

            etQuery.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    searchUser()

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v?.windowToken, 0)

                    return@OnKeyListener true
                }
                false
            })

        }

        viewModel.getListUsers().observe(this, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)

            }
        })

    }

    private fun searchUser() {
        binding.apply {
            val searchText = etQuery.text.toString()
            if (searchText.isEmpty()) return
            showLoading(true)
            viewModel.findUsers(searchText)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_favorite -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.menu_theme -> {
                val intent = Intent(this, ThemeActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}