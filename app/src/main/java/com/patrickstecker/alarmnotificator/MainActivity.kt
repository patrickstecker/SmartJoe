package com.patrickstecker.alarmnotificator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.patrickstecker.alarmnotificator.fragments.AlarmNotificatorFragment
import com.patrickstecker.alarmnotificator.fragments.DashboardFragment
import com.patrickstecker.alarmnotificator.fragments.TodoListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.action_home
        bottomNav.setOnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.action_alarm -> {
                    openFragment(AlarmNotificatorFragment.newInstance())
                    true
                }
                R.id.action_home -> {
                    openFragment(DashboardFragment.newInstance())
                    true
                }
                R.id.action_todos -> {
                    openFragment(TodoListFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        openFragment(DashboardFragment.newInstance())
    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}