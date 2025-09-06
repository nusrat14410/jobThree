package com.example.jobthree

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobthree.databinding.ActivityFriendListBinding
import com.example.jobthree.databinding.ItemUserBinding

class FriendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding
    private val vm: UsersViewModel by viewModels()
    private val adapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        vm.users.observe(this) { list -> adapter.submit(list) }
    }

    override fun onStart() { super.onStart(); vm.start() }
    override fun onStop() { super.onStop(); vm.stop() }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu); return true
    }
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profile -> startActivity(Intent(this, MyProfileActivity::class.java))
            R.id.menu_map -> startActivity(Intent(this, GoogleMapActivity::class.java))
            R.id.menu_logout -> {
                vm.logout()
                startActivity(Intent(this, AuthActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class UsersAdapter : RecyclerView.Adapter<UsersVH>() {
    private val data = mutableListOf<AppUser>()
    fun submit(newData: List<AppUser>) { data.clear(); data.addAll(newData); notifyDataSetChanged() }
    override fun onCreateViewHolder(p: android.view.ViewGroup, v: Int): UsersVH {
        val inf = android.view.LayoutInflater.from(p.context)
        val b = ItemUserBinding.inflate(inf, p, false)
        return UsersVH(b)
    }
    override fun onBindViewHolder(h: UsersVH, i: Int) = h.bind(data[i])
    override fun getItemCount() = data.size
}
class UsersVH(private val b: ItemUserBinding)
    : androidx.recyclerview.widget.RecyclerView.ViewHolder(b.root) {
    fun bind(u: AppUser) {
        b.tvName.text = u.displayName ?: "(No name yet)"
        b.tvEmail.text = u.userEmail
    }
}