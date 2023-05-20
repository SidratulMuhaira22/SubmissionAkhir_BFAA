package com.sidratul.finalsubmissionbfaa.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sidratul.finalsubmissionbfaa.R
import com.sidratul.finalsubmissionbfaa.adapter.UserAdapter
import com.sidratul.finalsubmissionbfaa.databinding.ActivityFavoriteBinding
import com.sidratul.finalsubmissionbfaa.db.FavoriteUser
import com.sidratul.finalsubmissionbfaa.db.User
import com.sidratul.finalsubmissionbfaa.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Favorite User"

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                with(intent) {
                    putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    putExtra(DetailActivity.EXTRA_ID, user.id)
                    putExtra(DetailActivity.EXTRA_URL, user.avatar_url)
                }
                startActivity(intent)
            }
        })

        binding.apply {
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavoriteUser.adapter = adapter
        }
        viewModel.getFavUser()?.observe(this, {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        })
    }
    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User (
                user.login,
                user.id,
                user.avatar_url,
                user.url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}