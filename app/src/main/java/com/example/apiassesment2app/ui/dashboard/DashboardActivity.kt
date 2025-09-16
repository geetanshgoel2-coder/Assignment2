package com.example.apiassesment2app.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apiassesment2app.data.Repository
import com.example.apiassesment2app.databinding.ActivityDashboardBinding
import com.example.apiassesment2app.ui.details.DetailsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapter: EntityAdapter

    @Inject lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EntityAdapter { entityMap ->
            showEntitySummaryDialog(entityMap)
        }

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        val keypassFromLogin = intent.getStringExtra("keypass")
        val keypass = keypassFromLogin ?: "forex"
        load(keypass)
    }

    private fun load(keypass: String) {
        binding.progress.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val res = repo.dashboard(keypass)
                adapter.submitList(res.entities)
                binding.tvTotal.text = "Total: ${res.entityTotal}"
            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progress.visibility = View.GONE
            }
        }
    }

    private fun showEntitySummaryDialog(entity: Map<String, Any?>) {
        val title = extractAssetType(entity) ?: "Details"

        val message = buildSummaryText(entity)

        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Open full details") { _, _ ->
                val json = JSONObject(entity.mapValues { it.value ?: JSONObject.NULL }).toString()
                startActivity(Intent(this, DetailsActivity::class.java).apply {
                    putExtra(DetailsActivity.EXTRA_ENTITY_JSON, json)
                })
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun extractAssetType(entity: Map<String, Any?>): String? {
        val keysToTry = listOf("assetType", "type", "category", "name", "title")
        val first = keysToTry.firstOrNull { entity.containsKey(it) && entity[it] != null }
        return first?.let { "$it: ${entity[it]}" }
    }

    private fun buildSummaryText(entity: Map<String, Any?>): String {
        val sb = StringBuilder()

        val description = entity["description"]?.toString()?.takeIf { it.isNotBlank() }

        entity
            .filterKeys { it != "description" }
            .forEach { (k, v) ->
                sb.append("$k: ${v ?: ""}\n")
            }

        if (description != null) {
            sb.append("\nDescription:\n")
            sb.append(description)
        }

        return sb.toString().trim()
    }
}
