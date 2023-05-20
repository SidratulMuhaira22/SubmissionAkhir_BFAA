package com.sidratul.finalsubmissionbfaa.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sidratul.finalsubmissionbfaa.R
import com.sidratul.finalsubmissionbfaa.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var detalBinding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var progressBar2: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detalBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detalBinding.root)

        progressBar2 = detalBinding.progressBar2

        val actionbar = supportActionBar
        actionbar?.title = "Detail User"

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: "null"
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar_url = intent.getStringExtra(EXTRA_URL) ?: "null"
        val repo = intent.getStringExtra(EXTRA_URL2) ?: "null"

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel.setUserDetail(username)
        Log.d("Detail", "username: $username")

        viewModel.getUserDetail().observe(this, {
            detalBinding.apply {
                Glide.with(this@DetailActivity)
                    .load(it.avatar_url)
                    .centerCrop()
                    .into(imgAvatar)

                tvUsername.text = it.login
                tvName.text = it.name
                tvLocation.text = it.location
                tvCompany.text = it.company
                tvBio.text = it.bio
                tvFollowers.text = "${it.followers}"
                tvFollowing.text = "${it.following}"
            }
            showLoading(false)
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0) {
                        detalBinding.togFavorit.isChecked = true
                        _isChecked = true
                    } else {
                        detalBinding.togFavorit.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        detalBinding.togFavorit.setOnClickListener{
            _isChecked = !_isChecked
            if (_isChecked) {
                viewModel.addFavorite(username, id, avatar_url, repo )
            } else {
                viewModel.deleteFavorite(id)
            }
            detalBinding.togFavorit.isChecked = _isChecked
        }


        val followAdapter = FollowAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = followAdapter
        val tabs: TabLayout = findViewById(R.id.table)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        detalBinding.btnShare.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            val link =
                "https://github.com/$username"
            share.putExtra(Intent.EXTRA_SUBJECT, "More Info ")
            share.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(Intent.createChooser(share, "Share to"))
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar2.visibility = View.VISIBLE
        } else {
            progressBar2.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_URL2 = "extra_url2"

        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

    }

}