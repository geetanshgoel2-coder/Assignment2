package com.example.apiassesment2app.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apiassesment2app.databinding.ActivityDetailsBinding
import org.json.JSONObject

class DetailsActivity : AppCompatActivity() {

    companion object { const val EXTRA_ENTITY_JSON = "extra_entity_json" }

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonStr = intent.getStringExtra(EXTRA_ENTITY_JSON) ?: "{}"
        val obj = JSONObject(jsonStr)

        val desc = obj.optString("description", "")
        binding.tvDesc.text = if (desc.isNotBlank()) desc else "(no description)"

        val sb = StringBuilder()
        obj.keys().forEach { key ->
            if (key != "description") {
                val value = obj.opt(key)
                sb.append("$key: $value\n")
            }
        }

        binding.tvP1.text = "Details"
        binding.tvP2.text = sb.toString().trimEnd()
    }
}
